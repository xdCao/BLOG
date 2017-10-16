package app.admin.service.impl;
/*
    created by xdCao on 2017/10/16
*/

import app.admin.dao.CommentVoMapper;
import app.admin.model.CommentBo;
import app.admin.model.vo.CommentVo;
import app.admin.model.vo.CommentVoExample;
import app.admin.model.vo.ContentVo;
import app.admin.service.CommentService;
import app.admin.service.ContentService;
import app.common.constant.Constants;
import app.common.utils.DateKit;
import app.common.utils.TaleUtils;
import app.common.exceptions.TipException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER= LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentVoMapper commentVoMapper;

    @Autowired
    private ContentService contentService;

    //新增评论
    @Override
    @Transactional
    public String insertComment(CommentVo commentVo) {
        if (commentVo==null){
            return "评论对象为空";
        }
        if (StringUtils.isBlank(commentVo.getAuthor())){
            commentVo.setAuthor("热心网友");
        }
        if (StringUtils.isNotBlank(commentVo.getMail())&& !TaleUtils.isEmail(commentVo.getMail())){
            return "请输入正确的邮箱格式";
        }
        if (StringUtils.isBlank(commentVo.getContent())){
            return "评论内容不能为空";
        }
        if (commentVo.getContent().length()<5||commentVo.getContent().length()>2000){
            return "评论字数应在5-2000个字符";
        }
        if (null==commentVo.getCid()){
            return "没有关联的文章";
        }
        ContentVo contentVo=contentService.getContent(String.valueOf(commentVo.getCid()));
        if (null==contentVo){
            return "不存在的文章";
        }
        commentVo.setOwnerId(contentVo.getAuthorId());
        commentVo.setStatus("not_audit");
        commentVo.setCreated(DateKit.getCurrentUnixTime());
        commentVoMapper.insertSelective(commentVo);

        ContentVo temp=new ContentVo();
        temp.setCid(contentVo.getCid());
        temp.setCommentsNum(contentVo.getCommentsNum()+1);
        contentService.updateContentByCid(temp);

        return Constants.SUCCESS_RESULT;

    }


    //todo 子级评论在哪加载
    @Override
    public PageInfo<CommentBo> getCommentsOfArticle(Integer cid, int page, int limit) {
        if (null!=cid){
            PageHelper.startPage(page,limit);
            CommentVoExample example=new CommentVoExample();
            example.createCriteria().andCidEqualTo(cid).andParentEqualTo(0).andStatusIsNotNull().andStatusEqualTo("approved");
            example.setOrderByClause("coid desc");
            List<CommentVo> parents=commentVoMapper.selectByExampleWithBLOBs(example);//最上面一级的评论
            PageInfo<CommentVo> commentVoPageInfo=new PageInfo<>(parents);
            PageInfo<CommentBo> returnBo=copyPageInfo(commentVoPageInfo);
            if (parents.size()!=0){
                List<CommentBo> commentBos=new ArrayList<>(parents.size());
                parents.forEach(parent->{
                    CommentBo commentBo=new CommentBo(parent);
                    commentBos.add(commentBo);
                });
                returnBo.setList(commentBos);
            }
            return returnBo;
        }
        return null;
    }

    //复制原有的pageinfo信息，除去数据
    private <T> PageInfo<T> copyPageInfo(PageInfo ordinal) {
        PageInfo<T> returnBo = new PageInfo<T>();
        returnBo.setPageSize(ordinal.getPageSize());
        returnBo.setPageNum(ordinal.getPageNum());
        returnBo.setEndRow(ordinal.getEndRow());
        returnBo.setTotal(ordinal.getTotal());
        returnBo.setHasNextPage(ordinal.isHasNextPage());
        returnBo.setHasPreviousPage(ordinal.isHasPreviousPage());
        returnBo.setIsFirstPage(ordinal.isIsFirstPage());
        returnBo.setIsLastPage(ordinal.isIsLastPage());
        returnBo.setNavigateFirstPage(ordinal.getNavigateFirstPage());
        returnBo.setNavigateLastPage(ordinal.getNavigateLastPage());
        returnBo.setNavigatepageNums(ordinal.getNavigatepageNums());
        returnBo.setSize(ordinal.getSize());
        returnBo.setPrePage(ordinal.getPrePage());
        returnBo.setNextPage(ordinal.getNextPage());
        return returnBo;
    }

    @Override
    public PageInfo<CommentVo> getCommentOfArticle(CommentVoExample example, int page, int limit) {
        PageHelper.startPage(page,limit);
        List<CommentVo> commentVos=commentVoMapper.selectByExampleWithBLOBs(example);
        PageInfo<CommentVo> pageInfo=new PageInfo<>(commentVos);
        return pageInfo;
    }

    @Override
    public CommentVo getCommentById(Integer coid) {
        if (null!=coid){
            return commentVoMapper.selectByPrimaryKey(coid);
        }else {
            return null;
        }
    }

    @Override
    @Transactional
    public void delete(Integer coid, Integer cid) {
        if (null==coid){
            throw new TipException("主键为空");
        }
        commentVoMapper.deleteByPrimaryKey(coid);
        ContentVo contentVo=contentService.getContent(cid+"");
        if (null!=contentVo&&contentVo.getCommentsNum()>0){
            ContentVo temp=new ContentVo();
            temp.setCid(cid);
            temp.setCommentsNum(contentVo.getCommentsNum()-1);
            contentService.updateContentByCid(temp);
        }
    }

    @Override
    @Transactional
    public void update(CommentVo commentVo) {
        if (null!=commentVo&&null!=commentVo.getCoid()){
            commentVoMapper.updateByPrimaryKeyWithBLOBs(commentVo);
        }
    }
}
