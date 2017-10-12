package app.admin.service.impl;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.dao.RelationShipVoMapper;
import app.admin.model.vo.RelationShipVoExample;
import app.admin.model.vo.RelationShipVoKey;
import app.admin.service.RelationshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RelationshipServiceImpl implements RelationshipService {

    private static final Logger LOGGER= LoggerFactory.getLogger(RelationshipServiceImpl.class);

    @Autowired
    private RelationShipVoMapper relationShipVoMapper;

    @Override
    public void deleteById(Integer cid, Integer mid) {
        RelationShipVoExample example=new RelationShipVoExample();
        RelationShipVoExample.Criteria criteria = example.createCriteria();
        if (cid!=null){
            criteria.andCidEqualTo(cid);
        }
        if (mid!=null){
            criteria.andMidEqualTo(mid);
        }
        relationShipVoMapper.deleteByExample(example);
    }

    @Override
    public int countById(Integer cid, Integer mid) {
        LOGGER.debug("统计relationship cid={} ， mid={}",cid,mid);
        RelationShipVoExample example=new RelationShipVoExample();
        RelationShipVoExample.Criteria criteria = example.createCriteria();
        if (cid!=null){
            criteria.andCidEqualTo(cid);
        }
        if (mid!=null){
            criteria.andMidEqualTo(mid);
        }
        int count = relationShipVoMapper.countByExample(example);
        LOGGER.debug("统计relationship完毕： num={} ",count);
        return count;
    }

    @Override
    public void insertVo(RelationShipVoKey relationShipVoKey) {
        relationShipVoMapper.insert(relationShipVoKey);
    }

    @Override
    public List<RelationShipVoKey> getRelationshipById(Integer cid, Integer mid) {
        RelationShipVoExample example=new RelationShipVoExample();
        RelationShipVoExample.Criteria criteria = example.createCriteria();
        if (cid!=null){
            criteria.andCidEqualTo(cid);
        }
        if (mid!=null){
            criteria.andMidEqualTo(mid);
        }
        return relationShipVoMapper.selectByExample(example);
    }
}
