package app.common.actuator;


import app.common.utils.GsonUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Queue;

/**
 * created by xdCao on 2017/10/25
 */
@ConfigurationProperties(prefix = "endpoints.reqinfo",ignoreUnknownFields = false)
public class ReqInfoEndPoint extends AbstractEndpoint<String> implements ApplicationContextAware{

    ApplicationContext context;

    public ReqInfoEndPoint() {
        super("reqinfo");
    }

    @Override
    public String invoke() {
        ActuatorService actuatorService=context.getBean(ActuatorService.class);
        //todo
        Queue<ReqInfo> reqInfoQueue = actuatorService.getReqInfoList();
        return GsonUtils.toJsonString(reqInfoQueue);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }
}
