package app.admin.controller;
/*
    created by xdCao on 2017/10/13
*/

import app.admin.model.others.LogActions;
import app.admin.model.others.RestResponseBo;
import app.admin.model.others.Types;
import app.admin.model.vo.ContentVo;
import app.admin.model.vo.ContentVoExample;
import app.admin.model.vo.MetaVo;
import app.admin.model.vo.UserVo;
import app.admin.service.ContentService;
import app.admin.service.LogService;
import app.admin.service.MetaService;
import app.common.exceptions.TipException;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller("adminArticleController")
@RequestMapping(value = "/admin/article")
public class ArticleController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ContentService contentService;

    @Autowired
    private MetaService metaService;

    @Autowired
    private LogService logService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "limit",defaultValue = "15") int limit,
                        HttpServletRequest request){
        ContentVoExample example=new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());
        PageInfo<ContentVo> pageInfo=contentService.getArticlesWithPage(example,page,limit);
        request.setAttribute("articles",pageInfo);
        return "admin/article_list";
    }

    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request){
        List<MetaVo> categories=metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories",categories);
        return "admin/article_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid,HttpServletRequest request){
        ContentVo contentVo=contentService.getContent(cid);
        request.setAttribute("contents",contentVo);
        List<MetaVo> categories=metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories",categories);
        request.setAttribute("active","article");
        return "admin/article_edit";
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo publishArticle(ContentVo contents,HttpServletRequest request){
        UserVo userVo=this.getUser(request);
        contents.setAuthorId(userVo.getUid());
        contents.setType(Types.ARTICLE.getType());
        if (StringUtils.isBlank(contents.getCategories()))
            contents.setCategories("默认分类");
        try {
            contentService.publish(contents);
        }catch (Exception e){
            String msg="文章发布失败";
            if (e instanceof TipException){
                msg=e.getMessage();
            }else {
                LOGGER.error(msg,e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "/modify")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo modifyArticle(ContentVo contents,HttpServletRequest request){

        UserVo userVo=this.getUser(request);
        contents.setAuthorId(userVo.getUid());
        contents.setType(Types.ARTICLE.getType());
        try {
            contentService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "文章编辑失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }


    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int cid,HttpServletRequest request){
        try {
            contentService.deleteByCid(cid);
            logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid+"", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章删除失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }


}
