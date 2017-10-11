package app.admin.service.impl;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.dao.OptionVoMapper;
import app.admin.model.vo.OptionVo;
import app.admin.model.vo.OptionVoExample;
import app.admin.service.OptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class OptionServiceImpl implements OptionService {

    private static final Logger LOGGER= LoggerFactory.getLogger(OptionServiceImpl.class);

    @Autowired
    private OptionVoMapper optionVoMapper;

    @Override
    public void insertOption(OptionVo optionVo) {
        optionVoMapper.insertSelective(optionVo);
    }

    @Override
    public void insertOption(String name, String value) {
        OptionVo optionVo=new OptionVo();
        optionVo.setName(name);
        optionVo.setValue(value);
        if (optionVoMapper.selectByPrimaryKey(name)==null){
            optionVoMapper.insertSelective(optionVo);
        }else {
            optionVoMapper.updateByPrimaryKeySelective(optionVo);
        }
    }

    @Override
    public List<OptionVo> getOptions() {
        return optionVoMapper.selectByExample(new OptionVoExample());
    }

    @Override
    public void saveOptions(Map<String, String> options) {
        if (null!=options&&!options.isEmpty()){
            options.forEach(this::insertOption);
        }
    }

    @Override
    public OptionVo getOptionByName(String name) {
        return optionVoMapper.selectByPrimaryKey(name);
    }
}
