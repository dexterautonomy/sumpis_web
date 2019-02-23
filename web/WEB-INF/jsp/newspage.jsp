<%-- 
    Document   : homepage
    Created on : 18-Sep-2017, 23:38:07
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${headline}</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div id="fb-root"></div>
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
        <div class="div1" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="/sumpis/"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div>            
            <div style="float: right;">
                <form:form style="float: right;" commandName="fourthBeanObject" action="login" method="POST">
                    <table>
                        <tr>
                            <td>Username</td>
                            <td>Password</td>
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
                <a href="/sumpis">Index</a>&nbsp;&nbsp;
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
                <a href="signuppage">Sign Up</a>
            </div>
        </div><br><br>
        
        <%--ADVERT 1--%>
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adid}" varStatus="status" begin="0" end="2">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img  src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>
        
        <div class="linknews2">
            <p style="color: red; font-weight: bold; text-align: center;">${commentary}</p>
            <p style="font-weight: bold; font-size: 21px; text-decoration: underline; text-align: center; margin-top: 20px;">${headline}</p>            
            <p style="text-align: justify;">${story}</p>
            
            <div class="comment">
                <form action="commentaryControllerx?pg=${pg}&n=${n}&pgn=${pgn}" method="POST">
                    <button name="submit" value="comment">Comment</button>
                </form>
            </div><br><br>
            
            <div class="fb-share-button"
                data-href="http://127.0.0.1:8005/sumpis/getstory?pg=${pg}&n=${n}&pgn=${pgn}" data-layout="button_count" data-size="large" data-mobile-iframe="true">
                <a class="fb-xfbml-parse-ignore" target="_blank"
                    href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2F127.0.0.1%3A8005%2Fsumpis&amp;src=sdkpreparse">Share
                </a>
            </div><br><br>
            
            <span>Writer: <a href="profile?ux=${writer}">@${writer}</a></span><br>
            <p style="text-align: center;"><a href="sumpisnews?pg=${pg}">Back</a></p><hr>
            
            <c:forEach var="nxd" items="${cList}" varStatus="status">
                <ul style="list-style: none; border-left: 3px solid palegreen; border-right: 1px solid grey; 
                     border-bottom: 1px solid grey; padding: 0px; background-color: rgba(235,255,240,0.7); text-align: left; border-radius: 3px;">
                <li>
                    <div style="overflow: auto; background-color: palegreen; padding: 3px;">
                        <div style="float: left;">
                            <a href="profile?ux=${commentsUsername[status.index]}"><img style="border-radius: 50%;" src="${pageContext.request.contextPath}/resources/images/profile_pix/${pix[status.index]}" width="62" height="62" /></a>
                        </div>
                        <div style="border-radius: 2px; overflow: auto; border-bottom: 1px solid grey;">
                            <%--ADDING PROFILE--%>
                            <span style="float: left; color: blue; font-weight: bold;">
                                <a href="profile?ux=${commentsUsername[status.index]}">@${commentsUsername[status.index]}&nbsp;&nbsp;[${commentsDate[status.index]}]</a>
                            </span>
                            <span style="float: right; color: blue; font-style:italic; font-weight: bold;">${device[status.index]}</span>
                        </div>
                    </div>
                    <div style="text-align: justify; padding: 5px;">
                        <div style="text-align: justify; padding: 5px; width: 95%; margin-left: 5%; margin-right: 0%;">
                            ${quotez[status.index]}
                        </div>
                            ${mainComments[status.index]}
                    </div><br>
                    <form action="commentAction" method="post" style="text-align: center; margin-bottom: 5px;">
                        <input type="hidden" name="headline" value="${headline}"/>
                        <input type="hidden" name="writer2" value="${commentsUsername[status.index]}"/>
                        <input type="hidden" name="date2" value="${commentsDate[status.index]}"/>
                        <input type="hidden" name="nID" value="${n}"/>
                        <input type="hidden" name="cID" value="${nxd}"/>
                        <input type="hidden" name="pg" value="${pg}"/>
                        <input type="hidden" name="pgn" value="${pgn}"/>
                        <textarea type="text" name="subcomment" style="display: none;">${mainComments[status.index]}</textarea>
                        <button class="input"  name="choice" value="like">Like(s): ${commentLikes[status.index]}</button>&nbsp;&nbsp;
                        <button class="input"  name="choice" value="dislike">Dislike(s): ${commentsDislikes[status.index]}</button>&nbsp;&nbsp;
                        <button class="input"  name="choice" value="share">Share(s): ${commentsShares[status.index]}</button>&nbsp;&nbsp;
                        <button class="input"  name="choice" value="quote">Quote</button>&nbsp;&nbsp;
                        <button class="input"  name="choice" value="report">Report(s): ${reportz[status.index]}</button>
                    </form>
                </li>                        
                </ul>
            </c:forEach>
            ${prev}&nbsp;&nbsp;${next}
        </div>
        <%--ADVERT 2--%>
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adid}" varStatus="status" begin="3" end="5">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img  src="${pageContext.request.contextPath}/resources/images/adbanners/${adimg[status.index]}" width="250" height="100" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div><br><br><br>
    </body>
</html>