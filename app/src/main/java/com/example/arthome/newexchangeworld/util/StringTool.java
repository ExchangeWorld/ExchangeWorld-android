package com.example.arthome.newexchangeworld.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthome on 2016/10/30.
 */

public enum StringTool {
    INSTANCE;

    public String getFirstPhotoURL(String arrayString) {
        arrayString = arrayString.substring(2, arrayString.length() - 2);   //去掉前後中括號
        if (arrayString.indexOf("\",\"") > 0) {     //多於兩個URL則取第一個
            arrayString = arrayString.substring(0, arrayString.indexOf("\",\""));
        }
        return arrayString;
    }

    public List<String> getAllPhotoURL(String arrayString) {
        List<String> urlList = new ArrayList<>();

        if (arrayString.indexOf("[\"") == 0)    // [" 位置在字串開頭 0
            arrayString = arrayString.substring(2, arrayString.length() - 2);   //去掉前後中括號

        int dotPosition = arrayString.indexOf("\",\"");
        while (dotPosition > 0) {         //若一直有找到, 則取第一個URL加入list
            urlList.add(arrayString.substring(0, dotPosition));
            arrayString = arrayString.substring(dotPosition + 3, arrayString.length());
            dotPosition = arrayString.indexOf("\",\"");
        }
        urlList.add(arrayString);   //將剩下的最後一個url加入list
        for (int i = 0; i < urlList.size(); i++) {
        }

        return urlList;
    }
}
