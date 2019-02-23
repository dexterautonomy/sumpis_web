package com.teamwurkz.domain;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;

public class FreeBlogBeanObject implements Serializable
{
    private MultipartFile coverImage;
    private String title;
    private String niche;
    private String content;
    private MultipartFile contentImage;
    private String choice;
    private String date;
    
    public void setCoverImage(MultipartFile coverImage)
    {
        this.coverImage=coverImage;
    }
    
    public MultipartFile getCoverImage()
    {
        return coverImage;
    }
    
    public void setTitle(String title)
    {
        this.title=title;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setNiche(String niche)
    {
        this.niche=niche;
    }
    
    public String getNiche()
    {
        return niche;
    }
    
    public void setContent(String content)
    {
        this.content=content;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContentImage(MultipartFile contentImage)
    {
        this.contentImage=contentImage;
    }
    
    public MultipartFile getContentImage()
    {
        return contentImage;
    }
    
    public void setChoice(String choice)
    {
        this.choice=choice;
    }
    
    public String getChoice()
    {
        return choice;
    }
    
    public void setDate(String date)
    {
        this.date=date;
    }
    
    public String getDate()
    {
        return date;
    }
    
}
