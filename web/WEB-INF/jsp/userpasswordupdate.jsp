<%-- 
    Document   : userpasswordupdate
    Created on : 08-Nov-2017, 22:26:01
    Author     : DEXTER
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Settings</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">
            <div style="float: left; padding: 5px;">
                <a href="userpasswordchange"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
        <div class="document2" style="clear: both;">
            <span style="font-size: 22px;">${details}</span>&nbsp;
            <a href="logout">Logout</a>
        </div>
        
        <div id="error_jquery" style="color: red; text-align: center; margin-top: 20px;">${passwordinfo}</div>
        <div id="error_paswd" style="color: red; text-align: center;"></div>
        
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
        </div><br>
        
        <div style="max-width: 500px; padding: 30px; border: 1px solid grey; border-radius: 3px;
            background-color: rgba(232,248,240,0.1); margin-left: auto; margin-right: auto;">
            <form:form method="post" action="userpasswordchangecontroller" commandName="twentyfourthBeanObject">
                <div style="font-size: 24px; text-align: center;">Password Change Form</div><hr>
                <table align='center'>
                    <tr>
                        <td><span class="input_Label">Username</span></td>
                        <td><form:input path="userName" placeholder="Enter username" cssClass="input_FieldG" required="true"/></td>                        
                    </tr>
                    <tr>
                        <td><span class="input_Label">Current Password</span></td>
                        <td><form:password path="oldPassword" placeholder="Enter current password" cssClass="input_FieldG" required="true"/></td>
                    </tr>
                    <tr>
                        <td><span class="input_Label">New Password</span></td>
                        <td><form:password path="newPassword1" placeholder="Enter new password" cssClass="input_FieldG" required="true"/></td>
                    </tr>                    
                    <tr>
                        <td><span class="input_Label">Confirm New Password</span></td>
                        <td><form:password path="newPassword2" placeholder="Confirm new password" cssClass="input_FieldG" required="true"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <form:button id="signup_ButtonG" class="input" name="action" value="update">Update</form:button>
                            <form:button class="input" name="action" value="cancel">Cancel</form:button>
                        </td>
                    </tr>
                </table><hr>
            <p style="text-align: center; color: red; font-weight: bold;">${userpasswordinfo}</p>
            </form:form>
        </div><br>
    </body>
</html>