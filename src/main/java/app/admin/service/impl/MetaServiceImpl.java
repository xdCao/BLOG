package app.admin.service.impl;
/*
    created by xdCao on 2017/10/12
*/

import app.admin.dao.MetaVoMapper;
import app.admin.model.others.MetaDto;
import app.admin.model.others.Types;
import app.admin.model.vo.ContentVo;
import app.admin.model.vo.MetaVo;
import app.admin.model.vo.MetaVoExample;
import app.admin.model.vo.RelationShipVoKey;
import app.admin.service.ContentService;
import app.admin.service.MetaService;
import app.admin.service.RelationshipService;
import app.common.constant.Constants;
import app.common.exceptions.TipException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MetaServiceImpl implements MetaService {

    private static final Logger LOGGER= LoggerFactory.getLogger(MetaServiceImpl.class);

    @Autowired
    private MetaVoMapper metaVoMapper;

    @Autowired
    private ContentService contentService;

    @Autowired
    private RelationshipService relationshipService;


    @Override
    public MetaDto getMeta(String type, String name) {
        if (StringUtils.isNotBlank(type)&&StringUtils.isNotBlank(name)){
            return metaVoMapper.selectDtoByNameAndType(name,type);
        }
        return null;
    }

    @Override
    public List<MetaDto> getMetaList(String type, String orderBy, int limit) {
        if (StringUtils.isNotBlank(type)){
            if (StringUtils.isBlank(orderBy)){
                orderBy="count desc,a.mid desc";
            }
            if (limit<1||limit> Constants.MAX_POSTS){
                limit=10;
            }
            Map<String,Object> paraMap=new HashMap<>();
            paraMap.put("type",type);
            paraMap.put("order",orderBy);
            paraMap.put("limit",limit);
            return metaVoMapper.selectFromSql(paraMap);
        }
        return null;
    }

    @Override
    public List<MetaVo> getMetas(String types) {
        if (StringUtils.isNotBlank(types)){
            MetaVoExample example=new MetaVoExample();
            example.setOrderByClause("sort desc,mid desc");
            example.createCriteria().andTypeEqualTo(types);
            return metaVoMapper.selectByExample(example);
        }
        return null;
    }

    @Override
    public Integer countMeta(Integer mid) {
        return metaVoMapper.countWithSql(mid);
    }

    @Override
    public void saveMeta(MetaVo metaVo) {
        if (null!=metaVo){
            metaVoMapper.insertSelective(metaVo);
        }
    }

    @Override
    public void saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type)&&StringUtils.isNotBlank(name)){
            MetaVoExample example=new MetaVoExample();
            example.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
            List<MetaVo> metaVos=metaVoMapper.selectByExample(example);
            MetaVo meta;
            if (metaVos.size() != 0) {
                throw new TipException("已经存在该项");
            } else {
                meta = new MetaVo();
                meta.setName(name);
                if (null != mid) {
                    MetaVo original = metaVoMapper.selectByPrimaryKey(mid);
                    meta.setMid(mid);
                    metaVoMapper.updateByPrimaryKeySelective(meta);
//                    更新原有文章的categories
                    contentService.updateCategory(original.getName(),name);
                } else {
                    meta.setType(type);
                    metaVoMapper.insertSelective(meta);
                }
            }
        }
    }

    @Override
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdateMeta(cid, name, type);
            }
        }
    }

    @Override
    public void delete(int mid) {
        MetaVo metas = metaVoMapper.selectByPrimaryKey(mid);
        if (null != metas) {
            String type = metas.getType();
            String name = metas.getName();

            metaVoMapper.deleteByPrimaryKey(mid);

            List<RelationShipVoKey> rlist = relationshipService.getRelationshipById(null, mid);
            if (null != rlist) {
                for (RelationShipVoKey r : rlist) {
                    ContentVo contents = contentService.getContent(String.valueOf(r.getCid()));
                    if (null != contents) {
                        ContentVo temp = new ContentVo();
                        temp.setCid(r.getCid());
                        if (type.equals(Types.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, contents.getCategories()));
                        }
                        if (type.equals(Types.TAG.getType())) {
                            temp.setTags(reMeta(name, contents.getTags()));
                        }
                        contentService.updateContentByCid(temp);
                    }
                }
            }
            relationshipService.deleteById(null, mid);
        }
    }

    @Override
    public void update(MetaVo metaVo) {
        if (null!=metaVo&&null!=metaVo.getMid()){
            metaVoMapper.updateByPrimaryKeySelective(metaVo);
        }
    }

    private void saveOrUpdateMeta(Integer cid,String name,String type){
        MetaVoExample example=new MetaVoExample();
        example.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
        List<MetaVo> metaVos=metaVoMapper.selectByExample(example);
        int mid;
        MetaVo metaVo;
        if (metaVos.size()==1){
            metaVo=metaVos.get(0);
            mid=metaVo.getMid();
        }else if (metaVos.size()>1){
            throw new TipException("查询到多条数据");
        }else {
            metaVo=new MetaVo();
            metaVo.setSlug(name);
            metaVo.setName(name);
            metaVo.setType(type);
            mid=metaVoMapper.insertSelective(metaVo);
        }
        if (mid!=0){
            int count = relationshipService.countById(cid, mid);
            if (count==0){
                RelationShipVoKey relationShipVoKey=new RelationShipVoKey();
                relationShipVoKey.setCid(cid);
                relationShipVoKey.setMid(mid);
                relationshipService.insertVo(relationShipVoKey);
            }
        }
    }


    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

}
