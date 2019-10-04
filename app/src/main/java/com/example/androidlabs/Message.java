package com.example.androidlabs;

class Message {
    private String text;
    private boolean IsSender;
    private long id;

    Message(String s, boolean IsSender, long id){
        this.text = s;
        this.IsSender = IsSender;
        this.id = id;
    }

    String getText(){
        return text;
    }
    boolean IsSender(){
        return IsSender;
    }
}
