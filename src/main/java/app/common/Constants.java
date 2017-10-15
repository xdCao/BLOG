package app.common;
/*
    created by xdCao on 2017/10/10
*/

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final int MAX_TITLE_COUNT = 22;//标题最大长度
    public static final int MAX_TEXT_COUNT = 200000;//文章最大长度
    public static final int MAX_FILE_SIZE = 1048576;//附件最大为1MB
    public static Map<String, String> initConfig = new HashMap<>();

    public static final int MAX_POSTS = 9999;//最大获取文章数目
    public static String LOGIN_SESSION_KEY="login_user";
    public static String AES_SALT = "0123456789abcdef";//aes加密
    public static final String USER_IN_COOKIE = "S_L_ID";

}
