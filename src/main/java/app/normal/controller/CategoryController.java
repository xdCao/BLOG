package app.normal.controller;
/*
    created by xdCao on 2017/10/17
*/

import app.admin.model.others.MetaDto;
import app.admin.model.others.Types;
import app.admin.model.vo.ContentVo;
import app.admin.service.ContentService;
import app.admin.service.MetaService;
import app.common.constant.Constants;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CategoryController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private MetaService metaService;

    @Autowired
    private ContentService contentService;

    @GetMapping(value = "/category/{keyword}")
    public String category(HttpServletRequest request, @PathVariable String keyword,
                           @RequestParam(value = "limit",defaultValue = "12") Integer limit){
        return this.category(request,keyword,limit,1);
    }

    @GetMapping(value = "/category/{keyword}/{page}")
    public String category(HttpServletRequest request, @PathVariable String keyword,
                           @RequestParam(value = "limit",defaultValue = "12") Integer limit,
                           @PathVariable Integer page){
        page=page<0||page> Constants.MAX_PAGE?1:page;
        MetaDto metaDto=metaService.getMeta(Types.CATEGORY.getType(),keyword);
        if (null==metaDto){
            return this.renderError();
        }
        PageInfo<ContentVo> pageInfo=contentService.getArticles(metaDto.getMid(),page,limit);
        request.setAttribute("articles",pageInfo);
        request.setAttribute("meta",metaDto);
        request.setAttribute("type","分类");
        request.setAttribute("keyword",keyword);
        return this.render("page-category");
    }



}
