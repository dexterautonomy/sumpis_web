<%-- 
    Document   : userprofiledit
    Created on : 16-Jun-2018, 08:08:01
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="usermessage"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right; padding: 4px; border: 1px solid; border-radius: 2px;">
                <form:form action="searchController" method="POST" commandName="eleventhBeanObject">
                    <span style="font-size: 14px;">Add a colleague/partner and work on files and contents together<hr>
                    <span class="input_Label">Search by username </span> @
                    <form:input path="name" placeholder="Search by username" size="24" cssClass="input_FieldA" required="true"/>
                    <input id="search_Button" type="submit" value="Search" class="input"/><br>
                    <span id="search_Info" style="text-align: center; font-size: 15px; color: yellow;">${outputError}</span>
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
        <div class="document2">
            <span style="font-size: 22px;">${details}</span>&nbsp;
            <a href="logout">Logout</a>
        </div><br><br>
        
        <%--ADVERT 1--%>
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adid}" varStatus="status" begin="0" end="2">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
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
                    <div style="width: 300px;">
                        <form method="post" action="u_px" enctype="multipart/form-data">
                            <fieldset>
                                <legend><span style="color: red;">Change display photo (jpeg/png 400kb)</span></legend>
                            <input type="hidden" name="username" value="${profile}"/>
                            <input type="file" name="file" required="true"/><br>
                            <input class="input" type="submit" value="Upload"/>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div><hr>
            
            <form action="u_pr" method="post">
                <fieldset>
                    <legend><h1 style="text-align: center; text-decoration: underline; color: green;">Personal Data</h1></legend>
                    <div style="text-align: center;"><a href="u_p?e=edit">Edit</a></div>
                <table cellpadding='8'>
                    <tbody>
                        <input type="hidden" name="username" value="${profile}"/>
                        <tr>
                            <td class="px">Gender:</td>
                            <td class="pz">${gender}</td>
                            <%--<td>
                                <select class="input_Field" name="sex" style="display: ${display}">
                                    <option>Unspecified</option>
                                    <option selected>Male</option>
                                    <option>Female</option>
                                </select>
                            </td>--%>
                        </tr>
                        <tr>
                            <td class="px">E-mail:</td>
                            <td class="pz"><a href="mailto:${mail}" style="color: blue; font-style: italic;">${mail}</a></td>
                            <td>
                                <input class="input_Field" type="email" maxlength="100" name="email" value="${mail}" placeholder="kelvinsmith@sumpis.com" style="display: ${display}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="px">Location:</td>
                            <td class="pz">${location}</td>
                            <td>
                                <input class="input_Field" type="text" maxlength="100" name="loc" value="${location}" placeholder="My Address" style="display: ${display}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="px">Profession:</td>
                            <td class="pz">${job}</td>
                            <td>
                                <input class="input_Field" type="text" maxlength="50" name="work" value="${job}" placeholder="My Profession/Job/Career" style="display: ${display}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="px">Hobbies:</td>
                            <td class="pz">${hobby}</td>
                            <td>
                                <input class="input_Field" type="text" maxlength="100"  name="hob" value="${hobby}" placeholder="Things I like doing" style="display: ${display}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="px">Relationship:</td>
                            <td class="pz">${relationship}</td>
                            <td>
                                <select class="input_Field" name="rel" style="display: ${display}">
                                    <option selected>Single</option>
                                    <option>Casual</option>
                                    <option>Married</option>
                                    <option>Divorced</option>
                                    <option>Searching</option>
                                    <option>Single mum</option>
                                    <option>Complicated</option>
                                    <option>Co-habitation</option>
                                    <option>In a relationship</option>
                                    <option>Recently separated</option>
                                    <option>Single not searching</option>
                                </select>
                                    
                                <%--<input class="input_Field" type="text" name="rel" maxlength="50" value="${relationship}" placeholder="Relationship status" style="display: ${display}"/>--%>
                            </td>
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
                            <td class="pz">${newsposts} post</td>
                        </tr>
                        <tr>
                            <td class="px">Blogposts:</td>
                            <td class="pz">${blogposts} post</td>
                        </tr>
                        <tr>
                            <td class="px">Quote:</td>
                            <td class="pz">${me}</td>
                            <td>
                                <textarea name="about" cols="50" rows="5" maxlength="50" placeholder="What I think" style="display: ${display}">${me}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td>
                                <input id="post_Message" class="input" type="submit" value="Update" style="display: ${display}"/>
                            </td>
                        </tr>                    
                    </tbody>
                </table>
                </fieldset>
            </form>
                            
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
            </div><hr><br>
        </div>
    </body>
</html>