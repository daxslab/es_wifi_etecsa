package com.daxslab.eswifi_etecsa.utils;

public class StringTrimmer {

    public static String trim(String string, char ch){
        return trim(string, ch, ch);
    }

    public static String trim(String string, char leadingChar, char trailingChar){
        return string.replaceAll("^["+leadingChar+"]+|["+trailingChar+"]+$", "");
    }

    public static String trim(String string, String regex){
        return trim(string, regex, regex);
    }

    public static String trim(String string, String leadingRegex, String trailingRegex){
        return string.replaceAll("^("+leadingRegex+")+|("+trailingRegex+")+$", "");
    }

}
