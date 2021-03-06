package com.danebrown.calculus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by danebrown on 2021/3/5
 * mail: tain198127@163.com
 * Master公式
 * 用于分析递归函数的时间复杂度
 * 重点：只能用于子问题规模一致的情况
 *
 * @author danebrown
 */
@Data
@AllArgsConstructor
public class MasterRules {


    /**
     * 要处理的数据的数据量
     */
    private long N;
    /**
     * 每次调动耗用的时间，ms
     */
    private long T;
    /**
     * 子调用降频系数。例如100个数，如果每次递归做二分法，则b是2，如果做3分发，则b是3
     */
    private long b;
    /**
     * 递归次数
     */
    private long a;
    /**
     * 每次递归调用的时候，所用的额外的调用次数
     */
    private long d;

    private static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * 获取执行时间
     * @return
     */
    public double getOTime(){
        return getOVal()*T;
    }

    /**
     * 获取大O的算法值
     * @return
     */
    public double getOVal(){
        if (log(a, b) == b) {
            return (N^d)*Math.log(N);
        }
        if (log(a, b) < d) {
            return N^d;
        }
        return Math.pow(N,log(a,b));
    }
    /**
     * 获取大Ode数学表达式
     * @return
     */
    public String getO() {
        if (log(a, b) == b) {
            return "N^d*logN";
        }
        if (log(a, b) < d) {
            return "O(N^t)";
        }
        return "O(N^log(a,b))";

    }

}
