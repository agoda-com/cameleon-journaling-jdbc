package com.agoda.camelon;

import java.io.Console;

public class ChangeEventCapture {

    public void BeforeQuery(String s, String sql){
        System.out.println("BeforeQuery "+s +" sql"+sql);
    }

    public void AfterQuery(String s, String sql){
       System.out.println("AfterQuery "+s +" sql"+sql);
    }
}

