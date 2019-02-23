<%-- 
    Document   : admincategorypage
    Created on : 20-Dec-2017, 22:02:14
    Author     : DEXTER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sumblog categories</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="text-align: center; float: left; padding: 5px;">
                <a href="categoryselection"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right;">
                <form:form action="adminSearchController" method="post" commandName="eleventhBeanObject" cssClass="adminSearch">
                    <span class="input_Label">Search by username</span> @
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <div id="search_Info" style="color: yellow; font-size: 15px;"></div>
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
        <div class="admin">
            Admin: ${admin} &nbsp; <a href="logout" style="font-size: 14px;">Logout</a>
        </div>
        
        <p style="color: green; font-size: 30px; text-align: center; text-decoration: underline; font-weight: bold;">Sumblog Categories</p>
        
        <div class="catdiv" style="margin-bottom: 100px;">            
            <form method="POST" action="${pageContext.request.contextPath}/categoryListController?pg=1">
                <button class="category1" name="action" value="writing">Creative Writing</button>
                <button class="category2" name="action" value="arts">Arts/History</button>
                <button class="category3" name="action" value="health">Health</button>
                <button class="category4" name="action" value="diy">DIY Section</button>
                <button class="category5" name="action" value="entertainment">Entertainment</button>
                <button class="category6" name="action" value="relpol">Religion/Politics</button>
                <button class="category7" name="action" value="info">Information/Updates</button>                
                <button class="category8" name="action" value="flt">Fashion/Lifestyle/Trends</button>
                <button class="category9" name="action" value="sports">Sports</button>
            </form>
        </div>
    </body>
</html>