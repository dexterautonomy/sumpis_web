<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Messaging</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="usermessage"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>

            <div style="float: right;">
                <form:form action="adminSearchController" method="post" commandName="eleventhBeanObject" cssClass="adminSearch">
                    <span class="input_Label">Search by username </span>@
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
        <div style="text-align: center; border-bottom: 1px solid grey;" class="upload">
            <form:form  action="flc" enctype="multipart/form-data" method="post" commandName="seventhBeanObject">                
                <span class="uploadText">Forward file</span>&nbsp;(pdf, docx, doc, txt only)
                <table align="center">
                    <tr>
                        <td>File: <input type="file" name="file" required="true"/></td>
                        <td>To: @ 
                            <form:select path="sendTo" cssClass="input_Field">
                                <form:options items="${clients}"/>
                            </form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input id="upload_File" class="input" type="submit" value="Forward"/>
                        </td>
                    </tr>
                </table>
            </form:form>
        </div>
            
        <p id="outcome" style="color: red; text-align: center; font-weight: bold;">${outcome}</p>
        
        <div style="padding-bottom: 20px; margin-bottom: 60px;">        
            <form:form method="POST" action="wrc" commandName="eightBeanObject" class="formClass">
                To:<br>@                
                <form:select path="sendTo" cssClass="input_Field">
                    <form:options items="${clients}"/>
                </form:select>
                <br><br>
                
                <table align='center'>
                    <tr>
                        <td>
                            Subject:<br><form:input path="subject" placeholder="Subject" size="35" cssClass="input_Field" style="text-align: center;"/>
                        </td>
                    </tr>
                </table><br>                        
                Compose Message<br>
                <form:textarea id="txt_Area_InformAll" class="txtjst1" cols="115" rows="25" path="writeUp" placeholder="Write message here"/><br><br>
                <form:button id="post_Message" class="input" name="action" value="individual">Send Message</form:button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <form:button id="post_Message2" class="input" name="action" value="all">Send To All Users</form:button>                
            </form:form>
        </div>
    </body>
</html>