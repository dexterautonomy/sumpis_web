<%-- 
    Document   : premiumpage
    Created on : 02-Apr-2018, 09:19:36
    Author     : DEXTER


SUMPIS IS GOING TO BE A FREE APP, SO NO NEED FOR THIS

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Go Premium</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div class="memberdiv">            
            <div style="float: left;">
                <a href="premium" class="header">Sumpis</a>
            </div>
            <div class="bizX">
                <a href="/sumpis/">Index</a>&nbsp;&nbsp;
                <a href="registeredMemberControl">Home</a>&nbsp;&nbsp;
                <a href="premium" style="color: yellow;">Go <span style="font-style: italic;">Premium</span></a>&nbsp;&nbsp;
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
            
        <p style="text-align: center; color: blue; font-size: 28px; font-weight: bold; text-decoration: underline;">
            Premium Subscription Packages
        </p>
        <div class="catdiv" style="margin-bottom: 50px;">            
            <form method="POST" action="the gateway controller address">
                <input type="hidden" name="" value=""/>
                <input type="hidden" name="" value=""/>
                <input type="hidden" name="RESPONSE_URL" value="sumpis controller that will handle the saving of the payment by processing the response sent by payment gateway"/>
                <input type="hidden" name="TRANSACTION_ID" value=""/>
                <input type="hidden" name="DESCRIPTION" value=""/>
                <input type="hidden" name="LOGO_URL" value="URL of sumpis logo"/>
                
                <button class="category1" name="AMOUNT" value="2">1 Day: $2</button>
                <button class="category2" name="AMOUNT" value="4">1 Week: $4</button>
                <button class="category8" name="AMOUNT" value="5">2 Weeks: $5</button>
                <button class="category5" name="AMOUNT" value="7">1 Month: $7</button>
                <button class="category4" name="AMOUNT" value="12">3 Months: $12</button>
                <button class="category3" name="AMOUNT" value="15">6 Months: $15</button>
                <button class="category6" name="AMOUNT" value="23">9 Months: $23</button>
                <button class="category7" name="AMOUNT" value="30">1 Year: $30</button>
                <button class="category5" name="AMOUNT" value="50">2 Years: $50</button>
            </form>
        </div>
    </body>
</html>
--%>