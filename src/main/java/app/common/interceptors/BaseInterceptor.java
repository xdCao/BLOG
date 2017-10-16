package app.common.interceptors;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.AdminCommons;
import app.admin.model.Types;
import app.admin.model.vo.OptionVo;
import app.admin.model.vo.UserVo;
import app.admin.service.OptionService;
import app.admin.service.UserService;
import app.common.cache.MapCache;
import app.common.constant.Constants;
import app.common.utils.Commons;
import app.common.utils.IPKit;
import app.common.utils.TaleUtils;
import app.common.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BaseInterceptor implements HandlerInterceptor{

    private static final Logger LOGGER= LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Autowired
    private UserService userService;

    @Autowired
    private OptionService optionService;

    private MapCache cache=MapCache.getInstance();

    @Autowired
    private Commons commons;

    @Autowired
    private AdminCommons adminCommons;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String uri=httpServletRequest.getRequestURI();
        LOGGER.info("UserAgent: {}", httpServletRequest.getHeader(USER_AGENT));
        LOGGER.info("用户访问地址：{}，来路地址：{}",uri, IPKit.getIpAddrByRequest(httpServletRequest));

        //请求的拦截处理
        UserVo userVo= TaleUtils.getLoginUser(httpServletRequest);
        if (null==userVo){
            Integer uid=TaleUtils.getCookieUid(httpServletRequest);
            if (null!=uid){
                userVo=userService.queryUserById(uid);
                httpServletRequest.getSession().setAttribute(Constants.LOGIN_SESSION_KEY,userVo);
            }
        }
        if (uri.startsWith("/admin")&&!uri.startsWith("/admin/login")&&null==userVo){
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/admin/login");
            return false;
        }

        //设置get请求的token
        if (httpServletRequest.getMethod().equalsIgnoreCase("get")){
            String csrf_token= UUID.UU64();
            cache.hashSet(Types.CSRF_TOKEN.getType(),csrf_token,uri,30*60);
            httpServletRequest.setAttribute("_csrf_token",csrf_token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        OptionVo ov = optionService.getOptionByName("site_record");
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
        httpServletRequest.setAttribute("option", ov);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
