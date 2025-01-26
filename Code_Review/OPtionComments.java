
///////////////comments//////////

package com.aviva.aem.test;
//As per java naming convention, class should be named Option and not OPtion
//If intentional, make the same change for all 3 files.
public class OPtion {              
    protected String name;
    private String value;          

    public OPtion(){               //no usages

    }

    public OPtion(String value, String name) { // public function should not edit private or protected variables
        name = this.name;
        value = this.value;
    }
// standard getter and setter functions can be implemented
}


