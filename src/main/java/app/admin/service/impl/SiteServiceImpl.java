package app.admin.service.impl;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.dao.AttachVoMapper;
import app.admin.dao.CommentVoMapper;
import app.admin.dao.ContentVoMapper;
import app.admin.dao.MetaVoMapper;
import app.admin.model.*;
import app.admin.model.vo.*;
import app.admin.service.SiteService;

import com.github.pagehelper.PageHelper;
import com.sun.tools.javac.comp.Todo;
import com.sun.xml.internal.bind.v2.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER= LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    private CommentVoMapper commentVoMapper;

    @Autowired
    private ContentVoMapper contentVoMapper;

    @Autowired
    private AttachVoMapper attachVoMapper;

    @Autowired
    private MetaVoMapper metaVoMapper;


    @Override
    public List<CommentVo> recentComments(int limit) {
        LOGGER.debug("----------取出最近评论----------");
        if (limit<0||limit>10){
            limit=10;
        }
        CommentVoExample example=new CommentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1,limit);
        List<CommentVo> commentVoList=commentVoMapper.selectByExampleWithBLOBs(example);
        LOGGER.debug("----------取出最近评论成功----------");
        return commentVoList;
    }

    @Override
    public List<ContentVo> recentContents(int limit) {
        LOGGER.debug("----------取出最近内容----------");
        if (limit<0||limit>10){
            limit=10;
        }
        ContentVoExample example=new ContentVoExample();
        example.createCriteria().andStatusEqualTo(Types.PUBLISH.getType()).andTypeEqualTo(Types.ARTICLE.getType());
        example.setOrderByClause("created desc");
        PageHelper.startPage(1,limit);
        List<ContentVo> contentVos=contentVoMapper.selectByExample(example);
        LOGGER.debug("----------取出最近内容成功----------");
        return contentVos;
    }

    @Override
    public CommentVo getComment(Integer coid) {
        return null;
    }

    @Override
    public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
        return null;
    }

    @Override
    public StatisticsBo getStatistics() {
        LOGGER.debug("---------获取后台统计数据--------");
        StatisticsBo statisticsBo=new StatisticsBo();
        ContentVoExample contentVoExample=new ContentVoExample();
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        int articles=contentVoMapper.countByExample(contentVoExample);
        int comments = commentVoMapper.countByExample(new CommentVoExample());
        int attachs = attachVoMapper.countByExample(new AttachVoExample());
        MetaVoExample metaVoExample=new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
        int links = metaVoMapper.countByExample(metaVoExample);
        statisticsBo.setArticles((long) articles);
        statisticsBo.setAttachs((long) attachs);
        statisticsBo.setComments((long) comments);
        statisticsBo.setLinks((long) links);
        LOGGER.debug("----------返回后台统计数据---------");
        return statisticsBo;
    }

    @Override
    public List<ArchiveBo> getArchives() {
        return null;
    }

    @Override
    public List<MetaDto> metas(String type, String orderBy, int limit) {
        return null;
    }
}
