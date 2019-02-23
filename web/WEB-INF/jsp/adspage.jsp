<%-- 
    Document   : adspage
    Created on : 19-Jun-2018, 00:32:39
    Author     : DEXTER
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Advert Placement</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv">            
            <div style="float: left; padding: 5px;">
                <a href="ad_ctr?pg=1"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
                <a href="userpasswordchange">Settings</a>&nbsp;&nbsp;
                <a href="ad_ctr?pg=1">Ad</a>
            </div>
        </div>        
        <div class="document2" style="overflow: auto;">
            <div>
                <span style="font-size: 22px;">${details}</span>&nbsp;
            <a href="logout">Logout</a>
            </div>
        </div><br>
        
        <h1 style="text-align: center; color: blue; text-decoration: underline;">DashBoard</h1>
        <p style="text-align: center; color: red;">${info}</p>
        
        <div style="overflow: auto; max-width: 1050px; margin-left: auto; margin-right: auto; border: 1px solid grey; border-radius: 4px;">
            <div style="float: left; padding: 20px; background-color: pink; border-left: 3px solid red; border-radius: 3px;">
                <span class="ad"><a href="ad_ctr?pg=1&e=vx" style="text-decoration: none;">New Ad</a></span><br><br>
                <span class="ad"><a href="ad_ctr?pg=1&e=vz" style="text-decoration: none;">View Ad</a></span>
            </div>
            <div style="float: right; padding: 20px; background-color: pink; border-right: 3px solid red; border-radius: 3px;">
                <span class="ad"><a href="adcredit" style="text-decoration: none;">Buy Ads Credit</a></span><br><br>
                <span class="ad"><a href="" style="text-decoration: none;">Terms &amp; Condition</a></span>
            </div>
        </div>
        
        <%--ADVERT 1
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adidx}" varStatus="status" begin="0" end="2">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img style="display: ${dispzx[status.index]} " src="${pageContext.request.contextPath}/resources/images/adbanners/${adimgx[status.index]}" width="250" height="100" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>--%>
            
        <div style="width: 50px; padding: 10px; display: ${displayx};">
            <form action="adupload" method="post" enctype="multipart/form-data" style="width: 680px; margin: 0 auto; margin-bottom: 100px;">
                <fieldset>
                    <legend><h2 style="color: blue;">Create New Ad</h2></legend>
                    <table>
                        <tbody>
                        <tr>
                            <td>
                                Landing Page:
                            </td>
                            <td>
                                <input class="input_Field" type="url" name="landingpage" size="35" placeholder="webpage to land request" required/><br><br>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                Ad Image
                            </td>
                            <td>
                                <input type="file" name="file" required/><br><br>
                            </td>
                        </tr>
                            
                        <tr>
                            <td>
                                Payment Mode:
                            </td>
                            <td>
                                <select class="input_Field" name="payment">
                                    <option selected>CPM</option>
                                    <option>CPC</option>
                                </select>
                            </td>
                        </tr>
                           
                        <tr>
                            <td>
                                Targeted Ad (Page):
                            </td>
                            <td>
                                <select class="input_Field" name="target">
                                    <option>DIY</option>
                                    <option>HEALTH</option>
                                    <option>SPORTS</option>
                                    <option selected>GENERAL</option>
                                    <option>WRITING/ART</option>
                                    <option>ENTERTAINMENT</option>
                                    <option>RELIGION/POLITICS</option>
                                    <option>TECH/UPDATES/INFO</option>
                                    <option>LIFESTYLE/TRENDS/FASHION</option>
                                </select><br><br>
                            </td>
                        </tr>
                            
                        <tr>
                            <td>
                            </td>
                            <td>
                                <input id="post_Message" class="input" type="submit" name="Submit"/><br><br>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </fieldset>
            </form>
        </div>
                
        <div style="padding: 10px;  display: ${displayz};">
            <h2 style="text-align: center; text-decoration: underline; color: blue;">My Ad(s)</h2>
            <h4 style="text-align: center; color: green;">Balance: ${balance}</h4>
            <p style="text-align: center;"><a href='adcredit' style="color: red; text-decoration: underline;">Buy Ads Credit</a></p>
            <table cellpadding='6' align='center'>
                <tr>
                    <th class='tdmateheader'>Approve</th>
                    <th class='tdmateheader'>Mode</th>
                    <th class='tdmateheader'>Created</th>
                    <%--<th class='tdmateheader'>Ends</th>--%>
                    <th class='tdmateheader'>Clicks</th>
                    <th class='tdmateheader'>Views</th>
                    <th class='tdmateheader'>Banner</th>
                    <th class='tdmateheader'>Redirect</th>
                    <th class='tdmateheader'>Target</th>
                    <th class='tdmateheader'>Pause</th>
                    <th class='tdmateheader'>Action</th>
                </tr>
                    
                <c:forEach var="id" varStatus="status" items="${adid}">
                    <form:form action="ad_ctr?pg=${pg}&e=vz" method="post" style="margin-bottom: 50px;" commandName="adClass">
                    <tr>
                        <td class='tdwriter'>
                            <div style="font-size: 12px; text-align: center;">
                                ${approve[status.index]}<br>
                                <form:hidden path='var1' value="${id}"/>
                            </div>
                        </td>
                        <td class='tdmate'>
                            <div style="text-align: center; font-size: 12px;">
                                ${pymt[status.index]}<br>
                                <form:select path='var2' class="input_Field" cssStyle="display: ${dispx}; text-align: center;">
                                    <form:option value="CPM">CPM</form:option>
                                    <form:option value="CPC">CPC</form:option>
                                </form:select>
                            </div>
                        </td>
                        <td class='tdmate'>
                            <div style="font-size: 13px;">${sDate[status.index]}</div>
                        </td>
                        <%--<td class='tdmate'>
                            <div style="font-size: 13px;">${eDate[status.index]}</div>
                        </td>--%>
                        <td class='tdstat'>
                            <div style="text-align: center; font-size: 12px;">${clickz[status.index]}</div>
                        </td>
                        <td class='tdstat'>
                            <div style="text-align: center; font-size: 12px;">${viewz[status.index]}</div>
                        </td>
                        <td class='tdmate'>
                            <a href="${lndPage[status.index]}">
                                <img src='${pageContext.request.contextPath}/resources/images/adbanners/${adImg[status.index]}' width='100' height='80'/>
                            </a>
                        </td>
                        <td class='tdmate'>
                            <div style="font-size: 13px;">
                                <a href="${lndPage[status.index]}">${lndPage[status.index]}</a><br>
                                <form:input path='var7' value="${lndPage[status.index]}" required="true" cssClass="input_Field" cssStyle="display: ${dispx}; text-align: center;"/>
                            </div>
                        </td>
                        <td class='tdmate'>
                            <div style="font-size: 12px;">
                                ${trgPage[status.index]}<br>
                                <form:select path='var8' class="input_Field" style="display: ${dispx}; font-size: 12px;">
                                    <form:option value="DIY">DIY</form:option>
                                    <form:option value="HEALTH">HEALTH</form:option>
                                    <form:option value="SPORTS">SPORTS</form:option>
                                    <form:option value="GENERAL" selected="true"> GENERAL</form:option>
                                    <form:option value="WRITING/ART">WRITING/ART</form:option>
                                    <form:option value="ENTERTAINMENT">ENTERTAINMENT</form:option>
                                    <form:option value="RELIGION/POLITICS">RELIGION/POLITICS</form:option>
                                    <form:option value="TECH/UPDATES/INFO">TECH/UPDATES/INFO</form:option>
                                    <form:option value="LIFESTYLE/TRENDS/FASHION">LIFESTYLE/TRENDS/FASHION</form:option>
                                </form:select>
                            </div>
                        </td>
                        <td class='tdmate'>
                            <div style="font-size: 12px;">${onPause[status.index]}</div>
                        </td>
                            
                        <td class='tdmate'>
                            <button class="inputGreen"  name="var5" value="st">Activate</button>
                            <button class="input"  name="var5" value="et">Edit</button>
                            <button class="inputPause"  name="var5" value="ps">Pause</button>                            
                            <button class="inputGreen"  name="var5" value="upd">Update</button><br><br>
                            <button class="inputRed"  name="var5" value="dst">Destroy</button>
                        </td>
                    </tr>
                    </form:form>
                </c:forEach>
            </table>
            <p style="text-align: center;">${prev}&nbsp;&nbsp;&nbsp;${next}</p>
        </div>
        
        <%--ADVERT 2
        <div style="margin: 0 auto; text-align: center;">
            <c:forEach var="id" items="${adidx}" varStatus="status" begin="3" end="3">
                <ul style="list-style: none; display: inline-block;">
                    <li>
                        <a href="k?n=${id}">
                            <img style="display: ${dispzx[status.index]} " src="${pageContext.request.contextPath}/resources/images/adbanners/${adimgx[status.index]}" width="400" height="150" alt="advert"/>
                        </a>
                    </li>
                </ul>
            </c:forEach>
        </div>--%>
    </body>
</html>