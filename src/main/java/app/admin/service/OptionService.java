package app.admin.service;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.model.vo.OptionVo;

import java.util.List;
import java.util.Map;

public interface OptionService {

    void insertOption(OptionVo optionVo);

    void insertOption(String name,String value);

    List<OptionVo> getOptions();

    void saveOptions(Map<String,String> options);

    OptionVo getOptionByName(String name);
}
