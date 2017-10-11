package app.admin.model;
/*
    created by xdCao on 2017/10/10
*/

import java.io.Serializable;

public class StatisticsBo implements Serializable{

    private Long articles;
    private Long comments;
    private Long links;
    private Long attachs;

    public Long getArticles() {
        return articles;
    }

    public void setArticles(Long articles) {
        this.articles = articles;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLinks() {
        return links;
    }

    public void setLinks(Long links) {
        this.links = links;
    }

    public Long getAttachs() {
        return attachs;
    }

    public void setAttachs(Long attachs) {
        this.attachs = attachs;
    }

    @Override
    public String toString() {
        return "统计数据：{ "+"文章数： "+articles+" 评论数： "+comments+" 链接数： "+links+" attachs: "+attachs+"  }";
    }


}
