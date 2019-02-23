package com.teamwurkz.domain;
public class FieldClass
{
    private String field;
    private int count;
    private String choice;
    private String length;
    
    public void setField(String field)
    {
        this.field=field;
    }
    public String getField()
    {
        return field;
    }
    public void setChoice(String choice)
    {
        this.choice=choice;
    }
    public String getChoice()
    {
        return choice;
    }
    public void setLength(String length)
    {
        this.length=length;
    }
    public String getLength()
    {
        return length;
    }
    public void setCount(int count)
    {
        this.count=count;
    }
    public int getCount()
    {
        return count;
    }
}