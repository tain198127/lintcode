package com.danebrown.lentcode;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by danebrown on 2021/9/8
 * mail: tain198127@163.com
 *两只队伍打比赛，A队只投两分球，命中率100%，百发百中。B队只投三分球，命中率66.66%。请问如果打一场比赛，A和B谁能赢？
 *
 *
 *
 * 这里要稍微补充一下篮球规则，投不中弹框而出的叫做篮板球，两边的球员要进行争抢，这里假设两边球员的争抢能力相同。谁会赢
 * @author danebrown
 */
public class BasketballTest {
    public static void main(String[] args) {
        BasketballTest basketballTest = new BasketballTest();
        basketballTest.test();
    }

    public void test(){
        Integer scoreA = 0;
        Integer scoreB = 0;
        Integer count = 100000;
        int aTimes = 0;
        int bTimes = 0;
        String right = ballRight();
        while (--count > 0) {
            while (true) {
                if ("A".equals(right)) {
                    scoreA += 2;
                    aTimes++;
                    right = "B";
                    break;
                } else {
                    bTimes++;
                    boolean pitch = pitch();
                    if (pitch) {
                        scoreB += 3;
                        right = "A";

                        break;
                    } else {
                        right = ballRight();
                    }
                }
            }
        }
        int bScore = (int) Math.ceil((double) bTimes*(double) 3 *(2D/3D));
        System.out.printf("A 得分 %d,A进攻次数:%d,A计算得分 %d\n" , scoreA,aTimes,aTimes*2);
        System.out.printf("B 得分 %d,A进攻次数:%d B计算得分 %d\n" ,scoreB,bTimes,bScore);
    }

    // B投球
    private boolean pitch() {
        return ThreadLocalRandom.current().nextDouble() < 0.66D;
    }

    // 争夺球权
    private String ballRight() {
        return ThreadLocalRandom.current().nextBoolean()?"A":"B";
    }
}
