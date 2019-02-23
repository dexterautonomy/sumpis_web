<%-- 
    Document   : freelance
    Created on : 13-Dec-2017, 15:31:25
    Author     : DEXTER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Freelance Bloggers</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div class="memberdiv" style="overflow: auto;">            
            <div style="text-align: center; float: left; padding: 5px;">
                <a href="freelanceController"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
        
        <div style="text-align: center; margin-top: 10px; margin-left: auto; margin-right: auto;">        
            <form:form method="POST" action="blogPostHandler" commandName="freelanceBloggerClass">
                <p style="font-size: 25px; text-decoration: underline;">Manage SumBloggers</p>
                <p style="text-align: center; color: red; font-size: 19px;">${errorXX}</p>
                <table align="center">
                    <tr>
                        <td>
                            <div style="text-align: left; margin-bottom: 20px; font-weight: bold;">
                                USERNAME: @ <form:input path="username" placeholder="Enter username" size="35" cssClass="input_Field"/>
                            </div>
                            <div style="text-align: left; font-weight: bold;">
                                CATEGORY
                            </div><hr>
                            <div style='text-align: left; color: green; font-weight: bold;'>
                                <form:checkbox path="category" value="WRITING"/>Creative Writing
                                <form:checkbox path="category" value="ARTS"/>Arts
                                <form:checkbox path="category" value="HISTORY"/>History
                                <form:checkbox path="category" value="LITERATURE"/>Literature
                                <form:checkbox path="category" value="SPORTS"/>Sports<br><br>
                                <form:checkbox path="category" value="ENTERTAINMENT"/>Entertainment
                                <form:checkbox path="category" value="HEALTH"/>Health Tips
                                <form:checkbox path="category" value="DIY"/>DIY
                                <form:checkbox path="category" value="TRENDS"/>Global Trends<br><br>
                                <form:checkbox path="category" value="RELIGION"/>World Religion
                                <form:checkbox path="category" value="POLITICS"/>Global Politics
                                <form:checkbox path="category" value="FASHION"/>World Fashion<br><br>
                                <form:checkbox path="category" value="LIFESTYLE"/>Lifestyles
                                <form:checkbox path="category" value="UPDATES"/>Updates
                            </div><hr>
                        </td>
                    </tr>
                </table>
                <p>
                    <form:button id="post_Message" class="input"  name="choice" value="add">Register</form:button>
                    <form:button id="post_Message2" class="input"  name="choice" value="edit">Update</form:button>
                    <form:button id="XX1" class="input"  name="choice" value="ban">Ban</form:button>
                    <form:button id="XX2" class="input"  name="choice" value="block">Block</form:button>
                    <form:button id="XX3" class="input"  name="choice" value="unban">Unban</form:button>
                    <form:button id="XX4" class="input"  name="choice" value="unblock">Unblock</form:button>
                    <form:button id="XX4" class="input"  name="choice" value="delete">Remove</form:button>
                </p>
            </form:form>
        </div>
    </body>
</html>
