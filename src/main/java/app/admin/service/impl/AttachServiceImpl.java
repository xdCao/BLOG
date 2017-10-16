package app.admin.service.impl;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.dao.AttachVoMapper;
import app.admin.model.vo.AttachVo;
import app.admin.model.vo.AttachVoExample;
import app.admin.service.AttachService;
import app.common.utils.DateKit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttachServiceImpl implements AttachService {

    private static final Logger LOGGER= LoggerFactory.getLogger(AttachServiceImpl.class);

    @Autowired
    private AttachVoMapper attachVoMapper;

    @Override
    public PageInfo<AttachVo> getAttachs(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        AttachVoExample example=new AttachVoExample();
        example.setOrderByClause("id desc");
        List<AttachVo> attachVos = attachVoMapper.selectByExample(example);
        return new PageInfo<>(attachVos);
    }

    @Override
    @Transactional
    public void save(String fname, String fkey, String ftype, Integer authorId) {
        AttachVo attachVo=new AttachVo();
        attachVo.setFname(fname);
        attachVo.setFkey(fkey);
        attachVo.setFtype(ftype);
        attachVo.setAuthorId(authorId);
        attachVo.setCreated(DateKit.getCurrentUnixTime());
        attachVoMapper.insertSelective(attachVo);
    }

    @Override
    public AttachVo selectById(Integer id) {
        if (null!=id){
            return attachVoMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (null!=id){
            attachVoMapper.deleteByPrimaryKey(id);
        }
    }
}
