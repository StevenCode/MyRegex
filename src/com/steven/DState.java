package com.steven;

public class DState {
    NfaList nList;
    DState[] next=new DState[256];
    DState left;
    DState right;

    public DState(NfaList list)
    {
        nList=list;
    }

    public DState()
    {

    }

}
