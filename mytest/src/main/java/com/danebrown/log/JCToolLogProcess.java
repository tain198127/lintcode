package com.danebrown.log;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

/**
 * Created by danebrown on 2021/7/18
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Component
@Log4j2
public class JCToolLogProcess {
    public final static Marker marker = MarkerManager.getMarker("JCToolLogProcess");

    public void runLog(int times) {
        for (int i = 0; i < times; i++) {
//            ThreadContext.putIfNull("logFileName", "JCToolLogProcess");
            log.info(marker, "JCToolLogProcess info :{}", i);
//            ThreadContext.remove("logFileName");
        }
    }

    public void runDebugLog(int times) {
        for (int i = 0; i < times; i++) {
//            ThreadContext.putIfNull("logFileName", "SyncLogProcess");
            log.debug(marker, "SyncLogProcess debug :{}", i);
//            ThreadContext.remove("logFileName");
        }

    }
}
