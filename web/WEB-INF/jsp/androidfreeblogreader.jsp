<%-- 
    Document   : androidfreeblogreader
    Created on : 11-May-2018, 14:35:38
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
        
        
        <div class="linknewsAndroid">
            <p style="font-weight: bold; font-size: 28px; text-decoration: underline; text-align: center; margin-top: 20px;">${headline}</p>
            <p style="font-size: 24px; text-align: justify;">${content}</p><br>
            
            <div class="fb-share-button"
                data-href="handlerrequestcontroller?headline=${headline}&date=${date}&writer=${writer}&id=${id}&pageNumb=${backToMenuList}&table=${table}&page=${pageX}"
                data-layout="button_count" data-size="large"
                data-mobile-iframe="true">
                <a class="fb-xfbml-parse-ignore" target="_blank"
                    href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2F127.0.0.1%3A8005%2Fsumpis&amp;src=sdkpreparse">Share
                </a>
            </div><br><br>
            
            <form action="androidLikeCounter" method="POST">
                <input type="hidden" name="username" value="${username}"/>
                <input type="hidden" name="writer" value="${writer}"/>
                <input type="hidden" name="headline" value="${headline}"/>
                <input type="hidden" name="date" value="${date}"/>
                <input type="hidden" name="id" value="${id}"/>
                <input type="hidden" name="pgn" value="${pgn}"/>
                <input type="hidden" name="table" value="${table}"/>
                
                <span style="font-style: italic; font-weight: bold; color: blue;">
                    <button class="inputandroid" name="choice" value="like">${likesX} Like(s)</button>&nbsp;&nbsp;
                    <button class="inputandroid" name="choice" value="comment">Comment</button>
                </span>
            </form><br>
            
            <div style="font-size: 20px;">
                <span style="font-style: italic; font-weight: bold; color: blue;">${viewsX} person(s) viewed this.</span><br><br>
                <span>Writer: @${writer}</span><br><hr>
            </div>
                
            <c:forEach var="titleOne" items="${title}" varStatus="status">
                <ul style="list-style: none; border: 1px solid grey; padding: 0px;
                background-color: rgba(235,255,240,0.7); text-align: left; border-radius: 2px;">
                <li>
                    <div style="background-color: lightgreen; border-radius: 2px; padding: 3px; border-bottom: 1px solid grey; overflow: auto;">
                        <span style="float: left; color: blue; font-weight: bold; font-size: 22px;">@${commentsUsername[status.index]}&nbsp;&nbsp;[${commentsDate[status.index]}]</span>
                        <span style="float: right; color: blue; font-style:italic; font-weight: bold;">${device[status.index]}</span>
                    </div>
                    <div style="text-align: justify; padding: 5px; font-size: 22px;">${mainComments[status.index]}</div><br>
                    
                    <form action="androidcommentactiondx" method="post" style="text-align: center;">
                        <input type="hidden" name="username" value="${username}"/>
                        <input type="hidden" name="writer" value="${writer}"/>
                        <input type="hidden" name="headline" value="${headline}"/>
                        <input type="hidden" name="date" value="${date}"/>
                        <input type="hidden" name="table" value="${table}"/>
                        <input type="hidden" name="pgn" value="${pgn}"/>
                        <input type="hidden" name="id" value="${id}"/>
                        <input type="hidden" name="date2" value="${commentsDate[status.index]}"/>
                        <input type="hidden" name="writer2" value="${commentsUsername[status.index]}"/>
                        <button style="margin-bottom: 5px;" class="inputandroid"  name="choice" value="report">Report(s): ${reportz[status.index]}</button>
                    </form>
                </li>                        
                </ul>
            </c:forEach>
            <span style="font-size: 20px;">${prev}&nbsp;&nbsp;${next}</span>
        </div>
    </body>
</html>