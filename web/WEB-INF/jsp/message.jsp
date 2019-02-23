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
        <title>Compose Message</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="usermessage"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right; padding: 4px; border: 1px solid; border-radius: 2px;">
                <form:form action="searchController" method="POST" commandName="eleventhBeanObject">
                    <span style="font-size: 14px;">Add a colleague/partner and work on files and contents together<hr>
                    <span class="input_Label">Search by username </span> @
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
        </div>
        <div style="border-bottom: 1px solid grey;">
            <form:form action="flg" enctype="multipart/form-data" method="POST" commandName="seventhBeanObject" cssClass="upload">
                <span class="uploadText">Forward file</span>&nbsp;(pdf, docx, doc, txt only) <span style="color: red;">Max: 1MB</span>
                <table align="center">
                    <tr>
                        <td>File: <input type="file" name="file" required="true"/></td>
                        <td>To: @ 
                            <form:select path="sendTo" cssClass="input_Field">
                                <form:options items="${colleagues}"/>
                            </form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input id="upload_File" class="input" type="submit" value="Forward"/>
                        </td>
                    </tr>
                </table>
            </form:form>
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
        
        <p id="outcome" style="color: red; text-align: center; font-weight: bold;">${outcome}</p>        
        <div style="margin-bottom: 30px;">
            <form:form method="POST" action="wrt" commandName="eightBeanObject" cssClass="formClass">
                To: <br>@                
                <form:select path="sendTo" cssClass="input_Field">
                    <form:options items="${colleagues}"/>
                </form:select>
                <br><br>                
                <table align='center'>
                    <tr>
                        <td>
                            Subject:<br> <form:input path="subject" placeholder="Subject(optional)" size="40" cssStyle="text-align: center;" cssClass="input_Fieldx" required="true"/>
                        </td>
                    </tr>
                </table><br>                        
                Compose Message<br>
                <form:textarea id="txt_Area_InformAll" cols="100" rows="20" path="writeUp" placeholder="Write message here" required="true"/><br><br>
                <input id="post_Message" class="input" type="Submit" value="Send Message"/><br>                
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
        </div><br><br><br>
    </body>
</html>