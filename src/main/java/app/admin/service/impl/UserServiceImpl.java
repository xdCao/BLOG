package app.admin.service.impl;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.dao.UserVoMapper;
import app.admin.model.vo.UserVo;
import app.admin.model.vo.UserVoExample;
import app.admin.service.UserService;
import app.common.utils.TaleUtils;
import app.common.exceptions.TipException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserVoMapper userVoMapper;


    @Override
    public Integer insertUser(UserVo userVo) {
        Integer uid=null;
        if (StringUtils.isNotBlank(userVo.getUsername())&&StringUtils.isNotBlank(userVo.getEmail())){
            String encodedPwd= TaleUtils.MD5encode(userVo.getUsername()+userVo.getPassword());
            userVo.setPassword(encodedPwd);
            userVoMapper.insertSelective(userVo);
        }
        return userVo.getUid();
    }

    @Override
    public UserVo queryUserById(Integer uid) {
        UserVo userVo=null;
        if (uid!=null){
            userVo=userVoMapper.selectByPrimaryKey(uid);
        }
        return userVo;
    }

    @Override
    public UserVo login(String userName, String password) {
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            throw new TipException("用户名密码不能为空");
        }
        UserVoExample example=new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria().andUsernameEqualTo(userName);
        int count = userVoMapper.countByExample(example);
        if (count<1){
            throw new TipException("不存在该用户");
        }
        String pwd=TaleUtils.MD5encode(userName+password);
        criteria.andPasswordEqualTo(pwd);
        List<UserVo> userVos=userVoMapper.selectByExample(example);
        if (userVos.size()!=1){
            throw new TipException("用户名或密码错误");
        }
        return userVos.get(0);
    }

    @Override
    public void updateByUid(UserVo userVo) {
        if (null==userVo||userVo.getUid()==null){
            throw new TipException("更改的用户信息为空");
        }
        int i = userVoMapper.updateByPrimaryKeySelective(userVo);
        if (i!=1){
            throw new TipException("更新目标不只一个");
        }
    }
}
