package app.admin.model;
/*
    created by xdCao on 2017/10/16
*/

import app.admin.model.vo.CommentVo;

import java.util.List;

public class CommentBo extends CommentVo{

    private int levels;
    private List<CommentVo> children;

    public CommentBo(CommentVo commentVo) {
        setAuthor(commentVo.getAuthor());
        setAuthorId(commentVo.getAuthorId());
        setAgent(commentVo.getAgent());
        setCid(commentVo.getCid());
        setCoid(commentVo.getCoid());
        setContent(commentVo.getContent());
        setCreated(commentVo.getCreated());
        setIp(commentVo.getIp());
        setMail(commentVo.getMail());
        setOwnerId(commentVo.getOwnerId());
        setUrl(commentVo.getUrl());
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public List<CommentVo> getChildren() {
        return children;
    }

    public void setChildren(List<CommentVo> children) {
        this.children = children;
    }
}
