package app.common.actuator;

/**
 * created by xdCao on 2017/10/25
 */

public class ReqInfo {

    private String url;
    private String ip;
    private String className;
    private String methodName;
    private String args;
    private long costTime;

    public ReqInfo(){}

    public ReqInfo(String url, String ip, String className, String methodName, String args,int time) {
        this.url = url;
        this.ip = ip;
        this.className = className;
        this.methodName = methodName;
        this.args = args;
        this.costTime=time;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
