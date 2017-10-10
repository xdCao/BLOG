package app.admin.controller;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.service.SiteService;
import app.common.controller.BaseController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SiteService siteService;


    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        LOGGER.info("----------进入管理员首页-----------");

    }

}
