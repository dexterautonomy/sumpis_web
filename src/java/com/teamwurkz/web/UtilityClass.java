package com.teamwurkz.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityClass
{
    public String getDate()
    {
        String daySuffix;
        SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a");
        String[] dateAndTime=sdf.format(new Date()).split(" ");
        int day=Integer.parseInt(dateAndTime[0]);
        switch (day) 
        {
            case 1:
                daySuffix=String.valueOf(day + "st");
                break;
            case 2:
                daySuffix=String.valueOf(day+"nd");
                break;
            case 3:
                daySuffix=String.valueOf(day+"rd");
                break;
            case 21:
                daySuffix=String.valueOf(day+"st");
                break;
            case 22:
                daySuffix=String.valueOf(day+"nd");
                break;
            case 23:
                daySuffix=String.valueOf(day+"rd");
                break;
            case 31:
                daySuffix=String.valueOf(day+"st");
                break;
            default:
                daySuffix=String.valueOf(day+"th");
                break;
        }
        return sdf.format(new Date()).replaceFirst(dateAndTime[0], daySuffix);
    }
}