package com.steven;

public class Main {

    public static void main(String[] args) {
        String s = "ab+c";
        String aa = "ababc";

        Pattern compile = Pattern.compile(s);
        boolean match = compile.match(aa);
        System.out.println(match);
    }
}
