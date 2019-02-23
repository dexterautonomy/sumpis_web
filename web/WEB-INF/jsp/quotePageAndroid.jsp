<%-- 
    Document   : quotePageAndroid
    Created on : 06-May-2018, 16:25:06
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
        <title>Quoting @${commenter}</title>
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
            
        <div style="text-align: center; margin-top: 40px; margin-bottom: 15px;">
            <%--
            <form:form enctype="multipart/form-data" method="POST" action="submitQuote" commandName="fourteenthBeanObject">                
                <p style="color: red; font-size: 20px;">${commentaryimage}</p>                
                <form:input path="writer" value="${writer}"/>
                <form:input path="newsTitle" value="${headline}"/>
                <form:input path="date" value="${date}"/>
                <form:input path="id" value="${pageNumb}"/>
                <form:input path="spare" value="${pgn}"/>
                <form:input path="date2" value="<%= timeStamp %>"/>
                <form:input path="space" placeholder="Content" cols="100" rows="20"/>
                Edit statement:<br>
                <form:textarea id="txt_Area_InformAll2" path="newsPost2" placeholder="Quoted text" cols="80" rows="20"/><br><br>
                Write a comment/reaction in the box below:<br>
                <form:textarea id="txt_Area_InformAll" path="newsPost" placeholder="Content" cols="80" rows="20"/><br><br>
                <form:button id="post_Message" class="input" name="choice" value="post_Story">Post Quote</form:button><br><br>
            </form:form>
            --%>
                
            <form method="POST" action="submitQuoteAndroid">
                <p style="color: red; font-size: 20px;">${commentaryimage}</p>                
                <input type="hidden" name="writer" value="${writer}"/>
                <input type="hidden" name="username" value="${username}"/>                
                <input type="hidden" name="headline" value="${headline}"/>
                <input type="hidden" name="date" value="${date}"/>
                <input type="hidden" name="id" value="${pageNumb}"/>
                <input type="hidden" name="pgn" value="${pgn}"/>
                <input type="hidden" name="date2" value="<%= timeStamp %>"/>
                <input type="hidden" name="space" value="${space}"/>
                
                <textarea style="font-size: 18px;" type="text" name="quotedtext"  cols="80" rows="20" id="txt_Area_InformAll2">${subcomment}</textarea><br><br>
                <textarea style="font-size: 18px; display: none;" type="text" name="quotedtext2" cols="90" rows="20">${subcomment}</textarea><br><br>
                <textarea style="font-size: 18px;" id="txt_Area_InformAll" type="text" name="qoutingtext" cols="80" rows="20" placeholder="Comment"></textarea><br><br>
                
                <button id="post_Message" class="inputandroid" name="choice" value="post_Story">Post Quote</button><br><br>
            </form>
                
                
            <div style="font-size: 20px; margin-top: 30px;">
                <a href="andwx?news=${headline}&date=${date}&id=${pageNumb}&pgn=${pgn}&username=${username}">Back</a>
            </div>
        </div>
                
        <%--
        <div style="text-align: center; margin-top: 40px; margin-bottom: 15px;">
            <form method="POST" action="submitQuote">
                <p style="color: red; font-size: 20px;">${commentaryimage}</p>                
                <input type="text" name="writer" value="${writer}"/>
                <input type="text" name="newsTitle" value="${headline}"/>
                <input type="text" name=="date" value="${date}"/>
                <input type="text" name=="id" value="${pageNumb}"/>
                <input type="text" name=="spare" value="${pgn}"/>
                <input type="text" name=="date2" value="<%= timeStamp %>"/>
                Edit statement:<br>
                <textarea type="text" id="txt_Area_InformAll2" name="newsPost2" placeholder="Quoted text" cols="80" rows="20"></textarea><br><br>
                Write a comment/reaction in the box below:<br>
                <textarea id="txt_Area_InformAll" type="text" name="newsPost" placeholder="Content" cols="80" rows="20"></textarea><br><br>
                <button id="post_Message" class="input" name="choice" value="post_Story">Post Quote</button><br><br>
            </form>
        </div>--%>
    </body>
</html>