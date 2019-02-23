package com.teamwurkz.web;

import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.FileUploadClass;
import com.teamwurkz.domain.FreeBlogBeanObject;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.SearchPersonClass;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginClass
{
    @Autowired
    SignUpController succ;
    @Autowired
    MessagingController msc;
    @Autowired
    ClientController cc;
    @Value("${imagepath.proploc}")
    String pathFromPropertiesFile;

    @Value("${advert.display}")//Properties file pending when we start allowing adverts. For now it is display: none
    String displayadvert;
    
    @Value("${admin.name}")
    String admin;
    private Context ctx;
    private DataSource ds;
    private Connection con;
    
    private final Charset ISO=Charset.forName("ISO-8859-1");
    private final Charset UTF=Charset.forName("UTF-8");
    
    public LoginClass()
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
            Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    //optimized 3
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginMember(@ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    RedirectAttributes ra, HttpSession session)
    {
        ResultSet rs=null;
        ResultSet rsAdmin=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        model.addAttribute("eleventhBeanObject", new SearchPersonClass());
        session=req.getSession();
        String username=mc.getUserName().toLowerCase();
        String password=mc.getPassword();        
        if(!username.matches("\\s*") && !password.matches("\\s*"))
        {
            try
            {
                rsAdmin=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdmin.next())
                {                                           
                    if(username.equals(rsAdmin.getString(1)))
                    {                  
                        if(password.equals(rsAdmin.getString(5)))
                        {
                            session.setAttribute("admin", rsAdmin.getString(2) + " " + rsAdmin.getString(3));                                
                            //Use these four session to secure admin's pages against unauthorized access from users.
                            session.setAttribute("firstname", rsAdmin.getString(2));
                            session.setAttribute("lastname", rsAdmin.getString(3));
                            session.setAttribute("username", username);
                            session.setAttribute("password", password);                                
                            return "redirect:/adminControl";
                        }
                    }
                }
                //users login
                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers");                
                while(rs.next())
                {    
                    if(username.equals(rs.getString(1)))
                    {                  
                        if(password.equals(rs.getString(5)))
                        {
                            if(rs.getString(6)==null)
                            {
                                session.setAttribute("details", rs.getString(2) + " " + rs.getString(3));
                                session.setAttribute("firstname", rs.getString(2));
                                session.setAttribute("lastname", rs.getString(3));
                                session.setAttribute("username", username);
                                session.setAttribute("password", password);
                                session.setAttribute("telephone", rs.getString(4));
                                
                                return "redirect:/registeredMemberControl";
                            }
                            else if(rs.getString(6).equals("suspended"))
                            {
                                model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                                model.addAttribute("loginError", "Try again later.");
                                return "homepage";
                            }                                
                        }                    
                        else
                        {
                            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                            model.addAttribute("loginError", "Password is incorrect.");
                            return "homepage";
                        }
                    }
                }
                model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                model.addAttribute("loginError", "Username does not exist");
                return "homepage";
            }        
            catch (SQLException ex)
            {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rs!=null)
                    {
                        rs.close();
                    }
                    if(rsAdmin!=null)
                    {
                        rsAdmin.close();
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
            model.addAttribute("loginError", "Enter required fields please.");
        }
        return "homepage";
    }    
    //optimized 2
    @RequestMapping(value="/registeredMemberControl")
    public String getHomePage(@ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
        //statistics
        succ.statistics("MEMBER_PAGE");
        
        //ResultSet rs=null;
        ResultSet rsFreelanceCheck=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        //Date d1=new Date();
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        if(username!=null)
        {
            try
            {
                rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+username+"'");
                while(rsFreelanceCheck.next())
                {
                    if(username.equals(rsFreelanceCheck.getString(2)) && rsFreelanceCheck.getString(3).equals(""))
                    {
                        String[] niche=rsFreelanceCheck.getString(5).split("\\s+");
                        session.setAttribute("categoryList", niche);
                        model.addAttribute("sumBlogger", "<a style='color: yellow;' href='featuredmvp'>Sumblog @<i>"+username+"</i></a>");
                    }
                }
                
                /*rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");                
                while(rs.next())
                {
                    String subscriptionDate=rs.getString(9);Date d2=null;
                    if(subscriptionDate.contains("st"))
                    {
                        d2=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(subscriptionDate.replace("st", ""));
                    }
                    else if(subscriptionDate.contains("nd"))
                    {
                        d2=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(subscriptionDate.replace("nd", ""));
                    }
                    else if(subscriptionDate.contains("rd"))
                    {
                        d2=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(subscriptionDate.replace("rd", ""));
                    }
                    else if(subscriptionDate.contains("th"))
                    {
                        d2=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(subscriptionDate.replace("th", ""));
                    }
                                
                    if(d1.compareTo(d2)==-1)
                    {
                        session.setAttribute("subscription", "Premium Expires: "+rs.getString(9));
                    }
                    else
                    {
                        session.removeAttribute("subscription");
                    }
                }*/
            }
            catch (SQLException ex)
            {
                Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    /*if(rs!=null)
                    {
                        rs.close();
                    }*/
                    if(rsFreelanceCheck!=null)
                    {
                        rsFreelanceCheck.close();
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            session.setAttribute("colleagues", msc.colleagueList(session));
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
            model.addAttribute("displayadvert", displayadvert);
            return "registeredMember";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
            return "homepage";
        }        
    }
    
    @RequestMapping(value="/adminfeaturedmvp")
    public String adminfeaturedMostValuedPost(HttpSession session, HttpServletRequest req, ModelMap model,
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rsFreelanceCheck=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            try
            {
                rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsFreelanceCheck.next())
                {
                    if(password.equals(rsFreelanceCheck.getString(5)) && firstname.equals(rsFreelanceCheck.getString(2)) 
                    && lastname.equals(rsFreelanceCheck.getString(3)))
                    {
                        session.setAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("freeBlogBeanObject", new FreeBlogBeanObject());
                        return "adminfreeblog";
                    }
                }
                session.invalidate();
                model.addAttribute("expired", "Unauthorized access. You have been logged out!!!");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rsFreelanceCheck!=null)
                    {
                        rsFreelanceCheck.close();
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
    
    @RequestMapping(value="/featuredmvp")
    public String featuredMostValuedPost(HttpSession session, HttpServletRequest req, ModelMap model,
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rsFreelanceCheck=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        if(username!=null)
        {
            try
            {
                rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable");
                while(rsFreelanceCheck.next())
                {
                    if(username.equals(rsFreelanceCheck.getString(2)) && rsFreelanceCheck.getString(3).equals(""))
                    {
                        session.setAttribute("colleagues", msc.colleagueList(session));
                        model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                        model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        model.addAttribute("freeBlogBeanObject", new FreeBlogBeanObject());            
                        return "freeBlog";
                    }
                }
                session.invalidate();
                model.addAttribute("expired", "Unauthorized access. You have been logged out!!!");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rsFreelanceCheck!=null)
                    {
                        rsFreelanceCheck.close();
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
    
    @RequestMapping(value="/adminmvpsumblog")
    public String adminmvpFreeBlog(HttpSession session, HttpServletRequest req, ModelMap model, 
    @ModelAttribute("freeBlogBeanObject")FreeBlogBeanObject fbbo, RedirectAttributes ra, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rsFreelanceCheck=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            try 
            {
                rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsFreelanceCheck.next())
                {
                    if(password.equals(rsFreelanceCheck.getString(5)) && firstname.equals(rsFreelanceCheck.getString(2)) 
                    && lastname.equals(rsFreelanceCheck.getString(3)))
                    {
                        String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                        String title=cc.titleCase(new String(fbbo.getTitle().getBytes(ISO), UTF));
                        String content=new String(fbbo.getContent().getBytes(ISO), UTF);
                        fbbo.setTitle(title);
                        fbbo.setContent(content);
                        
                        switch(fbbo.getChoice())
                        {
                            case "add_Image":
                            {                                        
                                MultipartFile contentImage=fbbo.getContentImage();
                                if(contentImage.getSize()<5000000 && contentImage.getSize()!=0)
                                {
                                    String file1=contentImage.getOriginalFilename();                        
                                    if(file1.length()<30)
                                    {
                                        if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                                        || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                                        || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                                        {
                                            String imageRef="<br><br><img src='resources/images/general_Images/" + file1 + "' width='400' height='300'/><br><br>";
                                            fbbo.setTitle(title);
                                            fbbo.setContent(content + imageRef);
                                            model.addAttribute("errorZZ", file1+ " added successfully.");                                                                                
                                            File pathFile1=new File(imagePath, file1);                                                                                
                                            contentImage.transferTo(pathFile1);
                                            return "adminfreeblog";
                                        }
                                        else
                                        {
                                            model.addAttribute("errorZZ", "File format should be .jpg, .png, .gif or .webp");
                                            return "adminfreeblog";
                                        }
                                    }
                                    else
                                    {
                                        model.addAttribute("errorZZ", "File name is lengthy.");
                                        return "adminfreeblog";
                                    }
                                }
                                else if(contentImage.getSize()==0)
                                {
                                    model.addAttribute("errorZZ", "You tried to upload an empty file.");
                                    return "adminfreeblog";
                                }
                                else
                                {
                                    model.addAttribute("errorZZ", "File size exceeded.");
                                    return "adminfreeblog";
                                }
                            }
                                    
                            case "post_Story":
                            {
                                int titleCount=cc.wordCount(title);
                                int numberOfWords=cc.wordCount(content);
                                String category=fbbo.getNiche().toLowerCase();                                        
                                String date=new UtilityClass().getDate();
                                MultipartFile coverImage=fbbo.getCoverImage();
                                String coverImageName=coverImage.getOriginalFilename();
                                        
                                if(coverImage.getSize()<5000000 && coverImage.getSize()!=0)
                                {
                                    if(!title.matches("\\s*"))
                                    {
                                        if(titleCount<21)
                                        {
                                            if(!content.matches("\\s*"))
                                            {
                                                username=admin;
                                                content=content.replaceAll("<#", "<pre>");
                                                content=content.replaceAll("#>", "</pre>");
                                                cc.saveMsg("<span style=\"color: green; font-weight: bold;\">Blogpost</span>", username, title, content, date, "");
                                                switch(category)
                                                {
                                                    case "writing":
                                                    {
                                                        saveNiches("ENABLED", "writing", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "arts":
                                                    {
                                                        saveNiches("ENABLED", "arts", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                        
                                                    case "history":
                                                    {
                                                        saveNiches("ENABLED", "arts", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "literature":
                                                    {
                                                        saveNiches("ENABLED", "writing", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                        
                                                    case "sports":
                                                    {
                                                        saveNiches("ENABLED", "sports", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "health":
                                                    {
                                                        saveNiches("ENABLED", "health", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "diy":
                                                    {
                                                        saveNiches("ENABLED", "diy", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "entertainment":
                                                    {
                                                        saveNiches("ENABLED", "entertainment", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "religion":
                                                    {
                                                        saveNiches("ENABLED", "relpol", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "politics":
                                                    {
                                                        saveNiches("ENABLED", "relpol", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                        
                                                    case "updates":
                                                    {
                                                        saveNiches("ENABLED", "info", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                            
                                                    case "fashion":
                                                    {
                                                        saveNiches("ENABLED", "flt", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                           
                                                    case "lifestyle":
                                                    {
                                                        saveNiches("ENABLED", "flt", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                          
                                                    case "trends":
                                                    {
                                                        saveNiches("ENABLED", "flt", date, username, title, content, coverImageName);
                                                    }
                                                    break;
                                                }
                                                File pathFile1=new File(imagePath, coverImageName);                                                                                
                                                coverImage.transferTo(pathFile1);
                                                ra.addFlashAttribute("errorZZ", title+" posted successfully. Total words="+ numberOfWords);
                                                return "redirect:/adminfeaturedmvp";
                                            }                
                                            else
                                            {
                                                model.addAttribute("errorZZ", "Cannot post an empty story.");
                                                return "adminfreeblog";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("errorZZ", "Title is lengthy should be 20 words or less.");
                                            return "adminfreeblog";
                                        }
                                    }            
                                    else
                                    {
                                        model.addAttribute("errorZZ", "Title cannot be empty.");
                                        return "adminfreeblog";
                                    }
                                }
                                else if(coverImage.getSize()==0)
                                {
                                    model.addAttribute("errorZZ", "Cover image must be present.");
                                    return "adminfreeblog";
                                }
                                else
                                {
                                    model.addAttribute("errorZZ", "File size exceeded!");
                                    return "adminfreeblog";
                                }                                        
                            }
                        }
                    }
                }
            }
            catch (SQLException | IOException | IllegalStateException ex)
            {
                Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rsFreelanceCheck!=null)
                    {
                        rsFreelanceCheck.close();
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
    
    @RequestMapping(value="/mvpsumblog")
    public String mvpFreeBlog(HttpSession session, HttpServletRequest req, ModelMap model, 
    @ModelAttribute("freeBlogBeanObject")FreeBlogBeanObject fbbo, RedirectAttributes ra, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rsFreelanceCheck=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        if(username!=null)
        {
            try 
            {
                rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+username+"'");
                while(rsFreelanceCheck.next())
                {
                    if(!rsFreelanceCheck.getString(3).equals("BANNED"))
                    {
                        if(!rsFreelanceCheck.getString(4).equals("BLOCKED"))
                        {
                            String title=cc.titleCase(new String(fbbo.getTitle().getBytes(ISO), UTF));
                            String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                            String content=new String(fbbo.getContent().getBytes(ISO), UTF);
                            fbbo.setTitle(title);
                            fbbo.setContent(content);
                                
                            switch(fbbo.getChoice())
                            {
                                case "add_Image":
                                {
                                    MultipartFile contentImage=fbbo.getContentImage();
                                    if(contentImage.getSize()<250009 && contentImage.getSize()!=0)
                                    {
                                        String file1=contentImage.getOriginalFilename();                        
                                        if(file1.length()<30)
                                        {
                                            if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                                            || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                                            || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                                            {
                                                String imageRef="<_" + file1 + "_>";
                                                fbbo.setTitle(title);
                                                fbbo.setContent(content + imageRef);
                                                model.addAttribute("errorZZ", file1+ " added successfully.");                                                                                
                                                File pathFile1=new File(imagePath, file1);                                                                                
                                                contentImage.transferTo(pathFile1);
                                                return "freeBlog";
                                            }
                                            else
                                            {
                                                model.addAttribute("errorZZ", "File format should be .jpg, .png, .gif or .webp");
                                                return "freeBlog";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("errorZZ", "File name is lengthy.");
                                            return "freeBlog";
                                        }
                                    }
                                    else if(contentImage.getSize()==0)
                                    {
                                        model.addAttribute("errorZZ", "You tried to upload an empty file.");
                                        return "freeBlog";
                                    }
                                    else
                                    {
                                        model.addAttribute("errorZZ", "File size exceeded!");
                                        return "freeBlog";
                                    }
                                }
                                    
                                case "post_Story":
                                {
                                    int titleCount=cc.wordCount(title);
                                    int numberOfWords=cc.wordCount(content);
                                    String category=fbbo.getNiche().toLowerCase();                                        
                                    String date=new UtilityClass().getDate();
                                    MultipartFile coverImage=fbbo.getCoverImage();
                                    String coverImageName=coverImage.getOriginalFilename();
                                            
                                    if(coverImage.getSize()<300009 && coverImage.getSize()!=0)
                                    {
                                        if(!title.matches("\\s*"))
                                        {
                                            if(titleCount<21)
                                            {
                                                if(!content.matches("\\s*"))
                                                {
                                                    if(numberOfWords<1009)
                                                    {
                                                        content=content.replaceAll("<_", "<br><br><img width='400' height='300' src='resources/images/general_Images/");
                                                        content=content.replaceAll("_>", "'/><br><br>");
                                                        content=content.replaceAll("<#", "<pre>");
                                                        content=content.replaceAll("#>", "</pre>");
                                                        cc.saveMsg("<span style=\"color: green; font-weight: bold;\">Blogpost</span>", username, title, content, date, "");
                                        
                                                        switch(category)
                                                        {
                                                            case "writing":
                                                            {
                                                                saveNiches("DISABLED", "writing", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "arts":
                                                            {
                                                                saveNiches("DISABLED", "arts", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                        
                                                            case "history":
                                                            {
                                                                saveNiches("DISABLED", "arts", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "literature":
                                                            {
                                                                saveNiches("DISABLED", "writing", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                        
                                                            case "sports":
                                                            {
                                                                saveNiches("DISABLED", "sports", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "health":
                                                            {
                                                                saveNiches("DISABLED", "health", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "diy":
                                                            {
                                                                saveNiches("DISABLED", "diy", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "entertainment":
                                                            {
                                                                saveNiches("DISABLED", "entertainment", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "religion":
                                                            {
                                                                saveNiches("DISABLED", "relpol", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "politics":
                                                            {
                                                                saveNiches("DISABLED", "relpol", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                        
                                                            case "updates":
                                                            {
                                                                saveNiches("DISABLED", "info", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                            
                                                            case "fashion":
                                                            {
                                                                saveNiches("DISABLED", "flt", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                           
                                                            case "lifestyle":
                                                            {
                                                                saveNiches("DISABLED", "flt", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                          
                                                            case "trends":
                                                            {
                                                                saveNiches("DISABLED", "flt", date, username, title, content, coverImageName);
                                                            }
                                                            break;
                                                        }
                                                        cc.updateReg_SaveNews_Blog(username, "blogtable", "blogposts");
                                                        File pathFile1=new File(imagePath, coverImageName);                                                                                
                                                        coverImage.transferTo(pathFile1);
                                                        ra.addFlashAttribute("errorZZ", title+" posted successfully. Total words="+ numberOfWords);
                                                        return "redirect:/featuredmvp";
                                                    }
                                                    else
                                                    {
                                                        model.addAttribute("errorZZ", "Word count should be 1000 or less.");
                                                        return "freeBlog";
                                                    }
                                                }
                                                else
                                                {
                                                    model.addAttribute("errorZZ", "Cannot post an empty story.");
                                                    return "freeBlog";
                                                }
                                            }
                                            else
                                            {
                                                model.addAttribute("errorZZ", "Title is lengthy, should be 20 words or less");
                                                return "freeBlog";
                                            }
                                        }            
                                        else
                                        {
                                            model.addAttribute("errorZZ", "Title cannot be empty.");
                                            return "freeBlog";
                                        }
                                    }
                                    else if(coverImage.getSize()==0)
                                    {
                                        model.addAttribute("errorZZ", "Cover image must be present.");
                                        return "freeBlog";
                                    }
                                    else
                                    {
                                        model.addAttribute("errorZZ", "File size exceeded.");
                                        return "freeBlog";
                                    }                                        
                                }
                            }
                        }
                        model.addAttribute("errorZZ", "You have been blocked.");
                        return "freeBlog";
                    }
                    model.addAttribute("errorZZ", "You have been banned.");
                    return "freeBlog";
                }
            }
            catch (SQLException | IOException | IllegalStateException ex)
            {
                Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rsFreelanceCheck!=null)
                    {
                        rsFreelanceCheck.close();
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
    
    //prepareStatement does update, insert and the rest.
    protected void saveNiches(String a, String b, String c, String d, String e, String f, String g)
    {
        PreparedStatement psFreeBlogSaveStory=null;
        
        try
        {
            psFreeBlogSaveStory=con.prepareStatement("INSERT INTO blogtable (approve, tablex, datex, username, title, content, coverimage, likes, views) VALUES(?,?,?,?,?,?,?,?,?)");
            psFreeBlogSaveStory.setString(1, a);
            psFreeBlogSaveStory.setString(2, b);
            psFreeBlogSaveStory.setString(3, c);
            psFreeBlogSaveStory.setString(4, d);
            psFreeBlogSaveStory.setString(5, e);
            psFreeBlogSaveStory.setString(6, f);
            psFreeBlogSaveStory.setString(7, g);
            psFreeBlogSaveStory.setInt(8, 0);
            psFreeBlogSaveStory.setInt(9, 0);
            psFreeBlogSaveStory.executeUpdate();            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
            {
                try
                {
                    if(psFreeBlogSaveStory!=null)
                    {
                        psFreeBlogSaveStory.close();
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    //optimized 1
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String loggingOut(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("fourthBeanObject") MemberClass mc, 
    @ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, HttpSession session, ModelMap model)
    {
        session=req.getSession();
        session.invalidate();
        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        return "homepage";
    }
}