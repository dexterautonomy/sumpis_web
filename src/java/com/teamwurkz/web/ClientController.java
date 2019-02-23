package com.teamwurkz.web;
import com.teamwurkz.domain.AdminBlogPostApproval;
import com.teamwurkz.domain.AdminControlClass;
import com.teamwurkz.domain.ColleagueClass;
import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.FileUploadClass;
import com.teamwurkz.domain.LikeObject;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.NewsPostClass;
import com.teamwurkz.domain.PasswordChangeClass;
import com.teamwurkz.domain.SearchPersonClass;
import com.teamwurkz.domain.WorkOnFile;
import com.teamwurkz.domain.WriteUs;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POITextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientController
{
    @Value("${advert.display}")//Properties file pending when we start allowing adverts. For now it is display: none
    String displayadvert;
    @Autowired
    SignUpController succ;    
    @Autowired
    MessagingController msc;
    @Autowired
    LoginClass lc;
    @Value("${admin.name}")
    String admin;
    @Value("${imagepath.proploc}")
    String pathFromPropertiesFile;    
    @Autowired
    MenuController mk;    
    
    private Context ctx;
    private DataSource ds;
    private Connection con;
    
    
    private final Charset ISO=Charset.forName("ISO-8859-1");
    private final Charset UTF=Charset.forName("UTF-8");
        
    public ClientController()
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
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RequestMapping(value="/usermessage")
    public String clientMessage(ModelMap model, HttpServletRequest req,
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, HttpSession session)
    {
        ResultSet rsAdminCheck=null;
        //statistics
        succ.statistics("COMPOSE");
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
            model.addAttribute("seventhBeanObject", new WorkOnFile());
            model.addAttribute("eightBeanObject", new WriteUs());            
            try
            {
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        session.setAttribute("clients", msc.adminList());
                        return "adminMessagePage";
                    }
                }
                model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                model.addAttribute("friendrequest", succ.getFriendRequest(username));
                session.setAttribute("colleagues", msc.colleagueList(session));
                return "message";
            }
            catch (SQLException ex)
            {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
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
    
    @RequestMapping(value="/flc", method=RequestMethod.POST)
    public String fileToClient(@ModelAttribute("seventhBeanObject") WorkOnFile wof, HttpServletRequest req, ModelMap model, 
    @ModelAttribute("eightBeanObject") WriteUs wu, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        ResultSet rsAdminCheck=null;
        
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
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                        model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        try
                        {
                            String fileToString;                
                            String receiver=wof.getSendTo();
                            MultipartFile uploadedFile=wof.getFile();
                            String nameOfFile=uploadedFile.getOriginalFilename();
                            String dateOfPost=new UtilityClass().getDate();
                            String nx="New";
                            ByteArrayInputStream fileStream=new ByteArrayInputStream(uploadedFile.getBytes());
                            
                            if(uploadedFile.getSize()!=0)
                            {
                                if(uploadedFile.getSize()<=5000000)//5MB
                                {
                                    if(nameOfFile.endsWith(".pdf"))
                                    {
                                        PDDocument pdf=PDDocument.load(fileStream);
                                        PDFTextStripper strippingThePDF=new PDFTextStripper();
                                        fileToString=strippingThePDF.getText(pdf);                        
                                        saveMsg(admin, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);                        
                                        model.addAttribute("outcome", "Success!");
                                    }
                                    else if(nameOfFile.endsWith(".docx"))
                                    {
                                        XWPFDocument doc=new XWPFDocument(fileStream);
                                        POITextExtractor ext=new XWPFWordExtractor(doc);                        
                                        fileToString=ext.getText(); 
                                        saveMsg(admin, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                                        model.addAttribute("outcome", "Success!");                   
                                    }
                                    else if(nameOfFile.endsWith(".doc"))
                                    {
                                        HWPFDocument doc=new HWPFDocument(fileStream);
                                        WordExtractor we=new WordExtractor(doc);
                                        fileToString=we.getText();
                                        saveMsg(admin, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                                        model.addAttribute("outcome", "Success!");                    
                                    }
                                    else if(nameOfFile.endsWith(".txt"))
                                    {
                                        fileToString=IOUtils.toString(fileStream, "UTF-8");            
                                        saveMsg(admin, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                                        model.addAttribute("outcome", "Success!");
                                    }
                                    else
                                    {
                                        model.addAttribute("outcome", "Format not supported!");
                                    }
                                }
                                else
                                {
                                    model.addAttribute("outcome", "Permitted size exceeded!");
                                }
                            }
                            else
                            {
                                model.addAttribute("outcome", "Empty file!");
                            }            
                        }
                        catch (IOException ex)
                        {   
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @RequestMapping(value="/flg", method=RequestMethod.POST)
    public String fileToAdmin(@ModelAttribute("seventhBeanObject") WorkOnFile wof, HttpServletRequest req, ModelMap model, 
    @ModelAttribute("eightBeanObject") WriteUs wu, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc,
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, HttpSession session)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        
        if(username!=null)
        {
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));            
            try
            {
                String fileToString;            
                String receiver=wof.getSendTo();
                MultipartFile uploadedFile=wof.getFile();
                String nameOfFile=uploadedFile.getOriginalFilename();
                String dateOfPost=new UtilityClass().getDate();
                String nx="New";
                ByteArrayInputStream fileStream=new ByteArrayInputStream(uploadedFile.getBytes());
                
                if(uploadedFile.getSize()!=0)
                {
                    if(uploadedFile.getSize()<=1000000)//1MB
                    {
                        if(nameOfFile.endsWith(".pdf"))
                        {
                            PDDocument pdf=PDDocument.load(fileStream);
                            PDFTextStripper strippingThePDF=new PDFTextStripper();
                            fileToString=strippingThePDF.getText(pdf);
                            saveMsg(username, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                            model.addAttribute("outcome", "Success!");
                        }            
                        else if(nameOfFile.endsWith(".docx"))
                        {
                            XWPFDocument doc=new XWPFDocument(fileStream);
                            POITextExtractor ext=new XWPFWordExtractor(doc);                        
                            fileToString =ext.getText(); 
                            saveMsg(username, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                            model.addAttribute("outcome", "Success!");                   
                        }                    
                        else if(nameOfFile.endsWith(".doc"))
                        {
                            HWPFDocument doc=new HWPFDocument(fileStream);
                            WordExtractor we=new WordExtractor(doc);
                            fileToString=we.getText();
                            saveMsg(username, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                            model.addAttribute("outcome", "Success!");                    
                        }        
                        else if(nameOfFile.endsWith(".txt"))
                        {
                            fileToString=IOUtils.toString(fileStream, "UTF-8");
                            saveMsg(username, receiver, nameOfFile, "<pre>"+fileToString+"</pre>", dateOfPost, nx);
                            model.addAttribute("outcome", "Success!");
                        }        
                        else
                        {
                            model.addAttribute("outcome", "Format not supported!");
                        }
                    }
                    else
                    {
                        model.addAttribute("outcome", "Permitted size exceeded!");
                    }
                }            
                else
                {
                    model.addAttribute("outcome", "Empty file!");
                }            
            }
            catch (IOException ex)
            {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }        
            return "message";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
            return "homepage";
        }
    }
    
    protected void saveMsg(String a, String b, String c, String d, String e, String f)
    {
        PreparedStatement ps=null;
        
        try
        {   
            ps=con.prepareStatement("INSERT INTO messages (sender, receiver, subject, message, datex, unread, approval) VALUES(?,?,?,?,?,?,?)");            
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.setString(4, d);
            ps.setString(5, e);
            ps.setString(6, f);
            ps.setString(7, "ENABLED");
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
    
    @RequestMapping(value="/wrc", method=RequestMethod.POST)
    public String adminNoteWriteUp(@ModelAttribute("seventhBeanObject") WorkOnFile wof, HttpServletRequest req, ModelMap model, 
    @ModelAttribute("eightBeanObject") WriteUs wu, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc,
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, HttpSession session)
    {
        ResultSet rsAdminCheck=null;
        ResultSet rsAllUsers=null;
        
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
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        String count=succ.getUnreadCount(admin);
                        model.addAttribute("unreadMessages", count);
                        String receiver=wu.getSendTo();
                        String subject=titleCase(new String(wu.getSubject().trim().getBytes(ISO), UTF));
                        String dateOfPost=new UtilityClass().getDate();
                        String content=new String(wu.getWriteUp().trim().getBytes(ISO), UTF);
                        String nx="New";
                        wu.setSubject(subject);
                        wu.setWriteUp(content);
                        
                        switch (wu.getAction())
                        {
                            case "individual":
                            {
                                if(!subject.matches("\\s*") && !content.matches("\\s*"))
                                {
                                    content=content.replaceAll("<#", "<pre>");
                                    content=content.replaceAll("#>", "</pre>");
                                                        
                                    saveMsg(admin, receiver, subject, content, dateOfPost, nx);
                                    saveMsg("<span style=\"color: red; font-weight: bold;\">Outbox</span>", admin, "Message sent to: @"+receiver, content, dateOfPost, "");
                                    model.addAttribute("outcome", "Success!");
                                }    
                                else
                                {
                                    model.addAttribute("outcome", "Enter required fields. Thank you!");
                                }    
                                return "adminMessagePage";
                            }
                            
                            case "all":
                            {
                                if(!subject.matches("\\s*") && !content.matches("\\s*"))
                                {
                                    content=content.replaceAll("<#", "<pre>");
                                    content=content.replaceAll("#>", "</pre>");
                                    rsAllUsers=con.createStatement().executeQuery("SELECT username FROM registeredusers");
                                    while(rsAllUsers.next())
                                    {
                                        saveMsg("Sumpis", rsAllUsers.getString(1), subject, content, dateOfPost, nx);
                                        model.addAttribute("outcome", "Success!");
                                    }
                                    saveMsg("Sumpis", admin, subject, content, dateOfPost, "");
                                }    
                                else
                                {
                                    model.addAttribute("outcome", "Enter required fields. Thank you!");
                                }    
                                return "adminMessagePage";
                            }
                        }
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
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
                    }
                    if(rsAllUsers!=null)
                    {
                        rsAllUsers.close();
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
    
    @RequestMapping(value="/wrt", method=RequestMethod.POST)
    public String clientNoteWriteUp(@ModelAttribute("seventhBeanObject") WorkOnFile wof, HttpServletRequest req, ModelMap model, 
    @ModelAttribute("eightBeanObject") WriteUs wu, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc,
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, HttpSession session)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
            String receiver=wu.getSendTo();
            String subject=titleCase(new String(wu.getSubject().trim().getBytes(ISO), UTF));
            String content=new String(wu.getWriteUp().trim().getBytes(ISO), UTF);
            String nx="New";
            wu.setSubject(subject);
            wu.setWriteUp(content);
            String dateOfPost=new UtilityClass().getDate();
            
            if(subject.matches("\\s*"))
            {
                subject="No Subject";
            }
            
            if(!content.matches("\\s*"))
            {
                content=content.replaceAll("<_", "<br><br><img width='400' height='300' src='resources/images/general_Images/");
                content=content.replaceAll("_>", "'/><br><br>");
                content=content.replaceAll("<#", "<pre>");
                content=content.replaceAll("#>", "</pre>");
                
                saveMsg(username, receiver, subject, content, dateOfPost, nx);
                saveMsg("<span style=\"color: red; font-weight: bold;\">Outbox</span>", username, "Message sent to: @"+receiver, content, dateOfPost, "");
                model.addAttribute("outcome", "Success!");
            }            
            else
            {
                model.addAttribute("outcome", "Enter required fields. Thank you!");
            }            
            return "message";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    @RequestMapping(value="/add")
    public String addCtx(@RequestParam("u")String person, @RequestParam("action")String action, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    HttpSession session, ModelMap model, RedirectAttributes ra)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String sender=(String)session.getAttribute("username");        
        if(sender!=null)
        {
            if(sender.equals(person))
            {
                ra.addFlashAttribute("friendRequest", "This is you!");
                return "redirect:/profile?ux="+person;
            }
            
            switch(action)
            {
                case "add":
                {
                    try
                    {
                        rs=con.createStatement().executeQuery("SELECT * FROM contactdb");            
                        while(rs.next())
                        {
                            String checkSender=rs.getString(2);
                            String checkRecipient=rs.getString(3);
                            String confirm=rs.getString(4);
                    
                            if(checkSender.equals(sender) && checkRecipient.equals(person) && confirm.equals(""))
                            {
                                ra.addFlashAttribute("friendRequest", "Cannot send request to a person twice, please exercise patience "
                                + "whilst they accept your request. Thank you.");
                                return "redirect:/profile?ux="+person;
                            }                
                            else if(checkSender.equals(sender) && checkRecipient.equals(person) && confirm.equals("confirmed"))
                            {                        
                                ra.addFlashAttribute("friendRequest", "You are colleagues with this person already.");
                                return "redirect:/profile?ux="+person;
                            }                
                            else if(checkSender.equals(person) && checkRecipient.equals(sender) && confirm.equals(""))
                            {                        
                                ra.addFlashAttribute("friendRequest", "This person has sent you a request already, please accept it. Thank you.");
                                return "redirect:/profile?ux="+person;
                            }                
                            else if(checkSender.equals(person) && checkRecipient.equals(sender) && confirm.equals("confirmed"))
                            {                        
                                ra.addFlashAttribute("friendRequest", "You are colleagues with this person already.");
                                return "redirect:/profile?ux="+person;
                            }
                        }            
                        saveFriendRequest(sender, person, "");
                        ra.addFlashAttribute("friendRequest", "Request sent.");
                    }        
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
                break;
                
                case "report":
                {
                    String reportLink="<a style=\"font-weight: bold; text-align: center; color: purple\" href=\"profile?ux="+person+"\">Report Link</a>";
                    String msg="<div style=\"font-weight: bold; font-size:22px; text-align: center; color: red\">Report</div>Reporting @"+person+"<br><br>Misconduct<br><br>"+ reportLink;
                    saveMsg(sender, admin, reportLink, msg, new UtilityClass().getDate(), "New");
                    ra.addFlashAttribute("friendRequest", "Report taken!");
                }
                break;
                
            }
        }        
        else
            ra.addFlashAttribute("friendRequest", "Please sign-up.");
        return "redirect:/profile?ux="+person;
    }
    
    @RequestMapping(value="/u_p")
    public String updateProfile(@RequestParam(value="e", required=false)String edit, @RequestParam(value="a", required=false)String edit2, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session, ModelMap model, HttpServletRequest req)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        
        if(username!=null)
        {
            String pix;
            String gender;
            String mail;
            String location;
            String job;
            String me;
            int newsposts;
            int blogposts;
            String hobby;
            String relationship;
            String joined;
            String sumblogger="No";
            
            model.addAttribute("display", "none");
            if(edit!=null)
            {
                model.addAttribute("display", "inline");
            }
            
            if(edit2!=null)
            {
                model.addAttribute("disp", "inline");
            }
            try
            {
                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='" + username + "'");
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
                    
                    model.addAttribute("profile", username);
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
                    model.addAttribute("eleventhBeanObject", new SearchPersonClass());
                    model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                    model.addAttribute("friendrequest", succ.getFriendRequest(username));
                    return "userprofiledit";
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
    
    @RequestMapping(value="/u_pr")
    public String saveUpdate(@RequestParam("username")String username, HttpSession session, 
    @RequestParam("email")String mail, @RequestParam("loc")String location, @RequestParam("work")String job, 
    @RequestParam("hob")String hobby, @RequestParam("rel")String relationship, @RequestParam("about")String about, 
    ModelMap model, HttpServletRequest req, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec)
    {
        PreparedStatement ps=null;
        
        session=req.getSession();
        String user=(String)session.getAttribute("username");
        
        if(user!=null)
        {
            try
            {
                ps=con.prepareStatement("UPDATE registeredusers SET mail='"+mail+"', location='"+location+"', "
                + "job='"+job+"', hobby='"+hobby+"', relationship='"+relationship+"', aboutyou='"+about+"' WHERE username='"+username+"'");
                ps.executeUpdate();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
            return "redirect:/u_p";
        }
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    @RequestMapping(value="/u_px")
    public String saveUpdatePix(@RequestParam("username")String username, @RequestParam("file")MultipartFile file, 
    ModelMap model, HttpServletRequest req, @ModelAttribute("fourthBeanObject")MemberClass mc, RedirectAttributes ra, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        PreparedStatement ps=null;
        
        session=req.getSession();
        String user=(String)session.getAttribute("username");
        String imagePath=pathFromPropertiesFile+"/resources/images/profile_pix";
        String fileName=file.getOriginalFilename();
        
        if(user!=null)
        {
            if(fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".jpeg")
            || fileName.endsWith(".JPG") || fileName.endsWith(".PNG") || fileName.endsWith(".GIF") || fileName.endsWith(".JPEG") 
            || fileName.endsWith(".webp") || fileName.endsWith(".WEBP"))
            {
                if(file.getSize()<400001)
                {
                    try
                    {
                        File pathFile=new File(imagePath, fileName);                                                                                
                        file.transferTo(pathFile);
                        ps=con.prepareStatement("UPDATE registeredusers SET pix='"+fileName+"' WHERE username='"+username+"'");
                        ps.executeUpdate();
                        return "redirect:/u_p";
                    }
                    catch (SQLException | IOException | IllegalStateException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
                else
                {
                    ra.addFlashAttribute("friendRequest", "File size exceeded");
                }
            }
            else
            {
                ra.addFlashAttribute("friendRequest", "File format should be .jpg, .png, .gif or .webp");
            }
            return "redirect:/u_p";
        }
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    private void saveFriendRequest(String a, String b, String c)
    {
        PreparedStatement ps=null;
        
        try
        {   
            ps=con.prepareStatement("INSERT INTO contactdb (sender, receiver, confirmation) VALUES(?,?,?)");            
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);            
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
    @RequestMapping(value="/colleagueController", method=RequestMethod.GET)
    public String colleagueControl(ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, HttpSession session)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 3);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));            
            List<String> friendRequests=new LinkedList<>();
            List<String> friendList=new LinkedList<>();
            List<String> friendList2=new LinkedList<>();
            try
            {
                rs=con.createStatement().executeQuery("SELECT * FROM contactdb");            
                while(rs.next())
                {
                    String sender=rs.getString(2);
                    String recipient=rs.getString(3);
                    String confirmation=rs.getString(4);                
                    if(recipient.equals(username) && confirmation.equals(""))
                    {
                        friendRequests.add(sender);
                    }
                    if(recipient.equals(username) && confirmation.equals("confirmed"))
                    {
                        friendList.add(sender);
                    }                    
                    if(sender.equals(username) && confirmation.equals("confirmed"))
                    {
                        friendList2.add(recipient);
                    }
                }               
                model.addAttribute("friendRequestList", friendRequests);
                model.addAttribute("friendList", friendList);
                model.addAttribute("friendList2", friendList2);
            }        
            catch (SQLException ex)
            {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
            model.addAttribute("thirteenthBeanObject", new ColleagueClass());
            return "colleagues";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    @RequestMapping(value="/acceptController", method=RequestMethod.GET)
    public String acceptControl(ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("thirteenthBeanObject")ColleagueClass cc, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, HttpSession session, RedirectAttributes ra)
    {
        PreparedStatement ps=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            try
            {
                model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                model.addAttribute("friendrequest", succ.getFriendRequest(username));
                String accept=cc.getAccept();
                switch (cc.getChoice())
                {
                    case "accept":
                    {
                        ps=con.prepareStatement("UPDATE contactdb SET confirmation='confirmed' "
                        + "WHERE sender='" + accept + "' AND receiver='" + username + "'");                        
                        ps.executeUpdate();                        
                        ra.addFlashAttribute("acceptMessage", "You and @" + accept + " are now colleagues");
                        return "redirect:/colleagueController";
                    }                    
                    case "pend":
                    {
                        ps=con.prepareStatement("DELETE FROM contactdb WHERE "
                        + "sender='" + accept + "' AND receiver='" + username + "'");
                        ps.executeUpdate();                        
                        ra.addFlashAttribute("acceptMessage", "You have postponed @" + accept + "");
                        return "redirect:/colleagueController";
                    }                
                    case "remove":
                    {
                        ps=con.prepareStatement("UPDATE contactdb SET confirmation='' WHERE "
                        + "sender='" + accept + "' AND receiver='" + username + "'");
                        ps.executeUpdate();                        
                        ra.addFlashAttribute("removeMessage", "You have removed @" + accept + " from your list of colleagues");
                        return "redirect:/colleagueController";
                    }                
                    case "remove2":
                    {
                        ps=con.prepareStatement("UPDATE contactdb SET confirmation='' WHERE "
                        + "sender='" + username + "' AND receiver='" + accept + "'");
                        ps.executeUpdate();
                        ra.addFlashAttribute("removeMessage", "You have removed @" + accept + " from your list of colleagues");
                        return "redirect:/colleagueController";
                    }
                }
            }                
            catch (SQLException ex)
            {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
            return "colleagues";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    @RequestMapping(value="/adminNewsController")
    public String adminSumpisNewsController(@ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, ModelMap model, HttpServletRequest req, HttpSession session)
    {
        ResultSet rsAdminCheck=null;
        
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
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {                        
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("fourteenthBeanObject", new NewsPostClass());
                                                
                        return "adminsumpisnews";
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
    @RequestMapping(value="/newsController")
    public String sumpisNewsController(@ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, ModelMap model, HttpServletRequest req, HttpSession session)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));            
            model.addAttribute("fourteenthBeanObject", new NewsPostClass());
            return "sumpisnews";           
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    @RequestMapping(value="/adminNewsPostController", method=RequestMethod.POST)
    public String adminNewsPostController(@ModelAttribute("fourteenthBeanObject")NewsPostClass npc, 
    ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    RedirectAttributes ra, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpSession session, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc)
    {
        ResultSet rsAdminCheck=null;
        
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
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        String count=succ.getUnreadCount(admin);
                        model.addAttribute("unreadMessages", count);            
                        try
                        {
                            String title=titleCase(npc.getNewsTitle());
                            String content=new String(npc.getNewsPost().getBytes(ISO), UTF);
                            String choice=npc.getChoice();
                            npc.setNewsTitle(title);
                            npc.setNewsPost(content);
                            switch(choice)
                            {
                                case "post_Story":
                                {
                                    int titleCount=wordCount(title);
                                    //int numberOfWords=wordCount(content);
                                    String date=new UtilityClass().getDate();            
                                    String check=npc.getCheck(); 
                                    
                                    if(!title.matches("\\s*"))
                                    {
                                        if(titleCount<21)
                                        {
                                            if(!content.matches("\\s*"))
                                            {
                                                if(check!=null)
                                                {
                                                    /*make admin unlimited
                                                    if(numberOfWords<1001)
                                                    {*/
                                                        content=content.replaceAll("<#", "<pre>");
                                                        content=content.replaceAll("#>", "</pre>");
                                                        saveNews("ENABLED", date, username, title, content);
                                                        saveMsg("<span style=\"color: blue; font-weight: bold;\">Newspost</span>", admin, title, content, date, "");
                                                        ra.addFlashAttribute("newsposted", "Posted successfully!!!");
                                                        return "redirect:/adminNewsController";
                                                    //}                                    
                                                    /*else
                                                    {
                                                        model.addAttribute("newserror", "Permitted word count exceeded. "
                                                        + "Total = " + numberOfWords + " words. Expected = 1001 words or less.");
                                                        return "adminsumpisnews";
                                                    }*/
                                                }                            
                                                else
                                                {
                                                    model.addAttribute("newserror", "Select the checkbox. Thankyou.");
                                                    return "adminsumpisnews";
                                                }
                                            }                
                                            else
                                            {
                                                model.addAttribute("newserror", "Cannot post an empty story.");
                                                return "adminsumpisnews";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("newserror", "Title is too long should be 20 words or less.");
                                            return "adminsumpisnews";
                                        }
                                    }            
                                    else
                                    {
                                        model.addAttribute("newserror", "Title cannot be empty.");
                                        return "adminsumpisnews";
                                    }
                                }
                                
                                case "add_Image":
                                {
                                    String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                                    MultipartFile imageFile1=npc.getFile1();
                                    if(imageFile1.getSize()<5000009 && imageFile1.getSize()!=0)
                                    {
                                        String file1=imageFile1.getOriginalFilename();                        
                                        if(file1.length()<30)
                                        {
                                            if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                                            || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                                            || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                                            {
                                                String imageRef="<br><br><img src='resources/images/general_Images/" + file1 + "' width='250' height='150' alt='descriptive image'/><br><br>";
                                                npc.setNewsTitle(title);
                                                npc.setNewsPost(content + imageRef);
                                                model.addAttribute("newsposted", file1+ " added successfully.");                                                                                
                                                File pathFile1=new File(imagePath, file1);                                                                                
                                                imageFile1.transferTo(pathFile1);
                                                return "adminsumpisnews";
                                            }
                                            else
                                            {
                                                model.addAttribute("newserror", "File format should be .jpg, .png, .gif or .webp.");
                                                return "adminsumpisnews";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("newserror", "File name is lengthy.");
                                            return "adminsumpisnews";
                                        }
                                    }
                                    else if(imageFile1.getSize()==0)
                                    {
                                        model.addAttribute("newserror", "You tried to upload an empty file.");
                                        return "adminsumpisnews";
                                    }
                                    else
                                    {
                                        model.addAttribute("newserror", "File size exceeded.");
                                        return "adminsumpisnews";
                                    }
                                }
                            }
                        }
                        catch (IllegalStateException | IOException ex)
                        {
                            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
    @RequestMapping(value="/newsPostController", method=RequestMethod.POST)
    public String newsPostController(@ModelAttribute("fourteenthBeanObject")NewsPostClass npc, 
    ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    RedirectAttributes ra, HttpSession session, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {            
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
            try
            {
                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers");                
                while(rs.next())
                {
                    if(username.equals(rs.getString(1)))
                    {
                        String suspension=rs.getString(7);                        
                        if(suspension==null)
                        {
                            String title=titleCase(npc.getNewsTitle());
                            String content=new String(npc.getNewsPost().getBytes(ISO), UTF);
                            String choice=npc.getChoice();
                            npc.setNewsTitle(title);
                            npc.setNewsPost(content);
                            
                            switch(choice)
                            {
                                case "post_Story":
                                {
                                    int titleCount=wordCount(title);
                                    int numberOfWords=wordCount(content);
                                    String date=new UtilityClass().getDate();           
                                    String check=npc.getCheck();
                                    
                                    if(!title.matches("\\s*"))
                                    {                                        
                                        if(titleCount<16)
                                        {
                                            if(!content.matches("\\s*"))
                                            {
                                                if(check!=null)
                                                {
                                                    if(numberOfWords<1005)
                                                    {
                                                        content=content.replaceAll("<_", "<br><br><img alt='descriptive image' width='250' height='150' src='resources/images/general_Images/");
                                                        content=content.replaceAll("_>", "'/><br><br>");
                                                        content=content.replaceAll("<#", "<pre>");
                                                        content=content.replaceAll("#>", "</pre>");
                                                        saveNews("DISABLED", date, username, title, content);
                                                        updateReg_SaveNews_Blog(username, "newz", "newsposts");
                                                        saveMsg("<span style=\"color: blue; font-weight: bold;\">Newspost</span>", username, title, content, date, "");
                                                        ra.addFlashAttribute("newsposted", "Posted successfully!!!");
                                                        return "redirect:/newsController";
                                                    }                                    
                                                    else
                                                    {
                                                        model.addAttribute("newserror", "Word count should be less than 1000.");
                                                        return "sumpisnews";
                                                    }
                                                }                            
                                                else
                                                {
                                                    model.addAttribute("newserror", "Select the checkbox. Thankyou.");
                                                    return "sumpisnews";
                                                }
                                            }                
                                            else
                                            {
                                                model.addAttribute("newserror", "Cannot post an empty story.");
                                                return "sumpisnews";
                                            }
                                        }                                    
                                        else
                                        {
                                            model.addAttribute("newserror", "Title count should be 15 words or less.");
                                            return "sumpisnews";
                                        }
                                    }            
                                    else
                                    {
                                        model.addAttribute("newserror", "Title cannot be empty.");
                                        return "sumpisnews";
                                    }
                                }
                                
                                case "add_Image":
                                {
                                    String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                                    MultipartFile imageFile1=npc.getFile1();
                                    if(imageFile1.getSize()<250009 && imageFile1.getSize()!=0)
                                    {
                                        String file1=imageFile1.getOriginalFilename();
                                        if(file1.length()<30)
                                        {
                                            if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                                            || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                                            || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                                            {
                                                String imageRef="<_" + file1 + "_>";
                                                npc.setNewsTitle(title);
                                                npc.setNewsPost(content + imageRef);
                                                model.addAttribute("newsposted", file1+ " added successfully.");                                                                                
                                                File pathFile1=new File(imagePath, file1);                                                                                
                                                imageFile1.transferTo(pathFile1);
                                                return "sumpisnews";
                                            }
                                            else
                                            {
                                                model.addAttribute("newserror", "File format should be .jpg, .png, .gif or .webp");
                                                return "sumpisnews";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("newserror", "File name is lengthy.");
                                            return "sumpisnews";
                                        }
                                    }
                                    else if(imageFile1.getSize()==0)
                                    {
                                        model.addAttribute("newserror", "You tried to upload an empty file.");
                                        return "sumpisnews";
                                    }
                                    else
                                    {
                                        model.addAttribute("newserror", "File size exceeded.");
                                        return "sumpisnews";
                                    }
                                }
                            }
                        }
                        else if(suspension.equals("suspended"))
                        {
                            model.addAttribute("newserror", "You are barred from posting stories. Write to @"+admin+".");
                            return "sumpisnews";
                        }
                    }
                }                
            }            
            catch (SQLException | IllegalStateException | IOException ex)            
            {            
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    public void saveNews(String check, String date, String username, String title, String content)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("INSERT INTO newz (approve, datex, username, title, content) VALUES(?,?,?,?,?)");
            ps.setString(1, check);
            ps.setString(2, date);
            ps.setString(3, username);
            ps.setString(4, title);
            ps.setString(5, content);
            ps.executeUpdate();
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void updateReg_SaveNews_Blog(String username, String newspost_or_blogpost, String column)
    {
        ResultSet rs=null;
        PreparedStatement ps=null;
        
        int count=0;
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM "+newspost_or_blogpost+" WHERE username='"+username+"'");
            while(rs.next())
            {
                ++count;
            }
            ps=con.prepareStatement("UPDATE registeredusers SET "+column+"="+count+" WHERE username='"+username+"'");
            ps.executeUpdate();
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    protected int wordCount(String content)
    {
        List<String> word=new LinkedList<>();
        BreakIterator brk=BreakIterator.getWordInstance();
        brk.setText(content);
        int lastIndex=brk.first();
        while(BreakIterator.DONE != lastIndex)
        {
            int firstIndex=lastIndex;
            lastIndex=brk.next();
            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(content.charAt(firstIndex)))
            {
                word.add(content.substring(firstIndex, lastIndex));
            }            
        }
        return word.size();
    }
    @RequestMapping(value="/sumpisnews")
    public String news(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, 
    HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session, 
    @RequestParam("pg")int pg)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        //statistics
        succ.statistics("GENERAL");
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        try
        {
            session=req.getSession();
            String username=(String)session.getAttribute("username");
            String password=(String)session.getAttribute("password");
            String firstname=(String)session.getAttribute("firstname");
            String lastname=(String)session.getAttribute("lastname");
            
            model.addAttribute("pg", pg);
            
            List<Integer> idList=new LinkedList<>();
            List<String> dateList=new LinkedList<>();
            List<String> titleList=new LinkedList<>();
            List<String> writerList=new LinkedList<>();
            List<String> statusList=new LinkedList<>();
            
            
            int id=0;
            int setoff=30;//make it 30 later
            if(username!=null)
            {
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        if(pg==1)
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM newz ORDER BY newsid DESC LIMIT " + 0 + ", " + setoff);
                            while(rs.next())
                            {
                                id=rs.getInt(1);
                                idList.add(id);
                                statusList.add(rs.getString(2));
                                dateList.add(rs.getString(3));
                                writerList.add(rs.getString(4));
                                titleList.add(rs.getString(5));
                            }
                            model.addAttribute("id", idList);
                            model.addAttribute("statusX", statusList);
                            model.addAttribute("date", dateList);
                            model.addAttribute("writer", writerList);
                            model.addAttribute("title", titleList);
                            
                            if(id!=0)
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='sumpisnews?pg="+ ++pg+"'>Next</a>");
                            }
                        }
                        else
                        {                
                            rs=con.createStatement().executeQuery("SELECT * FROM newz ORDER BY newsid DESC LIMIT " + (pg-1)*setoff + ", " + setoff);
                            while(rs.next())
                            {
                                id=rs.getInt(1);
                                idList.add(id);
                                statusList.add(rs.getString(2));
                                dateList.add(rs.getString(3));
                                writerList.add(rs.getString(4));
                                titleList.add(rs.getString(5));
                            }
                            model.addAttribute("id", idList);
                            model.addAttribute("statusX", statusList);
                            model.addAttribute("date", dateList);
                            model.addAttribute("writer", writerList);
                            model.addAttribute("title", titleList);
                            
                            model.addAttribute("prev", "<a style='text-decoration: none' href='sumpisnews?pg="+ (pg-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                            if(id!=0)
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href=sumpisnews?pg="+ ++pg+">Next</a>");
                            }
                        }  
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("fifteenthBeanObject", new AdminControlClass());
                        return "adminnewscheckmate";
                    }
                }                        
            }
            if(pg==1)
            {
                rs=con.createStatement().executeQuery("SELECT * FROM newz ORDER BY newsid DESC LIMIT 0, " + setoff);
                while(rs.next())
                {
                    id=rs.getInt(1);
                    if(rs.getString(2).equals("ENABLED"))
                    {
                        idList.add(id);
                        dateList.add(rs.getString(3));
                        writerList.add(rs.getString(4));
                        titleList.add(rs.getString(5));
                    }
                }
                model.addAttribute("id", idList);
                model.addAttribute("date", dateList);
                model.addAttribute("writer", writerList);
                model.addAttribute("title", titleList);
                
                if(id!=0)
                {
                    model.addAttribute("next", "<a style='text-decoration: none' href='sumpisnews?pg="+ ++pg+"'>Next</a>");
                }
            }
            else
            {                
                rs=con.createStatement().executeQuery("SELECT * FROM newz ORDER BY newsid DESC LIMIT " + (pg-1)*setoff + ", " + setoff);
                while(rs.next())
                {
                    id=rs.getInt(1);    
                    if(rs.getString(2).equals("ENABLED"))
                    {
                        idList.add(id);
                        dateList.add(rs.getString(3));
                        writerList.add(rs.getString(4));
                        titleList.add(rs.getString(5));
                    }
                }
                model.addAttribute("id", idList);
                model.addAttribute("date", dateList);
                model.addAttribute("title", titleList);
                model.addAttribute("writer", writerList);
                model.addAttribute("prev", "<a style='text-decoration: none' href='sumpisnews?pg="+ (pg-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                
                if(id!=0)
                {
                    model.addAttribute("next", "<a style='text-decoration: none' href=sumpisnews?pg="+ ++pg+">Next</a>");
                }
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
        return "summarisednews";
    }
    
    @RequestMapping(value="/admingetstory")
    public String news2x(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn, 
    ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
        return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
    }
    
    @RequestMapping(value="/getstory")
    public String news2(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn, 
    ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsAllUsers=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        try
        {
            session=req.getSession();
            String username=(String)session.getAttribute("username");
            String password=(String)session.getAttribute("password");
            String firstname=(String)session.getAttribute("firstname");
            String lastname=(String)session.getAttribute("lastname");
            
            List<Integer> cList=new LinkedList<>();
            List<String> commentsUsername=new LinkedList<>();
            List<String> commentsDate=new LinkedList<>();
            List<String> mainComments=new LinkedList<>();
            List<String> commentLikes=new LinkedList<>();
            List<String> commentsDislikes=new LinkedList<>();
            List<String> commentsShares=new LinkedList<>();
            List<String> quotez=new LinkedList<>();
            List<String> reportz=new LinkedList<>();
            List<String> device=new LinkedList<>();
            List<String> pixList=new LinkedList<>();
            
            rs=con.createStatement().executeQuery("SELECT * FROM newz WHERE newsid=" + n);            
            while(rs.next())
            {
                model.addAttribute("n", n);
                model.addAttribute("date", rs.getString(3));
                model.addAttribute("writer", rs.getString(4));
                model.addAttribute("headline", rs.getString(5));
                model.addAttribute("story", rs.getString(6));
                model.addAttribute("pg", pg);
                model.addAttribute("pgn", pgn);
                    
                //this is for comments
                int setoff=30;//make it 30 later
                int nID=0;
                if(pgn==1)
                {
                    rsAllUsers=con.createStatement().executeQuery("SELECT * FROM commentx INNER JOIN registeredusers ON commentx.username=registeredusers.username "
                    + "WHERE postid="+n+ " AND approval='ENABLED' ORDER BY commentid ASC LIMIT 0,"+setoff);
                    while(rsAllUsers.next())
                    {
                        nID=rsAllUsers.getInt(1);
                        cList.add(nID);//used to identify all comments of a particular post esp for liking, disliking, quoting etc
                        commentsUsername.add(rsAllUsers.getString(4));
                        commentsDate.add(rsAllUsers.getString(5));
                        mainComments.add(rsAllUsers.getString(6));
                        quotez.add(rsAllUsers.getString(7));
                        commentLikes.add(rsAllUsers.getString(8));
                        commentsDislikes.add(rsAllUsers.getString(9));
                        commentsShares.add(rsAllUsers.getString(10));
                        reportz.add(rsAllUsers.getString(11));
                        device.add(rsAllUsers.getString(13));
                        pixList.add(rsAllUsers.getString(24));
                    }
                    
                    model.addAttribute("cList", cList);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("commentLikes", commentLikes);
                    model.addAttribute("commentsDislikes", commentsDislikes);
                    model.addAttribute("commentsShares", commentsShares);
                    model.addAttribute("quotez", quotez);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("device", device);
                    model.addAttribute("pix", pixList);
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='getstory?pg="+pg+"&n="+n+"&pgn="+ ++pgn+"'>Next</a>");
                    }
                }                    
                else
                {
                    rsAllUsers=con.createStatement().executeQuery("SELECT * FROM commentx INNER JOIN registeredusers ON commentx.username=registeredusers.username "
                    + "WHERE postid="+n+" AND approval='ENABLED' ORDER BY commentid ASC LIMIT "+(pgn-1)*setoff+","+setoff);            
                    while(rsAllUsers.next())
                    {
                        nID=rsAllUsers.getInt(1);
                        cList.add(nID);//used to identify all comments of a particular post esp for liking, disliking, quoting etc
                        commentsUsername.add(rsAllUsers.getString(4));
                        commentsDate.add(rsAllUsers.getString(5));
                        mainComments.add(rsAllUsers.getString(6));
                        quotez.add(rsAllUsers.getString(7));
                        commentLikes.add(rsAllUsers.getString(8));
                        commentsDislikes.add(rsAllUsers.getString(9));
                        commentsShares.add(rsAllUsers.getString(10));
                        reportz.add(rsAllUsers.getString(11));
                        device.add(rsAllUsers.getString(13));
                        pixList.add(rsAllUsers.getString(24));
                    }
                    
                    //model.addAttribute("nList", nList);
                    model.addAttribute("cList", cList);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("commentLikes", commentLikes);
                    model.addAttribute("commentsDislikes", commentsDislikes);
                    model.addAttribute("commentsShares", commentsShares);
                    model.addAttribute("quotez", quotez);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("device", device);
                    model.addAttribute("pix", pixList);
                                                
                    model.addAttribute("prev", "<a style='text-decoration: none' href='getstory?pg="+pg+"&n="+n+"&pgn="+(pgn-1)+"'>Previous</a>");
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='getstory?pg="+pg+"&n="+n+"&pgn="+ ++pgn+"'>Next</a>");
                    }
                }
                    
                if(username!=null)//this is for admin
                {
                    try
                    {
                        rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                        while(rsAdminCheck.next())
                        {
                            if(firstname.equals(rsAdminCheck.getString(2)) && lastname.equals(rsAdminCheck.getString(3)) 
                            && password.equals(rsAdminCheck.getString(5)))
                            {
                                model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                                return "adminnewsreaderpage";
                            }
                        }
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    }                        
                }
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
                if(rsAllUsers!=null)
                {
                    rsAllUsers.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "newspage";
    }
    
    @RequestMapping(value="/adminCrossCheckNews")
    public String adminCrossCheckNews(@RequestParam("n")int n, @RequestParam("pg")int pg, @RequestParam("pgn")int pgn, 
    @RequestParam("action")String action, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    HttpSession session, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, RedirectAttributes ra)
    {
        ResultSet rs=null;
        PreparedStatement ps=null;
        ResultSet rsAdminCheck=null;
        
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
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        switch(action)
                        {
                            case "approve":
                            {
                                /*
                                ps=con.prepareStatement("UPDATE newz SET approve='ENABLED' WHERE newsid="+n);
                                ps.executeUpdate();
                                ra.addFlashAttribute("approval", "Newspost approved!");
                                */
                                String headline="";
                                rs=con.createStatement().executeQuery("SELECT * FROM newz WHERE newsid=" + n);            
                                while(rs.next())
                                {
                                    headline=rs.getString(5);
                                    saveNews("ENABLED", rs.getString(3), rs.getString(4), headline, rs.getString(6));
                                }
                                ps=con.prepareStatement("DELETE FROM newz WHERE newsid="+n);
                                //ps=con.prepareStatement("UPDATE newz SET approve='DISABLED' WHERE newsid="+n);
                                ps.executeUpdate();
                                //ra.addFlashAttribute("approval", "Newspost approved!");
                                ra.addFlashAttribute("error", "APPROVED: '"+headline+"'");
                                return "redirect:sumpisnews?pg="+pg;
                            }
                            //break;
                            
                            case "disprove":
                            {
                                ps=con.prepareStatement("UPDATE newz SET approve='DISABLED' WHERE newsid="+n);
                                ps.executeUpdate();
                                ra.addFlashAttribute("approval", "Newspost disproved!");
                            }
                            break;
                            
                            case "repost":
                            {
                                String headline="";
                                rs=con.createStatement().executeQuery("SELECT * FROM newz WHERE newsid=" + n);            
                                while(rs.next())
                                {
                                    headline=rs.getString(5);
                                    saveNews("ENABLED", new UtilityClass().getDate(), rs.getString(4), headline, rs.getString(6));
                                }
                                ps=con.prepareStatement("DELETE FROM newz WHERE newsid="+n);
                                //ps=con.prepareStatement("UPDATE newz SET approve='DISABLED' WHERE newsid="+n);
                                ps.executeUpdate();
                                //ra.addFlashAttribute("approval", "REPOSTED: '"+headline+"'");
                                ra.addFlashAttribute("error", "REPOSTED: '"+headline+"'");
                                return "redirect:sumpisnews?pg="+pg;
                            }
                            //break;
                        }
                        return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
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
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    @RequestMapping(value="/adminAcceptApproval")
    public String adminAcceptApproval(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn, 
    @RequestParam("t")String t, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpServletRequest req, HttpSession session, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, ModelMap model, @ModelAttribute("adminBlogPostApproval")AdminBlogPostApproval abpa, 
    @ModelAttribute("likeObject")LikeObject lo, @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, 
    RedirectAttributes ra)
    {
        PreparedStatement ps=null;
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            try
            {
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        String headline=abpa.getHeadline();
                        String writer=abpa.getWriter();
                        String content=abpa.getContent();
                        String currentDate=new UtilityClass().getDate();
                        String coverimage=abpa.getCovimage();
                        
                        switch(abpa.getAction())
                        {
                            case "approveblog":
                            {
                                /*
                                ps=con.prepareStatement("UPDATE blogtable SET approve='ENABLED' WHERE postid="+n);
                                ps.executeUpdate();
                                ra.addFlashAttribute("errorQQ", "Approved!");                                
                                return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
                                */
                                
                                lc.saveNiches("ENABLED", t, currentDate, writer, headline, content, coverimage);
                                ps=con.prepareStatement("DELETE FROM blogtable WHERE postid="+n);
                                ps.executeUpdate();
                                ra.addFlashAttribute("blogmessage", "APPROVED: '"+headline+"'");
                                return "redirect:/categoryListController?pg=1&action="+t;
                            }
                            
                            case "disproveblog":
                            {
                                ps=con.prepareStatement("UPDATE blogtable SET approve='DISABLED' WHERE postid="+n);
                                ps.executeUpdate();
                                ra.addFlashAttribute("errorQQ", "Disabled!");                                
                                return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
                            }
                            
                            case "repostblog":
                            {
                                lc.saveNiches("ENABLED", t, currentDate, writer, headline, content, coverimage);
                                
                                abpa.setDate(currentDate);
                                lo.setDate(currentDate);
                                //ps=con.prepareStatement("UPDATE blogtable SET approve='DISABLED' WHERE postid="+n);
                                ps=con.prepareStatement("DELETE FROM blogtable WHERE postid="+n);
                                ps.executeUpdate();
                                ra.addFlashAttribute("blogmessage", "REPOSTED: '"+headline+"'");
                                return "redirect:/categoryListController?pg=1&action="+t;
                            }
                        }
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
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
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
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";
    }
    
    protected String titleCase(String title)
    {
        if(!title.matches("\\s*"))
        {
            String[] words=title.trim().split("\\s+");
            String titleCase="";            
            for(int i=0; i<words.length; i++)
            {
                words[i]=Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1).toLowerCase();
                titleCase +=words[i] + " ";
            }
            return titleCase;
        }
        else
        {
            return "";
        }        
    }
    @RequestMapping(value="/userpasswordchange")
    public String usersPasswordChange(ModelMap model, HttpServletRequest req,  
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
        ResultSet rsUserCheck=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 3);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        if(username!=null)
        {
            try
            {
                rsUserCheck=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='" + username + "'");
                while(rsUserCheck.next())
                {
                    model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                    model.addAttribute("friendrequest", succ.getFriendRequest(username));
                    model.addAttribute("twentyfourthBeanObject", new PasswordChangeClass());
                    return "userpasswordupdate";
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
                    if(rsUserCheck!=null)
                    {
                        rsUserCheck.close();
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
    @RequestMapping(value="/userpasswordchangecontroller")
    public String userPasswordChangeController(ModelMap model,HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("twentyfourthBeanObject")PasswordChangeClass pcc, 
    HttpSession session)
    {
        ResultSet rsUserCheck=null;
        PreparedStatement psUserPasswordUpdate=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 3);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        String date=new UtilityClass().getDate();
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            try
            {
                rsUserCheck=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='" + username + "'");
                while(rsUserCheck.next())
                {
                    if(password.equals(rsUserCheck.getString(5)) && firstname.equals(rsUserCheck.getString(2)) 
                    && lastname.equals(rsUserCheck.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                        model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        
                        String userX=pcc.getUserName().trim();
                        String oldX=pcc.getOldPassword();
                        String newPass1=pcc.getNewPassword1();
                        String newPass2=pcc.getNewPassword2();
                        String action=pcc.getAction();
                        
                        switch (action)
                        {
                            case "update":
                            {
                                if(!userX.matches("\\s*"))
                                {
                                        if(!oldX.matches("\\s*"))
                                        {
                                            if(!newPass1.matches("\\s*"))
                                            {
                                                if(!newPass2.matches("\\s*"))
                                                {
                                                    if(newPass1.equals(newPass2))
                                                    {
                                                        if(newPass1.length()>7)
                                                        {
                                                            if(userX.equals(username))
                                                            {
                                                                if(oldX.equals(password))
                                                                {
                                                                    session.setAttribute("password", newPass1);
                                                                    psUserPasswordUpdate=con.prepareStatement("UPDATE registeredusers SET "
                                                                    + "password='"+newPass1+"' WHERE username='"+userX+"'");
                                                                    psUserPasswordUpdate.executeUpdate();
                                                                    
                                                                    String msg="Password changed to "+newPass1+" on "+ date;
                                                                    saveMsg("Sumpis", username, "Password Change", msg, date, "New");
                                                                    
                                                                    model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                                                                    model.addAttribute("friendrequest", succ.getFriendRequest(username));
                                                                    model.addAttribute("userpasswordinfo", "Password updated successfully");
                                                                    return "userpasswordupdate";
                                                                }
                                                                else
                                                                {
                                                                    model.addAttribute("userpasswordinfo", "Current password did not match existing password.");
                                                                    return "userpasswordupdate";
                                                                }
                                                            }
                                                            else
                                                            {
                                                                model.addAttribute("userpasswordinfo", "Username did not match.");
                                                                return "userpasswordupdate";
                                                            }
                                                        }
                                                        else
                                                        {
                                                            model.addAttribute("userpasswordinfo", "Password must be more than or equal to 8 characters.");
                                                            return "userpasswordupdate";
                                                        }
                                                    }
                                                    else
                                                    {
                                                        model.addAttribute("userpasswordinfo", "New passwords did not match.");
                                                        return "userpasswordupdate";
                                                    }
                                                }
                                                else
                                                {
                                                    model.addAttribute("userpasswordinfo", "Please confirm new password");
                                                    return "userpasswordupdate";
                                                }
                                            }
                                            else
                                            {
                                                model.addAttribute("userpasswordinfo", "Please enter new password");
                                                return "userpasswordupdate";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("userpasswordinfo", "Please enter current password");
                                            return "userpasswordupdate";
                                        }
                                }
                                else
                                {
                                    model.addAttribute("userpasswordinfo", "Please enter username");
                                    return "userpasswordupdate";
                                }
                            }                            
                            case "cancel":
                            {
                                model.addAttribute("displayadvert", displayadvert);
                                return "registeredMember";
                            }
                        }                        
                        return "userpasswordupdate";
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
                    if(rsUserCheck!=null)
                    {
                        rsUserCheck.close();
                    }
                    if(psUserPasswordUpdate!=null)
                    {
                        psUserPasswordUpdate.close();
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
    
    @RequestMapping(value="/comments", method=RequestMethod.POST)
    public String comments(ModelMap model, @RequestParam("name")String name, 
    @RequestParam("contact")String contact, @RequestParam("message")String message)
    {
        if(!name.matches("\\s*") && !contact.matches("\\s*") && !message.matches("\\s*") )
        {
            String msg="Contact: "+contact+"<br><br>"+message;
            saveMsg(name, admin, "Guest", msg, new UtilityClass().getDate(), "New");
            model.addAttribute("success", "Posted. Thank you for your time.<br><br>");
        }            
        else
        {
            model.addAttribute("success", "Please all fields in the form are compulsory.");
        }
        return "aboutuspage";
    }
    
    @RequestMapping(value="/commentaryControllerx", method=RequestMethod.POST)
    public String commentary(@RequestParam("n")int n, @RequestParam("pg")int pg, @RequestParam("pgn")int pgn, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, ModelMap model, RedirectAttributes ra)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        model.addAttribute("fourteenthBeanObject", new NewsPostClass());//bean object in commentaryPage
        model.addAttribute("n", n);
        model.addAttribute("pg", pg);//current page number you were on the news page
        model.addAttribute("pgn", pgn);//very important for comments
        
        if(username!=null)
        {
            try
            {
                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
                while(rs.next())
                {
                    if(rs.getString(7)==null)//FOR SUSPENSION PURPOSE
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                        model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        return "commentaryPage";
                    }
                    else
                    {
                        ra.addFlashAttribute("commentary", "You have been suspended from making comments/reactions");
                    }
                }
                session.invalidate();
                ra.addFlashAttribute("expired", "Unauthorized access!");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
            ra.addFlashAttribute("commentary", "Please login to write a comment/reaction. Thank you!");
        return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
    }
    
    @RequestMapping(value="/submitCommentary", method=RequestMethod.POST)
    public String submitcommentary(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, RedirectAttributes ra, 
    @ModelAttribute("fourteenthBeanObject")NewsPostClass npc)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        
        model.addAttribute("n", npc.getNmb());
        model.addAttribute("pg", npc.getId());
        model.addAttribute("pgn", npc.getSpare());
        
        int n=npc.getNmb();
        String comment=new String(npc.getNewsPost().getBytes(ISO), UTF);
        npc.setNewsPost(comment);
        
        if(username!=null)
        {            
            switch(npc.getChoice())
            {
                case "post_Story":
                {
                    int numberOfWords=wordCount(comment);
                    if(numberOfWords<1009)
                    {
                        comment=comment.replaceAll("<_", "<br><br><img width='250' height='150' alt='descriptive image' src='resources/images/general_Images/");
                        comment=comment.replaceAll("_>", "'/><br><br>");
                        saveComment(n, username, new UtilityClass().getDate(), comment, null, null, null);
                        ra.addFlashAttribute("commentary", "Comment/Reaction has been posted!");
                        return "redirect:/getstory?pg="+npc.getId()+"&n="+npc.getNmb()+"&pgn="+npc.getSpare();
                    }
                    else
                    {
                        model.addAttribute("commentaryimage", "Comment is too lengthy, should be 1000 words or less.");
                        return "commentaryPage";
                    }
                }
                
                case "add_Image":
                {
                    String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                    MultipartFile imageFile1=npc.getFile1();
                    if(imageFile1.getSize()<250009 && imageFile1.getSize()!=0)
                    {
                        String file1=imageFile1.getOriginalFilename();                        
                        if(file1.length()<30)
                        {
                            if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                            || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                            || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                            {
                                try
                                {
                                    String imageRef="<_" + file1 + "_>";
                                    npc.setNewsPost(comment + imageRef);
                                    File pathFile1=new File(imagePath, file1);
                                    imageFile1.transferTo(pathFile1);
                                    model.addAttribute("commentaryimage", file1+" added!");
                                    return "commentaryPage";
                                }
                                catch (IOException | IllegalStateException ex)
                                {
                                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else
                            {
                                model.addAttribute("commentaryimage", "File format should be .jpg, .png, .gif or .webp");
                                return "commentaryPage";
                            }
                        }
                        else
                        {
                            model.addAttribute("commentaryimage", "Image name is lengthy!");
                                return "commentaryPage";
                        }
                    }
                    else if(imageFile1.getSize()==0)
                    {
                        model.addAttribute("commentaryimage", "You tried uploading an empty file!");
                        return "commentaryPage";
                    }
                    else
                    {
                        model.addAttribute("commentaryimage", "File size exceeded!");
                        return "commentaryPage";
                    }
                }
            }
        }
        else
        {
            session.invalidate();
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Try again!");
        }
        return "homepage";
    }
    
    
    @RequestMapping(value="/blogcommentary", method=RequestMethod.POST)
    public String blogcommentary(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session, HttpServletRequest req, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("fourteenthBeanObject")NewsPostClass npc, RedirectAttributes ra)
    {
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        /*model.addAttribute("writer", npc.getWriter());
        model.addAttribute("headline", npc.getNewsTitle());
        model.addAttribute("date", npc.getDate());*/
        model.addAttribute("pg", npc.getSpare());
        model.addAttribute("n", npc.getId());
        model.addAttribute("pgn", npc.getPage());
        model.addAttribute("t", npc.getTable());
            
        if(username!=null)
        {
            String content=new String(npc.getNewsPost().getBytes(ISO), UTF);
            npc.setNewsPost(content);
            switch(npc.getChoice())
            {
                case "post_Story":
                {
                    int numberOfWords=wordCount(npc.getNewsPost());
                    if(numberOfWords<1009)
                    {
                        content=content.replaceAll("<_", "<br><br><img width='250' height='150' alt='descriptive image' src='resources/images/general_Images/");
                        content=content.replaceAll("_>", "'/><br><br>");
                        saveBlogComment(npc.getId(), npc.getTable(), username, new UtilityClass().getDate(), content, "");
                        ra.addFlashAttribute("errorQQ", "Posted!");
                        return "redirect:/rdr?pg="+npc.getSpare()+"&n="+npc.getId()+"&pgn="+npc.getPage()+"&t="+npc.getTable();
                    }
                    else
                    {
                        model.addAttribute("commentaryimage", "Comment is too lengthy, should be 1000 words or less.");
                        return "tablePage";
                    }
                }
                
                case "add_Image":
                {
                    //Advert
                    List<List<String>> ad=new LinkedList<>();
                    switch(npc.getTable())
                    {
                        case "writing":
                        {
                            ad=succ.adAlgorithm("WRITING/ART", 6);
                        }
                        break;
                        case "arts":
                        {
                            ad=succ.adAlgorithm("WRITING/ART", 6);
                        }
                        break;
                        case "health":
                        {
                            ad=succ.adAlgorithm("HEALTH", 6);
                        }
                        break;
                        case "diy":
                        {
                            ad=succ.adAlgorithm("DIY", 6);
                        }
                        break;
                        case "relpol":
                        {
                            ad=succ.adAlgorithm("RELIGION/POLITICS", 6);
                        }
                        break;
                        case "entertainment":
                        {
                            ad=succ.adAlgorithm("ENTERTAINMENT", 6);
                        }
                        break;
                        case "sports":
                        {
                            ad=succ.adAlgorithm("SPORTS", 6);
                        }
                        break;
                        case "info":
                        {
                            ad=succ.adAlgorithm("TECH/UPDATES/INFO", 6);
                        }
                        break;
                        case "flt":
                        {
                            ad=succ.adAlgorithm("LIFESTYLE/TRENDS/FASHION", 6);
                        }
                        break;                
                    }
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                    MultipartFile imageFile1=npc.getFile1();
                    if(imageFile1.getSize()<250009 && imageFile1.getSize()!=0)
                    {
                        String file1=imageFile1.getOriginalFilename();                        
                        if(file1.length()<30)
                        {
                            if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                            || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                            || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                            {
                                try
                                {
                                    String imageRef="<_" + file1 + "_>";
                                    npc.setNewsPost(npc.getNewsPost() + imageRef);
                                    
                                    model.addAttribute("newsposted", file1+ " added successfully.");                                                                                
                                    File pathFile1=new File(imagePath, file1);
                                    imageFile1.transferTo(pathFile1);
                                    model.addAttribute("commentaryimage", file1+" added!");
                                    return "tablePage";
                                }
                                catch (IOException | IllegalStateException ex)
                                {
                                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else
                            {
                                model.addAttribute("commentaryimage", "File format should be .jpg, .png, .gif or .webp");
                                return "tablePage";
                            }
                        }
                        else
                        {
                            model.addAttribute("commentaryimage", "Image name is lengthy!");
                            return "tablePage";
                        }
                    }
                    else if(imageFile1.getSize()==0)
                    {
                        model.addAttribute("commentaryimage", "You tried uploading an empty file!");
                        return "tablePage";
                    }
                    else
                    {
                        model.addAttribute("commentaryimage", "File size exceeded!");
                        return "tablePage";
                    }
                }
            }
        }
        else
        {
            session.invalidate();
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Try again!");
        }
        return "homepage";
    }
    
    protected void saveComment(int a, String b, String c, String d, String e, String f, String g)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("INSERT INTO commentx (postid, approval, username, datey, comment, quote, liked, disliked, shared, report, tablex, device) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, a);//id specifying all about the news
            ps.setString(2, "ENABLED");//approval
            ps.setString(3, b);//username
            ps.setString(4, c);//datey is the date of this comment was made. NEEDED FOR LIKES, DISLIKED AND SHARED of the comment
            ps.setString(5, d);//comments
            ps.setString(6, e);//quotes
            ps.setInt(7, 0);//for liking of comment
            ps.setInt(8, 0);//for disliking of comment
            ps.setInt(9, 0);//for sharing of comment
            ps.setInt(10, 0);//report
            ps.setString(11, f);//table
            ps.setString(12, g);//device
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    protected void saveBlogComment(int a, String b, String c, String d, String e, String f)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("INSERT INTO blogcommentx (postid, approval, tablex, username, datey, comment, report, device) VALUES(?,?,?,?,?,?,?,?)");
            ps.setInt(1, a);//id specifying all about the blogpost
            ps.setString(2, "ENABLED");//approval
            ps.setString(3, b);//table
            ps.setString(4, c);//username
            ps.setString(5, d);//date
            ps.setString(6, e);//comment
            ps.setInt(7, 0);//report
            ps.setString(8, f);//device
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @RequestMapping(value="/commentAction", method=RequestMethod.POST)
    public String commentAction(@RequestParam("nID")int n, @RequestParam("cID")int c, @RequestParam("pgn")int pgn, 
    @RequestParam("pg")int pg, @RequestParam("choice")String choice, @RequestParam("subcomment")String subcomment, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session,      
    @ModelAttribute("firstBeanObject")FieldClass fc, RedirectAttributes ra, ModelMap model, 
    @RequestParam("headline")String headline, @RequestParam("writer2")String writer2, @RequestParam("date2")String date2)
    {
        ResultSet rs=null;
        PreparedStatement ps=null;
        ResultSet rsX=null;
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        
        int kk=0;
            
        if(username!=null)
        {
            try
            {
                switch(choice)
                {
                    case "like":
                    {
                        rs=con.createStatement().executeQuery("SELECT * FROM newsreact WHERE postid="+n+" AND commentid="+c);
                        while(rs.next())
                        {
                            if(username.equals(rs.getString(4)) && rs.getString(5).equals("LIKED"))
                            {
                                ra.addFlashAttribute("commentary", "You can only like once.");
                                return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                            }
                            else if(username.equals(rs.getString(4)) && rs.getString(6).equals("DISLIKED"))
                            {
                                ra.addFlashAttribute("commentary", "You cannot dislike and like.");
                                return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                            }
                        }
                        
                        if(username.equals(admin))//For promotion
                        {
                            rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                            while(rsX.next())
                            {
                                kk=rsX.getInt(8);                            
                            }                        
                            updateSubComment(c, "liked", 925+kk);
                            return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                        }
                        else
                        {
                            mk.saveLikes(n, c, username, "LIKED", "", "");
                            rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                            while(rsX.next())
                            {
                                kk=rsX.getInt(8);                            
                            }                        
                            updateSubComment(c, "liked", ++kk);
                            return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                        }
                    }
                
                    case "dislike":
                    {
                        rs=con.createStatement().executeQuery("SELECT * FROM newsreact WHERE postid="+n+" AND commentid="+c);
                        while(rs.next())
                        {
                            if(username.equals(rs.getString(4)) && rs.getString(6).equals("DISLIKED"))
                            {
                                ra.addFlashAttribute("commentary", "You can only dislike once.");
                                return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                            }
                            else if(username.equals(rs.getString(4)) && rs.getString(5).equals("LIKED"))
                            {
                                ra.addFlashAttribute("commentary", "You cannot like and dislike.");
                                return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                            }
                        }
                        
                        if(username.equals(admin))//For promotion
                        {
                            rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                            while(rsX.next())
                            {
                                kk=rsX.getInt(9);                            
                            }                        
                            updateSubComment(c, "disliked", 247+kk);
                            return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                        }
                        else
                        {
                            mk.saveLikes(n, c, username, "", "DISLIKED", "");
                            rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                            while(rsX.next())
                            {
                                kk=rsX.getInt(9);                            
                            }
                            updateSubComment(c, "disliked", ++kk);
                            return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                        }
                    }
                
                    case "share":
                    {
                        rs=con.createStatement().executeQuery("SELECT * FROM newsreact WHERE postid="+n+" AND commentid="+c);
                        while(rs.next())
                        {
                            if(username.equals(rs.getString(4)) && rs.getString(7).equals("SHARED"))
                            {
                                ra.addFlashAttribute("commentary", "You can only share once.");
                                return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                            }
                        }
                        
                        if(username.equals(admin))//For promotion
                        {
                            rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                            while(rsX.next())
                            {
                                kk=rsX.getInt(10);                            
                            }                        
                            updateSubComment(c, "shared", 729+kk);
                            return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                        }
                        else
                        {
                            mk.saveLikes(n, c, username, "", "", "SHARED");
                            rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                            while(rsX.next())
                            {
                                kk=rsX.getInt(10);                            
                            }
                            updateSubComment(c, "shared", ++kk);
                            return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                        }
                    }
                
                    case "quote":
                    {
                        //Advert
                        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
                        model.addAttribute("adid", ad.get(0));
                        model.addAttribute("adimg", ad.get(1));
                        
                    
                        rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
                        while(rs.next())
                        {
                            if(username.equals(rs.getString(1)))
                            {                        
                                if(rs.getString(7)==null)//FOR SUSPENSION PURPOSE
                                {
                                    NewsPostClass np=new NewsPostClass();
                                    String writer2Pix="";
                                    rsX=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+writer2+"'");
                                    while(rsX.next())
                                    {
                                        writer2Pix=rsX.getString(11);
                                    }
                                    
                                    String path=req.getContextPath();

                                    np.setSpace("<div style='overflow: auto; padding: 3px;'>"
                                    + "<a href='profile?ux="+writer2+"'><div style='float: left;'><img src='"+path+"/resources/images/profile_pix/"+writer2Pix+"' width='52' height='52' alt='profile pic' style=\"border-radius: 50%;\"/></div></a>"
                                    + "<div style='border-bottom: 1px solid grey;'><a href='profile?ux="+writer2+"'>@"+writer2 +" ["+ date2+"]</a></div>"
                                    + "</div>");

                                    subcomment=subcomment.replaceAll("<br><br><img width='250' height='150' alt='descriptive image' src='resources/images/general_Images/", "<_");
                                    subcomment=subcomment.replaceAll("'/><br><br>", "_>");
                                    np.setNewsPost2(new String(subcomment.getBytes(ISO), UTF));
                                    np.setCheck(new String(subcomment.getBytes(ISO), UTF));
                                    np.setLink(writer2);
                    
                                    model.addAttribute("commenter", writer2);
                                    model.addAttribute("pg", pg);
                                    model.addAttribute("n", n);
                                    model.addAttribute("pgn", pgn);
                                    model.addAttribute("fourteenthBeanObject", np);
                                    model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                                    model.addAttribute("friendrequest", succ.getFriendRequest(username));
                                    return "quotePage";
                                }
                                else
                                {
                                    ra.addFlashAttribute("commentary", "Please try again later");
                                    return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                                }
                            }
                            else
                            {
                                session.invalidate();
                                model.addAttribute("expired", "Unauthorized access!");
                            }
                        }
                    }
                
                    case "report":
                    {
                        String foul="";
                        rsX=con.createStatement().executeQuery("SELECT * FROM commentx WHERE commentid="+c);
                        while(rsX.next())
                        {
                            foul=rsX.getString(6);
                            kk=rsX.getInt(11);                            
                        }
                        updateSubComment(c, "report", ++kk);
                        String reportLink="<a style=\"font-weight: bold; text-align: center; color: purple\" href=\"getstory?pg="+pg+"&n="+n+"&pgn="+pgn+"\">Report Link</a>";
                        String msg="<div style=\"font-weight: bold; font-size:22px; text-align: center; color: red\">"+headline+"</div>Reporting @"+writer2+"<br><br>"+ foul +"<br><br>"+ reportLink;
                        saveMsg(username, admin, reportLink, msg, new UtilityClass().getDate(), "New");
                        
                        ra.addFlashAttribute("commentary", "Report will be investigated.");
                        return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                    }
                
                    case "remove":
                    {
                        rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                        while(rsAdminCheck.next())
                        {
                            if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                            && lastname.equals(rsAdminCheck.getString(3)))
                            {
                                ps=con.prepareStatement("UPDATE commentx SET approval='DISABLED' WHERE commentid="+c);
                                ps.executeUpdate();
                                return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
                            }
                        }
                        session.invalidate();
                        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                        model.addAttribute("expired", "Try again!");
                        return "homepage";
                    }                
                }
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
                    if(rsX!=null)
                    {
                        rsX.close();
                    }
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
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
        else
            ra.addFlashAttribute("commentary", "Please login to perform action!");
        return "redirect:/getstory?pg="+pg+"&n="+n+"&pgn="+pgn;
    }
    
    @RequestMapping(value="/commentactiondx", method=RequestMethod.POST)
    public String commentActionDx(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn, 
    @RequestParam("t")String t, @RequestParam("choice")String choice, @RequestParam("date2")String date2, 
    @RequestParam("writer2")String writer2, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc,  
    HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @RequestParam("headline")String headline, @RequestParam("cID")int cID, RedirectAttributes ra)
    {
        PreparedStatement ps=null;
        ResultSet rsX=null;
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        int kk=0;
            
        if(username!=null)
        {
            try
            {
                switch(choice)
                {
                    case "report":
                    {
                        String foul="";//to increment the report count
                        rsX=con.createStatement().executeQuery("SELECT * FROM blogcommentx WHERE commentid="+cID);
                        while(rsX.next())
                        {
                            foul=rsX.getString(7);
                            kk=rsX.getInt(8);                            
                        }
                        updateBlogComment(cID, ++kk);
                        String reportLink="<a style=\"font-weight: bold; text-align: center; color: purple\" href=\"handlerrequestcontroller?pg="
                        +pg+"&n="+n+"&pgn="+pgn+"&t="+t+"\">Report Link</a>";
                        String msg="<div style=\"font-weight: bold; font-size:22px; text-align: center; color: red\">"+headline+"</div>Reporting @"+writer2+"<br><br>"+ foul +"<br><br>"+ reportLink;
                        saveMsg(username, admin, reportLink, msg, new UtilityClass().getDate(), "New");
                        ra.addFlashAttribute("errorQQ", "Report will be investigated. Thank you!");
                        return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
                    }
                
                    case "remove":
                    {
                        rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                        while(rsAdminCheck.next())
                        {
                            if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                            && lastname.equals(rsAdminCheck.getString(3)))
                            {
                                ps=con.prepareStatement("UPDATE blogcommentx SET approval='DISABLED' WHERE commentid="+cID);
                                ps.executeUpdate();
                                return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
                            }
                        }
                        session.invalidate();
                        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
                        model.addAttribute("expired", "Try again!");
                        return "homepage";
                    }                
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    if(rsX!=null)
                    {
                        rsX.close();
                    }
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
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
        else
            ra.addFlashAttribute("errorQQ", "Please login to perform action!");
        return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
    }
    
    public void updateSubComment(int a, String b, int c)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("UPDATE commentx SET "+b+"="+c+" WHERE commentid="+a);
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void updateBlogComment(int a, int b)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("UPDATE blogcommentx SET report="+b+" WHERE commentid="+a);
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @RequestMapping(value="/submitQuote", method=RequestMethod.POST)
    public String submitquote(ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, 
    RedirectAttributes ra, @ModelAttribute("fourteenthBeanObject")NewsPostClass npc)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();        
        String username=(String)session.getAttribute("username");
        model.addAttribute("commenter", npc.getLink());
        model.addAttribute("writer", npc.getWriter());
        model.addAttribute("headline", npc.getNewsTitle());
        model.addAttribute("date", npc.getDate());
        model.addAttribute("pg", npc.getId());
        model.addAttribute("n", npc.getNmb());
        model.addAttribute("pgn", npc.getSpare());
        
        String xy1=new String(npc.getNewsPost().getBytes(ISO), UTF);
        
        String quotex1=npc.getNewsPost2().replaceAll("<_", "<br><br><img width='250' height='150' alt='descriptive image' src='resources/images/general_Images/");
        quotex1=quotex1.replaceAll("_>", "'/><br><br>");
        
        npc.setNewsPost2(new String(npc.getNewsPost2().getBytes(ISO), UTF));
        npc.setCheck(new String(npc.getCheck().getBytes(ISO), UTF));
        
        String quoteDetails=""
        + "<div style=\"overflow: auto; background-color: rgb(255,215,229); border-left: 3px solid pink; border-radius: 2px; margin-bottom: 5px;\">"
        + npc.getSpace()
        + "<div style=\"text-align: justify; padding: 5px;\">"+new String(quotex1.getBytes(ISO), UTF)+"</div>"
        + "</div>";
        
        if(username!=null)
        {            
            switch(npc.getChoice())
            {
                case "post_Story":
                {
                    int numberOfWords=wordCount(npc.getNewsPost());
                    if(npc.getNewsPost2().matches("\\s*"))
                    {
                        model.addAttribute("commentaryimage", "The quoted text cannot be empty.");
                        return "quotePage";
                    }
                    else if(!npc.getCheck().contains(npc.getNewsPost2().trim()))
                    {
                        model.addAttribute("commentaryimage", "Attempting to quote wrongly.");
                        return "quotePage";
                    }
                    else if(npc.getNewsPost().matches("\\s*"))
                    {
                        model.addAttribute("commentaryimage", "Cannot post an empty comment.");
                        return "quotePage";
                    }
                    else if(numberOfWords<1009)
                    {
                        xy1=xy1.replaceAll("<_", "<br><br><img width='250' height='150' alt='descriptive image' src='resources/images/general_Images/");
                        xy1=xy1.replaceAll("_>", "'/><br><br>");
                                                            
                        saveComment(npc.getNmb(), username, new UtilityClass().getDate(), xy1, quoteDetails, null, null);
                        ra.addFlashAttribute("commentary", "Posted!");
                        return "redirect:/getstory?pg="+npc.getId()+"&n="+npc.getNmb()+"&&pgn="+npc.getSpare();
                    }
                    else
                    {
                        model.addAttribute("commentaryimage", "Comment is too lengthy, should be 1000 words or less.");
                        return "quotePage";
                    }
                }
                
                case "add_Image":
                {
                    String imagePath=pathFromPropertiesFile+"/resources/images/general_Images";
                    MultipartFile imageFile1=npc.getFile1();
                    if(imageFile1.getSize()<250009 && imageFile1.getSize()!=0)
                    {
                        String file1=imageFile1.getOriginalFilename();                        
                        if(file1.length()<30)
                        {
                            if(file1.endsWith(".jpg") || file1.endsWith(".png") || file1.endsWith(".gif") || file1.endsWith(".jpeg")
                            || file1.endsWith(".JPG") || file1.endsWith(".PNG") || file1.endsWith(".GIF") || file1.endsWith(".JPEG") 
                            || file1.endsWith(".webp") || file1.endsWith(".WEBP"))
                            {
                                try
                                {
                                    String imageRef="<_" + file1 + "_>";
                                    String xx1=new String(npc.getNewsPost().getBytes(ISO), UTF);
                                    npc.setNewsPost(xx1 + imageRef);
                                    model.addAttribute("newsposted", file1+ " added successfully.");
                                    
                                    File pathFile1=new File(imagePath, file1);
                                    imageFile1.transferTo(pathFile1);
                                    model.addAttribute("commentaryimage", file1+" added!");
                                    return "quotePage";
                                }
                                catch (IOException | IllegalStateException ex)
                                {
                                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else
                            {
                                model.addAttribute("commentaryimage", "File format should be .jpg, .png, .gif or .webp");
                                return "quotePage";
                            }
                        }
                        else
                        {
                            model.addAttribute("commentaryimage", "Image name is lengthy!");
                                return "quotePage";
                        }
                    }
                    else if(imageFile1.getSize()==0)
                    {
                        model.addAttribute("commentaryimage", "You tried uploading an empty file!");
                        return "quotePage";
                    }
                    else
                    {
                        model.addAttribute("commentaryimage", "File size exceeded!");
                        return "quotePage";
                    }
                }
            }
        }
        else
        {
            session.invalidate();
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Try again!");
        }
        return "homepage";
    }
    
    public boolean validCheck(String input)
    {
        String[] inputX=input.split("\\s+");
        for(String cx:inputX)
        {
            char[] c=cx.toCharArray();
            for(char ch:c)
            {
                if(!Character.isLetterOrDigit(ch))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    @RequestMapping(value="/sch")
    public String search(@RequestParam("q")String username, @RequestParam("pg")int pg, @RequestParam("t")String table,  
    ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
        ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        try
        {
            List<Integer> cList=new LinkedList<>();
            List<String> posts=new LinkedList<>();
            List<String> date=new LinkedList<>();
            
            model.addAttribute("profile", username);
            model.addAttribute("pg", pg);
            int setoff=25;
            int nID=0;
            if(table.equals("newz"))
            {
                model.addAttribute("type", "Post(s)");
                if(pg==1)
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM newz WHERE username='"+username+"' AND approve='ENABLED' ORDER BY newsid DESC LIMIT 0,"+setoff);
                    while(rs.next())
                    {
                        nID=rs.getInt(1);
                        cList.add(nID);
                        date.add(rs.getString(3));
                        posts.add(rs.getString(5));
                    }
                    model.addAttribute("cList", cList);
                    model.addAttribute("date", date);
                    model.addAttribute("posts", posts);
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='sch?q="+username+"&t="+table+"&pg="+ ++pg+"'>Next</a>");
                    }
                }
                else
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM newz WHERE username='"+username+"' AND approve='ENABLED' ORDER BY newsid DESC LIMIT "+(pg-1)*setoff+","+setoff);            
                    while(rs.next())
                    {
                        nID=rs.getInt(1);
                        cList.add(nID);
                        date.add(rs.getString(3));
                        posts.add(rs.getString(5));
                    }
                    model.addAttribute("cList", cList);
                    model.addAttribute("date", date);
                    model.addAttribute("posts", posts);
                                                
                    model.addAttribute("prev", "<a style='text-decoration: none' href='sch?q="+username+"&t="+table+"&pg="+(pg-1)+"'>Previous</a>");
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='sch?q="+username+"&t="+table+"&pg="+ ++pg+"'>Next</a>");
                    }
                }
                return "profileposts1";
            }
            else if(table.equals("blogtable"))
            {
                setoff=12;
                List<String> tableColumn=new LinkedList<>();
                List<String> coverImageList=new LinkedList<>();
                List<String> likeList=new LinkedList<>();
                List<String> viewList=new LinkedList<>();
                
                model.addAttribute("type", "Articles");
                if(pg==1)
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE username='"+username+"' AND approve='ENABLED' ORDER BY postid DESC LIMIT 0,"+setoff);
                    while(rs.next())
                    {
                        nID=rs.getInt(1);
                        cList.add(nID);
                        tableColumn.add(rs.getString(3));
                        date.add(rs.getString(4));
                        posts.add(rs.getString(6));
                        coverImageList.add(rs.getString(8));                    
                        likeList.add(rs.getString(9));
                        viewList.add(rs.getString(10));
                    }
                    model.addAttribute("cList", cList);
                    model.addAttribute("tableColumn", tableColumn);
                    model.addAttribute("date", date);
                    model.addAttribute("posts", posts);
                    model.addAttribute("coverImage", coverImageList);                
                    model.addAttribute("like", likeList);
                    model.addAttribute("view", viewList);
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='sch?q="+username+"&t="+table+"&pg="+ ++pg+"'>Next</a>");
                    }
                }                    
                else
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE username='"+username+"' AND approve='ENABLED' ORDER BY postid DESC LIMIT "+(pg-1)*setoff+","+setoff);
                    while(rs.next())
                    {
                        nID=rs.getInt(1);
                        cList.add(nID);
                        tableColumn.add(rs.getString(3));
                        date.add(rs.getString(4));
                        posts.add(rs.getString(6));
                        coverImageList.add(rs.getString(8));                    
                        likeList.add(rs.getString(9));
                        viewList.add(rs.getString(10));
                    }
                    model.addAttribute("cList", cList);
                    model.addAttribute("tableColumn", tableColumn);
                    model.addAttribute("date", date);
                    model.addAttribute("posts", posts);
                    model.addAttribute("coverImage", coverImageList);                
                    model.addAttribute("like", likeList);
                    model.addAttribute("view", viewList);
                                                
                    model.addAttribute("prev", "<a style='text-decoration: none' href='sch?q="+username+"&t="+table+"&pg="+(pg-1)+"'>Previous</a>");
                    
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='sch?q="+username+"&t="+table+"&pg="+ ++pg+"'>Next</a>");
                    }
                }
                return "profileposts2";
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
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
        return "profileposts1";
    }
}