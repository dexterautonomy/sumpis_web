<%-- 
    Document   : androidnewspage
    Created on : 04-May-2018, 08:44:18
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
            <p style="text-align: justify; font-size: 24px;">${story}</p>
            
            <div class="commentandroid">
                <form action="androidCommentaryController?writer=${writer}&username=${usernameAndroid}&headline=${headline}&date=${date}&id=${id}&pgn=${pgn}" method="POST">
                    <button name="submit" value="comment">Comment</button>
                </form>
            </div><br><br>            
            
            
            <div class="fb-share-button"
                data-href="http://127.0.0.1:8005/sumpis/getstory?headline=${headline}&date=${date}&id=${current}&writer=${writer}&pgn=${pgn}" data-layout="button_count" data-size="large" data-mobile-iframe="true">
                <a class="fb-xfbml-parse-ignore" target="_blank"
                    href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2F127.0.0.1%3A8005%2Fsumpis&amp;src=sdkpreparse">Share
                </a>
            </div><br><br>   
            <span style="font-size: 20px;">Writer: @${writer}</span><hr>
            
            
            <c:forEach var="titleX" items="${title}" varStatus="status">
                <ul style="list-style: none; border: 1px solid grey; padding: 0px;
                background-color: rgba(235,255,240,0.7); text-align: left; border-radius: 3px;">
                <li>
                    <div style="background-color: lightgreen; border-radius: 2px; padding: 3px; border-bottom: 1px solid grey; overflow: auto;">
                        <span style="float: left; color: blue; font-weight: bold; font-size: 22px;">@${commentsUsername[status.index]}&nbsp;&nbsp;[${commentsDate[status.index]}]</span>
                        <span style="float: right; color: blue; font-style:italic; font-weight: bold;">${device[status.index]}</span>
                    </div>
                    <div style="text-align: justify; padding: 5px; font-size: 22px;">${quotez[status.index]} ${mainComments[status.index]}</div><br>
                    <form action="androidCommentAction" method="post" style="text-align: center; margin-bottom: 5px;">
                        <input type="hidden" name="writer" value="${writer}"/>
                        <input type="hidden" name="username" value="${usernameAndroid}"/>
                        <input type="hidden" name="headline" value="${headline}"/>
                        <input type="hidden" name="date" value="${date}"/>
                        <input type="hidden" name="id" value="${id}"/>
                        <input type="hidden" name="pgn" value="${pgn}"/>
                        <input type="hidden" name="date2" value="${commentsDate[status.index]}"/>
                        <input type="hidden" name="writer2" value="${commentsUsername[status.index]}"/>
                        
                        <textarea type="text" name="subcomment" style="display: none;">${mainComments[status.index]}</textarea>
                        
                        <button class="inputandroid"  name="choice" value="like">Like(s): ${commentLikes[status.index]}</button>&nbsp;&nbsp;
                        <button class="inputandroid"  name="choice" value="dislike">Dislike(s): ${commentsDislikes[status.index]}</button>&nbsp;&nbsp;
                        <button class="inputandroid"  name="choice" value="share">Share(s): ${commentsShares[status.index]}</button>&nbsp;&nbsp;
                        <button class="inputandroid"  name="choice" value="quote">Quote</button>&nbsp;&nbsp;
                        <button class="inputandroid"  name="choice" value="report">Report(s): ${reportz[status.index]}</button>
                    </form>
                </li>                        
                </ul>
            </c:forEach>
            
            <span style="font-size: 20px;">${prev}&nbsp;&nbsp;${next}</span>
        </div>
    </body>
</html>