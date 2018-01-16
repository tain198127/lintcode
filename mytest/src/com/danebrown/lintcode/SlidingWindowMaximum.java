/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.danebrown.lintcode;

import java.util.*;

/**
 * Created by dane on 2017/8/9.
 */
public class SlidingWindowMaximum {
    public static void main(String[] arg){
        SlidingWindowMaximum test = new SlidingWindowMaximum();

        Map<int[],Integer> input = new HashMap<>();

        input.put(new int[]{1,2,7,7,2},3);
        input.put(new int[]{1,2,7,7,2},1);
        input.keySet().forEach(ints -> {
            int i = input.get(ints);
            ArrayList<Integer> result =test.maxSlidingWindow(ints,i);
            System.out.println(result);
        });

    }
    public ArrayList<Integer> maxSlidingWindow(int[] nums, int k) {
        // write your code here
        ArrayList<Integer> result = new ArrayList<>();
        Collections.fill(result,Integer.MIN_VALUE);
        List<Integer> args = new ArrayList<>();
        for(int i = 0; i < nums.length;i++){
            args.add(nums[i]);
        }
        int gap = nums.length - k+1;
        //int max = Collections.max(args.subList(0,k));
        //result.add(max);
        int max = Integer.MAX_VALUE;
        for(int i = 0; i < gap; i++){
            if(args.get(i+k-1)>=max){
                max = args.get(i+k-1);
                result.add(max);
            }
            else{
                max = Collections.max(args.subList(i,i+k-1));
                result.add(max);
            }

        }
        return result;
    }

}
