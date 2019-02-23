<%-- 
    Document   : adminsumblogreaderpage
    Created on : 21-Dec-2017, 17:29:00
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Blogpost Overview</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
        <script>
            (function(d, s, id)
            {
                var js, fjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id)) return;
                js = d.createElement(s);
                js.id = id;
                js.src = 'https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.11';
                fjs.parentNode.insertBefore(js, fjs);
            }
            (document, 'script', 'facebook-jssdk'));
        </script>
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
                
        <div class="linknews2" style="margin-bottom: 100px;">
            <p style="font-size: 18px; text-align: center; color: blue;">${errorQQ}</p>
            <p style="font-weight: bold; font-size: 25px; text-decoration: underline; text-align: center;">${headline}</p>
            
            <p style="text-align: justify;">${content}</p>
            <span>Written: <a href="profile?ux=${writer}">@${writer}</a></span><br><br>
            <span style="font-style: italic; font-weight: bold; color: blue;">${viewsX} person(s) viewed this.</span><br><br>

            <div class="fb-share-button"
                data-href="handlerrequestcontroller?pg=${pg}&n=${n}&pgn=${pgn}&t=${t}"
                data-layout="button_count" data-size="large"
                data-mobile-iframe="true">
                <a class="fb-xfbml-parse-ignore" target="_blank"
                    href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2F127.0.0.1%3A8005%2Fsumpis&amp;src=sdkpreparse">Share
                </a>
            </div><br><br>
            
            <form action="${pageContext.request.contextPath}/likeCounter" method="POST">
                <input type="hidden" name="pg" value="${pg}"/>
                <input type="hidden" name="n" value="${n}"/>
                <input type="hidden" name="pgn" value="${pgn}"/>
                <input type="hidden" name="t" value="${t}"/>
                
                <span style="font-style: italic; font-weight: bold; color: blue;">
                <button class="input" name="action" value="like">${likesX} like(s)</button>
                </span>                
            </form><br>
            
            <div>
                <form:form method="POST" action="adminAcceptApproval?pg=${pg}&n=${n}&pgn=${pgn}&t=${t}" commandName="adminBlogPostApproval">
                    <form:hidden path="writer" value="${writer}"/>
                    <form:hidden path="headline" value="${headline}"/>
                    <form:hidden path="content" value="${content}"/>                   
                    <form:hidden path="likes" value="${likesX}"/>
                    <form:hidden path="views" value="${viewsX}"/>                    
                    <form:hidden path="covimage" value="${coverimage}"/>
                    <form:hidden path="adminapprove" value="${adminapproval}"/>
                    <form:button class="input" name="action" value="approveblog">Approve</form:button>
                    <form:button class="input" name="action" value="disproveblog">Disprove</form:button>                    
                    <form:button class="input" name="action" value="repostblog">Repost</form:button>
                </form:form>
            </div><br>
            
            <p style="text-align: center;"><a href="categoryListController?pg=${pg}&action=${t}">Back</a></p><hr>
            
            <c:forEach var="nxd" items="${cList}" varStatus="status">
                <ul style="list-style: none; border-left: 3px solid lightblue; border-right: 1px solid grey; 
                     border-bottom: 1px solid grey; padding: 0px; background-color: rgba(235,255,240,0.7); text-align: left; border-radius: 2px;">
                <li>
                    
                    <div style="overflow: auto; background-color: lightblue; padding: 3px;">
                        <div style="float: left;">
                            <a href="profile?ux=${commentsUsername[status.index]}"><img style="border-radius: 50%;" src="${pageContext.request.contextPath}/resources/images/profile_pix/${pix[status.index]}" width="62" height="62" /></a>
                        </div>
                        <div style="border-radius: 2px; overflow: auto; border-bottom: 1px solid grey;">
                            <span style="float: left; color: blue; font-weight: bold;;">
                                <a href="profile?ux=${commentsUsername[status.index]}">@${commentsUsername[status.index]}&nbsp;&nbsp;[${commentsDate[status.index]}]</a>
                            </span>
                        </div>
                    </div>
                    <div style="text-align: justify; padding: 5px;">${mainComments[status.index]}</div><br>
                    
                    <form action="commentactiondx" method="post" style="text-align: center;">
                        <input type="hidden" name="pg" value="${pg}"/>
                        <input type="hidden" name="n" value="${n}"/>
                        <input type="hidden" name="cID" value="${nxd}"/>
                        <input type="hidden" name="pgn" value="${pgn}"/>
                        <input type="hidden" name="t" value="${t}"/>
                        <input type="hidden" name="headline" value="${headline}"/>
                        <input type="hidden" name="date2" value="${commentsDate[status.index]}"/>
                        <input type="hidden" name="writer2" value="${commentsUsername[status.index]}"/>
                        <button style="margin-bottom: 5px;" class="input"  name="choice" value="remove">Remove</button>
                    </form>
                </li>                        
                </ul>
            </c:forEach>
            ${prev}&nbsp;&nbsp;${next}
        </div>                
    </body>
</html>