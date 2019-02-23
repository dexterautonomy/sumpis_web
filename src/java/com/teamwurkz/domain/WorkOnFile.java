package com.teamwurkz.domain;
import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;
public class WorkOnFile implements Serializable
{private MultipartFile file;
private String description;    
private String sendTo="Admin";
private String date;
public void setFile(MultipartFile file)
{this.file=file;}
public MultipartFile getFile()
{return file;}
public void setDescription(String description)
{this.description=description;}
public String getDescription()
{return description;}
public void setSendTo(String sendTo)
{this.sendTo=sendTo;}
public String getSendTo()
{return sendTo;}
public void setDate(String date)
{this.date=date;}
public String getDate()
{return date;}}