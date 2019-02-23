<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Friend request</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="colleagueController"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>

            <div style="float: right; padding: 4px; border: 1px solid; border-radius: 2px;">
                <form:form action="searchController" method="post" commandName="eleventhBeanObject">
                    <span style="font-size: 14px;">Add a colleague/partner and work on files and contents together<hr>
                    <span class="input_Label">Search by username </span></span> @
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <span id="search_Info" style="text-align: center; font-size: 15px; color: yellow;"></span>
                </form:form>
            </div>
            
            <div class="bizX">
                <a href="/sumpis/">Index</a>&nbsp;&nbsp;
                <a href="registeredMemberControl">Home</a>&nbsp;&nbsp;
                <a href="usermessage">Compose</a>&nbsp;&nbsp;
                <a href="userinbox?pg=1">Inbox</a><span class="notification">${unreadMessages}</span>&nbsp;&nbsp;
                <a href="colleagueController">Request</a><span class="notification">${friendrequest}</span>&nbsp;&nbsp;
                <a href="newsController">Post</a>&nbsp;&nbsp;
                <a href="u_p">Profile</a>&nbsp;&nbsp;
                <a href="userpasswordchange">Settings</a>
             </div>
        </div>
        
        <div class="document2"  style="overflow: auto;">
            <span style="font-size: 22px;">${details}</span>&nbsp;
            <a href="logout">Logout</a>
        </div>
            
        <div style="float: left; text-align: center;">
            <p style="font-size: 25px;">
                Colleague Request<br>
                <span style="font-size: 14px;">
                    Accept colleagues and work on projects together and distribute seamlessly on the platform.
                </span>
            </p>
            <span style="color: blue; font-size: 16px; text-align: center;">${acceptMessage}</span>
            
            <c:forEach begin="0" var="list" items="${friendRequestList}">
                    <ul style="list-style: none; text-align: left;">
                        <li>                                
                            <form:form action="acceptController" method="get" commandName="thirteenthBeanObject" cssStyle="padding-bottom: 7px; border-bottom: 1px solid grey;">
                                <span style="color: blue; font-weight: bold; font-size: 19px;">@${list}</span>
                                <form:hidden path="accept" value="${list}"/>
                                <form:button class="input" name="choice" value="accept">Accept</form:button>
                                <form:button class="input" name="choice" value="pend">Pend</form:button>
                            </form:form>
                        </li>
                    </ul>
            </c:forEach>            
        </div>
                
        <div style="text-align: center; float: right;">
            <p style="font-size: 25px;">
                Colleague List<br>
                <span style="font-size: 14px;">
                    Work on projects together with colleagues listed below. When you accept colleagues,<br>
                    they automatically appear on your drop-down list for communication and file/document exchange.
                </span>
            </p>
            <span style="color: blue; font-size: 16px; text-align: center;"></span>
            <span style="color: red; font-size: 16px; text-align: center;">${removeMessage}</span>
            <c:forEach begin="0" var="list" items="${friendList}">
                    <ul style="list-style: none; text-align: left;">
                        <li>                                
                            <form:form action="acceptController" method="get" commandName="thirteenthBeanObject"  cssStyle="padding-bottom: 7px; border-bottom: 1px solid grey;">
                                <span style="color: green; font-weight: bold; font-size: 19px;">@${list}</span>
                                <form:hidden path="accept" value="${list}"/>
                                <form:button class="input" name="choice" value="remove">Hide</form:button>
                            </form:form>
                        </li>
                    </ul>
            </c:forEach>

            <c:forEach begin="0" var="list2" items="${friendList2}">
                    <ul style="list-style: none; text-align: left;">
                        <li>                                
                            <form:form action="acceptController" method="get" commandName="thirteenthBeanObject"  cssStyle="padding-bottom: 7px; border-bottom: 1px solid grey;">
                                <span style="color: green; font-weight: bold; font-size: 19px;">@${list2}</span>
                                <form:hidden path="accept" value="${list2}"/>
                                <form:button class="input" name="choice" value="remove2">Hide Request</form:button>
                            </form:form>
                        </li>
                    </ul>
            </c:forEach> 
        </div><br>
        
        <%--ADVERT 1--%>
        <div style="margin: 0 auto; text-align: center; clear: both;">
            <c:forEach var="id" items="${adid}" varStatus="status">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img  src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>
        
    </body>
</html>