package com.steven;

public class Blank extends RegEx
{
    public Blank()
    {
        this.type = RegExType.Blank;
    }

    @Override
    public String toString()
    {
        return "";
    }
}
