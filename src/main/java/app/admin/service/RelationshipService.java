package app.admin.service;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.model.vo.RelationShipVoKey;

import java.util.List;

public interface RelationshipService {


    void deleteById(Integer cid, Integer mid);

    int countById(Integer cid,Integer mid);

    void insertVo(RelationShipVoKey relationShipVoKey);

    List<RelationShipVoKey> getRelationshipById(Integer cid,Integer mid);

}
