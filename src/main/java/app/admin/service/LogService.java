package app.admin.service;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.model.vo.LogVo;

import java.util.List;

public interface LogService {

    void insertLog(LogVo logVo);

    void insertLog(String action,String data,String ip,Integer authorId);

    List<LogVo> getLogs(int page,int limit);


}
