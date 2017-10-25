package app.common.aspect;

import app.common.actuator.ActuatorService;
import app.common.actuator.ReqInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 借口aop
 * Created by wangq on 2017/3/24.
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class);

    @Autowired
    private ActuatorService actuatorService;

    private ReqInfo reqInfo;

    private Long start;


    @Pointcut("execution(public * app.*.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        LOGGER.info("URL : " + request.getRequestURL().toString() + ",IP : " + request.getRemoteAddr() + ",CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + ",ARGS : " + Arrays.toString(joinPoint.getArgs()));
        reqInfo=new ReqInfo();
        reqInfo.setUrl(request.getRequestURL().toString());
        reqInfo.setIp(request.getRemoteAddr());
        reqInfo.setClassName(joinPoint.getSignature().getDeclaringTypeName());
        reqInfo.setMethodName(joinPoint.getSignature().getName());
        reqInfo.setArgs(Arrays.toString(joinPoint.getArgs()));
        start=System.currentTimeMillis();
    }

    @AfterReturning(returning = "object", pointcut = "webLog()")
    public void doAfterReturning(Object object) throws Throwable {
        // 处理完请求，返回内容
        LOGGER.info("RESPONSE : " + object);
        long end = System.currentTimeMillis();
        reqInfo.setCostTime(end-start);
        actuatorService.addReqInfo(reqInfo);
    }
}
