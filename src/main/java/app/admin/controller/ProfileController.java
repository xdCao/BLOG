package app.admin.controller;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.model.others.LogActions;
import app.admin.model.others.RestResponseBo;
import app.admin.model.vo.UserVo;
import app.admin.service.LogService;
import app.admin.service.UserService;
import app.common.constant.Constants;
import app.common.utils.GsonUtils;
import app.common.utils.TaleUtils;
import app.common.exceptions.TipException;
import app.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller("adminProfileController")
@RequestMapping(value = "/admin")
public class ProfileController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;


    @GetMapping(value = "/profile")
    public String profile(){
        return "admin/profile";
    }

    //修改个人信息
    @PostMapping(value = "/profile")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo saveProfile(@RequestParam String screenName,
                                      @RequestParam String email,
                                      HttpServletRequest request,
                                      HttpSession session){
        UserVo userVo=this.getUser(request);
        if (StringUtils.isNotBlank(screenName)&&StringUtils.isNotBlank(email)){
            UserVo temp=new UserVo();
            temp.setUid(userVo.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateByUid(temp);
            logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp),request.getRemoteAddr(),userVo.getUid());

            UserVo original=(UserVo)session.getAttribute(Constants.LOGIN_SESSION_KEY);
            original.setEmail(email);
            original.setScreenName(screenName);
            session.setAttribute(Constants.LOGIN_SESSION_KEY,original);
        }
        return RestResponseBo.ok();
    }


    //修改密码
    @PostMapping(value = "/password")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo upPwd(@RequestParam String oldPassword,
                                @RequestParam String password,
                                HttpServletRequest request,
                                HttpSession session){
        UserVo userVo=this.getUser(request);
        if (StringUtils.isBlank(oldPassword)||StringUtils.isBlank(password)){
            return RestResponseBo.fail("密码不能为空");
        }
        if (!userVo.getPassword().equals(TaleUtils.MD5encode(userVo.getUsername()+oldPassword))){
            return RestResponseBo.fail("新旧密码相同");
        }
        if (password.length()<6||password.length()>14){
            return RestResponseBo.fail("请输入6-14位的密码");
        }

       try {
           UserVo temp=new UserVo();
           temp.setUid(userVo.getUid());
           String pwd=TaleUtils.MD5encode(userVo.getUsername()+password);
           temp.setPassword(pwd);
           userService.updateByUid(temp);
           logService.insertLog(LogActions.UP_PWD.getAction(),GsonUtils.toJsonString(temp),request.getRemoteAddr(),temp.getUid());

           UserVo origin= (UserVo) session.getAttribute(Constants.LOGIN_SESSION_KEY);
           origin.setPassword(pwd);
           session.setAttribute(Constants.LOGIN_SESSION_KEY,origin);
           return RestResponseBo.ok();
       }catch (Exception e){
            String msg="修改密码失败";
            if (e instanceof TipException){
                msg=e.getMessage();
            }else {
                LOGGER.error(msg,e);
            }
            return RestResponseBo.fail(msg);


       }


    }

}
