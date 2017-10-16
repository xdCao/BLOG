package app.admin.controller;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.model.others.LogActions;
import app.admin.model.others.RestResponseBo;
import app.admin.model.others.Types;
import app.admin.model.vo.ContentVo;
import app.admin.model.vo.ContentVoExample;
import app.admin.model.vo.UserVo;
import app.admin.service.ContentService;
import app.admin.service.LogService;
import app.common.constant.Constants;
import app.common.exceptions.TipException;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("adminPageController")
@RequestMapping("/admin/page")
public class PageController extends BaseController{

    private static final Logger LOGGER= LoggerFactory.getLogger(PageController.class);

    @Autowired
    private ContentService contentService;

    @Autowired
    private LogService logService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request){
        ContentVoExample example=new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.PAGE.getType());
        PageInfo<ContentVo> contentVoPageInfo=contentService.getArticlesWithPage(example,1, Constants.MAX_POSTS);
        request.setAttribute("articles",contentVoPageInfo);
        return "admin/page_list";
    }

    @GetMapping(value = "/new")
    public String newPage(){
        return "admin/page_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editPage(@PathVariable String cid,HttpServletRequest request){
        ContentVo contentVo=contentService.getContent(cid);
        request.setAttribute("contents",contentVo);
        return "admin/page_edit";
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo publishPage(@RequestParam String title,@RequestParam String content,
                                      @RequestParam String status,@RequestParam String slug,
                                      @RequestParam(required = false) Integer allowComment,@RequestParam(required = false) Integer allowPing,
                                      HttpServletRequest request){
        UserVo userVo=this.getUser(request);
        ContentVo contentVo=new ContentVo();
        contentVo.setTitle(title);
        contentVo.setContent(content);
        contentVo.setStatus(status);
        contentVo.setSlug(slug);
        contentVo.setType(Types.PAGE.getType());

        if (null!=allowComment){
            contentVo.setAllowComment(allowComment==1);
        }
        if (null!=allowPing){
            contentVo.setAllowPing(allowPing==1);
        }
        contentVo.setAuthorId(userVo.getUid());

        //todo 这是个有代表性的地方，事务回滚与捕获自相矛盾
        try {
            contentService.publish(contentVo);
        }catch (Exception e){
            String msg="页面发布失败";
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
    public RestResponseBo modifyArticle(@RequestParam Integer cid,@RequestParam String title,
                                        @RequestParam String content,@RequestParam String status,
                                        @RequestParam String slug,@RequestParam(required = false) Integer allowComment,
                                        @RequestParam(required = false) Integer allowPing,
                                        HttpServletRequest request){
        UserVo userVo=this.getUser(request);
        ContentVo contentVo=new ContentVo();
        contentVo.setCid(cid);
        contentVo.setTitle(title);
        contentVo.setContent(content);
        contentVo.setStatus(status);
        contentVo.setSlug(slug);
        if (null!=allowComment){
            contentVo.setAllowComment(allowComment==1);
        }
        if (null!=allowPing){
            contentVo.setAllowPing(allowPing==1);
        }
        contentVo.setAuthorId(userVo.getUid());
        try {
            contentService.updateArticle(contentVo);
        }catch (Exception e){
            String msg="页面编辑失败";
            if (e instanceof TipException){
                msg=e.getMessage();
            }else {
                LOGGER.error(msg,e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam Integer cid,HttpServletRequest request){
        try {
            contentService.deleteByCid(cid);
            logService.insertLog(LogActions.DEL_PAGE.getAction(),cid+"",request.getRemoteAddr(),this.getUid(request));
        }catch (Exception e){
            String msg="页面删除失败";
            if (e instanceof TipException){
                msg=e.getMessage();
            }else {
                LOGGER.error(msg,e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }



}
