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
import app.common.utils.TaleUtils;
import app.common.exceptions.TipException;
import app.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller("adminAuthController")
@RequestMapping(value = "admin")
public class AuthController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public String login(){
        return "admin/login";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public RestResponseBo doLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam(required = false) String remeber_me,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        Integer errorCount=cache.get("login_error_count");
        try {
            UserVo userVo=userService.login(username,password);
            request.getSession().setAttribute(Constants.LOGIN_SESSION_KEY,userVo);
            if (StringUtils.isNotBlank(remeber_me)){
                TaleUtils.setCookie(response,userVo.getUid());
            }
            logService.insertLog(LogActions.LOGIN.getAction(),null,request.getRemoteAddr(),userVo.getUid());
        }catch (Exception e){
            errorCount=null==errorCount?1:errorCount+1;
            if (errorCount>3){
                return RestResponseBo.fail("密码错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count",errorCount,10*60);
            String msg="登录失败";
            if (e instanceof TipException){
                msg=e.getMessage();
            }else {
                LOGGER.error(msg,e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }


    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(Constants.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(Constants.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }

}
