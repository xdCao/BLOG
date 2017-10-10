package app.admin.service;

import app.admin.model.ArchiveBo;
import app.admin.model.BackResponseBo;
import app.admin.model.MetaDto;
import app.admin.model.StatisticsBo;
import app.admin.model.vo.CommentVo;
import app.admin.model.vo.ContentVo;


import java.util.List;

/*
    created by xdCao on 2017/10/10
*/
//站点服务
public interface SiteService {

    //最新评论
    List<CommentVo> recentComments(int limit);

    //最新发表的文章
    List<ContentVo> recentContents(int limit);

    //查询一条评论
    CommentVo getComment(Integer coid);

    //系统备份
    BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception;

    //获取后台统计数据
    StatisticsBo getStatistics();

    //查询文章归档
    List<ArchiveBo> getArchives();

    //获取分类、标签列表
    List<MetaDto> metas(String type, String orderBy, int limit);

}
