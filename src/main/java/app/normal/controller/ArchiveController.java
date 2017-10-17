package app.normal.controller;
/*
    created by xdCao on 2017/10/17
*/

import app.admin.model.others.ArchiveBo;
import app.admin.service.SiteService;
import app.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArchiveController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(ArchiveController.class);

    @Autowired
    private SiteService siteService;


    /**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = "archives")
    public String archives(HttpServletRequest request) {
        List<ArchiveBo> archives = siteService.getArchives();
        request.setAttribute("archives", archives);
        return this.render("archives");
    }



}

