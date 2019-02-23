<%-- 
    Document   : adminbloglistview
    Created on : 21-Dec-2017, 01:41:02
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sumblog check page</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>        
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="categoryselection"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
            <h1 style="color: green; text-decoration: underline;">Blogpost Verification and Approval Section</h1>
            <h2 style="color: green;">${section}</h2>
            
            <p style="color: red; ">${blogmessage}</p>
                    <table cellpadding='8' align='center'>
                        <tr>                            
                            <th class='tdmateheader'>Date</th>
                            <th class='tdmateheader'>Writer</th>
                            <th class='tdmateheader'>Status</th>
                            <th class='tdmateheader'>Cover</th>
                            <th class='tdmateheader'>Likes</th>
                            <th class='tdmateheader'>Views</th>
                            <th class='tdmateheader'>Title</th>
                            <th class='tdmateheader'>Action</th>
                        </tr>
                        <c:forEach var="nxd" varStatus="status" items="${cList}">
                        <form:form method="post" action="${pageContext.request.contextPath}/blogpostmanipulate?pg=${pg}&t=${t}" commandName="fifteenthBeanObject" class="formClass">
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
                            <td class='tdmate'>
                                <a href="${pageContext.request.contextPath}/handlerrequestcontroller?pg=${pg}&n=${nxd}&pgn=1&t=${t}">
                                    <img src='${pageContext.request.contextPath}/resources/images/general_Images/${covImg[status.index]}' width='150' height='100'/>
                                </a>
                            </td>                            
                            <td class='tdmate'>
                                <span style="font-size: 14px;">${like[status.index]}</span>
                            </td>                            
                            <td class='tdmate'>
                                <span style="font-size: 14px;">${view[status.index]}</span>
                            </td>
                            <td class='tdmate' style="text-align: left;">
                                <a href="${pageContext.request.contextPath}/handlerrequestcontroller?pg=${pg}&n=${nxd}&pgn=1&t=${t}">
                                    ${title[status.index]}
                                </a>
                                <form:hidden path="title" value="${title[status.index]}"/>
                            </td>
                            <td class='tdmate'>
                                <form:button class="input"  name="choice" value="message">Message</form:button>
                                <form:button class="input"  name="choice" value="ban">Ban</form:button>
                                <form:button class="input"  name="choice" value="block">Block</form:button>                        
                                <form:button class="input"  name="choice" value="unban">Unban</form:button>
                                <form:button class="input"  name="choice" value="unblock">Unblock</form:button> 
                            </td>
                        </tr>
                        </form:form>
                        </c:forEach>
                    </table>
        </div>
        <p style="text-align: center; margin-bottom: 150px;">${prev}${next}</p>
    </body>
</html>