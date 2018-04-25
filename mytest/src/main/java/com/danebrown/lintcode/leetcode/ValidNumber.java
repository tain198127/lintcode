package main.java.com.danebrown.lintcode.leetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ValidNumber {
    public static void main(String[] args) {
        ValidNumber vn = new ValidNumber();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = reader.readLine();
                if (input.equals("quit"))
                    break;
                boolean b = vn.testStr(input);
                System.out.println(String.format("%s 是 %s", input, b));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }


    }

    public boolean testStr(String s) {
        String str = s.trim();
        if (str.isEmpty())
            return false;
        char c = str.charAt(str.length() - 1);
        int endcode = (int) c;
        System.out.println(endcode);
        if (endcode < 46 || endcode > 57)
            return false;

        boolean rst = false;
        try {

            double d = Double.valueOf(str);
            rst = true;
        } catch (Exception ex) {
            rst = false;
        }
        return rst;
    }


}
