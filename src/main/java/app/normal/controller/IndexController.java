package app.normal.controller;
/*
    created by xdCao on 2017/10/16
*/

import app.admin.model.others.ArchiveBo;
import app.admin.model.others.CommentBo;
import app.admin.model.others.Types;
import app.admin.model.vo.ContentVo;
import app.admin.model.vo.MetaVo;
import app.admin.service.CommentService;
import app.admin.service.ContentService;
import app.admin.service.MetaService;
import app.admin.service.SiteService;
import app.common.constant.Constants;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.json.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController{

    private static final Logger LOGGER= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ContentService contentService;


    //显示首页
    @GetMapping(value = "")
    public String index(HttpServletRequest request, @RequestParam(value = "limit",defaultValue = "12") Integer limit){
        return this.index(request,1,limit);
    }

    //分页显示首页
    @GetMapping(value = "/page/{p}")
    public String index(HttpServletRequest request,
                        @PathVariable Integer p,@RequestParam(value = "limit",defaultValue = "12") Integer limit){
        p=p<0||p> Constants.MAX_PAGE?1:p;
        PageInfo<ContentVo> articles=contentService.getContents(p,limit);
        request.setAttribute("articles",articles);
        if (p>1){
            this.title(request,"第"+p+"页");
        }
        return this.render("index");
    }






}
