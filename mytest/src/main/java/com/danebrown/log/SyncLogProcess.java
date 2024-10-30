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
public class SyncLogProcess {
    public final static Marker marker = MarkerManager.getMarker("SyncLogProcess");

    public void runLog(int times) {
        for (int i = 0; i < times; i++) {
//            ThreadContext.putIfNull("logFileName", "SyncLogProcess");
            log.info(marker, "SyncLogProcess info :{}", i);
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
