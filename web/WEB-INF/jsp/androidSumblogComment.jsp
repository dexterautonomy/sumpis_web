<%-- 
    Document   : androidSumblogComment
    Created on : 13-May-2018, 22:54:18
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
        <title>${lastname}'s Reaction</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
            
        <%
            String daySuffix;
            SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a");
            String[] dateAndTime=sdf.format(new Date()).split(" ");
            int day=Integer.parseInt(dateAndTime[0]);
            if(day==1)
            {
                daySuffix=String.valueOf(day + "st");
            }
            else if(day==2)
            {
                daySuffix=String.valueOf(day+"nd");
            }
            else if(day==3)
            {
                daySuffix=String.valueOf(day+"rd");
            }
            else if(day==21)
            {
                daySuffix=String.valueOf(day+"st");
            }
            else if(day==22)
            {
                daySuffix=String.valueOf(day+"nd");
            }
            else if(day==23)
            {
                daySuffix=String.valueOf(day+"rd");
            }
            else if(day==31)
            {
                daySuffix=String.valueOf(day+"st");
            }
            else
            {
                daySuffix=String.valueOf(day+"th");
            }
            
            String timeStamp=sdf.format(new Date()).replaceFirst(dateAndTime[0], daySuffix);
        %>
            
        <div style="text-align: center; margin-top: 20px; margin-bottom: 15px;">
            <form method="POST" action="androidblogcommentary">
                <input type="hidden" name="username" value="${username}"/>
                <input type="hidden" name="writer" value="${writer}"/>
                <input type="hidden" name="headline" value="${headline}"/>
                <input type="hidden" name="date" value="${date}"/>
                <input type="hidden" name="id" value="${id}"/>
                <input type="hidden" name="pgn" value="${pgn}"/>
                <input type="hidden" name="table" value="${table}"/>
                <input type="hidden" name="date2" value="<%= timeStamp %>"/>
                
                <textarea style="font-size: 18px;" id="txt_Area_InformAll" name="blogcomment" placeholder="Comment" cols="90" rows="20"></textarea><br><br>
                
                <button id="post_Message" class="inputandroid" name="choice" value="post_Story">Post Comment</button><br><br>
            </form>
                
            <div style="font-size: 20px; margin-top: 30px;">
                <a href="androidrdr?blog=${headline}&username=${username}&date=${date}&id=${id}&pgn=${pgn}&table=${table}">Back</a>
            </div>
        </div>
    </body>
</html>