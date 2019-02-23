<%-- 
    Document   : adminpasswordupdate
    Created on : 08-Nov-2017, 20:59:49
    Author     : DEXTER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Password Update</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="memberdiv" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="adminpasswordchange"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
                <a href="generalinfo">Settings</a>&nbsp;&nbsp;
                <a href="adminpasswordchange">Password Change</a>
                
            </div>
        </div>        
        <div class="admin" style="clear: both;">
            <span style="font-size: 22px; color: purple;">Admin: ${admin}</span>
            <a style="font-size: 14px;" href="logout">Logout</a>
        </div>            
        
            <div id="error_jquery" style="color: red; text-align: center; margin-top: 20px;">${passwordinfo}</div>
            <div id="error_paswd" style="color: red; text-align: center;"></div>
            <div style="padding: 40px; border: 1px solid grey; border-radius: 5px; margin-left: auto; margin-right: auto; max-width: 500px;">
                <form:form method="POST" action="adminpasswordchangecontroller" commandName="twentythirdBeanObject">
                    <div style="font-size: 24px; text-align: center;">Admin Password Update Form</div><hr>
                    <table align='center'>
                    <tr>
                        <td><span class="input_Label">Username</span></td>
                        <td><form:input id="username_Admin" path="userName" placeholder="Enter username" cssClass="input_FieldG"/>
                        <br><span id="admin_Username_Info" style="color: red;"></span></td>                        
                    </tr>
                    <tr>
                        <td><span class="input_Label">Current Password</span></td>
                        <td><form:password path="oldPassword" placeholder="Enter current password" cssClass="input_FieldG"/></td>
                    </tr>
                    <tr>
                        <td><span class="input_Label">New Password</span></td>
                        <td><form:password id="pswd_Length1" path="newPassword1" placeholder="Enter new password" cssClass="input_FieldG"/>
                        <br><span id="pswd_Info1" style="color: red;"></span></td>
                    </tr>                    
                    <tr>
                        <td><span class="input_Label">Confirm New Password</span></td>
                        <td><form:password id="pswd_Length2" path="newPassword2" placeholder="Confirm new password" cssClass="input_FieldG"/>
                        <br><span id="pswd_Info2" style="color: red;"></span></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <form:button id="signup_ButtonG" class="input" name="action" value="update">Update</form:button>
                            <form:button class="input" name="action" value="cancel">Cancel</form:button>
                        </td>
                    </tr>
                </table>                
                </form:form>
            </div>
    </body>
</html>