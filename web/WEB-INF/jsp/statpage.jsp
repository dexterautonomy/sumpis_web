<%-- 
    Document   : statpage
    Created on : 26-Jun-2018, 16:58:24
    Author     : DEXTER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Statistics</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="text-align: center; float: left; padding: 5px;">
                <a href="stat?pg=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
        
        <div style="padding: 10px;  display: ${dispz};">            
            <h2 style="text-align: center; text-decoration: underline; color: blue;">Page Traffic</h2>
            
            <div style="text-align: center; margin-bottom: 10px;">
                <form method="post" action='stat?pg=1'>
                    <input name="dateSearch" size="24" class="input_Field" style="text-align: center;" placeholder="Month Year e.g (JUNE 2018)" required="true"/>
                    <input type="submit" value="Search" class="input"/>
                </form>
            </div>
            
            <table cellpadding='10' align='center'>
                <tr>
                    <th class='tdmateheader'>Date</th>
                    <th class='tdmateheader'>Page</th>
                    <th class='tdmateheader'>Traffic/Month</th>
                </tr>
                    
                <c:forEach var="id" varStatus="status" items="${idList}">
                    <tr>
                        <td class='tdwriter'>
                            <div style="text-align: center;">${date[status.index]}</div>
                        </td>
                        <td class='tdmate'>
                            <div style="text-align: center;">${page[status.index]}</div>
                        </td>
                        <td class='tdstat'>
                            <div style="text-align: center;">${count[status.index]}</div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <p style="text-align: center;">${prev}&nbsp;&nbsp;&nbsp;${next}</p>
        </div>
    </body>
</html>