<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Sumpis news</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="adminNewsController"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>
            <div style="float: right;">
                <form:form action="adminSearchController" method="post" commandName="eleventhBeanObject" cssClass="adminSearch">
                    <span class="input_Label">Search by username </span> @ 
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <span id="search_Info" style="text-align: center; font-size: 15px; color: yellow;">${outputError}</span>
                </form:form>
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
        <div class="admin" style="clear: both;">
            <span style="font-size: 22px; color: purple;">Admin: ${admin}</span>
            <a style="font-size: 14px;" href="logout">Logout</a>
        </div>
            
        <div style="text-align: center; margin-top: 40px; margin-bottom: 15px;">
            <form:form enctype="multipart/form-data" method="POST" action="adminNewsPostController" commandName="fourteenthBeanObject">
                <h2 id="outcome" style="color: green;">${newsposted}</h2>
                <h3 style="color: red;">${newserror}</h3>
                Title(20 words or less)<br>
                <form:input path="newsTitle" placeholder="Title" size="60" cssClass="input_Field" cssStyle="text-align: center;"/><br><br>
                Content<br>
                <form:textarea path="newsPost" placeholder="Content" cols="110" rows="20" id="txt_Area_InformAll"/><br><br>
                <form:checkbox path="check" value=""/>
                I have confirmed the veracity of this post before publishing.<br><br>
                <span style="color: red;">Warning: Uploaded file must not exceed 5MB</span><br><br> 
                Image File: <input type="file" name="file1"/>
                <form:button class="input" name="choice" value="add_Image">Upload</form:button><br><br><br>
                <form:button id="post_Message" class="input" name="choice" value="post_Story">Post</form:button><br><br>            
            </form:form>
        </div>
    </body>
</html>