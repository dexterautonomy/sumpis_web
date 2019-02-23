<%-- 
    Document   : adpage
    Created on : 20-Jun-2018, 12:56:18
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Advert Control Panel</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="text-align: center; float: left; padding: 5px;">
                <a href="adpanel?pg=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
        
        
        <h1 style="text-align: center; color: blue; text-decoration: underline;">Ad Control Panel</h1>
        <p style="text-align: center; color: red;">${info}</p>
    
        <div style="overflow: auto; width: 95%; margin-left: auto; margin-right: auto; border: 1px solid grey; border-radius: 4px;">
            <div style="float: left; padding: 20px; background-color: pink; border-right: 3px solid red; border-radius: 3px;">
                <span class="ad"><a href="">Update Pricing</a></span><br><br>
                <span class="ad"><a href="">Update Terms and Condition</a></span>
            </div>
        </div>
            
        <div style="padding: 10px;  display: ${dispz};">            
            <h2 style="text-align: center; text-decoration: underline;">Adverts</h2>
            <table cellpadding='2' align='center'>
                <tr>
                    <th class='tdmateheader'>Approve</th>
                    <th class='tdmateheader'>Publisher</th>
                    <th class='tdmateheader'>Balance ($)</th>
                    <th class='tdmateheader'>CPM ($)</th>
                    <th class='tdmateheader'>CPC ($)</th>
                    <th class='tdmateheader'>Expired</th>
                    <th class='tdmateheader'>Duration</th>
                    <th class='tdmateheader'>Pay Mode</th>
                    <th class='tdmateheader'>Created</th>
                    <%--<th class='tdmateheader'>Ends</th>--%>
                    <th class='tdmateheader'>Clicks</th>
                    <th class='tdmateheader'>Views</th>
                    <th class='tdmateheader'>Banner</th>
                    <th class='tdmateheader'>Redirect</th>
                    <th class='tdmateheader'>Target Ad</th>
                    <th class='tdmateheader'>Pause</th>
                    <th class='tdmateheader'>Action</th>
                </tr>
                    
                <c:forEach var="id" varStatus="status" items="${idList}">
                    <form:form action="adpanel" method="post" cssStyle="margin-bottom: 50px;" commandName="adClass">
                    <form:hidden path="var6" value="${pgList[0]}"/>
                    <input type="hidden" name="pg" value="0"/><%--very important--%>
                        <tr>
                            <td class='tdwriter'>
                                <div style="font-size: 12px; text-align: center;">
                                    <form:input path="var1" value="${id}" size="3" readonly="true" class="input_Field" style="text-align: center; background-color: green; color: white;"/><br><br>
                                    ${apList[status.index]}
                                </div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="text-align: center; font-size: 12px;">
                                    ${pubList[status.index]}
                                    <form:hidden path="var2" value="${pubList[status.index]}"/>
                                </div>
                            </td>
                        
                            <td class='tdstat'>
                                <div style="text-align: center; font-size: 12px;">${bList[status.index]}</div>
                            </td>
                            
                            <td class='tdstat'>
                                <div style="text-align: center; font-size: 12px;">
                                    ${cpmList[status.index]}<br>
                                    <form:input path="var3" value="${cpmList[status.index]}" size="3" class="adEditx" style="display: ${dispx};"/>
                                </div>
                            </td>
                        
                            <td class='tdstat'>
                                <div style="text-align: center; font-size: 12px;">
                                    ${cpcList[status.index]}<br>
                                    <form:input path="var4" value="${cpcList[status.index]}" size="3" class="adEditx" style="display: ${dispx};"/>
                                </div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="text-align: center; font-size: 12px;">${xList[status.index]}</div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="text-align: center; font-size: 12px;">${dList[status.index]}</div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="text-align: center; font-size: 12px;">${pList[status.index]}</div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="font-size: 13px;">${sList[status.index]}</div>
                            </td>
                        
                            <%--<td class='tdmate'>
                                <div style="font-size: 13px;">${eList[status.index]}</div>
                            </td>--%>
                        
                            <td class='tdstat'>
                                <div style="text-align: center; font-size: 12px;">${cList[status.index]}</div>
                            </td>
                        
                            <td class='tdstat'>
                                <div style="text-align: center; font-size: 12px;">${vList[status.index]}</div>
                            </td>
                        
                            <td class='tdmate'>
                                <a href="${landList[status.index]}">
                                    <img src='${pageContext.request.contextPath}/resources/images/adbanners/${imgList[status.index]}' width='100' height='80'/>
                                </a>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="font-size: 13px;"><a href="${landList[status.index]}">${landList[status.index]}</a></div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="font-size: 12px;">${tList[status.index]}</div>
                            </td>
                        
                            <td class='tdmate'>
                                <div style="font-size: 12px;">${pauseList[status.index]}</div>
                            </td>
                        
                            <td class='tdmate'>
                                <form:button class="inputGreen"  name="var5" value="apv">Approve</form:button>
                                <form:button class="input"  name="var5" value="msg">Message</form:button>
                                <form:button class="input"  name="var5" value="edit">Edit</form:button>
                                <form:button class="inputRed"  name="var5" value="stp">Deactivate</form:button>
                                <form:button class="inputGreen"  name="var5" value="upd">Update</form:button>
                            </td>
                        </tr>
                    </form:form>
                    </c:forEach>
                </table>
            <p style="text-align: center;">${prev[0]}&nbsp;&nbsp;&nbsp;${next[0]}</p>
        </div>
    </body>
</html>