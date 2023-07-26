package com.danebrown.lintcode;

import java.util.Stack;

public class MaxEnvelopes {
    /**
     * 只有右边的完全能装下左边的，才能返回false，相等或者有任意一个维度装不下，都返回true
     * @param envelopes
     * @param left
     * @param right
     * @return
     */
    public boolean isRightBigThanLeft(int[][] envelopes, int left, int right){
        int[] leftEnv = envelopes[left];
        int[] rightEnv = envelopes[right];
        if(rightEnv[0]>=leftEnv[0] && rightEnv[1]>=leftEnv[1]){
            return true;
        }
        return false;
    }

    //俄罗斯套娃，用单调栈
    public int maxEnvelopes(int[][] envelopes){
        int count =0;
        if(envelopes == null||envelopes.length ==0){
            return 0;
        }
        Stack<Integer> envs = new Stack<>();
        for(int i=0;i < envelopes.length;i++){
            if (!envs.isEmpty() && !isRightBigThanLeft(envelopes, envs.peek(),i)){
                int c= envs.size();
                count+= (c*(c-1))/2;
                envs.clear();
            }
            envs.push(i);
        }

        while (!envs.isEmpty()){
            int c= envs.size();
            count+= (c*(c-1))/2;
            envs.clear();
        }

        return count;
    }

    public static void main(String[] args) {
        MaxEnvelopes maxEnvelopes = new MaxEnvelopes();
        int i = maxEnvelopes.maxEnvelopes(new int[][]{new int[]{5,4},new int[]{6,4},new int[]{6,7},new int[]{2,3},new int[]{3,4},new int[]{4,5}});
        System.out.println(i);
    }
}
