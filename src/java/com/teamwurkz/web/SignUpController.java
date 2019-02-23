package com.teamwurkz.web;

import com.google.gson.Gson;
import com.teamwurkz.domain.AdClass;
import com.teamwurkz.domain.AddContactClass;
import com.teamwurkz.domain.AdminControlClass;
import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.FileUploadClass;
import com.teamwurkz.domain.LostAccountClass;
import com.teamwurkz.domain.LostOtpConfirmationClass;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.OTPconfirmationClass;
import com.teamwurkz.domain.SearchPersonClass;
import com.teamwurkz.domain.SendOtpClass;
import com.teamwurkz.domain.SignupClass;
import com.teamwurkz.domain.TestJson;
import com.teamwurkz.domain.WorkOnFile;
import com.teamwurkz.domain.WriteUs;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignUpController
{
    @Value("${imagepath.proploc}")
    String pathFromPropertiesFile;
    @Autowired
    ClientController cc;
    private Context ctx;
    private DataSource ds;
    private Connection con;
        
    @Value("${admin.name}")
    String admin;
    @Value("${advert.display}")//Properties file pending when we start allowing adverts. For now it is display: none
    String displayadvert;
    
    public SignUpController()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");            
            ctx=new InitialContext();
            ds=(DataSource)ctx.lookup("java:/comp/env/jdbc/sumpisdatabase");
            con=ds.getConnection();
        }        
        catch (ClassNotFoundException | NamingException | SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //optimized 1
    @RequestMapping(value="/signup", method=RequestMethod.POST)
    public String signup(@ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("fifthBeanObject")SignupClass sc, ModelMap model, 
    HttpServletRequest req, SendOtpClass soc, HttpSession session, 
    @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=adAlgorithm("GENERAL", 3);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        if(session.getAttribute("username")==null)
        {
            String firstname=sc.getFirstName().trim();
            String lastname=sc.getLastName().trim();
            String mobile=sc.getMobile().trim();
            String username=sc.getUserName().trim().toLowerCase();
            String password1=sc.getPassword1();
            String password2=sc.getPassword2();
            if(!firstname.matches("\\s*") && !lastname.matches("\\s*") && !mobile.matches("\\s*") && !username.matches("\\s*") &&
            !password1.matches("\\s*") && !password2.matches("\\s*"))
            {
                if(validCheck(username))
                {
                    if(password1.equals(password2))
                    {
                        if(password1.length()<8)
                        {
                            model.addAttribute("g", "Password must be 8 characters or more.");
                            return "registration";
                        }
                        try
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers");                    
                            while(rs.next())
                            {                        
                                if(username.equalsIgnoreCase(rs.getString(1)) || username.contains(admin))
                                {
                                    model.addAttribute("g", "Username exists. Try different username");
                                    return "registration";
                                }
                                if(mobile.equals(rs.getString(4)) && rs.getString(6)!=null)
                                {
                                    model.addAttribute("g", "Mobile number has been banned.");
                                    return "registration";
                                }
                                if(mobile.equals(rs.getString(4)))
                                {
                                    model.addAttribute("g", "Mobile number is already registered.");
                                    return "registration";
                                }
                                if(!mobile.startsWith("+"))
                                {
                                    model.addAttribute("g", "Please include country code.");
                                    return "registration";
                                }
                                else if(!mobile.matches("[+][1-9][0-9]*[1-9]\\d{2}[1-9]\\d{6}"))
                                {
                                    model.addAttribute("g", "Invalid mobile number.");
                                    return "registration";
                                }
                            }                    
                            String firstNameTitleCase=Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1).toLowerCase();
                            String lastNameTitleCase=Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1).toLowerCase();
                        
                            String OTP=getOTP();
                            //this line sends OTP to the new users, make sure you open this when ready to launch app
                            //soc=new SendOtpClass(); VERY IMPORTANT
                            //soc.sendOtp(mobile, OTP); VERY IMPORTANT
                        
                            model.addAttribute("otperror", "OTP sent to mobile number.");
                            session.setAttribute("OTP", OTP);//remove this once app goes online (used as TEST to display OTP)
                            session.setAttribute("otpFirstname", firstNameTitleCase);
                            session.setAttribute("otpLastname", lastNameTitleCase);
                            session.setAttribute("otpMobile", mobile);
                            session.setAttribute("otpUsername", username);
                            session.setAttribute("otpPassword", password1);                    
                            model.addAttribute("twentiethBeanObject", new OTPconfirmationClass());                    
                            return "confirmregistrationotp";
                        }                
                        catch (SQLException ex)
                        {
                            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        finally
                        {
                            try
                            {
                                if(rs!=null)
                                {
                                    rs.close();
                                }
                            }
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }            
                    else
                    {
                        model.addAttribute("g", "Passwords did not match");
                        //return "registration";
                    }
                }
                else
                {
                    model.addAttribute("g", "Username contains invalid character");
                }
            }
            else
                model.addAttribute("g", "Please enter all fields. Thank you!");
        }
        else
            model.addAttribute("g", "There is an active session. Use 'My Desk' tab <br> above to switch then <i>logout</i>.");
        return "registration";
    }
    
    //special for Ajax purpose (checking username on the fly for new signups)
    @RequestMapping(value="/ajaxUsernameCheck")
    @ResponseBody
    public String ajaxResponse(@RequestParam("sent")String ajax_Username)
    {
        ResultSet rs=null;
        Gson gson=new Gson();
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers");                    
            while(rs.next())
            {                        
                if(ajax_Username.equalsIgnoreCase(rs.getString(1)) || ajax_Username.contains(admin) )
                {
                    //return "exists";
                    
                    //Converting from POJO object to JSON object [ gson.toJson() ]
                    
                    String json=gson.toJson(new TestJson("exists", "yes"));
                    return json;
                }
                
                //Converting from JSON object to POJO object[ gson.fromJson() ]
                //TestJson tj=gson.fromJson(ajax_Username, TestJson.class);
                //return tj.getResponse()+ tj.getReport();
            }
            
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
    
    @RequestMapping(value="/completeregistration")//, method=RequestMethod.POST)
    public String completeRegistration(ModelMap model, HttpServletRequest req, 
    @ModelAttribute("twentiethBeanObject")OTPconfirmationClass otpc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("fifthBeanObject")SignupClass sc, HttpSession session)
    {
        session=req.getSession();
        String otpFirstname=(String)session.getAttribute("otpFirstname");
        String otpLastname=(String)session.getAttribute("otpLastname");
        String otpMobile=(String)session.getAttribute("otpMobile");
        String otpUsername=(String)session.getAttribute("otpUsername");
        String otpPassword=(String)session.getAttribute("otpPassword");        
        
        String OTP1=(String)session.getAttribute("OTP");        
        String OTP2=otpc.getOtp();        
        String action=otpc.getAction();        
        if(action!=null) //ensures that the action button is clicked
        {
            switch(action)
            {
                case "confirm":
                {
                    if(otpFirstname!=null && otpLastname!=null && otpMobile!=null && otpUsername!=null)
                    {
                        if(!OTP2.matches("\\s*"))
                        {
                            if(OTP1.equals(OTP2))
                            {
                                
                                String date=new UtilityClass().getDate();
                                String oneDayTest=endDateCalc(date, 1);//New signups have 1 day to test app
                                saveRegisteredUsers(otpUsername, otpFirstname, otpLastname, otpMobile, otpPassword, null, null, OTP1, oneDayTest, date);
                                model.addAttribute("otperror", "Sign-up successful. You have 24 hours free trial!");
                                //remove all those other session attributes i.e the otpFirstname and others
                                session.removeAttribute("otpFirstname");
                                session.removeAttribute("otpLastname");
                                session.removeAttribute("otpMobile");
                                session.removeAttribute("otpUsername");
                                session.removeAttribute("otpPassword");
                                return "confirmregistrationotp";
                            }
                            else
                            {
                                model.addAttribute("otperror", "OTP is wrong!");
                                return "confirmregistrationotp";
                            }
                        }
                        else
                        {
                            model.addAttribute("otperror", "Please enter the OTP sent to the mobile number you provided!");        
                            return "confirmregistrationotp";
                        }
                    }
                    else
                    {
                        model.addAttribute("otperror", "Invalid registration");
                        return "confirmregistrationotp";
                    }                    
                }            
                case "cancel":
                {
                    session.removeAttribute("otpFirstname");
                    session.removeAttribute("otpLastname");
                    session.removeAttribute("otpMobile");
                    session.removeAttribute("otpUsername");
                    session.removeAttribute("otpPassword");
                    model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                    return "homepage";                        
                }
            }
        }
        else
        {
            model.addAttribute("g", "Please sign-up, its absolutely free.");
            return "registration";
        }                
    return "confirmregistrationotp";    
    }
    
    private void saveRegisteredUsers(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("INSERT INTO registeredusers (username, firstname, "
            + "lastname, mobile, password, sumpissuspend, newspostsuspend, otp, premium, "
            + "dateofreg, pix, gender, mail, location, job, aboutyou, hobby, relationship, "
            + "newsposts, blogposts, sumblogger) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.setString(4, d);
            ps.setString(5, e);            
            ps.setString(6, f);
            ps.setString(7, g);
            ps.setString(8, h);
            ps.setString(9, i);
            ps.setString(10, j);
            ps.setString(11, "empty.png");
            ps.setString(12, "Not verified");
            ps.setString(13, "Not verified");
            ps.setString(14, "Not verified");
            ps.setString(15, "Not verified");
            ps.setString(16, "Not verified");
            ps.setString(17, "Not verified");
            ps.setString(18, "Not verified");
            
            ps.setInt(19, 0);
            ps.setInt(20, 0);
            ps.setString(21, "Not verified");
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(ps!=null)
                {
                    ps.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    //optimized
    @RequestMapping(value="/searchController", method=RequestMethod.POST)
    public String searchPerson(@ModelAttribute("eleventhBeanObject")SearchPersonClass spc, ModelMap model, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, HttpServletRequest req, HttpSession session, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rs=null;
        
        session=req.getSession();
        String usernameCheck=(String)session.getAttribute("username");        
        if(usernameCheck!=null)
        {
            String searchPerson=spc.getName().trim().toLowerCase();
            if(!searchPerson.matches("\\s*"))
            {
                try
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+searchPerson+"'");                    
                    while(rs.next())
                    {
                        String user=rs.getString(1);                            
                        if(!searchPerson.equals(usernameCheck))
                        {
                            if(searchPerson.equals(user))
                            {
                                return "redirect:/profile?ux="+user;
                            }
                        }
                        else
                        {
                            model.addAttribute("unreadMessages", getUnreadCount(usernameCheck));
                            model.addAttribute("friendrequest", getFriendRequest(usernameCheck));
                            model.addAttribute("twelfthBeanObject", new AddContactClass());
                            model.addAttribute("outputError", "The username belongs to you.");
                            model.addAttribute("displayadvert", displayadvert);
                            return "registeredMember";
                        }                        
                    }
                    model.addAttribute("unreadMessages", getUnreadCount(usernameCheck));
                    model.addAttribute("friendrequest", getFriendRequest(usernameCheck));
                    model.addAttribute("twelfthBeanObject", new AddContactClass());
                    model.addAttribute("outputError", "This username does not exist.");
                }                
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally
                {
                    try
                    {
                        if(rs!=null)
                        {
                            rs.close();
                        }
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else
            {
                model.addAttribute("unreadMessages", getUnreadCount(usernameCheck));
                model.addAttribute("friendrequest", getFriendRequest(usernameCheck));
                model.addAttribute("twelfthBeanObject", new AddContactClass());
                model.addAttribute("outputError", "Please enter a registered username. Thank you.");
            }
            model.addAttribute("displayadvert", displayadvert);
            return "registeredMember";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }    
    
    //optimized
    @RequestMapping(value="/adminSearchController", method=RequestMethod.POST)
    public String adminSearchPerson(ModelMap model, @ModelAttribute("firstBeanObject")FieldClass fc,
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, @ModelAttribute("seventhBeanObject") WorkOnFile wof, 
    @ModelAttribute("eightBeanObject") WriteUs wu, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    HttpSession session, RedirectAttributes ra)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();
        String usernameCheck=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname1=(String)session.getAttribute("firstname");
        String lastname1=(String)session.getAttribute("lastname");
        if(usernameCheck!=null)
        {
            try
            {
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + usernameCheck + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname1.equals(rsAdminCheck.getString(2)) 
                    && lastname1.equals(rsAdminCheck.getString(3)))
                    {
                        String searchPerson=spc.getName().trim();        
                        if(!searchPerson.matches("\\s*"))
                        {
                            try
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+searchPerson+"'");
                    
                                while(rs.next())
                                {
                                    String user=rs.getString(1);                                    
                                    if(!searchPerson.equals(usernameCheck))
                                    {
                                        if(searchPerson.equals(user))
                                        {
                                            ra.addFlashAttribute("fifteenthBeanObject", new AdminControlClass());
                                            return "redirect:/profile?ux="+user;
                                        }
                                    }                        
                                    else
                                    {
                                        model.addAttribute("unreadMessages", getUnreadCount(admin));
                                        model.addAttribute("outputError", "The username belongs to you.");
                                        return "adminMessagePage";
                                    }                        
                                }
                                model.addAttribute("unreadMessages", getUnreadCount(admin));
                                model.addAttribute("outputError", "This username does not exist.");
                            }                
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }        
                        else
                        {
                            model.addAttribute("unreadMessages", getUnreadCount(admin));
                            model.addAttribute("outputError", "Please enter a registered username. Thank you.");
                        }        
                        return "adminMessagePage";
                    }
                }
                session.invalidate();
                model.addAttribute("expired", "Unauthorized access. You have been logged out!!!");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rs!=null)
                    {
                        rs.close();
                    }
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");            
        }
        return "homepage";
    }
    
    protected String getUnreadCount(String user)
    {
        ResultSet rs=null;
        
        int count=0;
        try
        {            
            rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE receiver='" + user + "' AND unread='New'");
            while(rs.next())
            {
                count++;
            }                
            if(count==0)
            {
                return "";
            }                
            else
            {
                return ""+ count +"";
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;        
    }
    
    protected String getFriendRequest(String user)
    {
        ResultSet rs=null;
        int count=0;
        try
        {
            String friendRequestQuery="SELECT receiver FROM contactdb WHERE receiver='" + user + "' AND confirmation=''";            
            rs=con.createStatement().executeQuery(friendRequestQuery);
            while(rs.next())
            {
                count++;
            }                
            if(count==0)
            {
                return "";
            }                
            else
            {
                return ""+ count +"";
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;        
    }
    //optimized
    @RequestMapping(value="/accountrevalidation")
    public String forgottenAccount(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        //Advert
        List<List<String>> ad=adAlgorithm("GENERAL", 3);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        model.addAttribute("twentyfirstBeanObject", new LostAccountClass());
        return "accountsetup";
    }
    
    @RequestMapping(value="/retrieveLostDetails")
    public String retrieve(@ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("twentyfirstBeanObject")LostAccountClass lac, ModelMap model, HttpServletRequest req, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, SendOtpClass soc, 
    HttpSession session)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=adAlgorithm("GENERAL", 3);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String lostFirstnameX=lac.getFirstName().trim();
        String lostLastnameX=lac.getLastName().trim();
        String lostMobile=lac.getMobile().trim();
        
        if(session.getAttribute("username")==null)
        {
            if(lac.getAction()!=null) //accessing controller "/retrieve" without clicking button, not possible
            {
                if(!lostFirstnameX.matches("\\s*"))
                {
                    if(!lostLastnameX.matches("\\s*"))
                    {
                        if(!lostMobile.matches("\\s*"))
                        {
                            String lostFirstname=Character.toUpperCase(lostFirstnameX.charAt(0))+lostFirstnameX.substring(1).toLowerCase();
                            String lostLastname=Character.toUpperCase(lostLastnameX.charAt(0))+lostLastnameX.substring(1).toLowerCase();
                            try
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers");
                                String lostUsername;
                                String lostPassword;
                                String otp;                            
                                while(rs.next())
                                {
                                    if(lostFirstname.equals(rs.getString(2)) && lostLastname.equals(rs.getString(3)) 
                                    && lostMobile.equals(rs.getString(4)))
                                    {
                                        LostOtpConfirmationClass locc=new LostOtpConfirmationClass();                                            
                                        lostUsername=rs.getString(1);
                                        lostPassword=rs.getString(5);
                                    
                                        otp=getOTP(); //send this otp to the mobile number provided very important.
                                        
                                        //Very important line of code for sending lost details to users.
                                        //soc=new SendOtpClass();
                                        //soc.sendOtp(lostMobile, otp);
                                        model.addAttribute("otperror", "OTP sent to mobile number.");
                        
                                        session.setAttribute("lostOTP", otp);//remove this once app goes online (used as test to display OTP)
                                        
                                        session.setAttribute("lostOtpFirstname", lostFirstname);
                                        session.setAttribute("lostOtpLastname", lostLastname);
                                        session.setAttribute("lostOtpMobile", lostMobile);
                                        session.setAttribute("lostOtpUsername", lostUsername);
                                        session.setAttribute("lostOtpPassword", lostPassword);
                                        model.addAttribute("twentysecondBeanObject", locc);                                        
                                        return "otpconfirmationpage";
                                    }
                                }
                                model.addAttribute("g", "Data did not correlate.");
                                return "accountsetup";
                            }
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            finally
                            {
                                try
                                {
                                    if(rs!=null)
                                    {
                                        rs.close();
                                    }
                                }
                                catch (SQLException ex)
                                {
                                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        else
                        {
                            model.addAttribute("g", "Please enter mobile number");
                            return "accountsetup";
                        }                
                    }
                    else
                    {
                        model.addAttribute("g", "Please enter lastname");
                        return "accountsetup";
                    }
                }
                else
                {
                    model.addAttribute("g", "Please enter firstname");
                    return "accountsetup";
                }                
            }
        }
        else
        {
            model.addAttribute("g", "There is an active session, kindly log out and try again.");
            return "accountsetup";
        }
        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        return "homepage";
    }
    //optimized
    @RequestMapping(value="/accountrecovery")//, method=RequestMethod.POST)
    public String accountrecovery(ModelMap model, HttpServletRequest req, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("fifthBeanObject")SignupClass sc, @ModelAttribute("twentyfirstBeanObject")LostAccountClass lac, 
    @ModelAttribute("twentysecondBeanObject")LostOtpConfirmationClass locc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        session=req.getSession();
        String lostOtpFirstname=(String)session.getAttribute("lostOtpFirstname");
        String lostOtpLastname=(String)session.getAttribute("lostOtpLastname");        
        String lostOtpUsername=(String)session.getAttribute("lostOtpUsername");
        String lostOtpPassword=(String)session.getAttribute("lostOtpPassword");        
        String lostOTP1=(String)session.getAttribute("lostOTP");        
        String lostOTP2=locc.getOtp();        
        String action=locc.getAction();        
        if(action!=null) //ensures that the action button is clicked
        {
            switch(action)
            {
                case "confirm":
                {
                    if(!lostOTP2.matches("\\s*"))
                    {
                        if(lostOTP1.equals(lostOTP2))
                        {
                            if(lostOtpFirstname!=null && lostOtpLastname!=null)
                            {
                                model.addAttribute("otperror2", "Hello "+lostOtpFirstname+ " "
                                + lostOtpLastname+ "<br>Your username is: '" 
                                + lostOtpUsername + "' and password is: '"+ lostOtpPassword+"'");
                                //remove all those other session attributes i.e the otpFirstname and others
                                session.removeAttribute("lostOtpFirstname");
                                session.removeAttribute("lostOtpLastname");
                                session.removeAttribute("lostOtpMobile");
                                session.removeAttribute("lostOtpUsername");
                                session.removeAttribute("lostOtpPassword");
                                return "otpconfirmationpage";
                            }
                            else
                            {
                                model.addAttribute("g", "Enter details again!");
                                return "accountsetup";
                            }                                
                        }
                        else
                        {
                            model.addAttribute("otperror", "The OTP entered is wrong!");
                            return "otpconfirmationpage";
                        }
                    }
                    else
                    {
                        model.addAttribute("otperror", "Please enter the OTP sent to the mobile number you provided!");        
                        return "otpconfirmationpage";
                    }                    
                }            
                case "cancel":
                {
                    session.removeAttribute("lostOTP");
                    session.removeAttribute("lostOtpFirstname");
                    session.removeAttribute("lostOtpLastname");
                    session.removeAttribute("lostOtpMobile");
                    session.removeAttribute("lostOtpUsername");
                    session.removeAttribute("lostOtpPassword");
                    model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                    return "homepage";                        
                }
            }
        }
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Try again!");
            return "homepage";
        }                
    return "confirmregistrationotp";    
    }    
    //OTP generation code
    public String getOTP()
    {
        Random random=new Random();
        char[] capitalLetters={'A','B','C','D','E','F','G','H','I','J',
            'K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z'};
        char[] smallLetters={'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char[] numbers={'1','2','3','4','5','6','7','8','9'};
        String otpgen="";
        String shuffleOTP="";        
        
        for(int i=0; i<2; i++) //selects two random characters from capital letters
        {
            String xter= String.valueOf(capitalLetters[random.nextInt(capitalLetters.length)]);            
            while(otpgen.contains(xter))
            {
                xter = String.valueOf(capitalLetters[random.nextInt(capitalLetters.length)]);
            }
            otpgen += xter;
        }
        for(int i=0; i<3; i++) //selects three random characters from small letters
        {
            String xter= String.valueOf(smallLetters[random.nextInt(smallLetters.length)]);            
            while(otpgen.contains(xter))
            {
                xter = String.valueOf(smallLetters[random.nextInt(smallLetters.length)]);
            }
            otpgen += xter;
        }
        for(int i=0; i<3; i++) //selects three random characters from numbers/digits
        {
            String xter= String.valueOf(numbers[random.nextInt(numbers.length)]);
            
            while(otpgen.contains(xter))
            {
                xter = String.valueOf(numbers[random.nextInt(numbers.length)]);
            }
            otpgen += xter;
        }        
        for(int i=0; i<otpgen.length(); i++) //rearranges/shuffles the generated OTP
        {
            String xter = String.valueOf(otpgen.charAt(random.nextInt(otpgen.length())));            
            while(shuffleOTP.contains(xter))
            {
                xter = String.valueOf(otpgen.charAt(random.nextInt(otpgen.length())));
            }            
            shuffleOTP += xter;
        }        
        return shuffleOTP; 
    }
    
    @RequestMapping(value="/premium")//Processes purchase of subscription time (1)
    public String premium(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req,
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        if(username!=null)
        {
            model.addAttribute("unreadMessages", getUnreadCount(username));
            model.addAttribute("friendrequest", getFriendRequest(username));
            return "premiumpage";
        }
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "<a href='signuppage' style='text-decoration: none;' class='free'>Click here to sign-up and Go Premium. Sign-up is absolutely free!!!</a>");
        }
        return "homepage";
    }
    
    @RequestMapping(value="/adcredit")//Processes purchase of ads credit (1)
    public String adCredit(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req,
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        if(username!=null)
        {
            model.addAttribute("unreadMessages", getUnreadCount(username));
            model.addAttribute("friendrequest", getFriendRequest(username));
            return "adcreditpage";
        }
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        }
        return "homepage";
    }
    
    //This controller will handle responses from the webpay payment gateway
    @RequestMapping(value="/response controller")    
    public String payProcess(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req,
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        if(username!=null && username.equals("the username sent as response parameter from webpay"))
        {
            //I suspect I'd have to parse JSON String(response) here but before then I'd have to;
            //switch (type of payment - is it subscription or is it ads credit)
        }
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        }
        return "homepage";
    }
    
    private boolean validCheck(String input)
    {
        char[] c=input.toCharArray();
        for(char ch:c)
        {
            if(!Character.isLetterOrDigit(ch))
            {
                return false;
            }
        }
        return true;
    }
    
    @RequestMapping(value="/profile")    
    public String profile(@RequestParam("ux")String profile, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, ModelMap model, HttpServletRequest req, HttpSession session)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        
        String pix="empty.png";
        String gender="";
        String mail="";
        String location="";
        String job="";
        String me="";
        int newsposts=0;
        int blogposts=0;
        String hobby="";
        String relationship="";
        String joined="";
        String sumblogger="No";
        
        if(profile.equals(admin))
        {
            return "redirect:/aboutus";
        }
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+profile+"'");
            while(rs.next())
            {
                pix=rs.getString(11);
                gender=rs.getString(12);
                mail=rs.getString(13);
                location=rs.getString(14);
                job=rs.getString(15);
                me=rs.getString(16);
                hobby=rs.getString(17);
                relationship=rs.getString(18);
                joined=rs.getString(10);
                newsposts=rs.getInt(19);
                blogposts=rs.getInt(20);
                if(!rs.getString(21).equals("No"))
                {
                    sumblogger=rs.getString(21).replaceAll(" ", ". ");
                }
            }
                        
            model.addAttribute("profile", profile);
            model.addAttribute("pix", pix);
            model.addAttribute("gender", gender);
            model.addAttribute("mail", mail);
            model.addAttribute("location", location);
            model.addAttribute("job", job);
            model.addAttribute("me", me);
            model.addAttribute("newsposts", newsposts);
            model.addAttribute("blogposts", blogposts);
            model.addAttribute("hobby", hobby);
            model.addAttribute("relationship", relationship);
            model.addAttribute("joined", joined);
            model.addAttribute("sumblogger", sumblogger);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if(username==null)
        {
            return "profilepage";
        }
        else if(username.equals(admin))
        {
            model.addAttribute("eleventhBeanObject", new SearchPersonClass());
            model.addAttribute("fifteenthBeanObject", new AdminControlClass());
            model.addAttribute("unreadMessages", getUnreadCount(admin));
            return "adminprofilepage";
        }
        else
        {
            model.addAttribute("eleventhBeanObject", new SearchPersonClass());
            model.addAttribute("unreadMessages", getUnreadCount(username));
            model.addAttribute("friendrequest", getFriendRequest(username));
        }
        return "userprofilepage";
    }
    
    
    /*FORMER advert display code:
    protected List<List<String>> adAlgorithm(String category, int adNumber)
    {
        ResultSet rs=null;
        List<List<String>> all=new LinkedList<>();
        List<String> idList=new LinkedList<>();
        List<String> imgList=new LinkedList<>();
        List<String> dispList=new LinkedList<>();
        
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username WHERE approve='ENABLED' "
            + "AND pauseit='DISABLED' AND targetpage='"+category+"' AND expired='DISABLED' AND display='inline' ORDER BY adid ASC LIMIT 0,"+adNumber);
            
            while(rs.next())
            {
                int id=rs.getInt(1);
                String idx=String.valueOf(id);
                double balance=rs.getDouble(37);
                String img=rs.getString(9);
                String disp=rs.getString(13);
                String publisher=rs.getString(2);
                
                switch(rs.getString(4))
                {
                    case "CPM":
                    {
                        if(balance>0)
                        {
                            int views=rs.getInt(8);
                            idList.add(idx);
                            imgList.add(img);
                            dispList.add(disp);
                            updateCPM_CPC(id, "CPM", ++views, publisher);
                        }
                        else
                        {
                            zeroSetting(id, publisher);
                        }
                    }
                    break;
                        
                    case "CPC":
                    {
                        if(balance>0)
                        {
                            int views=rs.getInt(8);
                            idList.add(idx);
                            imgList.add(img);
                            dispList.add(disp);
                            updateCPM_CPC(id, "CPC", ++views, publisher);
                        }
                        else
                        {
                            zeroSetting(id, publisher);
                        }
                    }
                    break;
                }
            }
            all.add(idList);
            all.add(imgList);
            all.add(dispList);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return all;
    }
    */
    
    //This is the advert display code:
    protected List<List<String>> adAlgorithm(String category, int adNumber)
    {
        ResultSet rs=null;
        List<List<String>> all=new LinkedList<>();
        List<String> idList=new LinkedList<>();
        List<String> imgList=new LinkedList<>();
        Map<Integer, String> id_Img_Map=new HashMap<>();
        
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username WHERE approve='ENABLED' "
            + "AND pauseit='DISABLED' AND targetpage='"+category+"' AND expired='DISABLED' AND display='inline' ORDER BY adid ASC LIMIT 0,"+adNumber);
            
            while(rs.next())
            {
                int id=rs.getInt(1);
                String publisher=rs.getString(2);
                String img=rs.getString(9);
                double balance=rs.getDouble(37);
                
                switch(rs.getString(4))
                {
                    case "CPM":
                    {
                        if(balance>0)
                        {
                            int views=rs.getInt(8);
                            id_Img_Map.put(id, img);
                            updateCPM_CPC(id, "CPM", ++views, publisher);
                        }
                        else
                        {
                            zeroSetting(id, publisher);
                        }
                    }
                    break;
                        
                    case "CPC":
                    {
                        if(balance>0)
                        {
                            int views=rs.getInt(8);
                            id_Img_Map.put(id, img);
                            updateCPM_CPC(id, "CPC", ++views, publisher);
                        }
                        else
                        {
                            zeroSetting(id, publisher);
                        }
                    }
                    break;
                }
            }
            List<Integer> mapKeys=new LinkedList<>(id_Img_Map.keySet());
            Collections.shuffle(mapKeys);
            mapKeys.stream().map(a -> {idList.add(String.valueOf(a));return a;}).forEachOrdered(a -> {imgList.add(id_Img_Map.get(a));});
            /*This works as the fuunctional code above
            for (int a : mapKeys)
            {
                idList.add(String.valueOf(a));
                imgList.add(id_Img_Map.get(a));
            }*/
            
            all.add(idList);
            all.add(imgList);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return all;
    }
    
    private void updateCPM_CPC(int adid, String cpm_Cpc, int value, String publisher)
    {
        ResultSet rs1=null;
        PreparedStatement ps=null;
        PreparedStatement psx=null;
        
        try
        {
            rs1=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+publisher+"'");
            while(rs1.next())
            {
                int balance=rs1.getInt(22);
                
                switch (cpm_Cpc)
                {
                    case "CPM":
                    {
                        balance=balance-rs1.getInt(23);
                        ps=con.prepareStatement("UPDATE adstable SET views="+value+" WHERE adid="+adid);
                        ps.executeUpdate();
                        psx=con.prepareStatement("UPDATE registeredusers SET adsbalance="+balance+" WHERE username='"+publisher+"'");
                        psx.executeUpdate();
                    }
                    break;
            
                    case "CPC":
                    {
                        ps=con.prepareStatement("UPDATE adstable SET views="+value+" WHERE adid="+adid);
                        ps.executeUpdate();
                    }
                    break;
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs1!=null)
                {
                    rs1.close();
                }
                if(ps!=null)
                {
                    ps.close();
                }
                if(psx!=null)
                {
                    psx.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void zeroSetting(int adid, String publisher)
    {
        PreparedStatement ps=null;
        PreparedStatement psx=null;
        
        try
        {
            ps=con.prepareStatement("UPDATE adstable SET approve='DISABLED', expired='ENABLED', display='none' WHERE adid="+adid);
            ps.executeUpdate();
            
            psx=con.prepareStatement("UPDATE registeredusers SET adsbalance=0 WHERE username='"+publisher+"'");
            psx.executeUpdate();
        }
        catch (SQLException ex)
        {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(ps!=null)
                {
                    ps.close();
                }
                if(psx!=null)
                {
                    psx.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @RequestMapping(value="/k")
    public String updateClick(@RequestParam("n")int id, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        return "redirect:"+perClick(id);
    }
    
    private String perClick(int id)
    {
        ResultSet rs=null;
        PreparedStatement ps=null;
        PreparedStatement psx=null;
        
        String landingPage=null;
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username WHERE adid="+id);
            while(rs.next())
            {
                String publisher=rs.getString(2);
                int click=rs.getInt(7);
                landingPage=rs.getString(10);
                int balance=rs.getInt(37);
                
                switch (rs.getString(4))
                {
                    case "CPM":
                    {
                        ps=con.prepareStatement("UPDATE adstable SET clicks="+ ++click+" WHERE adid="+id);
                        ps.executeUpdate();
                        break;
                    }
                    
                    case "CPC":
                    {
                        balance=balance-rs.getInt(39);
                        ps=con.prepareStatement("UPDATE adstable SET clicks="+ ++click+" WHERE adid="+id);
                        ps.executeUpdate();
                        psx=con.prepareStatement("UPDATE registeredusers SET adsbalance="+balance+" WHERE username='"+publisher+"'");
                        psx.executeUpdate();
                        break;
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
                if(ps!=null)
                {
                    ps.close();
                }
                if(psx!=null)
                {
                    psx.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return landingPage;
    }
    
    
    @RequestMapping(value="/ad_ctr")
    public String clientAd(@RequestParam(value="pg", required=false)int pg, @ModelAttribute("adClass")AdClass adc, 
    @RequestParam(value="e", required=false)String e, HttpSession session, @ModelAttribute("firstBeanObject")FieldClass fc, 
    ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, RedirectAttributes ra)
    {
        //statistics
        statistics("AD_PAGE");
        
        ResultSet rs;
        PreparedStatement ps;
        
        /*Advert
        List<List<String>> ad=adAlgorithm("GENERAL", 4);
        model.addAttribute("adidx", ad.get(0));
        model.addAttribute("adimgx", ad.get(1));
        model.addAttribute("dispzx", ad.get(2));*/
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        model.addAttribute("unreadMessages", getUnreadCount(username));
        model.addAttribute("friendrequest", getFriendRequest(username));
        
        if(username!=null)
        {
            model.addAttribute("username", username);
            model.addAttribute("displayx", "none");
            model.addAttribute("displayz", "none");
            model.addAttribute("adClass", new AdClass());
            
            if(e!=null)
            {
                switch(e)
                {
                    case "vx":
                    {
                        model.addAttribute("displayx", "inline");
                    }
                    break;
                
                    case "vz":
                    {
                        int pgn=pg;//very important
                        model.addAttribute("pg", pg);
                        model.addAttribute("dispx", "none");
                        int balance=0;
                        List<Integer> adid=new LinkedList<>();
                        List<String> approve=new LinkedList<>();
                        List<String> pymt=new LinkedList<>();
                        List<String> sDate=new LinkedList<>();
                        //List<String> eDate=new LinkedList<>();
                        List<Integer> clickz=new LinkedList<>();
                        List<Integer> viewz=new LinkedList<>();
                        List<String> adImg=new LinkedList<>();
                        List<String> lndPage=new LinkedList<>();
                        List<String> trgPage=new LinkedList<>();
                        List<String> onPause=new LinkedList<>();
                        
                        try
                        {
                            int setoff=10;
                            int nID=0;
                            if(pg==1)
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username "
                                + "WHERE adstable.username='"+username+"' AND expired='DISABLED' LIMIT 0,"+setoff);
                                while(rs.next())
                                {
                                    nID=rs.getInt(1);
                                    adid.add(nID);
                                    approve.add(rs.getString(3));
                                    pymt.add(rs.getString(4));
                                    sDate.add(rs.getString(5));
                                    //eDate.add(rs.getString(6));
                                    clickz.add(rs.getInt(7));
                                    viewz.add(rs.getInt(8));
                                    adImg.add(rs.getString(9));
                                    lndPage.add(rs.getString(10));
                                    trgPage.add(rs.getString(11));
                                    onPause.add(rs.getString(12));
                                    balance=rs.getInt(37);
                                }
                                
                                model.addAttribute("adid", adid);
                                model.addAttribute("approve", approve);
                                model.addAttribute("pymt", pymt);
                                model.addAttribute("sDate", sDate);
                                //model.addAttribute("eDate", eDate);
                                model.addAttribute("clickz", clickz);
                                model.addAttribute("viewz", viewz);
                                model.addAttribute("adImg", adImg);
                                model.addAttribute("lndPage", lndPage);
                                model.addAttribute("trgPage", trgPage);
                                model.addAttribute("onPause", onPause);
                                model.addAttribute("balance", balance+" pts");
                                model.addAttribute("displayz", "inline");
                                
                                if(nID!=0)
                                {
                                    model.addAttribute("next", "<a style='text-decoration: none' href='ad_ctr?pg="+ ++pg+"&e=vz'>Next</a>");
                                }
                            }
                            else
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username "
                                + "WHERE adstable.username='"+username+"' AND expired='DISABLED' LIMIT "+(pg-1)*setoff+","+setoff);
                                while(rs.next())
                                {
                                    nID=rs.getInt(1);
                                    adid.add(nID);
                                    approve.add(rs.getString(3));
                                    pymt.add(rs.getString(4));
                                    sDate.add(rs.getString(5));
                                    //eDate.add(rs.getString(6));
                                    clickz.add(rs.getInt(7));
                                    viewz.add(rs.getInt(8));
                                    adImg.add(rs.getString(9));
                                    lndPage.add(rs.getString(10));
                                    trgPage.add(rs.getString(11));
                                    onPause.add(rs.getString(12));
                                    balance=rs.getInt(37);
                                }
                                
                                model.addAttribute("adid", adid);
                                model.addAttribute("approve", approve);
                                model.addAttribute("pymt", pymt);
                                model.addAttribute("sDate", sDate);
                                //model.addAttribute("eDate", eDate);
                                model.addAttribute("clickz", clickz);
                                model.addAttribute("viewz", viewz);
                                model.addAttribute("adImg", adImg);
                                model.addAttribute("lndPage", lndPage);
                                model.addAttribute("trgPage", trgPage);
                                model.addAttribute("onPause", onPause);
                                model.addAttribute("balance", balance+" pts");
                                model.addAttribute("displayz", "inline");
                                
                                model.addAttribute("prev", "<a style='text-decoration: none' href='ad_ctr?pg="+ (pg-1)+"&e=vz'>Previous</a>");
                                if(nID!=0)
                                {
                                    model.addAttribute("next", "<a style='text-decoration: none' href='ad_ctr?pg="+ ++pg+"&e=vz'>Next</a>");
                                }
                            }
                            
                            if(adc.getVar5()!=null)
                            {
                                switch (adc.getVar5())
                                {
                                    case "st":
                                    {
                                        ps=con.prepareStatement("UPDATE adstable SET pauseit='DISABLED' WHERE adid="+adc.getVar1());
                                        ps.executeUpdate();
                                        ra.addFlashAttribute("info", "Ad is active!");
                                        return "redirect:/ad_ctr?pg="+pgn+"&e=vz";
                                    }
                                    case "et":
                                    {
                                        model.addAttribute("dispx", "inline");
                                        break;
                                    }
                                    case "ps":
                                    {
                                        rs=con.createStatement().executeQuery("SELECT * FROM adstable WHERE adid="+adc.getVar1());
                                        while(rs.next())
                                        {
                                            if(!rs.getString(4).equals("FLAT"))
                                            {
                                                ps=con.prepareStatement("UPDATE adstable SET pauseit='ENABLED' WHERE adid="+adc.getVar1());
                                                ps.executeUpdate();
                                                ra.addFlashAttribute("info", "Ad has been paused!");
                                                return "redirect:/ad_ctr?pg="+pgn+"&e=vz";
                                            }
                                        }
                                        ra.addFlashAttribute("info", "FLAT RATE Ads cannot be paused!");
                                        return "redirect:/ad_ctr?pg="+pgn+"&e=vz";
                                    }
                                    case "dst":
                                    {
                                        ps=con.prepareStatement("UPDATE adstable SET expired='ENABLED' WHERE adid="+adc.getVar1());
                                        ps.executeUpdate();
                                        ra.addFlashAttribute("info", "Ad has been removed completely!");
                                        return "redirect:/ad_ctr?pg="+pgn+"&e=vz";
                                    }
                                    case "upd":
                                    {
                                        rs=con.createStatement().executeQuery("SELECT * FROM adstable WHERE adid="+adc.getVar1());
                                        while(rs.next())
                                        {
                                            editAdx(adc.getVar1(), adc.getVar2(), adc.getVar7(), adc.getVar8());
                                            ra.addFlashAttribute("info", "Changes saved");
                                            return "redirect:/ad_ctr?pg="+pgn+"&e=vz";
                                        }
                                    }
                                }
                            }
                        }
                        catch (SQLException ex)
                        {
                            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
            }
            return "adspage";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    private void editAdx(int id, String payOpt, String landingPage, String targetPage)
    {
        PreparedStatement ps;
        
        try
        {
            ps=con.prepareStatement("UPDATE adstable SET payopt='"+payOpt+"', landingpage='"+landingPage+"', targetpage='"+targetPage+"' WHERE adid="+id);
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RequestMapping(value="/adupload")
    public String adUpload(@RequestParam("file")MultipartFile file, @RequestParam("landingpage")String landingpage, 
    @RequestParam("payment")String payment, @RequestParam("target")String targetedAd, RedirectAttributes ra, HttpServletRequest req, 
    HttpSession session, @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, ModelMap model)
    {
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String fileName=file.getOriginalFilename();
        String imagePath=pathFromPropertiesFile+"/resources/images/adbanners";
        if(username!=null)
        {
            if(fileName.endsWith(".jpg") || fileName.endsWith(".gif") || fileName.endsWith(".png") || fileName.endsWith(".jpeg") 
            || fileName.endsWith(".JPG") || fileName.endsWith(".GIF") || fileName.endsWith(".PNG") || fileName.endsWith(".JPEG") 
            || fileName.endsWith(".webp") || fileName.endsWith(".WEBP") || fileName.endsWith(".bmp") || fileName.endsWith(".BMP"))
            {
                if(fileName.length()<40)
                {
                    if(file.getSize()<1050009)//change to 50009 later
                    {
                        String startDate=new UtilityClass().getDate();
                        try
                        {
                            switch(payment)
                            {
                                case "CPM":
                                {
                                    saveAd(username, payment, startDate, "", fileName, landingpage, targetedAd, 0);
                                }
                                break;
                
                                case "CPC":
                                {
                                    saveAd(username, payment, startDate, "", fileName, landingpage, targetedAd, 0);
                                }
                                break;
                            }
                            File adImgPath=new File(imagePath, fileName);
                            file.transferTo(adImgPath);
                            String content="<h1 style='text-align: center; color: purple;'>Ad Review</h1>"
                            + "<img width='400' height='300' src='resources/images/adbanners/"+fileName+"'/>";
                            cc.saveMsg(username, admin, "Ad Approval", content, startDate, "New");
                            return "redirect:/ad_ctr?pg=1&e=vz&u="+username;
                        }
                        catch (IOException | IllegalStateException ex)
                        {
                            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else
                    {
                        ra.addFlashAttribute("info", "File size should be less than 50kb!");
                        return "redirect:ad_ctr?pg=1&e=vx";
                    }
                }
                else
                {
                    ra.addFlashAttribute("info", "File name should be less than 40 characters!");
                    return "redirect:ad_ctr?pg=1&e=vx";
                }
            }
            else
            {
                ra.addFlashAttribute("info", "File format should be .jpg, .png, .gif or .webp");
                return "redirect:ad_ctr?pg=1&e=vx";
            }
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    private String endDateCalc(String startDate, int duration)
    {
        String endDate="";
        try
        {
            if(startDate.contains("st"))
            {
                startDate=startDate.replace("st", "");
            }
            else if(startDate.contains("nd"))
            {
                startDate=startDate.replace("nd", "");
            }
            else if(startDate.contains("rd"))
            {
                startDate=startDate.replace("rd", "");
            }
            else if(startDate.contains("th"))
            {
                startDate=startDate.replace("th", "");
            }
            
            Date date=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(startDate);
            Calendar c=Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, duration);
            endDate=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").format(c.getTime());
            
            String daySuffix;
            String[] splitEndDate=endDate.split(" ");
            int day=Integer.parseInt(splitEndDate[0]);
            switch (day)
            {
                case 1:
                    daySuffix=String.valueOf(day + "st");
                    break;
                case 2:
                    daySuffix=String.valueOf(day+"nd");
                    break;
                case 3:
                    daySuffix=String.valueOf(day+"rd");
                    break;
                case 21:
                    daySuffix=String.valueOf(day+"st");
                    break;
                case 22:
                    daySuffix=String.valueOf(day+"nd");
                    break;
                case 23:
                    daySuffix=String.valueOf(day+"rd");
                    break;
                case 31:
                    daySuffix=String.valueOf(day+"st");
                    break;
                default:
                    daySuffix=String.valueOf(day+"th");
                    break;
            }
            endDate=endDate.replaceFirst(splitEndDate[0], daySuffix);
        }
        catch (ParseException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return endDate;        
    }
    
    private void saveAd(String username, String payOpt, String startDate, String endDate, String adsImage, String landingPage, String target, 
    int duration)
    {
        PreparedStatement ps;
        
        try
        {
            ps=con.prepareStatement("INSERT INTO adstable (username, approve, payopt, startdate, enddate, clicks, views, "
            + "adsimage, landingpage, targetpage, pauseit, display, expired, duration) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                
            ps.setString(1, username);
            ps.setString(2, "DISABLED");
            ps.setString(3, payOpt);
            ps.setString(4, startDate);
            ps.setString(5, endDate);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setString(8, adsImage);
            ps.setString(9, landingPage);
            ps.setString(10, target);
            ps.setString(11, "ENABLED");
            ps.setString(12, "none");
            ps.setString(13, "DISABLED");
            ps.setInt(14, duration);
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void statistics(String visits)
    {
        ResultSet rs=null;
        PreparedStatement ps=null;
        SimpleDateFormat sdf=new SimpleDateFormat("MMMM yyyy");
        String date=sdf.format(new Date());
        int check=0;
        
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM stats WHERE datex='"+date+"' AND visits='"+visits+"'");
            while(rs.next())
            {//updates counts
                check=1;
                int count=rs.getInt(4);
                ps=con.prepareStatement("UPDATE stats SET count="+ (count+1) +" WHERE visits='"+visits+"' AND datex='"+date+"'");
                ps.executeUpdate();
            }
            
            if(check==0)//creates the first count
            {
                ps=con.prepareStatement("INSERT INTO stats(datex, visits, count) VALUES(?,?,?)");
                ps.setString(1, date);
                ps.setString(2, visits);
                ps.setInt(3, 1);
                ps.executeUpdate();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                }
                if(ps!=null)
                {
                    ps.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected Date dateSuffix(String sdf)//this method removes the st, nd, rd, and th from SimpleDateFormat so you can manipulate it into a proper date object
    {
        Date date=null;
        try
        {
            if(sdf.contains("st"))
            {
                date=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(sdf.replace("st", ""));
            }
            else if(sdf.contains("nd"))
            {
                date=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(sdf.replace("nd", ""));
            }
            else if(sdf.contains("rd"))
            {
                date=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(sdf.replace("rd", ""));
            }
            else if(sdf.contains("th"))
            {
                date=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(sdf.replace("th", ""));
            }
        }
        catch (ParseException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
}