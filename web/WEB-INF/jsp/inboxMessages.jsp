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
        <title>Inbox</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="userinbox?pg=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right; padding: 4px; border: 1px solid; border-radius: 2px;">
                <form:form action="searchController" method="post" commandName="eleventhBeanObject">
                    <span style="font-size: 14px;">Add a colleague/partner and work on files and contents together<hr>
                        <span class="input_Label">Search by username </span></span> @
                        <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <span id="search_Info" style="text-align: center; font-size: 15px; color: yellow;">${outputError}</span>
                </form:form>
            </div>
            <div class="bizX">
                <a href="/sumpis/">Index</a>&nbsp;&nbsp;
                <a href="registeredMemberControl">Home</a>&nbsp;&nbsp;
                <a href="usermessage">Compose</a>&nbsp;&nbsp;
                <a href="userinbox?pg=1">Inbox</a><span class="notification">${unreadMessages}</span>&nbsp;&nbsp;
                <a href="colleagueController">Request</a><span class="notification">${friendrequest}</span>&nbsp;&nbsp;
                <a href="newsController">Post</a>&nbsp;&nbsp;
                <a href="u_p">Profile</a>&nbsp;&nbsp;
                <a href="userpasswordchange">Settings</a>
            </div>
        </div>
        <div class="document2">
            <span style="font-size: 22px;">${details}</span>&nbsp;
            <a href="logout">Logout</a>
        </div><br>
        
        <%--ADVERT 1--%>
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adid}" varStatus="status" begin="0" end="2">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img  src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>
                    
        <div style="margin-top: 5px; margin-bottom: 150px;">            
            <p style="text-align: center; color: red; font-weight: bold;">${sendingerror}</p>
            <div style="width: 950px; text-align: center; margin-left: auto; margin-right: auto;">        
                <form:form method="POST" action="further" commandName="ninthBeanObject">
                    <div class="inboxImageCentering" style="border: ${border}; padding: ${padding}; border-radius: ${radius};">
                        ${fileContent}
                        
                        <div style="text-align: center;">
                            <form:textarea id="txt_Area" cols="100" rows="20" path="fileContent" style="display: none;"/><br>                     
                            <form:button id="button_A" class="input" name="choice" value="download" style="display: ${display};">Download</form:button>
                            <form:button id="button_B" class="input" name="choice" value="sendToInput" style="display: ${display};">Edit At Input</form:button>
                            <form:button id="button_C" class="input" name="choice" value="forwardTo" style="display: ${display};">Forward</form:button>
                            <form:button id="button_D" class="input" name="choice" value="newspost" style="display: ${display};">Post This</form:button>
                        </div>
                    </div>
                </form:form>
            </div><br>
            <div style="text-align: center; margin: 0 auto; max-width: 1300px;">
                <c:forEach var="nxd" items="${cList}" varStatus="status">
                    <ul style="list-style: none; border-left: 4px solid blue; border-bottom: 1px solid grey; border-top: 1px solid grey; 
                        border-right: 1px solid grey; padding: 10px; background-color: rgba(235,255,240,0.7); display: inline-block;
                        text-align: left; border-radius: 5px;">
                        <li>
                            <span style="font-weight: bold;">Sender:</span> @${sender[status.index]} 
                            <span style="font-weight: bold;">Date:</span> ${date[status.index]}
                            <span style="color: red; font-weight: bold;">${stat[status.index]}</span><hr>                            
                            Subject: <span style="font-weight: bold;">${subject[status.index]}</span>
                            
                            <form:form action="filereader?fr=${pg}" method="post" commandName="tenthBeanObject">
                                <form:hidden path="nid" value="${nxd}"/><br>
                                <div style="text-align: center;">
                                    <form:button class="input"  name="choice" value="read">Read</form:button>
                                    <form:button class="input"  name="choice" value="download">Download</form:button>
                                    <form:button class="input"  name="choice" value="forwardto">Forward</form:button>
                                    <form:button class="input"  name="choice" value="newspost">Post This</form:button>
                                    <form:button class="input"  name="choice" value="delete">Delete</form:button>
                                </div>
                            </form:form><hr>
                        </li>
                    </ul>
                </c:forEach>
            </div><br>
            
            <%--ADVERT 2--%>
            <div style="margin: 0 auto; text-align: center;">
                <c:forEach var="id" items="${adid}" varStatus="status" begin="3" end="5">
                    <ul style="list-style: none; display: inline-block;">
                        <li>
                            <a href="k?n=${id}">
                                <img  src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                            </a>
                        </li>
                    </ul>
                </c:forEach>
            </div>
            
            <p style="text-align: center;">${prev}${next}</p>
        </div>
    </body>
</html>