package com.teamwurkz.domain;

public class LikeObject
{
    private String writer;
    private String headline;
    private String date;
    private String action;
    
    public void setWriter(String writer)
    {
        this.writer=writer;
    }
    public String getWriter()
    {
        return writer;
    }
    
    public void setHeadline(String headline)
    {
        this.headline=headline;
    }
    public String getHeadline()
    {
        return headline;
    }
    
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
