package com.teamwurkz.domain;
public class WriteUs
{
    private String writeUp;
    private String subject;
    private String sendTo;
    //private String description;
    private String date="";
    private String action;
    
    public void setWriteUp(String writeUp)
    {
        this.writeUp=writeUp;
    }
    public String getWriteUp()
    {
        return writeUp;
    }
    public void setSubject(String subject)
    {
        this.subject=subject;
    }    
    public String getSubject()
    {
        return subject;
    }    
    public void setSendTo(String sendTo)
    {
        this.sendTo=sendTo;
    }    
    public String getSendTo()
    {
        return sendTo;
    }    
    /*public void setDescription(String description)
    {
        this.description=description;
    }    
    public String getDescription()
    {
        return description;
    }*/
    public void setDate(String date)
    {
        this.date=date;
    }    
    public String getDate()
    {
        return date;
    }
    
    public void setAction(String action)
    {
        this.action=action;
    }
    public String getAction()
    {
        return action;
    }
}