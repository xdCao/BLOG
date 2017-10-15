package app.admin.controller;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.model.RestResponseBo;
import app.admin.model.Types;
import app.admin.model.vo.MetaVo;
import app.admin.service.MetaService;
import app.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/links")
public class LinksController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(LinksController.class);

    @Autowired
    private MetaService metaService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request){
        List<MetaVo> metaVos=metaService.getMetas(Types.LINK.getType());
        request.setAttribute("links",metaVos);
        return "admin/links";
    }

    //保存友链
    @PostMapping(value = "save")
    @ResponseBody
    public RestResponseBo saveLink(@RequestParam String title,@RequestParam String url,
                                   @RequestParam String logo,@RequestParam Integer mid,
                                   @RequestParam(value = "sort",defaultValue = "0") int sort){
        try {
            MetaVo metaVo=new MetaVo();
            metaVo.setName(title);
            metaVo.setSlug(url);
            metaVo.setDescription(logo);
            metaVo.setSort(sort);
            metaVo.setType(Types.LINK.getType());
            if (null!=mid){
                metaVo.setMid(mid);
                metaService.update(metaVo);
            }else {
                metaService.saveMeta(metaVo);
            }
        }catch (Exception e){
            String msg="友链保存失败";
            LOGGER.error(msg,e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    //删除友链
    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam int mid){
        try {
            metaService.delete(mid);
        }catch (Exception e){
            String msg="友链删除失败";
            LOGGER.error(msg,e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }


}
