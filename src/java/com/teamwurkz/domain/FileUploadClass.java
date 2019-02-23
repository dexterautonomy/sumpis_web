package com.teamwurkz.domain;
import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;
public class FileUploadClass implements Serializable
{private MultipartFile file;
public void setFile(MultipartFile file)
{this.file=file;}
public MultipartFile getFile()
{return file;}}