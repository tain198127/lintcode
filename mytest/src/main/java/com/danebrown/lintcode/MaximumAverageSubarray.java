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

/**
 * Created by dane on 2017/8/7.
 */

/**
 * source :http://www.lintcode.com/zh-cn/problem/maximum-average-subarray/
 */
public class MaximumAverageSubarray {
    /**
     * @param nums an array with positive and negative numbers
     * @param k    an integer
     * @return the maximum average
     */
    public double maxAverage(int[] nums, int k) {
        // Write your code here
        double height = Integer.MIN_VALUE;
        double low = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > height) {
                height = nums[i];
            }
            if (nums[i] < low) {
                low = nums[i];
            }
        }
        while (height - low > 1e-6) {
            double mid = (height + low) / 2.0;
            if (search(nums, k, mid)) {
                low = mid;
            } else {
                height = mid;
            }
        }
        return height;
    }

    private boolean search(int[] nums, int k, double mid) {
        double min = 0.0;
        double[] sum = new double[nums.length + 1];
        sum[0] = 0.0;
        for (int i = 1; i <= nums.length; i++) {
            sum[i] = sum[i - 1] + nums[i - 1] - mid;
            if (i >= k && sum[i] >= min) {
                return true;
            }
            if (i >= k) {
                min = Math.min(min, sum[i - k + 1]);
            }
        }
        return false;
    }
}
