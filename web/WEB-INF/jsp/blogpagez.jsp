<%-- 
    Document   : blogpagez
    Created on : 10-Jun-2018, 01:01:16
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${blogpage}</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>        
    </head>
    <body class="bodyClass5">        
        <div class="div1" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="requestcontroller?pg=${pg}&t=${t}"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right;">
                <form:form style="float: right;" commandName="fourthBeanObject" action="login" method="POST">
                    <table>
                        <tr>
                            <td>Username</td>
                            <td>Password</td>
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
            <div class="biz">
                <a href="switcher">My Desk</a>&nbsp;&nbsp;
                <a href="/sumpis">Index</a>&nbsp;&nbsp;
                <a href="sumpisnews?pg=1">Forum</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=writing">Creative</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=arts">History/Art</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=health">Health</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=diy">DIY</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=entertainment">Entertainment</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=sports">Sports</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=relpol">Religion/Politics</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=info">Updates</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=flt">Lifestyle</a>&nbsp;&nbsp;
                <a href="signuppage">Sign Up</a>
            </div>
        </div>
        <h1 class="bloghead">
            ${blogheader}
        </h1>
        
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
        
        <div class="linknewsFreeBlog">                        
                <c:forEach var="n" varStatus="status" items="${n}">                    
                    <div style="overflow: auto;">                        
                        <a href="handlerrequestcontroller?pg=${pg}&n=${n}&pgn=1&t=${t}">
                            <img style="float: left; border-right: 2px solid grey; padding-right: 15px;" src='${pageContext.request.contextPath}/resources/images/general_Images/${coverImage[status.index]}' width='300' height='200'/>
                        </a><br>
                        <a style="font-size: 25px;" href="handlerrequestcontroller?pg=${pg}&n=${n}&pgn=1&t=${t}">${title[status.index]}</a><br>
                        <span style="font-size: 14px;">${date[status.index]}</span><br><br>
                        Written by: @<span style="font-size: 18px; font-style: italic;">${writer[status.index]}</span><br><br>
                        <span style="font-size: 14px;">Views: ${view[status.index]}</span><br>
                        <span style="font-size: 14px;">Likes: ${like[status.index]}</span>
                    </div><hr>
                </c:forEach>
        </div><br>
        
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
        </div>
        
        <p style="text-align: center; margin-bottom: 150px;">${prev}${next}</p>
    </body>
</html>