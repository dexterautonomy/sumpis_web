<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
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
        <title>Sumpis news</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="newsController"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div> 
            
            <div style="float: right; padding: 4px; border: 1px solid; border-radius: 2px;">
                <form:form action="searchController" method="post" commandName="eleventhBeanObject">
                    <span style="font-size: 14px;">Add a colleague/partner and work on files and contents together<hr>
                    <span class="input_Label">Search by username </span> @
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <span id="search_Info" style="text-align: center; font-size: 15px; color: yellow;"></span>
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
            <span style="font-size: 22px;">${details}</span>
            <a href="logout">Logout</a>
        </div>
            
        <div style="text-align: center; margin-top: 20px; margin-bottom: 5px;">
            <form:form enctype="multipart/form-data" method="POST" action="newsPostController" commandName="fourteenthBeanObject">
                <div style="color: green; font-size: 28px; font-weight: bold;">General Post Section</div>
                <p id="outcome" style="color: red;">${newsposted}</p>
                <p style="color: red;">${newserror}</p>
                
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
                
                Title(15 words or less)<br>
                <form:input path="newsTitle" placeholder="Title" size="60" cssClass="input_Field" cssStyle="text-align: center;"/><br><br>                
                Content (1000 words or less)<br>
                <form:textarea id="txt_Area_InformAll" path="newsPost" placeholder="Content" cols="100" rows="20"/><br><br>
                <form:checkbox path="check" value=""/> I have confirmed the veracity of this post before publishing.<br><br>
                <span style="color: red;">Warning: Uploaded file must not exceed 250kB</span><br><br> 
                Image File: <input type="file" name="file1"/>
                <form:button class="input" name="choice" value="add_Image">Upload</form:button><br><br>
                <form:button id="post_Message" class="input" name="choice" value="post_Story">Post</form:button><br>
            </form:form>
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
        </div><br><br>
            
        <footer style="text-align: center; background-color: rgba(111,111,111,0.8); color: white; border-top: 5px;">
            <span style="font-size: 14px;">
                <span style="font-size: 15px;">Disclaimer: </span>
                You are solely responsible for whatever you post and the reaction thereof.
                X-rated, political, racial, religious inciting contents and other contents of such nature are strongly prohibited.
            </span>
        </footer>
    </body>
</html>