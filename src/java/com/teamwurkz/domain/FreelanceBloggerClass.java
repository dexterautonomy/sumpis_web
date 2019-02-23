package com.teamwurkz.domain;

import java.io.Serializable;

public class FreelanceBloggerClass implements Serializable
{
    private String username;
    private String[] category;
    private String choice;
    
    public void setUsername(String username)
    {
        this.username=username;
    }
    public String getUsername()
    {
        return username;
    }
    
    public void setCategory(String[] category)
    {
        this.category=category;
    }
    
    public String[] getCategory()
    {
        return category;
    }
    
    public void setChoice(String choice)
    {
        this.choice=choice;
    }
    public String getChoice()
    {
        return choice;
    }
}