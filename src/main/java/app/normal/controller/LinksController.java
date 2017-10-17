package app.normal.controller;
/*
    created by xdCao on 2017/10/17
*/

import app.admin.model.others.Types;
import app.admin.model.vo.MetaVo;
import app.admin.service.MetaService;
import app.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LinksController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(LinksController.class);

    @Autowired
    private MetaService metaService;

    /**
     * 友链页
     *
     * @return
     */
    @GetMapping(value = "links")
    public String links(HttpServletRequest request) {
        List<MetaVo> links = metaService.getMetas(Types.LINK.getType());
        request.setAttribute("links", links);
        return this.render("links");
    }

}
