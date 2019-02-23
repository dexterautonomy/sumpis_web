package com.teamwurkz.domain;

import java.io.Serializable;

public class TestJson //implements Serializable
{
    String response;
    String report;
    
    public TestJson(String response, String report)
    {
        this.response=response;
        this.report=report;
    }
    
    public void setResponse(String response)
    {
        this.response=response;
    }
    
    public String getResponse()
    {
        return response;
    }
    
    public void setReport(String report)
    {
        this.report=report;
    }
    
    public String getReport()
    {
        return report;
    }
}