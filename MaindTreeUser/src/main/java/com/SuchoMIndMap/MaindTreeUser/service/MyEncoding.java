package com.SuchoMIndMap.MaindTreeUser.service;

import org.springframework.stereotype.Service;

@Service
public class MyEncoding {
    public String encode(String name, String phNo, String pass) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        for (i = 0; i < phNo.length() &&
                i < name.length() &&
                i < pass.length(); i++) {
            char c = (char) (phNo.charAt(i) + name.charAt(i) + pass.charAt(i));
            s.append(c);
        }
        if(i<pass.length()) s.append(pass.substring(i+1,pass.length()));
        return s.toString();
    }
}
