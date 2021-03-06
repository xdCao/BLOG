package app.common.utils;
/*
    created by xdCao on 2017/10/10
*/



import app.admin.model.vo.UserVo;
import app.common.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CommonUtils {


    public static UserVo getLoginUser(HttpServletRequest request) {
        HttpSession session=request.getSession();
        if (session==null){
            return null;
        }
        return (UserVo)session.getAttribute(Constants.LOGIN_SESSION_KEY);
    }
}
