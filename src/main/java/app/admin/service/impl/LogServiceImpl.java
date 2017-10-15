package app.admin.service.impl;
/*
    created by xdCao on 2017/10/11
*/

import app.admin.dao.LogVoMapper;
import app.admin.model.vo.LogVo;
import app.admin.model.vo.LogVoExample;
import app.admin.service.LogService;
import app.common.Constants;
import app.common.DateKit;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class LogServiceImpl implements LogService {

    private static final Logger LOGGER= LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private LogVoMapper logVoMapper;


    @Override
    public void insertLog(LogVo logVo) {
        logVoMapper.insert(logVo);
    }

    @Override
    public void insertLog(String action, String data, String ip, Integer authorId) {
        LogVo logVo=new LogVo();
        logVo.setAction(action);
        logVo.setData(data);
        logVo.setIp(ip);
        logVo.setAuthorId(authorId);
        logVo.setCreated(DateKit.getCurrentUnixTime());
        logVoMapper.insert(logVo);
    }

    @Override
    public List<LogVo> getLogs(int page, int limit) {
        LOGGER.debug("----------获取日志： 页数={}，行数={}---------",page,limit);
        if (page<=0){
            page=1;
        }
        if (limit<1||limit> Constants.MAX_POSTS){
            limit=10;
        }
        LogVoExample example=new LogVoExample();
        example.setOrderByClause("id desc");
        PageHelper.startPage(page, limit);
        List<LogVo> logVoList = logVoMapper.selectByExample(example);
        LOGGER.debug("----------返回日志----------");
        return logVoList;
    }
}
