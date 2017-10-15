package app.admin.service;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.model.vo.CommentVo;
import app.admin.model.vo.CommentVoExample;
import com.github.pagehelper.PageInfo;

public interface CommentService {

    //保存评论
    String insertComment(CommentVo commentVo);

    //获取文章下的评论
    PageInfo<CommentVo> getCommentsOfArticle(Integer cid,int page,int limit);

    //获取文章下的评论
    PageInfo<CommentVo> getCommentOfArticle(CommentVoExample example,int page,int limit);

    //根据主键查找
    CommentVo getCommentById(int coid);

    //删除评论
    void delete(Integer coid,Integer cid);

    //更新评论状态
    void update(CommentVo commentVo);


}
