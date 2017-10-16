package app.normal.controller;
/*
    created by xdCao on 2017/10/16
*/

import app.admin.model.others.CommentBo;
import app.admin.model.vo.ContentVo;
import app.admin.service.CommentService;
import app.admin.service.ContentService;
import app.common.constant.Constants;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ArticleController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;


    //文章页
    @GetMapping(value = {"/article/{cid}","article/{cid}.html"})
    public String article(HttpServletRequest request, @PathVariable String cid){
        ContentVo contentVo=contentService.getContent(cid);
        if (null==contentVo||"draft".equalsIgnoreCase(contentVo.getStatus())){
            return this.renderError();
        }
        request.setAttribute("article",contentVo);
        request.setAttribute("is_post",true);
        completeArticle(request,contentVo);
        updateArticleHit(contentVo.getCid(),contentVo.getHits());
        return this.render("post");
    }

    //文章页预览
    @GetMapping(value = {"article/{cid}/preview", "article/{cid}.html"})
    public String articlePreview(HttpServletRequest request,@PathVariable String cid){
        ContentVo contentVo=contentService.getContent(cid);
        if (null==contentVo){
            return this.renderError();
        }
        request.setAttribute("article",contentVo);
        request.setAttribute("is_post",true);
        completeArticle(request,contentVo);
        updateArticleHit(contentVo.getCid(),contentVo.getHits());
        return this.render("post");
    }


    /**
     * 抽取公共方法
     *
     * @param request
     * @param contents
     */
    private void completeArticle(HttpServletRequest request, ContentVo contents) {
        if (contents.getAllowComment()) {
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            request.setAttribute("cp", cp);
            PageInfo<CommentBo> commentsPaginator = commentService.getCommentsOfArticle(contents.getCid(), Integer.parseInt(cp), 6);
            request.setAttribute("comments", commentsPaginator);
        }
    }

    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHit(Integer cid, Integer chits) {
        Integer hits = cache.hashGet("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= Constants.HIT_EXCEED) {
            ContentVo temp = new ContentVo();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hashSet("article", "hits", 1);
        } else {
            cache.hashSet("article", "hits", hits);
        }
    }


}
