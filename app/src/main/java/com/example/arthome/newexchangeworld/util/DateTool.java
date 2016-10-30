package com.example.arthome.newexchangeworld.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arthome on 2016/10/30.
 */

public enum  DateTool {
    INSTANCE;

    public boolean isSameDay(Date dateL, Date dateR){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(dateL).equals(simpleDateFormat.format(dateR));
    }
}
