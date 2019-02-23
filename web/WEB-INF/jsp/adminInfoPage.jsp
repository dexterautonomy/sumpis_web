<%-- 
    Document   : adminInfoPage
    Created on : 05-Nov-2017, 22:41:13
    Author     : DEXTER
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Information Platform</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="generalinfo"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
                <a href="generalinfo">Settings</a>&nbsp;&nbsp;
                <a href="stat?pg=1">Statistics</a>&nbsp;&nbsp;
                <a href="adminpasswordchange">Password Change</a>
            </div>
        </div>
                
        <div class="admin" style="clear: both;">
            <span style="font-size: 22px; color: purple;">Admin: ${admin}</span>
            <a style="font-size: 14px;" href="logout">Logout</a>
        </div>
        <p id="error_Setup" style="color: red; text-align: center;">${info}</p>
        <div style="text-align: center;">            
            
            <div style="background-color: pink; display: inline-block; border: 1px solid grey; padding: 5px; border-radius: 5px;">
                <p style="color: red; font-size: 18px; text-decoration: underline;">${result}</p>
                <p style="font-size: 24px;">WORD LIST</p>
                <form method="POST" action="checkwordlist" style="padding: 20px;">
                    <table align='center'>
                    <tr>
                        <td><span class="input_Label">Word</span></td>
                        <td><input type="text" name="word" size="35" placeholder="Enter word" class="input_FieldYY"/></td>                        
                    </tr>
                    <tr>
                        <td><span class="input_Label">Category</span></td>
                            <td>
                                <select name="category" class="input_Field">
                                    <option>TESTS</option>
                                    <option>NOUNS</option>
                                    <option>PRONOUNS</option>
                                    <option>VERBS</option>
                                    <option>ADJECTIVES</option>
                                    <option>ADVERBS</option>
                                    <option>CONJUNCTIONS</option>                                    
                                    <option>PREPOSITIONS</option>                                    
                                </select>
                            </td>
                    </tr>                    
                    </table><br>
                    <button class="input" name="action" value="writetoproperties">Prop</button>
                    <button class="input" name="action" value="properties">File</button>
                    <button class="input" name="action" value="check">Check</button>
                    <button class="input" name="action" value="append">Append</button>
                    <button class="input" name="action" value="replace">Replace</button>
                    <button class="input" name="action" value="remove">Remove</button>
                    <button class="input" name="action" value="wordlist">List</button>
                </form>
            </div>
            
            <div style="background-color: orange; display: inline-block; border: 1px solid grey; padding: 5px; border-radius: 5px;">
                <p style="font-size: 24px;">MANAGE ADMIN</p>
                <form:form method="POST" action="addNewAdmin" commandName="nineteenthBeanObject" cssStyle="padding: 20px;">
                    <table align='center'>
                    <tr>
                        <td><span class="input_Label">Firstname</span></td>
                        <td><form:input path="firstName" placeholder="Firstname" cssClass="input_Field"/></td>                        
                    </tr>
                    <tr>
                        <td><span class="input_Label">Lastname</span></td>
                        <td><form:input path="lastName" placeholder="Lastname" cssClass="input_Field"/></td>
                    </tr>
                    <tr>
                        <td><span class="input_Label">Mobile</span></td>
                        <td><form:input id="telephone" path="mobile" placeholder="Mobile number" cssClass="input_Field"/><br>
                        <span id="telephone_Info" style="font-size: 14px;">Include country code<br>e.g +144521447</span></td>
                    </tr>
                    <tr>
                        <td><span class="input_Label">Username</span></td>
                        <td><form:input id="username_Admin" path="userName" placeholder="Username" cssClass="input_Field"/><br>
                        <span id="admin_Username_Info" style="color: red;"></span></td>
                    </tr>                    
                    <tr>
                        <td><span class="input_Label">Password</span></td>
                        <td><form:password id="pswd_Length1" path="password" placeholder="Password" cssClass="input_Field"/><br>
                        <span id="pswd_Info1" style="color: red;"></span></td>
                    </tr>                    
                </table><br>
                <form:button id="input_SubmitB" class="input" name="action" value="add">New Admin</form:button>
                <form:button id="remove_Admin" class="input" name="action" value="delete">Remove Admin</form:button>
                <form:button class="input" name="action" value="adminlist">Admin List</form:button>
                </form:form>
            </div>
            
            <div style="background-color: cyan; display: inline-block; border: 1px solid grey; padding: 5px; border-radius: 5px;">
                <p style="font-size: 24px;">NOTIFY USERS</p>
                <form:form method="POST" action="notifyallusers" commandName="eighteenthBeanObject" cssStyle="padding: 20px;">
                    <span class="input_Label">Subject</span><br>
                    <form:input cssClass="input_Field" path="title" placeholder="Subject" value="General Information" readonly="true" cssStyle="text-align: center;"/><br><br>                        
                    Content<br>
                    <form:textarea id="txt_Area_InformAll" cols="45" rows="10" path="info" placeholder="Inform every user of the application"/><br><br>
                    <form:button id="inform_All_Button" class="input" name="action" value="inform">Inform All</form:button>
                    <form:button class="input" name="action" value="delete">Delete All</form:button>
                </form:form>
            </div>
        </div>
        <div style="clear: both; text-align: center; padding: 10px; display: ${display};">
            <p style="font-size: 24px;">${headermain}</p>
            <table align='center' cellpadding='5' border='1'>
                <tr>
                    <th class="tdmateheaderAdmin">${header1}</th>
                    <th class="tdmateheaderAdmin">${header2}</th>
                    <th class="tdmateheaderAdmin">${header3}</th>
                    <th class="tdmateheaderAdmin">${header4}</th>
                    <th class="tdmateheaderAdmin">${header5}</th>
                </tr>
                <c:forEach var="username" varStatus="status" items="${forUsername}">
                    <tr>
                        <td class="tdmateAdmin">${username}</td>
                        <td class="tdmateAdmin">${forFirstname[status.index]}</td>
                        <td class="tdmateAdmin">${forLastname[status.index]}</td>
                        <td class="tdmateAdmin">${forMobile[status.index]}</td>
                        <td class="tdmateAdmin">${forPassword[status.index]}</td>
                    </tr>
                </c:forEach>                
            </table>           
        </div>
        <p style="text-align: center; font-weight: bold; text-decoration: underline;">${result1}</p>
        ${result2}
    </body>
</html>