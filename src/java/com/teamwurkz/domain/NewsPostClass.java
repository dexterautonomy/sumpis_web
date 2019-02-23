package com.teamwurkz.domain;
import org.springframework.web.multipart.MultipartFile;

public class NewsPostClass
{
    private String newsTitle;
    private String newsPost;
    private MultipartFile file1;
    private String choice;
    private String check;
    private String link;
    private String date;//important
    private String date2;//important
    private String writer;//important
    private int id;//important
    private int spare;//very important
    private String table;//very important
    private String page;//very important
    
    private String space;//very important
    //private String quoteedate;//very important
    private String newsPost2;//very important
    
    
    private String addImage;
    private String deleteImage;
    
    private int nmb;
    
    public void setNewsTitle(String newsTitle)
    {
        this.newsTitle=newsTitle;
    }
    public String getNewsTitle()
    {
        return newsTitle;
    }

    public void setNewsPost(String newsPost)
    {
        this.newsPost=newsPost;
    }

    public String getNewsPost()
    {
        return newsPost;
    }

    public void setCheck(String check)
    {
        this.check=check;
    }

    public String getCheck()
    {
        return check;
    }

    public void setFile1(MultipartFile file1)
    {
        this.file1=file1;
    }

    public MultipartFile getFile1()
    {
        return file1;
    }

    public void setChoice(String choice)
    {
        this.choice=choice;
    }

    public String getChoice()
    {
        return choice;
    }

    public void setAddImage(String addImage)
    {
        this.addImage=addImage;
    }
        
    public String getAddImage()
    {
        return addImage;
    }

    public void setDeleteImage(String deleteImage)
    {
        this.deleteImage=deleteImage;
    }
        
    public String getDeleteImage()
    {
        return deleteImage;
    }

    public void setLink(String link)
    {
        this.link=link;
    }

    public String getLink()
    {
        return link;
    }

    public void setDate(String date)
    {
        this.date=date;
    }

    public String getDate()
    {
        return date;
    }
        
    public void setDate2(String date2)
    {
        this.date2=date2;
    }

    public String getDate2()
    {
        return date2;
    }
        
    public void setWriter(String writer)
    {
        this.writer=writer;
    }

    public String getWriter()
    {
        return writer;
    }
        
    public void setId(int id)
    {
        this.id=id;
    }

    public int getId()
    {
        return id;
    }
        
    public void setSpare(int spare)
    {
        this.spare=spare;
    }

    public int getSpare()
    {
        return spare;
    }
    
    public void setTable(String table)
    {
        this.table=table;
    }

    public String getTable()
    {
        return table;
    }
    
    public void setPage(String page)
    {
        this.page=page;
    }

    public String getPage()
    {
        return page;
    }
    
    /*    
    public void setQuotee(String quotee)
    {
        this.quotee=quotee;
    }

    public String getQuotee()
    {
        return quotee;
    }
        
    public void setQuoteedate(String quoteedate)
    {
        this.quoteedate=quoteedate;
    }

    public String getQuoteedate()
    {
        return quoteedate;
    }
    */
    
    public void setSpace(String space)
    {
        this.space=space;
    }

    public String getSpace()
    {
        return space;
    }
    
    public void setNewsPost2(String newsPost2)
    {
        this.newsPost2=newsPost2;
    }

    public String getNewsPost2()
    {
        return newsPost2;
    }
    
    public void setNmb(int nmb)
    {
        this.nmb=nmb;
    }

    public int getNmb()
    {
        return nmb;
    }
}