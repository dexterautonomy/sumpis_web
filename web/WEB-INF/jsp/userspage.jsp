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
        <title>Sumpis user page</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="getUsers?us=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>

            <div style="float: right;">
                <form:form action="adminSearchController" method="post" commandName="eleventhBeanObject" cssClass="adminSearch">
                    <span class="input_Label">Search by username</span> @
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" reequired="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <div style="color: yellow; font-size: 15px;" id="search_Info"></div>
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
            
        <div>
            <p style="font-size: 28px; text-align: center; color: blue; text-decoration: underline; font-weight: bold;">Registered Users And Blacklist:</p>           
            <p >
            <table align="center" style="color: red; font-weight: bold;">
                <tr>
                    <td>Members:</td>
                    <td>${totalusers}</td>
                </tr>
                <%--<tr>
                    <td>Premium(Active):</td>
                    <td>${subscribers}</td>
                </tr>--%>
            </table>
            </p>
            <table align='center' cellpadding='9' style="text-align: center;">                
                <tr>
                    <th class='tdwriter'>Username</th>
                    <th class='tdmateheader'>Firstname</th>
                    <th class='tdmateheader'>Lastname</th>
                    <th class='tdmateheader'>Mobile</th>
                    <th class='tdmateheader'>Password</th>
                    <th class='tdmateheader'>On-Hold</th>
                    <th class='tdmateheader'>Post-Hold</th>
                    <th class='tdmateheader'>OTP</th>
                    <th class='tdmateheader'>Premium Exp</th>
                    <th class='tdmateheader'>RegDate</th>
                </tr>
                <tr>
                    <c:forEach var="data" items="${users}" varStatus="status">
                        <td class='tdmate'>${data}</td>                        
                            <c:if test="${(status.count mod 10)==0}">
                                </tr>
                                <tr>
                            </c:if>
                    </c:forEach>
                </tr>                
            </table>            
        </div>
        <p style="text-align: center;">${prev}${next}</p>
    </body>
</html>