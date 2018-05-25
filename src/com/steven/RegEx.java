package com.steven;

public class RegEx {
    public static final RegEx blank = new Blank();
    private boolean isPrefix = false;
    private boolean isSuffix = false;

    public boolean getIsPrefix() {
        return isPrefix;
    }

    public void setIsPrefix(boolean isPrefix) {
        this.isPrefix = isPrefix;
    }

    public boolean getIsSuffix() {
        return isSuffix;
    }

    public void setIsSuffix(boolean isSuffix) {
        this.isSuffix = isSuffix;
    }

    public RegExType type;
}
