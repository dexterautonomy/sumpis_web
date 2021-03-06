<%-- 
    Document   : confirmregistrationotp
    Created on : 07-Nov-2017, 14:08:11
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirm Registration OTP</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="divLost" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="/sumpis/"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right;">
                <form:form commandName="fourthBeanObject" action="login" method="POST">
                    <table>
                        <tr>
                            <td><span class="input_Label">Username</span></td>
                            <td><span class="input_Label">Password</span></td>
                        </tr>                        
                        <tr>
                            <td><form:input path="userName" placeholder="Username" cssClass="input_FieldA" size="28" required="true"/></td>
                            <td><form:password path="password" placeholder="Password" cssClass="input_FieldA" size="20" required="true"/></td>
                            <td><input id="login_Button" class="input" type="submit" value="Login"/></td>
                        </tr>                                            
                        <tr>
                            <td><span id="error_Login" style="color: yellow;">${loginError}</span></td>
                        </tr>
                        <tr>
                            <td><a href="accountrevalidation">Forgotten account details??</a></td>
                        </tr>
                    </table>
                </form:form>
            </div>
            <div class="bizX">
                <a href="/sumpis/">Index</a>
            </div>
        </div>
        
        <div class="otpconfirm">
            <div style="font-size: 30px; font-weight: bold; margin-top: 20px; color: rgb(0,0,230);">
                Welcome! Your account setup is just a step to go
            </div><br>
            
            
            <%--ADVERT 1--%>
            <div style="margin: 0 auto; text-align: center;">
                <c:forEach var="id" items="${adid}" varStatus="status" begin="0" end="2">
                    <ul style="list-style: none; display: inline-block;">
                        <li>
                            <a href="k?n=${id}">
                                <img src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                            </a>
                        </li>
                    </ul>
                </c:forEach>
            </div><br>
            <form:form method="POST" action="completeregistration" commandName="twentiethBeanObject">                
                <span style="font-size: 16px;">
                    Please enter the On-time password (OTP) sent to the mobile number you provided in the OTP field below<br>
                    If you did not get the OTP after five(5) minutes, kindly refresh this page. Do not refresh page until after five(5) minutes.
                </span><br><br>
                <span style="font-size: 27px;">${otpFirstname} &nbsp;${otpLastname}</span>&nbsp;&nbsp;
                <span style="font-size: 21px; font-style: italic; color: blue;">${otpUsername}</span>
                <p style="font-size: 20px;">${otpMobile}</p>
                <table align='center'>
                    <tr>
                        <td>OTP</td>
                        <td><form:input path="otp" placeholder="Enter OTP you received" cssClass="input_Field"/></td>
                    
                        This is for test purpose please remove this once application goes online: OTP is ${OTP}.
                        
                    </tr>                    
                    <tr>
                        <td>                            
                        </td>
                        <td>
                            <form:button id="signup_Button" name="action" class="input" value="confirm">Confirm</form:button>
                            <form:button name="action" class="input" value="cancel">Cancel</form:button>
                        </td>
                    </tr>
                </table>                
                    <p id="error_jquery" style="color: red;">${otperror}</p>
            </form:form>
        </div>
    </body>
</html>
