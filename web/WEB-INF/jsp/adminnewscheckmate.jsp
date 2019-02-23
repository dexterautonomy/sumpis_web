<%-- 
    Document   : adminnewscheckmate
    Created on : 27-Oct-2017, 23:49:01
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkmate Page</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>        
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto; padding: 5px;">            
            <div style="float: left;">
                <a href="sumpisnews?pg=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right;">
                <form:form action="adminSearchController" method="post" commandName="eleventhBeanObject" cssClass="adminSearch">
                    <span class="input_Label">Search by username </span> @ 
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <span id="search_Info" style="text-align: center; font-size: 15px; color: yellow;">${outputError}</span>
                </form:form>
            </div>
            <div class="adminMenu">
                <a href="/sumpis">Index</a>&nbsp;&nbsp;
                <a href="adminControl">Home</a>&nbsp;&nbsp;
                <a href="usermessage">Compose</a>&nbsp;&nbsp;
                <a href="adminInbox?pg=1">Inbox</a><span class="adminnotification">${unreadMessages}</span>&nbsp;&nbsp;
                <a href="adminNewsController">Post</a>&nbsp;&nbsp;
                <a href="adminfeaturedmvp" style="color: yellow;">Blog</a>&nbsp;&nbsp;
                <a href="freelanceController">Sumblogger</a>&nbsp;&nbsp;
                <a href="sumpisnews?pg=1">Check Posts</a>&nbsp;&nbsp;
                <a href="categoryselection">Check Articles</a>&nbsp;&nbsp;
                <a href="getUsers?us=1">Members</a>&nbsp;&nbsp;
                <a href="adpanel?pg=1">Ad Panel</a>&nbsp;&nbsp;
                <a href="stat?pg=1">Statistics</a>&nbsp;&nbsp;
                <a href="generalinfo">Settings</a>
            </div>
        </div>        
        <div class="admin" style="clear: both;">
            <span style="font-size: 22px; color: purple;">Admin: ${admin}</span>
            <a style="font-size: 14px;" href="logout">Logout</a>
        </div>        
        <div class="linknewsX" style="text-align: center;">
            <h1 style="color: green; text-decoration: underline;">Newspost Verification and Approval Section</h1>
            <p style="color: red; ">${error}</p>
            <table cellpadding='8' align='center'>
                <tr>                            
                    <th class='tdmateheader'>Date</th>
                    <th class='tdmateheader'>Writer</th>
                    <th class='tdmateheader'>Status</th>
                    <th class='tdmateheader'>Title</th>
                    <th class='tdmateheader'>Action</th>
                </tr>
                <c:forEach var="newstitle" varStatus="status" items="${title}">
                    <form:form method="post" action="${pageContext.request.contextPath}/manipulate?pg=${pg}" commandName="fifteenthBeanObject" class="formClass">
                        <tr>                                        
                            <td class='tdmate'>
                                <span style="font-size: 14px;">${date[status.index]}</span>
                                <form:hidden path="date" value="${date[status.index]}"/>
                            </td>
                            
                            <td class='tdwriter'>
                                <a href="profile?ux=${writer[status.index]}">
                                    @<span>${writer[status.index]}</span>
                                </a>
                                <form:hidden path="name" value="${writer[status.index]}"/>
                            </td>
                            
                            <td class='tdstat'>
                                <span>${statusX[status.index]}</span>
                            </td>
                            
                            <td class='tdmate' style="text-align: left;">
                                <a href="admingetstory/?pg=${pg}&n=${id[status.index]}&pgn=1">${newstitle}</a>
                                <form:hidden path="title" value="${newstitle}"/>
                            </td>
                            
                            <td class='tdmate'>
                                <form:button class="input"  name="choice" value="wck">Message</form:button>
                                <form:button class="input"  name="choice" value="ssk">Ban</form:button>
                                <form:button class="input"  name="choice" value="nsk">Block</form:button>                        
                                <form:button class="input"  name="choice" value="rsk">Unban</form:button>
                                <form:button class="input"  name="choice" value="usk">Unblock</form:button>
                            </td>
                        </tr>                                                                
                    </form:form>
                </c:forEach>
            </table>                            
        </div>
        <p style="text-align: center; margin-bottom: 150px;">${prev}${next}</p>
    </body>
</html>