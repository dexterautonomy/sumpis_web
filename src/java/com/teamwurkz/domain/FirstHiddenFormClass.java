package com.teamwurkz.domain;
public class FirstHiddenFormClass
{
    private String titleOfMessage;
    private String date;
    private String choice;
    private int nid;
    
    public void setTitleOfMessage(String titleOfMessage)
    {
        this.titleOfMessage=titleOfMessage;
    }
    
    public String getTitleOfMessage()
    {
        return titleOfMessage;
    }
    
    public void setDate(String date)
    {
        this.date=date;
    }
    
    public String getDate()
    {
        return date;
    }
    
    
    public void setChoice(String choice)
    {
        this.choice=choice;
    }

    public String getChoice()
    {
        return choice;
    }
    
    public void setNid(int nid)
    {
        this.nid=nid;
    }

    public int getNid()
    {
        return nid;
    }
}