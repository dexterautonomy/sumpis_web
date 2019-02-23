<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign-Up Platform</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">        
        <div class="div1" style="overflow: auto;">            
            <div style="float: left; padding: 5px;">
                <a href="signuppage"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
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
                <a href="/sumpis">Index</a>&nbsp;&nbsp;
                <a href="sumpisnews?pg=1">General</a>&nbsp;&nbsp;
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
            </div>
        </div>
        
        <div style="float: right; padding: 30px; border: 1px solid grey; margin-top: 40px; border-radius: 15px;
            background-color: rgba(232,247,240,0.2)">            
            <form:form commandName="fifthBeanObject" method="POST" action="signup" id="signup_Form">                
                <span style="font-weight: bold; font-size: 28px; color: green;">
                    <a href="signuppage">Sign-up it's absolutely free!</a>
                </span><hr>
                <table align='center'>
                    <tr>
                        <td><span class="input_Label">Firstname</span></td>
                        <td><form:input path="firstName" placeholder="Firstname" cssClass="input_Field" required="true"/></td>                        
                    </tr>
                    <tr>
                        <td><span class="input_Label">Lastname</span></td>
                        <td><form:input path="lastName" placeholder="Lastname" cssClass="input_Field"/></td>
                    </tr>
                    
                    <tr>
                        <td><span class="input_Label">Username</span></td>
                        <td><form:input id="ajax_Username" path="userName" placeholder="Username" cssClass="input_Field" required="true"/>
                        <br><span id="ajax_Username_Info" style="color: red;"></span></td>
                    </tr>
                    
                    <tr>
                        <td><span class="input_Label">Mobile</span></td>
                        <td><form:input id="telephone" path="mobile" placeholder="Mobile number" cssClass="input_Field" required="true"/>
                        <br><span id="telephone_Info" style="font-size: 14px;">Include country code<br>e.g +144521447</span></td>
                    </tr>
                    
                    <tr>
                        <td><span class="input_Label">Password</span></td>
                        <td><form:password id="pswd_Length1" path="password1" placeholder="Password" cssClass="input_Field" required="true"/>
                        <br><span id="pswd_Info1" style="color: red;"></span></td>
                    </tr>
                    
                    <tr>
                        <td><span class="input_Label">Confirm Password</span></td>
                        <td><form:password id="pswd_Length2" path="password2" placeholder="Confirm Password" cssClass="input_Field" required="true"/>
                        <br><span id="pswd_Info2" style="color: red;"></span></td>
                    </tr><br>
                    
                    <tr>
                        <td></td>
                        <td><input id="signup_Button" class="input" type="submit" value="Sign-up"/></td>
                        <td></td>
                    </tr>
                </table><hr>
                <span id="error_jquery" style="color: red; font-weight: bold; font-size: 16px;">${g}</span>
                <span id="error_paswd" style="color: red; font-weight: bold; font-size: 16px;"></span>
            </form:form>            
        </div>
    </body>
</html>