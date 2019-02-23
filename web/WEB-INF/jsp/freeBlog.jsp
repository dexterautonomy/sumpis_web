<%-- 
    Document   : sumBlog
    Created on : 13-Dec-2017, 23:34:56
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${firstname} @SumBlog</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div class="memberdiv">
            <div style="float: left; padding: 5px;">
                <a href="featuredmvp"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>
            <div class="bizX" style="clear: both;">
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
        </div>
        
        <p style="text-align: center; color: green; font-size: 28px; font-weight: bold;">
            Guest Platform
        </p>
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
        
        <p style="text-align: center; color: red; font-size: 18px;">${errorZZ}</p>
        <div style="text-align: center; margin: 0 auto;">
            <form:form action="mvpsumblog" method="POST" commandName="freeBlogBeanObject" cssStyle="" enctype="multipart/form-data">
                Category:<br>
                <form:select path="niche" cssClass="input_Field">
                    <form:options items="${categoryList}"/>
                </form:select><br><br>
                Title<br>
                <form:input path="title" placeholder="Title" size="60" cssClass="input_Field" cssStyle="text-align: center;"/><br><br>
                Content<br>
                <form:textarea id="txt_Area_InformAll" path="content" placeholder="Write post" cols="100" rows="20"/><br><br>
                Content Image: <input type="file" name="contentImage"/>                
                <form:button class="input" name="choice" value="add_Image">Upload</form:button><br><br>
                Cover Image: <input type="file" name="coverImage"/><br><br>
                <form:button id="post_Message" class="input" name="choice" value="post_Story">Post</form:button><br><br>
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
    </body>
</html>