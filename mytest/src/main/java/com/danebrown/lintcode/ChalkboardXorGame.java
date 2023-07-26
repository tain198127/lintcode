package com.danebrown.lintcode;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 黑板上写着一个非负整数数组 nums[i] 。
 *
 * Alice 和 Bob 轮流从黑板上擦掉一个数字，Alice 先手。
 * 如果擦除一个数字后，剩余的所有数字按位异或运算得出的结果等于 0 的话，
 * 当前玩家游戏失败。另外，如果只剩一个数字，按位异或运算得到它本身；如果无数字剩余，按位异或运算结果为0。
 *
 * 并且，轮到某个玩家时，如果当前黑板上所有数字按位异或运算结果等于 0 ，这个玩家获胜。
 *
 * 假设两个玩家每步都使用最优解，当且仅当 Alice 获胜时返回 true。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/chalkboard-xor-game
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class ChalkboardXorGame {
    public static void main(String[] args) {
        int[] nums = new int[ThreadLocalRandom.current().nextInt(1,100)];
        for (int i=0; i < nums.length;i++){
            nums[i] = ThreadLocalRandom.current().nextInt(1,100);
        }
        ChalkboardXorGame game = new ChalkboardXorGame();
        boolean result = game.xorGame(nums);
        System.out.println(result);

    }
    public boolean xorGame(int[] nums) {
        if(nums.length%2==0){
            return true;
        }
        int result=0;
        for(int i=0;i < nums.length;i++){
            result ^= nums[i];
        }
        return result == 0;
    }

}
