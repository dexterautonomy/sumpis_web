package com.teamwurkz.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Service;

@Service
public class Users implements HttpSessionListener
{
    public static int concurrentUsers=0;

    @Override
    public void sessionCreated(HttpSessionEvent se)
    {
        concurrentUsers++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
        concurrentUsers--;
    }
    
    public static int getUsers()
    {
        return concurrentUsers;
    }
}