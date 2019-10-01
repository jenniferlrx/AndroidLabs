package com.example.androidlabs;

public class Message {
    String text;
    boolean IsSender;

    public Message(String s, boolean IsSender){
        this.text = s;
        this.IsSender = IsSender;
    }

    public String getText(){
        return text;
    }
    public boolean IsSender(){
        return IsSender;
    }
}
