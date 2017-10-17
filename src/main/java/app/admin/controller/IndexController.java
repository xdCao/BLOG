package app.admin.controller;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.model.others.StatisticsBo;
import app.admin.model.vo.CommentVo;
import app.admin.model.vo.ContentVo;
import app.admin.model.vo.LogVo;
import app.admin.service.LogService;
import app.admin.service.SiteService;
import app.common.controller.BaseController;

import app.common.utils.TaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller("adminIndexController")
@RequestMapping("admin")
public class IndexController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private LogService logService;


    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        LOGGER.info("----------进入管理员首页-----------");
        List<CommentVo> commentVoList=siteService.recentComments(5);
        List<ContentVo> contentVoList=siteService.recentContents(5);
        StatisticsBo statisticsBo=siteService.getStatistics();
        List<LogVo> logVoList=logService.getLogs(1,5);

        request.setAttribute("comments",commentVoList);
        request.setAttribute("articles",contentVoList);
        request.setAttribute("statistics",statisticsBo);
        request.setAttribute("logs",logVoList);
        LOGGER.info("---------返回管理员首页页面----------");
        return "admin/index";
    }

    //注销
    @PostMapping(value = "/logout")
    public void logout(HttpSession session,HttpServletResponse response){
        TaleUtils.logout(session,response);
    }


}
