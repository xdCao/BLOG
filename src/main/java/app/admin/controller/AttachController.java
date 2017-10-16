package app.admin.controller;
/*
    附件管理
    created by xdCao on 2017/10/11
*/

import app.admin.model.others.LogActions;
import app.admin.model.others.RestResponseBo;
import app.admin.model.others.Types;
import app.admin.model.vo.AttachVo;
import app.admin.model.vo.UserVo;
import app.admin.service.AttachService;
import app.admin.service.LogService;
import app.common.utils.Commons;
import app.common.constant.Constants;
import app.common.utils.TaleUtils;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller("adminAttachController")
@RequestMapping(value = "/admin/attach")
public class AttachController extends BaseController{

    public static final String CLASSPATH = TaleUtils.getUploadFilePath();

    private static final Logger LOGGER= LoggerFactory.getLogger(AttachController.class);

    @Autowired
    private AttachService attachService;

    @Autowired
    private LogService logService;

    //附件页面
    @GetMapping(value = "")
    public String index(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "limit",defaultValue = "12") Integer limit){
        PageInfo<AttachVo> attachVoPageInfo=attachService.getAttachs(page,limit);
        request.setAttribute("attachs",attachVoPageInfo);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(),Commons.site_url()));
        request.setAttribute("max_file_size", Constants.MAX_FILE_SIZE/1024);
        return "admin/attach";
    }

    //上传文件
    @PostMapping(value = "/upload")
    @ResponseBody
    public RestResponseBo upload(HttpServletRequest request, @RequestParam("file")MultipartFile[] multipartFiles){
        UserVo userVo=this.getUser(request);
        Integer uid=userVo.getUid();
        LOGGER.info("用户"+userVo.getUsername()+"上传文件");
        List<String> errorFiles=new ArrayList<>();
        try {
            for (MultipartFile multipartFile:multipartFiles){
                String fname=multipartFile.getOriginalFilename();
                if (multipartFile.getSize()<=Constants.MAX_FILE_SIZE){
                    String fkey=TaleUtils.getFileKey(fname);
                    String ftype=TaleUtils.isImage(multipartFile.getInputStream())?Types.IMAGE.getType():Types.FILE.getType();
                    File file=new File(CLASSPATH+fkey);
                    try {
                        FileCopyUtils.copy(multipartFile.getInputStream(),new FileOutputStream(file));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    attachService.save(fname,fkey,ftype,uid);
                    LOGGER.info("文件："+fname+"上传成功");
                }else {
                    LOGGER.error("文件:"+fname+"上传失败");
                    errorFiles.add(fname);
                }
            }
        }catch (Exception e){
            return RestResponseBo.fail();
        }
        return RestResponseBo.ok(errorFiles);
    }

    //删除附件
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional
    public RestResponseBo delete(@RequestParam Integer id,HttpServletRequest request){
        try {
            AttachVo attachVo=attachService.selectById(id);
            if (null==attachVo){
                return RestResponseBo.fail("不存在该附件");
            }
            attachService.deleteById(id);
            boolean delete = new File(CLASSPATH + attachVo.getFkey()).delete();
            if (delete){
                logService.insertLog(LogActions.DELETE_FILE.getAction(),attachVo.getFkey(),request.getRemoteAddr(),this.getUid(request));
                LOGGER.info("删除文件： "+attachVo.getFname()+" 成功");
            }else {
                return RestResponseBo.fail("删除文件失败");
            }
        }catch (Exception e){
            String msg="删除文件失败";
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }





}
