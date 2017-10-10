package app.admin.service.impl;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.model.ArchiveBo;
import app.admin.model.BackResponseBo;
import app.admin.model.MetaDto;
import app.admin.model.StatisticsBo;
import app.admin.service.SiteService;
import app.common.model.CommentVo;
import app.common.model.ContentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER= LoggerFactory.getLogger(SiteServiceImpl.class);

    @Override
    public List<CommentVo> recentComments(int limit) {
        return null;
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
