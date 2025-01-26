/////////////code fix////////////


package com.aviva.aem.test;

public class Option {   
    protected String name;  
    private String value;     



    public Option(String value, String name) { 
        setName(name):
        setValue(value):
    }

    public String getName() {
        return this.name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    private void setValue(String value) {
        this.value = value;
    }
}


