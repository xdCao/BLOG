package app.admin.service.impl;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.dao.AttachVoMapper;
import app.admin.dao.CommentVoMapper;
import app.admin.dao.ContentVoMapper;
import app.admin.dao.MetaVoMapper;
import app.admin.model.ArchiveBo;
import app.admin.model.BackResponseBo;
import app.admin.model.MetaDto;
import app.admin.model.StatisticsBo;
import app.admin.model.vo.CommentVo;
import app.admin.model.vo.CommentVoExample;
import app.admin.model.vo.ContentVo;
import app.admin.service.SiteService;

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
        LOGGER.debug("取出最近评论");
        if (limit<0||limit>10){
            limit=10;
        }
        CommentVoExample example=new CommentVoExample();

    }

    @Override
    public List<ContentVo> recentContents(int limit) {
        return null;
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
        return null;
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
