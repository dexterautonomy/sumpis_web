<%-- 
    Document   : profilepage
    Created on : 12-Jun-2018, 16:28:11
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${profile}'s Profile</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div class="div1">            
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
            <div class="biz">
                <a href="switcher">My Desk</a>&nbsp;&nbsp;
                <a href="/sumpis">Index</a>&nbsp;&nbsp;
                <a href="sumpisnews?pg=1">General</a>&nbsp;&nbsp;
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
        </div><br><br>
        
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
                        
        <div class="prf">
            <p style="color: red; font-weight: bold; text-align: center;">${friendRequest}</p>
            <div class="prf2">
                <div>
                    <img style="border-radius: 50%;" src="${pageContext.request.contextPath}/resources/images/profile_pix/${pix}" width="250" height="250" alt='profile picture'/>
                </div>
            </div><hr>
            <fieldset>
                <legend><h1 style="color: green;">Personal Info</h1></legend>
            <table cellpadding='8'>
                <tbody>
                    <tr>
                        <td class="px">Username:</td>
                        <td class="pz">@${profile}</td>
                    </tr>
                    <tr>
                        <td class="px">Gender:</td>
                        <td class="pz">${gender}</td>
                    </tr>
                    <tr>
                        <td class="px">E-mail:</td>
                        <td class="pz"><a href="mailto:${mail}" style="color: blue; font-style: italic;">${mail}</a></td>
                    </tr>
                    <tr>
                        <td class="px">Location:</td>
                        <td class="pz">${location}</td>
                    </tr>
                    <tr>
                        <td class="px">Profession:</td>
                        <td class="pz">${job}</td>
                    </tr>
                    <tr>
                        <td class="px">Hobbies:</td>
                        <td class="pz">${hobby}</td>
                    </tr>
                    <tr>
                        <td class="px">Relationship:</td>
                        <td class="pz">${relationship}</td>
                    </tr>
                    <tr>
                        <td class="px">Member since:</td>
                        <td class="pz">${joined}</td>
                    </tr>
                    <tr>
                        <td class="px">Sumbloger:</td>
                        <td class="pz">${sumblogger}</td>
                    </tr>
                    <tr>
                        <td class="px">Newsposts:</td>
                        <td class="pz"><a href="sch?q=${profile}&t=newz&pg=1">${newsposts} post</a></td>
                    </tr>
                    <tr>
                        <td class="px">Blogposts:</td>
                        <td class="pz"><a href="sch?q=${profile}&t=blogtable&pg=1">${blogposts} post</a></td>
                    </tr>
                    <tr>
                        <td class="px">Quote:</td>
                        <td class="pz">${me}</td>
                    </tr>
                </tbody>
            </table><br><br><hr>
           </fieldset>
            
            <%--ADVERT 2--%>
            <div style="margin: 0 auto; text-align: center;">
                <c:forEach var="id" items="${adid}" varStatus="status" begin="3" end="5">
                    <ul style="list-style: none; display: inline-block;">
                        <li>
                            <a href="k?n=${id}">
                                <img src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                            </a>
                        </li>
                    </ul>
                </c:forEach>
            </div><hr>
        </div>
    </body>
</html>