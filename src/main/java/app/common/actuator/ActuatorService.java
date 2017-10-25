package app.common.actuator;

import app.common.constant.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * created by xdCao on 2017/10/25
 */
@Service
public class ActuatorService {

    private int size=0;

    private Queue<ReqInfo> reqInfoList=new ConcurrentLinkedDeque<>();


    public void addReqInfo(ReqInfo reqInfo) {
        if (size> Constants.MAX_ACUATOR_QUEUE){
            reqInfoList.remove();
            size=size-1;
        }
        reqInfoList.add(reqInfo);
        size=size+1;
    }

    public Queue<ReqInfo> getReqInfoList() {
        return reqInfoList;
    }


}
