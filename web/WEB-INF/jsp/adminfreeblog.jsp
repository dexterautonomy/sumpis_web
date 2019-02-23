<%-- 
    Document   : adminfreeblog
    Created on : 18-Jan-2018, 16:53:54
    Author     : DEXTER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin SumBlog</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div class="memberdiv" style="padding-top: 15px; text-align: center;">
            <a href="adminfeaturedmvp" style="font-size: 40px; font-weight: bold;">Admin</a><br><br>
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
            <span style="font-size: 22px;">Admin: ${admin}</span>&nbsp;
            <a href="logout" style="font-size: 14px;">Logout</a>
        </div>
            
        <p style="text-align: center; color: green; font-size: 28px; font-weight: bold;">
            Blogging Platform
        </p>
        
        <p style="text-align: center; color: red; font-size: 18px;">${errorZZ}</p>
        <div style="text-align: center; margin: 0 auto; max-width: 1000px; margin-bottom: 50px;">
            <form:form action="adminmvpsumblog" method="POST" commandName="freeBlogBeanObject" cssStyle="" enctype="multipart/form-data">
                Category:<br>
                <form:select path="niche" cssClass="input_Field">
                    <form:options items="${categoryList}"/>
                </form:select><br><br>
                Title<br><form:input path="title" placeholder="Title" size="40" cssClass="input_Field" cssStyle="text-align: center;"/><br><br>
                Content<br>
                <form:textarea id="txt_Area_InformAll" path="content" placeholder="Write post" cols="100" rows="20"/><br><br>
                Content Image: <input type="file" name="contentImage"/>                
                <form:button class="input" name="choice" value="add_Image">Upload</form:button><br><br>
                Cover Image: <input type="file" name="coverImage"/><br><br>
                <form:button id="post_Message" class="input" name="choice" value="post_Story">Post</form:button><br><br>
            </form:form>
        </div>        
    </body>
</html>