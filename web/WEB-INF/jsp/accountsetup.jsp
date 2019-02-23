<%-- 
    Document   : accountsetup
    Created on : 06-Nov-2017, 21:38:33
    Author     : DEXTER
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Setup</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="divLost" style="overflow: auto;">
            <div style="float: left; padding: 5px;">
                <a href="accountrevalidation"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div><br>
            <div class="bizX" style="text-align: center;">
                <a href="registeredMemberControl">My Desk</a>&nbsp;&nbsp;&nbsp;
                <a href="/sumpis/">Index</a>
            </div>
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
        </div>
        
        <div class="account_Setup" style="border: 1px solid grey; padding: 5px; border-radius: 5px;">            
            <form:form method="POST" action="retrieveLostDetails" commandName="twentyfirstBeanObject"
                cssStyle="margin-top: 10px;">                
                <p style="font-size: 26px; color: blue; margin-bottom: 10px; text-align: center; font-weight: bold;">
                    Fill this form to retrieve your account information
                </p><hr><br>
                <table align='center'>
                    <tr>
                        <td><span class="input_Label">Firstname</span></td>
                        <td><form:input path="firstName" placeholder="Enter your firstname" size="30" cssClass="input_Field" required="true"/></td>                                                
                    </tr>
                    <tr>
                        <td><span class="input_Label">Lastname</span></td>
                        <td><form:input path="lastName" placeholder="Enter your lastname" size="30" cssClass="input_Field" required="true"/></td>
                    </tr>                    
                    <tr>
                        <td><span class="input_Label">Mobile</span></td>
                        <td>
                            <form:input id="telephone" path="mobile" placeholder="Mobile number" size="30" cssClass="input_Field"/>
                            <br><span id="telephone_Info" style="font-size: 14px; color: blue;">Include country code e.g +144521447</span>
                        </td>                        
                    </tr>
                    
                    <tr>
                        <td></td>
                        <td><br><div style="text-align: center;"><form:button id="input_SubmitB" class="input" name="action" value="submit">Submit</form:button></div></td>
                        <td></td>
                    </tr>
                </table>
                <p id="error_Setup" style="color: red; text-align: center;">${g}</p>
            </form:form><hr>
        </div><br>
        
        
        <%--ADVERT 2--%>
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adid}" varStatus="status" begin="3" end="3">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="400" height="150" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>
        
    </body>
</html>