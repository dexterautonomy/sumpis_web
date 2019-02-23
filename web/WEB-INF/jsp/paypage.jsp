<%-- 
    Document   : paypage
    Created on : 02-Apr-2018, 21:04:22
    Author     : DEXTER

NO MORE USEFUL I'D BE USING WEBPAY NOT API(WEBSERVICE)

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome ${firstname}</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="padding-top: 15px;">
            <div class="bizX">
                <a href="registeredMemberControl">Home</a>&nbsp;&nbsp;&nbsp;
                <a href="premium" style="color: yellow;">Go <span style="font-style: italic;">Premium</span></a>
            </div>
        </div>        
        <div class="document2">
            <span style="font-size: 22px;">You are logged in as ${details}</span>&nbsp;
            <a href="logout">Logout</a>
        </div><br>
            
        <div style="text-align: center; color: green; font-size: 28px; font-weight: bold;">
            Purchase Subscription
        </div><br>
        
        <div style="border: 1px solid grey; border-radius: 3px; padding: 7px; width: 650px; margin-left: auto; margin-right: auto;">
        <div style="text-align: center;">
            <img src='${pageContext.request.contextPath}/resources/images/system_images/img7.png' 
            width="130" height="35" alt="VisaMaster Cards"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <img src='${pageContext.request.contextPath}/resources/images/system_images/img1.png' 
            width="70" height="45" alt="AmEx Card"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <img src='${pageContext.request.contextPath}/resources/images/system_images/img5.png' 
            width="70" height="35" alt="Discover Card"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <img src='${pageContext.request.contextPath}/resources/images/system_images/img6.png' 
            width="70" height="35" alt="Verve Card"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <img src='${pageContext.request.contextPath}/resources/images/system_images/img4.png' 
            width="70" height="35" alt="multiple cards2"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </div><hr>
        <div style="text-align: center; color: blue; font-size: 18px;">
            ${charge}&nbsp;&nbsp;&nbsp;&nbsp;
            <img src='${pageContext.request.contextPath}/resources/images/system_images/securelock.png' 
            width="8" height="13" alt="HTTPS secure lock"/>
            <span style="font-size: 13px; color: grey;">Secure</span>
        </div><br>
        <div class="catdiv">            
            <form:form method="POST" action="" commandName="payformBean">
                <table align='center'>
                    <form:input path="charge" value="${amount}"/>
                    </tr>
                    <tr>
                        <td>Card Number</td>
                        <td>
                            <form:password path="cardNumber" cssClass="input_Field" placeholder="Card number" size="20" maxlength="17"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Expiration Date</td>
                        <td>
                            <form:select path="expMonth" cssClass="input_Field">
                                <form:options items="${month}"/>
                            </form:select>&nbsp;/
                            <form:select path="expYear" cssClass="input_Field">
                                <form:options items="${year}"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Cvv2(CVC)</td>
                        <td>
                            <form:password path="ceeVeeVee" cssClass="input_Field" placeholder="Cvv" size="7" maxlength="4"/>
                            <a href="aaa" style="font-size: 12px; color: blue;">What is this?</a>
                        </td>
                    </tr>
                    <tr>
                        <td>Mobile</td>
                        <td>
                            <form:input path="mobile" cssClass="input_Field" value="${telephone}" readonly="true"/>
                        </td>
                    </tr>
                </table>
                <div style="text-align: center;">
                    <p href="signuppage" style="font-size: 12px; text-decoration: none; color: black;">
                        By clicking Purchase below you confirm to have accepted our <a href="aaa" style="color: blue;">Terms of service</a> and <a href="aaa" style="color: blue;">Privacy Policy</a>
                    </p>
                    <form:button class="input" name="action" value="purchase">Purchase</form:button>
                </div>
            </form:form>
        </div><hr>
        </div> 
    </body>
</html>
--%>