package app.common.actuator;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by xdCao on 2017/10/25
 */
@Configuration
public class ActuatorConfig {

    @Bean
    public Endpoint<String> reqinfo(){
        Endpoint<String> reqinfo=new ReqInfoEndPoint();
        return reqinfo;
    }

}
