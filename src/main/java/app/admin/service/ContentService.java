package app.admin.service;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.model.vo.ContentVo;
import app.admin.model.vo.ContentVoExample;
import com.github.pagehelper.PageInfo;

public interface ContentService {


    void publish(ContentVo contentVo);

    PageInfo<ContentVo> getContents(Integer page,Integer limit);

    ContentVo getContent(String id);

    void  updateContentByCid(ContentVo contentVo);

    PageInfo<ContentVo> getArticles(Integer mid,int page,int limit);

    PageInfo<ContentVo> getArticles(String keyword,int page,int limit);

    PageInfo<ContentVo> getArticlesWithPage(ContentVoExample example,int page,int limit);

    void deleteByCid(Integer cid);

    void updateArticle(ContentVo contentVo);

    void updateCategory(String ordinal,String newCat);


}
