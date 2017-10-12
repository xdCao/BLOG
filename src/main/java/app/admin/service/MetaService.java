package app.admin.service;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.model.MetaDto;
import app.admin.model.vo.MetaVo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface MetaService {


    MetaDto getMeta(String type,String name);

    List<MetaDto> getMetaList(String type,String orderBy,int limit);

    List<MetaVo> getMetas(String types);

    Integer countMeta(Integer mid);

    void saveMeta(MetaVo metaVo);

    void saveMeta(String type,String name,Integer mid);

    void saveMetas(Integer cid, String category, String type);

    void delete(int mid);

    void update(MetaVo metaVo);


}
