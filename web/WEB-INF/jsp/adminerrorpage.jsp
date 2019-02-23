<%-- 
    Document   : adminerrorpage
    Created on : 09-Apr-2018, 17:56:17
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>        
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="text-align: center; float: left; padding: 5px;">
                <a href="/sumpis/adminControl"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>
            <div class="adminMenu">
                <a href="/sumpis">Index</a>&nbsp;&nbsp;
                <a href="adminControl">Home</a>&nbsp;&nbsp;
                <a href="usermessage">Compose</a>&nbsp;&nbsp;
                <a href="adminInbox?pg=1">Inbox</a><span class="adminnotification">${unreadMessages}</span>&nbsp;&nbsp;
                <a href="adminNewsController">Post</a>&nbsp;&nbsp;
                <a href="adminfeaturedmvp" style="color: yellow;">Blog</a>&nbsp;&nbsp;
                <a href="freelanceController">Sumblogger</a>&nbsp;&nbsp;
                <a href="sumpisnews?pg=1">Check Posts</a>&nbsp;&nbsp;
                <a href="categoryselection">Check Articles</a>&nbsp;&nbsp;
                <a href="getUsers?us=1">Members</a>&nbsp;&nbsp;
                <a href="adpanel?pg=1">Ad Panel</a>&nbsp;&nbsp;
                <a href="stat?pg=1">Statistics</a>&nbsp;&nbsp;
                <a href="generalinfo">Settings</a>
            </div>
        </div>        
        <div class="admin">
            Admin: ${admin} &nbsp; <a href="/sumpis/logout" style="font-size: 14px;">Logout</a>
        </div>
        
        <%--<div style="height: 100px; width: 300px; background-color: green; text-align: center; font-size: 20px; color: white; margin-left: auto; margin-right: auto; margin-top: 100px;">
                Google Ads
        </div><br>--%>
        
        <div style="text-align: center; margin-top: 100px;">
            <p style="font-family: Monotype Corsiva; color: red; font-size: 27px;">Oops!!! Sorry,${name} an error occured.</p>
            <span style="font-size: 18px; color: blue;">A wrong command was probably sent. Please use any tab above to resume control. Thanks.</span>
        </div>
        
        <div style="height: 100px; width: 300px; background-color: green; text-align: center; font-size: 20px; color: white; margin-left: auto; margin-right: auto; margin-top: 100px;">
                Google Ads
        </div>
    </body>
</html>