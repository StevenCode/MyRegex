package com.company;

public class Main {

    public static void main(String[] args) {
        String s = "a\\n";

        Parser parser = new Parser(s);
        RegEx re = parser.parse();
        System.out.println(re.toString());
    }
}
