<%-- 
    Document   : commentaryPage
    Created on : 21-Jan-2018, 12:10:25
    Author     : DEXTER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${lastname}'s Reaction</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">            
        <div class="memberdiv" style="text-align: center;">
            <div style="float: left; padding: 5px;">
                <a href="registeredMemberControl"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>
            <div class="bizX" style="clear: both;">
                <a href="/sumpis/">Index</a>&nbsp;&nbsp;
                <a href="registeredMemberControl">Home</a>&nbsp;&nbsp;
                <a href="usermessage">Compose</a>&nbsp;&nbsp;
                <a href="userinbox?pg=1">Inbox</a><span class="notification">${unreadMessages}</span>&nbsp;&nbsp;
                <a href="colleagueController">Request</a><span class="notification">${friendrequest}</span>&nbsp;&nbsp;
                <a href="newsController">Post</a>&nbsp;&nbsp;
                <a href="u_p">Profile</a>&nbsp;&nbsp;
                <a href="ad_ctr?pg=1">Ad</a>&nbsp;&nbsp;
                <a href="userpasswordchange">Settings</a>
            </div>
        </div>
        <div class="document2">
            <span style="font-size: 22px;">${details}</span>
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
        
        <div style="text-align: center; margin-top: 40px; margin-bottom: 15px;">
            <form:form enctype="multipart/form-data" method="POST" action="submitCommentary" commandName="fourteenthBeanObject">
                <p style="color: red; font-size: 20px;">${commentaryimage}</p>                
                <form:hidden path="nmb" value="${n}"/>
                <form:hidden path="id" value="${pg}"/>
                <form:hidden path="spare" value="${pgn}"/>                
                Write a comment/reaction in the box below:<br>
                <form:textarea id="txt_Area_InformAll" path="newsPost" placeholder="Content" cols="90" rows="20"/><br><br>
                <span style="color: red;">Warning: Uploaded file must not exceed 400kB</span><br><br> 
                Image File: <input type="file" name="file1"/>                
                <form:button class="input" name="choice" value="add_Image">Upload image</form:button><br><br>
                <form:button id="post_Message" class="input" name="choice" value="post_Story">Post Comment</form:button><br><br>
            </form:form>                
                
            <div class="commentBack">
                <a href="getstory?pg=${pg}&n=${n}&pgn=${pgn}">Back</a>
            </div>
        </div>
            
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
    
        <footer style="text-align: center; border-top: 5px;">
            <span style="font-size: 13px;">                <ul style="list-style: none; display: inline-block;">

                <span style="font-size: 14px;">Disclaimer: </span>
                You are solely responsible for whatever you post and the reactions it results to thereof.
                X-rated, political, racial and ethnical inciting contents are strongly prohibited.
            </span>
        </footer>
    </body>
</html>