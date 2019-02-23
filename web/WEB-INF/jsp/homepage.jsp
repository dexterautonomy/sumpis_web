<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sumpis</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
        <script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
    </head>
    <body class="bodyClass5">
        <div id="fb-root"></div>
        <script>
            (
                function(d, s, id)
                {
                    var js, fjs = d.getElementsByTagName(s)[0];
                    if (d.getElementById(id))
                        return;
                    js = d.createElement(s);
                    js.id = id;
                    js.src = 'https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.11';
                    fjs.parentNode.insertBefore(js, fjs);
                }
                (document, 'script', 'facebook-jssdk')
            );
        </script>
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
                <a href="sumpisnews?pg=1">Forum</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=writing">Creative</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=arts">History/Art</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=health">Health</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=diy">DIY</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=entertainment">Entertainment</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=sports">Sports</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=relpol">Religion/Politics</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=info">Updates</a>&nbsp;&nbsp;
                <a href="requestcontroller?pg=1&t=flt">Lifestyle</a>&nbsp;&nbsp;
                <a href="aboutus">About Us</a>&nbsp;&nbsp;
                <a href="signuppage">Sign Up</a>
            </div>
        </div>
        <div style="background-color: rgba(232, 255, 245, 0.6);">
            <div class="document">
                    Sumpis is an online text-rewriting tool that can summarise, paraphrase, rearrange and edit lengthy 
                    piece of text-base contents which may be essays, articles, business and legal documents, technical 
                    reports, term papers, projects, thesis, blog and news posts etc. You can use 
                    this application either as an individual or together with your colleagues working on 
                    same document/content. This application re-establishes text-base contents 
                    in simple and clear terms while keeping the main idea the same.
            </div>
            <div class="signup">
                <a href="signuppage">
                    <span class="free">Click Here</span>&nbsp; for extra features which include using the "advanced summary algorithm (ASA v1.0)", 
                    uploading larger file sizes and many more. Guess what? It's absolutely 
                    <span class="free"> FREE!!!</span>
                </a>
            </div>
            <div class="generalinfo">${generalinfo}</div>
        </div>
        <div>
            <form:form action="upload" enctype="multipart/form-data" method="POST" commandName="thirdBeanObject" cssClass="upload">
                <span class="uploadText">Select file/document</span>&nbsp;
                <span class="basic">(pdf, docx, doc, txt only)</span>&nbsp;
                <span style="color: red; text-decoration: underline;">File should not exceed 500kb</span><br>
                <input type="file" name="file" required="true"/>
                <input class="input" type="submit" value="Upload"/>
            </form:form>
        </div>
        <div style="text-align: center; margin-top: 5px; border-top: 1px solid grey; padding-top: 5px;">            
                <div style="margin-bottom: 10px;" class="fb-like" data-href="https://www.facebook.com/Sumpis-341115226366611/" 
                     data-layout="standard" data-action="like" data-size="large" data-show-faces="true" data-share="true">
                </div>
            <div id="app_Info" style="color: red; font-weight: bold;">${fileUploadError}${outputErrorMessage}${expired}</div>
            <div style="color: red; font-weight: bold;">${exp}</div>
        </div>
        
        <%--ADVERT 1--%>
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adid}" varStatus="status" begin="0" end="2">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img style="border-radius: 5px;" src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>
        
        <div style="text-align: center; padding: 5px;">
            <div>        
                <form:form method="post" action="submit" commandName="firstBeanObject" class="formClass">                    
                    <form:textarea id="txt_Area" cols="100" rows="20" path="field" placeholder="Write, paste or upload document here." required="true"/><br>
                    <form:input path="count" size="7" disabled="true" cssClass="input_Field"/> sentence(s).
                    Summarise in <form:input id="specify_Count" path="length" size="5" value="0" placeholder="Digits only" cssClass="input_Field"/> sentences<br><br>
                    
                    <form:button id="button_A" class="input"  name="choice" value="getSentenceCount">Sentence count</form:button>
                    <form:button id="button_XX" class="input"  name="choice" value="getWordCount">Word count</form:button>
                    <form:button id="button_B" class="input"  name="choice" value="paraphrase">Paraphrase</form:button>
                    <form:button id="button_C" class="input"  name="choice" value="summary">Summarise</form:button>
                    <form:button id="button_E" class="input"  name="choice" value="paraphrase_summary">Paraphrase & Summarise</form:button>
                    <form:button id="button_F" class="input" name="choice" value="rearrange">Rearrange</form:button>
                </form:form>
            </div><br>
            <div>
                <form:form method="post" action="outputToInput" commandName="secondBeanObject" class="formClass">                    
                    <form:textarea id="txt_AreaB" class="txtjst1" cols="100" rows="20" path="field2" placeholder="Text output" required="true"/><br>
                    <form:input cssClass="input_Field" path="count2" size="7" disabled="true"/> sentence(s).<br><br><br>
                    <form:button id="button_G" class="input" name="action" value="resendToInput">Resend To Input</form:button>
                    <form:button id="button_H" class="input" name="action" value="download">Download</form:button>
                </form:form>
            </div>
        </div><br><br>
        
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
        </div>
        
        <div style="text-align: center; padding-bottom: 15px;">
            <span style="font-style: italic; font-size: 12px;">Powered by </span><br>
            <a href="http://www.hingebridge.com" target="_blank" class="hbt">HingeBridge</a><br>
            <span style="font-weight: normal; font-size: 14px;">&copy; ${year}</span>
        </div>
        <footer class="foot">
            <span style="font-size: 13px;">
                <span style="font-size: 14px;">Disclaimer: </span>
                The developers of this web application have used their best efforts in preparing the program and therefore make 
                no warranty of any kind, expressed or implied, with regard to the web application or to the functionality therein. 
                The developers shall not be held liable in the event of or consequential damages in connection with, or that results 
                out of the functionality, performance, furnishing, and/or use of this web application. Sumpis is protected by copyright 
                law, and appropriate permission should be obtained from the developers prior to any prohibited reproduction, storage in 
                a retrieval system, or transmission in any form or by any means, electronic, mechanical, photocopying, recording, or likewise.
            </span>
        </footer>
    </body>
</html>