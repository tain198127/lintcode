package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.slots.block.flow.TrafficShapingController;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsTrafficShapingController implements TrafficShapingController {
    @Override
    public boolean canPass(Node node, int acquireCount, boolean prioritized) {
        return false;
    }

    @Override
    public boolean canPass(Node node, int acquireCount) {
        return false;
    }
}
