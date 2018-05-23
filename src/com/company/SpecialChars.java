package com.company;

public class SpecialChars extends RegEx{
    public char c;

    public SpecialChars(char c) {
        this.c = c;
        this.type = RegExType.SpecialChars;
    }

    @Override
    public String toString() {
        return "Special character::\\" + c;
    }
}
