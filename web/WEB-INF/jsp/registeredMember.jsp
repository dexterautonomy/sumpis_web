<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome ${firstname}</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="registeredMemberControl"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
                <a href="usermessage">Compose</a>&nbsp;&nbsp;
                <a href="userinbox?pg=1">Inbox</a><span class="notification">${unreadMessages}</span>&nbsp;&nbsp;
                <a href="colleagueController">Request</a><span class="notification">${friendrequest}</span>&nbsp;&nbsp;
                <a href="newsController">Post</a>&nbsp;&nbsp;
                ${sumBlogger}&nbsp;&nbsp;
                <a href="u_p">Profile</a>&nbsp;&nbsp;
                <a href="userpasswordchange">Settings</a>&nbsp;&nbsp;
                <a style="display: ${displayadvert}" href="ad_ctr?pg=1">Ad</a>
            </div>
        </div>        
        <div class="document2" style="overflow: auto;">
            <%--<div style="float: right; font-size: 17px; color: red;">${subscription}</div>--%>
            <div style="clear: both;">
                <span style="font-size: 22px;">${details}</span>&nbsp;
                <a href="logout">Logout</a>
            </div>
            
        </div>
        
        <div style="border-bottom: 1px solid grey;">
            <form:form class="upload" action="upload2" enctype="multipart/form-data" method="post" commandName="thirdBeanObject">
                <span class="uploadText">Select file/document</span>&nbsp;<span class="basic">(pdf, docx, doc, txt only)</span>&nbsp;
                <span style="color: red; text-decoration: underline">File should not exceed 500kb</span><br>                                
                <input type="file" name="file" required="true"/>
                <input class="input" type="submit" value="Upload"/>
            </form:form>
        </div>
            
        <p id="app_Info" style="text-align: center; color: red; font-weight: bold;">${fileUploadError}${outputErrorMessage}${expired}</p>
        
        <%--Model A
            ADVERT 1--%>
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
        </div>
        <div style="text-align: center; padding: 5px;">
            <p>        
                <form:form method="POST" action="submit2" commandName="firstBeanObject" cssClass="formClass">
                    <form:textarea id="txt_Area" cols="100" rows="20" path="field" placeholder="Write, paste or upload document here." required="true"/><br>
                    <form:input path="count" size="7" disabled="true" cssClass="input_Field"/> sentence(s).                    
                    Summarise in <form:input id="specify_Count" path="length" size="5" value="0" placeholder="Digits only" cssClass="input_Field"/> sentences<br><br>
                    
                    <form:button id="button_A" class="input" name="choice" value="getSentenceCount">Sentence Count</form:button>
                    <form:button id="button_XX" class="input"  name="choice" value="getWordCount">Word count</form:button>
                    <form:button id="button_B" class="input" name="choice" value="paraphrase">Paraphrase</form:button>
                    <%--<form:button id="button_C" class="input" name="choice" value="summary">Summarise</form:button>--%>
                    <form:button id="button_D" class="input" name="choice" value="streamlinedsummary">Streamline-Summary</form:button>
                    <form:button id="button_E" class="input" name="choice" value="paraphrase_summary">Paraphrase & Summarise</form:button>
                    <form:button id="button_F" class="input" name="choice" value="rearrange">Rearrange</form:button>
                </form:form>
            </p><br>
        
            <p>
                <form:form method="POST" action="outputToInput2" commandName="secondBeanObject" cssClass="formClass">
                    <form:textarea id="txt_AreaB" class="txtjst1" cols="100" rows="20" path="field2" placeholder="Text output" required="true"/><br>
                    Text output is currently <form:input path="count2" size="7" disabled="true" cssClass="input_Field"/> sentence(s).<br><br><br>
                    <form:button id="button_G" class="input" name="action" value="resendToInput">Resend To Input</form:button>
                    <form:button id="button_H" class="input" name="action" value="download">Download</form:button>
                    <form:button id="button_I" class="input" name="action" value="forward">Forward</form:button>
                    <form:button id="button_J" class="input" name="action" value="newspost">Post This</form:button>
                </form:form>
            </p>
        </div>
                        
        <%--Model A
            ADVERT 2--%>
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
        </div><br><br><br>
    </body>
</html>