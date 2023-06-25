package com.example.autoinvest.account;



public class InformationObject {
    private String name;
    private String value;
    public InformationObject(String name, String value){
        this.name=name;
        this.value=value;
    }

    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }
}
