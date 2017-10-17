package app.normal.controller;
/*
    created by xdCao on 2017/10/17
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
public class CustomController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(CustomController.class);


    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    /**
     * 自定义页面,如关于的页面
     */
    @GetMapping(value = "/{pagename}")
    public String page(@PathVariable String pagename, HttpServletRequest request) {
        ContentVo contents = contentService.getContent(pagename);
        if (null == contents) {
            return this.renderError();
        }
        if (contents.getAllowComment()) {
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            PageInfo<CommentBo> commentsPaginator = commentService.getCommentsOfArticle(contents.getCid(), Integer.parseInt(cp), 6);
            request.setAttribute("comments", commentsPaginator);
        }
        request.setAttribute("article", contents);
        updateArticleHit(contents.getCid(), contents.getHits());
        return this.render("page");
    }

    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    protected void updateArticleHit(Integer cid, Integer chits) {
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
