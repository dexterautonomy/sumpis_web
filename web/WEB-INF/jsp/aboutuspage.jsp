<%-- 
    Document   : aboutuspage
    Created on : 14-Dec-2017, 11:59:46
    Author     : DEXTER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>About Us</title>
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/system_images/logo.png"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sumpis.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jQueryFile.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sumpisjs.js"></script>
    </head>
    <body class="bodyClass5">
        <div class="memberdiv">
            <div style="float: left; padding: 5px;">
                <a href="/sumpis/"><img src="${pageContext.request.contextPath}/resources/images/system_images/logo.png" width="55" height="55" alt="sumpis logo"/></a>
            </div> 
            <div class="bizX" style="margin-top: 10px;">
                <a href="switcher">My Desk</a>&nbsp;&nbsp;
                <a href="/sumpis/">Index</a>&nbsp;&nbsp;
                <a href="aboutus">About Us</a>&nbsp;&nbsp;
                <a href="#terms">Sumblog Terms &amp; Condition</a>&nbsp;&nbsp;
                <a href="#contactus">Contact Us</a>
            </div>
        </div><br><br>
        
         <p style="text-align: center; color: red; margin: auto;">${success}</p>
        <div class="aboutusIntro">
            <div style="font-size: 28px; font-weight: bold; text-align: center;">About Sumpis</div><br>
            Sumpis is a professional text re-writing tool that offers a variety
            of writing solutions as well as a content and blogging site. It is
            developed by a team of writers cum computer programmers. This web application is aimed
            at alleviating the stress in writing, real-time editing, communication
            and transfer of text-based write-ups between colleagues. Users can utilise the functionalities
            of this web application to achieve quite a number of text re-writing 
            functionalities which include summarising, paraphrasing,
            finding the total sentence(s) in a text-based write-up, finding the total
            word count in a write-up, rearranging a text-based write-up as well as
            uploading and sharing text-based documents between colleagues which
            may be essays, articles, business and legal documents, technical reports, 
            term papers, academic projects, thesis, letters, blog posts, news etc.<br><br>
            <%--The motivation behind the development of Sumpis is due to but not limited to
            the need to: 
            <ul>
                <li>
                    easily summarise, paraphrase and/or rearrange a very lengthy
                    piece of purely text-based write-up with a single click of the
                    respective button(s).
                </li>
                <li>
                    get the total number of word(s) and sentence(s) in a lengthy piece of write-up/text.
                </li>
                <li>
                    seamlessly write, edit and share purely text-based contents, projects,
                    news or blog posts as well as events and happenings around the world between colleagues
                    irrespective of distance and time contraints.
                    </li>
                <li>
                    earn an honest living while taking up a writing career 
                    <a href="#terms" style="text-decoration: none;">@sumblogging platform</a> 
                    as part of the <b>"work, write and earn"</b> initiative.
                </li>
                <li>
                    also post news, information and events around the world.
                </li>
            </ul><br><br>--%>
            
            <%--
            <span id="howtousesumpis" style="font-size: 28px; font-weight: bold;">How To Use Sumpis</span><br>
            Sumpis has a lot of functionalities that are quite easy to use anyway- with just a click of the appropriate buttons, links and/or tabs.
            <div class="nav1">
                <ol>
                    <li>
                        <a>Switch</a>: This enables switching between the index page and the registered member account page. 
                        This is made possible in situations where a logged on member wants to visit the index page after 
                        logging in. There is no point logging out, simply use the switch tab to switch between your account/active session and the 
                        index page while your account session remains alive. This tab is not functional for visitors/unregistered members.
                    </li>
                    <li>
                        <a>Short News</a>: This tab is for accessing the short/summarised news list around the world.
                    </li>
                    <li>
                        <a>Writing Art</a>: This tab is for accessing the WRITING ART sumblog post list.
                    </li>
                    <li>
                        <a>How-To</a>: This tab is for accessing the HOW-TO sumblog post list.
                    </li>
                    <li>
                        <a>Entertainment/Trend</a>: This tab is for accessing the LIFESTYLE/ENTERTAINMENT sumblog post list.
                    </li>
                    <li>
                        <a>Religion/Politics</a>: This tab is for accessing the RELIGION/POLITICS sumblog post list.
                    </li>
                    <li>
                        <a>Health/Lifestyle</a>: This tab is for accessing the HEALTH/TREND sumblog post list.
                    </li>
                    <li>
                        <a>About Us</a>: This tab is for accessing the ABOUT US page. It is the current page you are viewing. 
                    </li>
                    <li>
                        <a>Sign Up</a>: This tab is for registration of new members.
                    </li>
                    <li>
                        The buttons are self explanatory save for <button class="input">Summarise</button> button  and  the
                        <button class="input">Streamline-Summary</button> button. The latter uses a more advance summary algorithm. 
                        This is a bonus to all registered users of @Sumpis.
                    </li>
                </ol>
            </div>
            
            Navigating through Sumpis is easy and convenient for users. The application is broken into three (3) categories 
            viz: visitors, registered members and sumbloggers. The three (3) categories have clearance level viz:
            <ol>
                <li>
                    <span>VISITORS:</span><br>
                    They can use the basic aspect of Sumpis which include:<br>
                    <ul>
                        <li>
                            <b>100kb</b> file upload button: 
                            <button class="input">Upload</button>
                        </li>
                        <li>
                            And these buttons: 
                            <button class="input">Sentence count</button>
                            <button class="input">Word count</button>
                            <button class="input">Paraphrase</button>
                            <button class="input">Summarise</button>
                            <button class="input">Paraphrase & Summarise</button>
                            <button class="input">Rearrange</button>
                            <button class="input">Resend To Input</button>
                            <button class="input">Download</button>
                        </li>
                    </ul>                    
                </li><br>
                
                <li>
                    <span>REGISTERED MEMBERS:</span><br>
                    They can use both the basic aspect of Sumpis and some additional features which include:<br>
                    <ul>
                        <li>
                            A search field:<br>
                            Search by username @
                            <input placeholder="Search by username" size="24" class="input_FieldA"/>
                            <button class="input">Search</button>
                        </li>
                        <li>
                            <b>500kb</b> file upload button: 
                            <button class="input">Upload</button>
                        </li>
                        <li>
                            And these buttons: (notice the Streamline-Summary button) 
                            <button class="input">Sentence count</button>
                            <button class="input">Word count</button>
                            <button class="input">Paraphrase</button>
                            <button class="input">Streamline-Summary</button>
                            <button class="input">Paraphrase & Summarise</button>
                            <button class="input">Rearrange</button>
                            <button class="input">Resend To Input</button>
                            <button class="input">Download</button>
                            <button class="input">Forward</button>
                            <button class="input">Post This</button>
                        </li>                        
                    </ul>                    
                </li><br>
                
                <li>
                    <span>SUMBLOGGERS:</span><br>
                    These are REGISTERED MEMBERS that are also sumbloggers
                    <ul>                        
                        <li>
                            An additional tab:<br>
                            <div>
                                <a style="color: red;">Sumblog</a>
                            </div>
                        </li>                        
                    </ul>                    
                </li>                
            </ol><br><hr><br>
            
            <span id="sumblg" style="font-size: 28px; font-weight: bold;">Sumblogging</span><br>
            This is a tool that enables registered users to blog on the Sumpis platform and earn money based on the number 
            of likes and views their posts attract. It enables aspiring bloggers who do not yet have a blog to create a 
            pseudo blog on the platform and also promote already existing bloggers.<br><br>
            
            <span id="terms" style="font-size: 28px; font-weight: bold;">Sumblog Terms and Condition</span><br>
            The following are the terms and condition for aspiring and existing sumbloggers. Please ensure you go through it 
            carefully and seek clarification from @admin on areas you do not understand. These terms bind all sumbloggers 
            and is a prerequisite to becoming one. <span style="color: red; font-weight: bold;">Please note that everything on sumpis 
            is absolutely free!!!</span>
            <ol>
                <li>
                    To become a sumblogger, you must have an account on Sumpis. Click on the link <a href="signuppage">Sign-up</a> to sign-up.
                </li><br>
                <li>
                    Once you have a registered account, aspiring sumbloggers should simply write a short message to @admin stating their intention 
                    and area of specialisation for example<br><br><i>Hi Admin!<br>I'd like to be a sumblogger, I specialise on content writing, politics 
                    and literature. Thank you.<br>@melissa</i>.<br><br>
                    The area of specialisation are: General Writing, Arts, Literature, Entertainment, Global Updates, Health & Health Tips, DIY(Do It Yourself Guide), 
                    Global Trends, World Religion, Global Politics, Information & Did You Know Series, World Fashion and Lifestyles.<br><br>
                </li>
                <li>
                    Your message will be received and processed within 24 hours after which the <span style="color: red; text-decoration: none;">Sumblog</span> tab 
                    appears on your registered account page with your username in italics beside it like so <a href="" style="color: red; text-decoration: none;">Sumblog @<span style="font-style: italic; color: red;">melissa</a>                    
                </li><br>
                <li>
                    Clicking on the link takes you to the Sumblog messaging center where the category drop-down list has the area 
                    of specialisation you specified during registration and matched according to the specialisation currently on the platform. 
                </li><br>
                <li>
                    If by any circumstance, you want to modify, remove or add another area of specialisation, simply write to @admin, but be aware that 
                    the maximum area of specialisation is two (2).
                </li><br>
                <li>
                    All posts pass scrutiny by @TeamSumpis before being allowed to the outside world and by this note we reiterate 
                    that racial, political and or religious inciteful, plegiarized, x-rated contents etc are completely disallowed on 
                    this platform.
                </li><br>
                <li>
                    Any Sumblogger who violates term (6) above, will be banned or blocked depending on the severity of the situation. Racially inciteful 
                    posts will automatically attract immediate ban while others will have to be deliberated by @TeamSumpis before proper action is taken. 
                </li><br>
                <li>
                    If a blog post contains graphic content especially gory images, please crave the attention of the purported audience by including
                    (Graphic Content) or (Viewers Discretion) like so in title of post;<br><b>Steps In The Caesarean Section (Graphic Content)</b> or 
                    <b>Steps In The Caesarean Section (Viewers Discretion).</b><br>
                    If a blog post contains sensual or mild sexual contents, please use (+18) beside the title to crave the attention of the purported 
                    audience. This so, does not guarantee a pass for such posts as @TeamSumpis has a say to the passage of explicit contents.
                </li><br>
                <li>
                    @admin does not in any way tamper with or modify blog posts, this goes to say, any post that do not obey point (8) above, will not be verified.
                </li><br>
                <li>
                    All sumbloggers are solely responsible for what they post online.
                </li><br>
                <li>
                    Please this platform is not a media for preaching and/or teaching violence or hate of any type, on any kind. 
                </li><br>
                <li>
                    Usernames are displayed on every news and blog posts which makes it possible to be searched upon 
                    by registered members alike only, so we implore that you do not accept any colleague request that you do not know in person. 
                    This app is designed for colleagues and not for some random search-add users. Therefore, if anybody must add you, it should be 
                    someone you know in person or perhaps someone who has the intention of doing business with you for example someone who likes and has been 
                    following your write-ups/blogposts/newspost. In the latter case, extreme care has to be taken as to the level and amount of confidential information you give 
                    as @Sumpis will not be held liable for anything or event whatsoever that transpires. We are keen on ensuring the privacy of users on this platform.
                </li><br>
                <li>
                    To earn money, sumblog post must be viewed and liked by users of the app.<br>Each view and like attract 1 point respectively. 
                    There is the like to view ratio and the view weight. The closer the like-view ratio of a sumblog posts to 1 indicates how creative, 
                    interesting, informative or helpful the post is. The view weight on the other hand is the total number of views multiplied by 0.1.
                    Therefore, Total Post Point(TPP) = (LIKES/VIEWS) X (VIEWS x 0.1) = LV-RATIO X V-WEIGHT = LIKES X 0.1. Therefore, 
                    Sumblog Earning(SE) = Total Post Point(TPP) X 5 cents.
                    So, if a sumblogger writes 20 post in a month and each post attracts about 3000 likes, that is a total of 60,000 likes. 
                    This implies, 60,000 x 0.1 x 5 cents = $300.
                </li><br>
                <li>
                    Term (13) above, is subject to review at anytime.
                </li><br>
                <li>
                    Sumbloggers have a period of thirty (30) days to claim their earnings as likes after 30 days of blog post will not be included.
                </li><br>
                <li>
                    Sumbloggers are to provide details of their bank accounts during claiming of earnings.
                </li><br>
                <li>
                    Sumbloggers are encouraged to individually or collectively promote their blog post by inviting friends and family to read, like 
                    and/or share their posts on the platform so as to increase their earnings as @TeamSumpis will not be involved in any post 
                    promotion whatsoever.
                </li><br>
                <li>
                    All sumbloggers are encouraged to make blog post as succinctly as possible. Use the streamline-summarise, paraphrase and rearrange buttons 
                    to reduce blog posts.  
                </li><br>
                <li>
                    Sumpis adheres to the rules of standard English grammar, this goes to stress the need for all blog and news posts to adhere to the rules of standard
                    English grammar if Sumpis text rewrite functionalities should be employed. Please it should be noted that write-ups that contain colloquial and/or 
                    slangy expressions may be improperly re-written by Sumpis and may portray a different sense from the original.
                </li><br>
                <li>
                    Always crosscheck the output of @Sumpis functionalities before proceeding.
                </li><br>
            </ol><br><hr><br>
            --%>
            
            <div id="contactus" style="font-size: 28px; font-weight: bold; text-align: center;">Contact Us</div><br>
            <span>You are welcomed to write to us, be aware that your questions, suggestions and comments are well received and the necessary actions will be considered. 
            Please fill the form below, all fields are compulsory. If you are a registered member on the Sumpis platform, we shall send a response 
            through to your inbox if you use your username. If you are not registered, we shall contact you through the contact information you provide.
            </span><br>
            
            <div style="padding: 15px;">
            <form action="comments" method="POST">
                Fullname:<br>
                <input type="text" name="name" placeholder="Enter fullname" size="40" maxlength="30" Class="input_Field"/><br><br>
                Contact (username for members):<br>
                <input type="text" name="contact" placeholder="Enter contact information or @sumpis username (for members only)" size="40" maxlength="14" Class="input_Field"/><br><br>
                Comment:<br>
                <textarea id="txt_Area_InformAll" name="message" placeholder="Comment" cols="50" rows="15"></textarea><br><br>
                <input type="submit" id="post_Message" class="input" value="Submit"/>
            </form><hr>
            <div style="text-align: center;"><a href="#top" style="font-size: 20px; text-decoration: none;">Top</a></div>
            </div>
        </div>
    </body>
</html>