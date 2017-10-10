package app.common.controller;
/*
    created by xdCao on 2017/10/10
*/

import app.common.CommonUtils;
import app.common.model.UserVo;
import app.common.cache.MapCache;

import javax.servlet.http.HttpServletRequest;

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

}
