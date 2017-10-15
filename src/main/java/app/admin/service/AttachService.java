package app.admin.service;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.model.vo.AttachVo;
import com.github.pagehelper.PageInfo;

public interface AttachService {

    PageInfo<AttachVo> getAttachs(Integer page,Integer limit);

    void save(String fname,String fkey,String ftype,Integer authorId);

    AttachVo selectById(Integer id);

    void deleteById(Integer id);


}
