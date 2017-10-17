package app.common.controller;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.model.vo.UserVo;
import app.common.utils.CommonUtils;
import app.common.cache.MapCache;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    public static String THEME="themes/default";

    protected MapCache cache=MapCache.getInstance();

    //主页的页面主题
    public String render(String viewName){
        return THEME+"/"+viewName;
    }

    public BaseController title(HttpServletRequest request,String title){
        request.setAttribute("title",title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request,String keywords){
        request.setAttribute("keywords",keywords);
        return this;
    }

    public UserVo getUser(HttpServletRequest request){
        return CommonUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.getUser(request).getUid();
    }

    public String renderError(){
        return "comm/error_404";
    }

    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    public void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }



}
