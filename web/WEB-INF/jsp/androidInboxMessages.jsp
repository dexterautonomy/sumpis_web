<%-- 
    Document   : androidInboxMessages
    Created on : 31-May-2018, 20:16:42
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    
    <body class="bodyClass5">                    
        <div class="linknewsAndroid">            
            <div class="inboxImageCentering" style="font-size: 24px;">                
                ${fileContent}<br>
                <p style="text-align: center; font-size: 20px;">${back}</p>
            </div>
            
            <div style="text-align: center; margin-left: auto; margin-right: auto;">
                <c:forEach var="msg" items="${name}" varStatus="status">
                    <ul style="list-style: none; border: 1px solid grey; padding: 10px; background-color: rgba(235,255,240,0.7); 
                        display: inline-block; text-align: left;">
                        <li>
                            <span style="font-weight: bold;">Sender:</span> @${sender[status.index]} 
                            <span style="font-weight: bold;">Date:</span> ${date[status.index]}
                            <span style="color: red; font-weight: bold;">${statux[status.index]}</span><hr>                            
                            Title: <span style="font-weight: bold;">${msg}</span>                            
                            <p><span style="font-weight: bold;">Description: </span>${desc[status.index]}</p>
                            
                            <form method="POST" action="${pageContext.request.contextPath}/andinboxreader" style="text-align: center;">
                                <input type="hidden" name="username" value="${username}"/>
                                <input type="hidden" name="title" value="${msg}"/>
                                <input type="hidden" name="date" value="${date[status.index]}"/>
                                <input type="hidden" name="page" value="${page}"/>
                                
                                <button class="inputandroid"  name="choice" value="read">Read</button>
                                <button class="inputandroid"  name="choice" value="delete">Delete</button>
                            </form><hr>
                        </li>
                    </ul>
                </c:forEach>
            </div><br>
        </div>
    </body>
</html>