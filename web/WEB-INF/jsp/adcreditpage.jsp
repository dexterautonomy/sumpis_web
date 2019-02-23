<%-- 
    Document   : adcreditpage
    Created on : 30-Jun-2018, 16:42:49
    Author     : DEXTER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Buy Ads Credit</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="adcredit"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>
            <div class="bizX">
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
            <span style="font-size: 22px;">You are logged in as ${details}</span>&nbsp;
            <a href="logout">Logout</a>
        </div>
            
        <p style="text-align: center; color: blue; font-size: 28px; font-weight: bold; text-decoration: underline;">Ads Credit Bundle</p>
        <div class="catdiv" style="margin-bottom: 50px;">            
            <form method="POST" action="the gateway controller address">
                <input type="hidden" name="" value=""/>
                <input type="hidden" name="" value=""/>
                <input type="hidden" name="RESPONSE_URL" value="sumpis controller that will handle the saving of the payment by processing the response sent by payment gateway"/>
                <input type="hidden" name="TRANSACTION_ID" value=""/>
                <input type="hidden" name="DESCRIPTION" value=""/>
                <input type="hidden" name="LOGO_URL" value="URL of sumpis logo"/>
                
                <button style="border-radius: 50%" class="category1" name="AMOUNT" value="5">1500 Credit unit: $5</button>
                <button style="border-radius: 50%" class="category2" name="AMOUNT" value="10">3200 Credit unit: $10</button>
                <button style="border-radius: 50%" class="category8" name="AMOUNT" value="15">4850 Credit unit: $15</button>
                <button style="border-radius: 50%" class="category5" name="AMOUNT" value="30">9800 Credit unit: $30</button>
                <button style="border-radius: 50%" class="category4" name="AMOUNT" value="50">16400 Credit unit: $50</button>
                <button style="border-radius: 50%" class="category3" name="AMOUNT" value="150">49500 Credit unit: $150</button>
                <button style="border-radius: 50%" class="category6" name="AMOUNT" value="300">99500 Credit unit: $300</button>
                <button style="border-radius: 50%" class="category7" name="AMOUNT" value="500">165900 Credit unit: $500</button>
                <button style="border-radius: 50%" class="category5" name="AMOUNT" value="1000">332000 Credit unit: $1000</button>
            </form>
        </div>
    </body>
</html>