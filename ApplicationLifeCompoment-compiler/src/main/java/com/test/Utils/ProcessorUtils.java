package com.test.Utils;

public class ProcessorUtils {
    private ProcessorUtils(){}
    public static boolean isEmpty(String value){
        if(value == null || value.length() == 0){
            return true;
        }
        return false;
    }
}

