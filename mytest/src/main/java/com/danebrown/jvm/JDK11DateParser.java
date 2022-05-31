/*
 *
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
 *
 */

package com.danebrown.jvm;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class JDK11DateParser {


    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        String pattern = "YYYYMMddHHmmssSSSSSS";
        try {
            pattern = "YYYYMMddHHmmssSSSSSS";
            testPattern(now, pattern);
        } catch (Exception ex) {
            log.error(ex);
        }

//        try {
//            pattern = "YYYYMMddHHmmssNNNNNN";
//            testPattern(now, pattern);
//        }catch (Exception ex){
//            log.error(ex);
//        }
        try {
            pattern = "uuuuMMddHHMmSsSSSSSS";
            testPattern(now, pattern);
        }catch (Exception ex){
            log.error(ex);
        }
//        try {
//
//            pattern = "uuuuMMddHHMmSsNNNNNN";
//            testPattern(now, pattern);
//        }
//        catch (Exception ex){
//            log.error(ex);
//        }

    }

    /**
     * ERROR
     * patter: YYYYMMddHHmmssSSSSSS
     * format: 20220523174603767283
     * length: 20
     * patter: YYYYMMddHHmmssNNNNNN
     * format: 2022052317460363963767283000
     * length: 28
     * patter: uuuuMMddHHMmSsSSSSSS
     * format: 202205231754673767283
     * length: 21
     * patter: uuuuMMddHHMmSsNNNNNN
     * format: 20220523175467363963767283000
     * length: 29
     * <p>
     * SUCCESS:
     * patter: YYYYMMddHHmmssSSSSSS
     * format: 20220523174628593280
     * length: 20
     * patter: YYYYMMddHHmmssNNNNNN
     * format: 2022052317462863988593280000
     * length: 28
     * patter: uuuuMMddHHMmSsSSSSSS
     * format: 2022052317546528593280
     * length: 22
     * patter: uuuuMMddHHMmSsNNNNNN
     * format: 202205231754652863988593280000
     * length: 30
     *
     * JDK8
     * 21
     * patter: YYYYMMddHHmmssSSSSSS
     * format: 20220523175400323000
     * length: 20
     * patter: uuuuMMddHHMmSsSSSSSS
     * format: 202205231755430323000
     * length: 21
     *
     * 22
     * patter: YYYYMMddHHmmssSSSSSS
     * format: 20220523175428518000
     * length: 20
     * patter: uuuuMMddHHMmSsSSSSSS
     * format: 2022052317554528518000
     * length: 22
     *
     * @param now
     * @param pattern
     */
    private static void testPattern(LocalDateTime now, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String nowString = formatter.format(now);
        System.out.println("patter: " + pattern);
        System.out.println("format: " + nowString);
        System.out.println("length: " + nowString.length());
    }

}
