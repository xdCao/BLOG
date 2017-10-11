package app.admin.service;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.model.vo.UserVo;

public interface UserService {

    Integer insertUser(UserVo userVo);

    UserVo queryUserById(Integer uid);

    UserVo login(String userName,String password);

    void updateByUid(UserVo userVo);

}
