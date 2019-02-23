<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin: ${firstname}</title>
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
        <div style="margin-bottom: 30px;">
            <form:form cssClass="uploadspecial" action="adminupload" enctype="multipart/form-data" method="post" commandName="thirdBeanObject">
                <span class="uploadText">Admin query upload</span>&nbsp;
                <span style="font-size: 13px;">(pdf, docx, doc, txt only)</span><br>
                <input type="file" name="file" required="true"/>
                <input class="input" type="submit" value="Upload"/><br><br>                
                <span style="font-size: 15px; text-decoration: underline;">There is/are ${peopleOnline} person(s) currently online.</span>
            </form:form>
        </div>
        <p id="adminPageInfo" style="text-align: center; color: red; font-weight: bold;">${error1}</p>
        
        <div style="text-align: center; padding-bottom: 20px; margin-bottom: 250px;">        
            <form:form method="POST" action="takewords" commandName="sixthBeanObject">
                Word<br>
                <form:input path="word" size="60" placeholder="Add/remove word, phrase, clause, sentence etc." cssClass="input_Field" cssStyle="text-align: center;"/><br><br>
                Synonym(s)/Queries<br>
                <form:textarea id="txt_Area_InformAll" class="txtjst1" cols="115" rows="25" path="synonym" placeholder="Add synonym(s) and make sure you separate multiple synonyms with a comma and just one space."/><br><br>
                <form:button id="post_Message" class="input"  name="action" value="add">Single Context Word</form:button>
                <form:button id="doubleContext" class="input"  name="action" value="multiple">Double/Single Context Query</form:button>
                <form:button id="deleteWord" class="input"  name="action" value="delete">Remove Word</form:button>&nbsp;&nbsp;&nbsp;&nbsp;

                <form:button id="post_Message2" class="input"  name="action" value="phraseSingle">Single Phrase/Clause</form:button>
                <form:button id="doubleContext2" class="input"  name="action" value="phraseMultiple">Multiple Phrase/Clause</form:button>
                <form:button id="deleteWord2" class="input"  name="action" value="deletePhrase">Remove Phrase/Clause</form:button>
            </form:form>
        </div>
    </body>
</html>