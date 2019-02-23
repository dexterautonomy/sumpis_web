<%-- 
    Document   : inboxMessages
    Created on : 07-Oct-2017, 19:52:08
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Inbox</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="adminInbox?pg=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
        <p id="error_Inbox" style="text-align: center; color: red;">${error}</p>        
        <div style="margin-top: 20px; padding-bottom: 20px;">
            <div style="width: 65%; text-align: center; margin-left: auto; margin-right: auto;">        
                <form:form method="POST" action="adminFurther" commandName="ninthBeanObject" class="formClass">
                    <div class="inboxImageCentering" style="border: ${border}; padding: ${padding}; border-radius: ${radius};">
                        ${fileContent}
                    
                        <div style="text-align: center;">
                            <form:textarea id="txt_Area" cols="100" rows="20" path="fileContent" style="display: none;"/><br>                     
                            <form:button id="button_A" class="input" name="choice" value="download" style="display: ${display};">Download</form:button>
                            <form:button id="button_B" class="input" name="choice" value="forwardTo" style="display: ${display};">Forward</form:button>
                            <form:button id="button_C" class="input" name="choice" value="newspost" style="display: ${display};">Post This</form:button>                
                        </div>
                    </div>
                </form:form>
            </div><br>
            <div style="text-align: center; max-width: 1300px; text-align: center; margin: 0 auto;">
                <c:forEach var="nxd" items="${cList}" varStatus="status">
                    <ul style="list-style: none; border-left: 4px solid red; border-bottom: 1px solid grey; border-top: 1px solid grey; 
                        border-right: 1px solid grey; padding: 10px; background-color: rgba(235,255,240, 0.7); border-radius: 5px;
                        display: inline-block; text-align: left;">
                        <li>
                            <span style="font-weight: bold;">Sender:</span> @${sender[status.index]} 
                            <span style="font-weight: bold;">Date:</span> ${date[status.index]}
                            <span style="color: red; font-weight: bold;">${stat[status.index]}</span><hr>                            
                            Subject: <span style="font-weight: bold;">${subject[status.index]}</span>
                            
                            <form:form action="adminFileReader?admfr=${pg}" method="post" commandName="tenthBeanObject">
                                <form:hidden path="nid" value="${nxd}"/><br>
                                <div style="text-align: center;">
                                    <form:button class="input" name="choice" value="read">Read</form:button>
                                    <form:button class="input" name="choice" value="download">Download</form:button>
                                    <form:button class="input" name="choice" value="forwardto">Forward</form:button>
                                    <form:button class="input" name="choice" value="newspost">Post This</form:button>
                                    <form:button class="input" name="choice" value="delete">Delete</form:button>
                                </div>
                            </form:form><hr>
                        </li>
                    </ul>
                </c:forEach>
            </div>
            <p style="text-align: center;">${prev}${next}</p>
        </div>
    </body>
</html>