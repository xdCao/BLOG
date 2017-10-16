package app.admin.controller;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.model.MetaDto;
import app.admin.model.RestResponseBo;
import app.admin.model.Types;
import app.admin.service.MetaService;
import app.common.constant.Constants;
import app.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/category")
public class CategoryController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private MetaService metaService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request){
        List<MetaDto> categories=metaService.getMetaList(Types.CATEGORY.getType(),null, Constants.MAX_POSTS);
        List<MetaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, Constants.MAX_POSTS);
        request.setAttribute("categories",categories);
        request.setAttribute("tags",tags);
        return "admin/category";
    }

    //保存分类
    @PostMapping(value = "save")
    @ResponseBody
    public RestResponseBo saveCategory(@RequestParam String cname,@RequestParam Integer mid){
        try {
            metaService.saveMeta(Types.CATEGORY.getType(),cname,mid);
        }catch (Exception e){
            String msg="分类保存失败";
            LOGGER.error(msg,e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    //删除分类
    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam int mid){
        try {
            metaService.delete(mid);
        }catch (Exception e){
            String msg="删除分类失败";
            LOGGER.error(msg,e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }


}
