package app.admin.service.impl;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.dao.ContentVoMapper;
import app.admin.dao.MetaVoMapper;
import app.admin.dao.RelationShipVoMapper;
import app.admin.model.Types;
import app.admin.model.vo.ContentVo;
import app.admin.model.vo.ContentVoExample;
import app.admin.service.ContentService;
import app.admin.service.MetaService;
import app.admin.service.RelationshipService;
import app.common.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.javadoc.JavadocTodo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger LOGGER= LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    private ContentVoMapper contentVoMapper;

    @Autowired
    private MetaVoMapper metaVoMapper;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private MetaService metaService;


    //发布文章
    @Override
    public void publish(ContentVo contentVo) {
        if (null==contentVo){
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isBlank(contentVo.getTitle())){
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contentVo.getContent())){
            throw new TipException("文章内容不能为空");
        }

        int titleLen=contentVo.getTitle().length();
        if (titleLen> Constants.MAX_TITLE_COUNT){
            throw new TipException("文章标题过长");
        }
        int contentLen=contentVo.getContent().length();
        if (contentLen>Constants.MAX_TEXT_COUNT){
            throw new TipException("文章内容超过最大长度："+Constants.MAX_TEXT_COUNT);
        }
        if (null==contentVo.getAuthorId()){
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isNotBlank(contentVo.getSlug())){
            if (contentVo.getSlug().length()<5){
                throw new TipException("输入的路径过短");
            }
            if (!TaleUtils.isPath(contentVo.getSlug()))
                throw new TipException("输入的路径不合法");
            ContentVoExample example=new ContentVoExample();
            example.createCriteria().andTypeEqualTo(contentVo.getType()).andSlugEqualTo(contentVo.getSlug());
            int count = contentVoMapper.countByExample(example);
            if (count>0)
                throw new TipException("该路径已经存在，请重新输入");
        }else {
            contentVo.setSlug(null);
        }
        contentVo.setContent(EmojiParser.parseToAliases(contentVo.getContent()));

        int time= DateKit.getCurrentUnixTime();
        contentVo.setCreated(time);
        contentVo.setModified(time);
        contentVo.setHits(0);
        contentVo.setCommentsNum(0);

        String tags=contentVo.getTags();
        String category=contentVo.getCategories();
        contentVoMapper.insert(contentVo);
        Integer cid = contentVo.getCid();

        metaService.saveMetas(cid,tags, Types.TAG.getType());
        metaService.saveMetas(cid,category,Types.CATEGORY.getType());

    }

    @Override
    public PageInfo<ContentVo> getContents(Integer page, Integer limit) {
        LOGGER.debug("获取最近文章");
        ContentVoExample example=new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        PageHelper.startPage(page,limit);
        List<ContentVo> contentVos = contentVoMapper.selectByExampleWithBLOBs(example);
        PageInfo<ContentVo> pageInfo=new PageInfo<>(contentVos);
        LOGGER.debug("返回最近文章列表");
        return pageInfo;
    }

    //根据主键或者slug获取文章
    @Override
    public ContentVo getContent(String id) {
        if (StringUtils.isNotBlank(id)){
            if (Tools.isNumber(id)){
                ContentVo contentVo=contentVoMapper.selectByPrimaryKey(Integer.valueOf(id));
                if (contentVo!=null){
                    contentVo.setHits(contentVo.getHits()+1);
                    contentVoMapper.updateByPrimaryKey(contentVo);
                }
                return contentVo;
            }else {
                ContentVoExample example=new ContentVoExample();
                example.createCriteria().andSlugEqualTo(id);
                List<ContentVo> contentVos = contentVoMapper.selectByExampleWithBLOBs(example);
                if (contentVos.size()!=1){
                    throw new TipException("根据slug返回的结果不止一个");
                }
                return contentVos.get(0);
            }
        }
        return null;
    }

    //更新文章
    @Override
    public void updateContentByCid(ContentVo contentVo) {
        if (null != contentVo && null != contentVo.getCid()) {
            contentVoMapper.updateByPrimaryKeySelective(contentVo);
        }
    }

    //返回某个目录下的部分文章及文章总数
    @Override
    public PageInfo<ContentVo> getArticles(Integer mid, int page, int limit) {
        int total=metaVoMapper.countWithSql(mid);
        PageHelper.startPage(page,limit);
        List<ContentVo> list=contentVoMapper.findByCatalog(mid);
        PageInfo<ContentVo> pageInfo=new PageInfo<>(list);
        pageInfo.setTotal(total);
        return pageInfo;
    }


    //根据关键字获取内容
    @Override
    public PageInfo<ContentVo> getArticles(String keyword, int page, int limit) {
        PageHelper.startPage(page,limit);
        ContentVoExample example=new ContentVoExample();
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType())
                .andStatusEqualTo(Types.PUBLISH.getType())
                .andTitleLike("%"+keyword+"%");
        example.setOrderByClause("created desc");
        List<ContentVo> contentVos = contentVoMapper.selectByExampleWithBLOBs(example);
        return new PageInfo<>(contentVos);
    }

    @Override
    public PageInfo<ContentVo> getArticlesWithPage(ContentVoExample example, int page, int limit) {
        PageHelper.startPage(page, limit);
        List<ContentVo> contentVos = contentVoMapper.selectByExampleWithBLOBs(example);
        return new PageInfo<>(contentVos);
    }

    @Override
    public void deleteByCid(Integer cid) {
        ContentVo contents = this.getContent(cid + "");
        if (null != contents) {
            contentVoMapper.deleteByPrimaryKey(cid);
            relationshipService.deleteById(cid, null);
        }
    }

    @Override
    public void updateArticle(ContentVo contents) {
        if (null == contents || null == contents.getCid()) {
            throw new TipException("文章对象不能为空");
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (contents.getTitle().length() > 200) {
            throw new TipException("文章标题过长");
        }
        if (contents.getContent().length() > 65000) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isBlank(contents.getSlug())) {
            contents.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contents.setModified(time);
        Integer cid = contents.getCid();
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        contentVoMapper.updateByPrimaryKeySelective(contents);
        relationshipService.deleteById(cid, null);
        metaService.saveMetas(cid, contents.getTags(), Types.TAG.getType());
        metaService.saveMetas(cid, contents.getCategories(), Types.CATEGORY.getType());
    }

    @Override
    public void updateCategory(String ordinal, String newCat) {
        ContentVo contentVo = new ContentVo();
        contentVo.setCategories(newCat);
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andCategoriesEqualTo(ordinal);
        contentVoMapper.updateByExampleSelective(contentVo, example);
    }
}
