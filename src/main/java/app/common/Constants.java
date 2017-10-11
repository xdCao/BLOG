package app.common;
/*
    created by xdCao on 2017/10/10
*/

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static Map<String, String> initConfig = new HashMap<>();

    public static final int MAX_POSTS = 9999;
    public static String LOGIN_SESSION_KEY="login_user";
    public static String AES_SALT = "0123456789abcdef";
    public static final String USER_IN_COOKIE = "S_L_ID";

}
