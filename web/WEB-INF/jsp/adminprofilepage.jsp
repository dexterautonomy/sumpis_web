<%-- 
    Document   : adminprofilepage
    Created on : 15-Jun-2018, 08:51:55
    Author     : DEXTER
--%>

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
        <div class="memberdiv" style="overflow: auto;">            
            <div style="text-align: center; float: left; padding: 5px;">
                <a href="adminControl"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right;">
                <form:form action="adminSearchController" method="post" commandName="eleventhBeanObject" cssClass="adminSearch">
                    <span class="input_Label">Search by username</span> @
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <div id="search_Info" style="color: yellow; font-size: 15px;"></div>
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
        <div class="admin">
            Admin: ${admin} &nbsp; <a href="logout" style="font-size: 14px;">Logout</a>
        </div>
        <div class="prf">
            <p style="color: red; font-weight: bold; text-align: center;">${friendRequest}</p>
            <div class="prf1">
                <div>
                    <img style="border-radius: 50%;" src="${pageContext.request.contextPath}/resources/images/profile_pix/${pix}" width="250" height="250" alt='profile picture'/>
                    <div style="float: right">
                        <ul style="list-style: none;">
                            <li>
                                <form:form method="POST" action="manipulate?pg=1" commandName="fifteenthBeanObject" class="formClass">
                                    <h2 style="color: red; font-size: 20px;">${note}</h2>                    
                                    <p style="color: green; font-weight: bold; font-size: 20px;">${adminSearchOutput}</p>
                                    <form:hidden path="name" value="${profile}"/>                        
                                    <form:button class="input"  name="choice" value="write">Message</form:button>
                                    <form:button class="input"  name="choice" value="sumpissuspend">Ban User</form:button>
                                    <form:button class="input"  name="choice" value="newssuspend">Block User</form:button>                        
                                    <form:button class="input"  name="choice" value="redspend">Unban</form:button>
                                    <form:button class="input"  name="choice" value="newsunsuspend">Unblock</form:button>
                                    <form:button class="input"  name="choice" value="pix">Remove pic</form:button>                                    
                                </form:form>
                            </li>
                        </ul>
                    </div>
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
                        <td class="px">Sumblogger:</td>
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
        </div>
    </body>
</html>