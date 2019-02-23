package com.teamwurkz.web;

import com.teamwurkz.domain.AdClass;
import com.teamwurkz.domain.AdminControlClass;
import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.FileUploadClass;
import com.teamwurkz.domain.FreelanceBloggerClass;
import com.teamwurkz.domain.GeneralInfoClass;
import com.teamwurkz.domain.InsertClass;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.NewAdminClass;
import com.teamwurkz.domain.PasswordChangeClass;
import com.teamwurkz.domain.SearchPersonClass;
import com.teamwurkz.domain.WorkOnFile;
import com.teamwurkz.domain.WriteUs;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController
{
    @Autowired
    SignUpController succ;    
    @Autowired
    MessagingController msc;    
    @Value("${imagepath.proploc}")
    String pathFromPropertiesFile;    
    @Value("${admin.name}")
    String admin;
    private Context ctx;
    private DataSource ds;
    private Connection con;
    
    public AdminController()
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
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    //perfect optimization 1
    @RequestMapping(value="/adminControl")
    public String getHomePage(@ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
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
                        String[] niche={"WRITING", "ARTS", "HISTORY", "LITERATURE", "SPORTS", "HEALTH", "DIY", "ENTERTAINMENT", "RELIGION", "POLITICS", "UPDATES", "FASHION", "LIFESTYLE", "TRENDS"};
                        session.setAttribute("categoryList", niche);
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("sixthBeanObject", new InsertClass());                
                        session.setAttribute("clients", msc.adminList());                
                        session.setAttribute("peopleOnline", Users.getUsers());            
                        return "adminpage";
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
    
    //perfect optimization 2
    @RequestMapping(value="/takewords", method=RequestMethod.POST)
    public String takeWords(@ModelAttribute("sixthBeanObject")InsertClass ic, ModelMap model, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc,
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("thirdBeanObject")FileUploadClass fuc, HttpServletRequest req, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        PreparedStatement ps=null;
        
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
                        String word=ic.getWord().toLowerCase().trim();
                        String moreWords=ic.getSynonym().trim();        
                        switch (ic.getAction()) 
                        {
                            case "add":
                                if(!word.matches("\\s*") && !moreWords.matches("\\s*"))
                                {
                                    try
                                    {
                                        rs=con.createStatement().executeQuery("SELECT word FROM contextdatabase");                                
                                        while(rs.next())
                                        {                                    
                                            if(word.equalsIgnoreCase(rs.getString(1)))
                                            {
                                                model.addAttribute("error1", "'"+word+"'" +" already exists.");
                                                return "adminpage";
                                            }
                                        }                                
                                        saveWords(word, moreWords.toLowerCase());
                                        model.addAttribute("error1", "'"+word+"'" +" saved successfully..");
                                    }                            
                                    catch (SQLException ex)
                                    {
                                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }                        
                                else
                                {
                                    model.addAttribute("error1", "Enter required fields.");
                                }
                                break;
                        
                            case "delete":
                                if(!word.matches("\\s*"))
                                {
                                    try
                                    {
                                        rs=con.createStatement().executeQuery("SELECT word FROM contextdatabase");                                
                                        while(rs.next())
                                        {                                                
                                            if(word.equalsIgnoreCase(rs.getString(1)))
                                            {
                                                ps=con.prepareStatement("DELETE FROM contextdatabase WHERE word='"+ word +"'");
                                                ps.executeUpdate();
                                                model.addAttribute("error1", "'"+word+"'" +" deleted");
                                                return "adminpage";
                                            }
                                        }                                
                                        model.addAttribute("error1", "'"+word+"'" +" not in record");
                                        return "adminpage";
                                    }                            
                                    catch (SQLException ex)
                                    {
                                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }                        
                                else
                                {
                                    model.addAttribute("error1", "Enter required field.");
                                }
                                break;
                        
                            case "multiple":
                                if(!moreWords.matches("\\s*"))
                                {                            
                                    try
                                    {
                                        String[] dexter=moreWords.split(";");
                                        for(String singleQuery:dexter)
                                        {
                                            ps=con.prepareStatement(singleQuery);
                                            ps.executeUpdate();
                                            model.addAttribute("error1", "Words added successfully..");
                                        }                                
                                        return "adminpage";
                                    }                                    
                                    catch (SQLException ex)
                                    {
                                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                else
                                {
                                    model.addAttribute("error1", "Enter query.");
                                }
                                break;
                                
                            case "phraseSingle":
                                if(!word.matches("\\s*") && !moreWords.matches("\\s*"))
                                {
                                    try
                                    {
                                        rs=con.createStatement().executeQuery("SELECT phrases FROM phrasereplacement");                                
                                        while(rs.next())
                                        {                                    
                                            if(word.equalsIgnoreCase(rs.getString(1)))
                                            {
                                                model.addAttribute("error1", "'"+word+"'" +" already exists.");
                                                return "adminpage";
                                            }
                                        }                                
                                        savePhrases(word, moreWords.toLowerCase());
                                        model.addAttribute("error1", "'"+word+"'" +" saved successfully..");
                                    }                            
                                    catch (SQLException ex)
                                    {
                                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }                        
                                else
                                {
                                    model.addAttribute("error1", "Enter phrase/clause.");
                                }
                                break;
                        
                            case "deletePhrase":
                                if(!word.matches("\\s*"))
                                {
                                    try
                                    {
                                        rs=con.createStatement().executeQuery("SELECT phrases FROM phrasereplacement");                                
                                        while(rs.next())
                                        {                                                
                                            if(word.equalsIgnoreCase(rs.getString(1)))
                                            {                                        
                                                ps=con.prepareStatement("DELETE FROM phrasereplacement WHERE phrases='"+ word +"'");
                                                ps.executeUpdate();
                                                model.addAttribute("error1", "'"+word+"'" +" deleted");
                                                return "adminpage";
                                            }
                                        }                                
                                        model.addAttribute("error1", "'"+word+"'" +" not in record.");
                                        return "adminpage";
                                    }                            
                                    catch (SQLException ex)
                                    {
                                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }                        
                                else
                                {
                                    model.addAttribute("error1", "Enter required field.");
                                }
                                break;
                        
                            case "phraseMultiple":
                                if(!moreWords.matches("\\s*"))
                                {                            
                                    try
                                    {
                                        String[] dexterEzeigbo=moreWords.split(";");                                            
                                        for(String singleQuery:dexterEzeigbo)
                                        {
                                            ps=con.prepareStatement(singleQuery);
                                            ps.executeUpdate();
                                            model.addAttribute("error1", "Phrase(s) added successfully..");
                                        }                                
                                        return "adminpage";
                                    }                                    
                                    catch (SQLException ex)
                                    {
                                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                else
                                {
                                    model.addAttribute("error1", "Enter query.");
                                }
                                break;
                        }
                        return "adminpage";
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
    private void saveWords(String a, String b)
    {
        PreparedStatement ps=null;
        
        try
        {
            ps=con.prepareStatement("INSERT INTO contextdatabase (word, synonym) VALUES(?,?)");
            ps.setString(1, a);
            ps.setString(2, b);
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
    
    private void savePhrases(String a, String b)
    {
        PreparedStatement ps=null;
        try
        {
            ps=con.prepareStatement("INSERT INTO phrasereplacement (phrases, replacement) VALUES(?,?)");
            ps.setString(1, a);
            ps.setString(2, b);
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
    //perfect optimization 3
    @RequestMapping(value="/adminupload", method=RequestMethod.POST)
    public String adminUploadedFile(@ModelAttribute("thirdBeanObject") FileUploadClass fuc, HttpServletRequest req, 
    @ModelAttribute("sixthBeanObject")InsertClass ic, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpSession session)
    {
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
                        String fileToString="";
                        try
                        {
                            MultipartFile uploadedFile=fuc.getFile();
                            String nameOfFile=uploadedFile.getOriginalFilename();
                            ByteArrayInputStream fileStream=new ByteArrayInputStream(uploadedFile.getBytes());            
                            
                            if((nameOfFile.endsWith(".pdf") || nameOfFile.endsWith(".docx") || 
                            nameOfFile.endsWith(".doc") || nameOfFile.endsWith(".txt")))// && uploadedFile.getSize()==0)
                            {
                                if(uploadedFile.getSize()==0)
                                {
                                    model.addAttribute("error1", "Empty file.");
                                }
                                else if(uploadedFile.getSize()<=5000000)//5MB
                                {
                                    if(nameOfFile.endsWith(".pdf"))
                                    {
                                        PDDocument pdf=PDDocument.load(fileStream);
                                        PDFTextStripper strippingThePDF=new PDFTextStripper();
                                        fileToString=strippingThePDF.getText(pdf);
                                    }
                                    else if(nameOfFile.endsWith(".docx"))
                                    {
                                        XWPFDocument doc=new XWPFDocument(fileStream);
                                        POITextExtractor ext=new XWPFWordExtractor(doc);                        
                                        fileToString =ext.getText();
                                    }
                                    else if(nameOfFile.endsWith(".doc"))
                                    {
                                        HWPFDocument doc=new HWPFDocument(fileStream);
                                        WordExtractor we=new WordExtractor(doc);
                                        fileToString=we.getText();
                                    }
                                    else if(nameOfFile.endsWith(".txt"))
                                    {
                                        fileToString=IOUtils.toString(fileStream, "UTF-8");
                                    }
                                }
                                else
                                {
                                    model.addAttribute("error1", "File size exceeded!");
                                }
                            }
                            else
                            {
                                fileToString="File format not supported";
                                model.addAttribute("error1", fileToString);
                            }
                            ic.setSynonym(fileToString);
                            return "adminpage";
                        }                
                        catch (IOException ex)
                        {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }        
                        ic.setSynonym(fileToString);
                        return "adminpage";
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
    //perfect optimization 4
    @RequestMapping(value="/manipulate")
    public String manipulate(HttpServletRequest req, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fifteenthBeanObject")AdminControlClass accs, @ModelAttribute("eightBeanObject") WriteUs wu,
    @ModelAttribute("seventhBeanObject") WorkOnFile wof, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, RedirectAttributes ra, 
    HttpSession session, @RequestParam("pg")int pgN)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        PreparedStatement ps=null;
        
        int currentPage=pgN;
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
                        try 
                        {
                            String name=accs.getName();              
                            String title=accs.getTitle();
                            //String date=accs.getDate();
                            model.addAttribute("unreadMessages", succ.getUnreadCount(admin));                
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers");
                            while(rs.next())
                            {
                                switch(accs.getChoice())
                                {
                                    case "write":
                                    {
                                        wu.setSendTo(name);
                                        return "adminMessagePage";
                                    }
                                    
                                    case "wck":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("error", "Cannot Execute Command");
                                            return "redirect:sumpisnews?pg="+currentPage;
                                        }
                                        else
                                        {
                                            String replyTitle="RE: " + title;
                                            //String description="Posted by @" + name + " on: " + date;
                                            wu.setSubject(replyTitle);
                                            //wu.setDescription(description);
                                            wu.setSendTo(name);
                                            return "adminMessagePage";
                                        }
                                    }
                    
                                    case "sumpissuspend":
                                    {
                                        if(name.equals(rs.getString(1)))
                                        {
                                            if(rs.getString(6)==null && rs.getString(7)==null)
                                            {
                                                try
                                                {
                                                    ps=con.prepareStatement("UPDATE registeredusers SET sumpissuspend='suspended', "
                                                    + "newspostsuspend='suspended' WHERE username='" + name + "'");
                                                    ps.executeUpdate();
                                                    ra.addFlashAttribute("friendRequest", "@" + name + " has been completely barred.");
                                                    return "redirect:/profile?ux="+name;
                                                }                    
                                                catch (SQLException ex)
                                                {
                                                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }                                    
                                            else if(rs.getString(6)==null && rs.getString(7).equals("suspended"))
                                            {
                                                try
                                                {
                                                    ps=con.prepareStatement("UPDATE registeredusers SET sumpissuspend='suspended' "
                                                    + "WHERE username='" + name + "'");
                                                    ps.executeUpdate();
                                                    ra.addFlashAttribute("friendRequest", "@" + name + " has been completely barred.");
                                                    return "redirect:/profile?ux="+name;
                                                }                    
                                                catch (SQLException ex)
                                                {
                                                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    }
                                    break;
                            
                                    case "ssk":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("error", "Cannot Execute Command");
                                            return "redirect:/sumpisnews?pg="+currentPage;
                                        }                                
                                        else
                                        {
                                            if(rs.getString(6)==null && rs.getString(7)==null)
                                            {
                                                try
                                                {
                                                    ps=con.prepareStatement("UPDATE registeredusers SET sumpissuspend='suspended', "
                                                    + "newspostsuspend='suspended' WHERE username='" + name + "'");
                                                    ps.executeUpdate();                                            
                                                    ra.addFlashAttribute("error", "@" + name + " has been completely barred.");
                                                    return "redirect:/sumpisnews?pg="+currentPage;
                                                }                    
                                                catch (SQLException ex)
                                                {
                                                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }                                    
                                            else if(rs.getString(6)==null && rs.getString(7).equals("suspended"))
                                            {
                                                try
                                                {
                                                    ps=con.prepareStatement("UPDATE registeredusers SET sumpissuspend='suspended' "
                                                    + "WHERE username='" + name + "'");
                                                    ps.executeUpdate();
                                                    
                                                    ra.addFlashAttribute("error", "@" + name + " has been completely barred.");
                                                    return "redirect:/sumpisnews?pg="+currentPage;
                                                }                    
                                                catch (SQLException ex)
                                                {
                                                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    }
                                    break;
                
                                    case "newssuspend":
                                    {
                                        try
                                        {
                                            ps=con.prepareStatement("UPDATE registeredusers SET newspostsuspend='suspended' "
                                            + "WHERE username='" + name + "'");
                                            ps.executeUpdate();
                                            ra.addFlashAttribute("friendRequest", "@" + name + " has been barred from posting stories.");
                                            return "redirect:/profile?ux="+name;
                                        }                    
                                        catch (SQLException ex)
                                        {
                                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                        }                                                               
                                    }
                                    break;
                            
                                    case "nsk":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("error", "Cannot Execute Command");
                                            return "redirect:/sumpisnews?pg="+currentPage;
                                        }
                                        else
                                        {
                                            try
                                            {
                                                ps=con.prepareStatement("UPDATE registeredusers SET newspostsuspend='suspended' "
                                                + "WHERE username='" + name + "'");
                                                ps.executeUpdate();                                
                                                ra.addFlashAttribute("error", "@" + name + " has been barred from posting stories.");
                                                return "redirect:/sumpisnews?pg="+currentPage;
                                            }                    
                                            catch (SQLException ex)
                                            {
                                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }                                
                                    }
                                    break;
                    
                                    case "redspend":
                                    {
                                        try
                                        {
                                            ps=con.prepareStatement("UPDATE registeredusers SET sumpissuspend=null "
                                            + "WHERE username='" + name + "'");
                                            ps.executeUpdate();
                                            ra.addFlashAttribute("friendRequest", "Ban has been lifted on @" + name + ".");
                                            return "redirect:/profile?ux="+name;
                                        }                    
                                        catch (SQLException ex)
                                        {
                                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    break;
                            
                                    case "rsk":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("error", "Sorry, Cannot Execute Command");
                                            return "redirect:/sumpisnews?pg="+currentPage;
                                        }                                
                                        else
                                        {
                                            try
                                            {
                                                ps=con.prepareStatement("UPDATE registeredusers SET sumpissuspend=null "
                                                + "WHERE username='" + name + "'");
                                                ps.executeUpdate();                                
                                                ra.addFlashAttribute("error", "Ban has been lifted on @" + name + ".");
                                                return "redirect:/sumpisnews?pg="+currentPage;
                                            }                    
                                            catch (SQLException ex)
                                            {
                                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                    break;
                        
                                    case "newsunsuspend":
                                    {
                                        try
                                        {
                                            ps=con.prepareStatement("UPDATE registeredusers SET newspostsuspend=null "
                                            + "WHERE username='" + name + "'");
                                            ps.executeUpdate();                                        
                                            ra.addFlashAttribute("friendRequest", "@" + name + " can now post stories.");
                                            return "redirect:/profile?ux="+name;
                                        }                                            
                                        catch (SQLException ex)
                                        {
                                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    break;

                                    case "usk":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("error", "Cannot Execute Command");
                                            return "redirect:/sumpisnews?pg="+currentPage;
                                        }                                
                                        else
                                        {
                                            try
                                            {
                                                ps=con.prepareStatement("UPDATE registeredusers SET newspostsuspend=null "
                                                + "WHERE username='" + name + "'");
                                                ps.executeUpdate();                                        
                                                ra.addFlashAttribute("error", "@" + name + " can now post stories.");
                                                return "redirect:/sumpisnews?pg="+currentPage;
                                            }                    
                                            catch (SQLException ex)
                                            {
                                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }                                
                                    }
                                    break;
                                    
                                    case "pix":
                                    {
                                        try
                                        {
                                            ps=con.prepareStatement("UPDATE registeredusers SET pix='empty.png' "
                                            + "WHERE username='" + name + "'");
                                            ps.executeUpdate();                                        
                                            ra.addFlashAttribute("error", "Profile pix removed!");
                                            return "redirect:/profile?ux="+name;
                                        }                    
                                        catch (SQLException ex)
                                        {
                                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            }                
                        }            
                        catch (SQLException ex)
                        {
                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
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
    //perfect optimization 5
    @RequestMapping(value="/getUsers")
    public String adminGetUsers(HttpServletRequest req, ModelMap model, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, HttpSession session, @RequestParam("us")int userz)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsREP=null;
        List<String> dataList=new LinkedList<>();
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            try
            {
                int setoff=300;
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        int kx=0;
                        int subscribers=0;
                        rsREP=con.createStatement().executeQuery("SELECT * FROM registeredusers");
                        while(rsREP.next())
                        {
                            ++kx;
                            
                            Date subDate=succ.dateSuffix(rsREP.getString(9));
                            if(new Date().compareTo(subDate)==-1)
                            {
                                ++subscribers;
                            }
                        }
                        model.addAttribute("totalusers", kx);
                        model.addAttribute("subscribers", subscribers);
                        
                        if(userz==1)
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers LIMIT 0," + setoff);
                            String xx="";
                            while(rs.next())
                            {
                                for(int i=1; i<11; i++) 
                                {
                                    dataList.add(rs.getString(i));
                                }
                                xx=rs.getString(1);
                            }
                            
                            if(!xx.equals(""))
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='getUsers?us="+ ++userz +"'>Next</a>");
                            }
                            model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                            model.addAttribute("users", dataList);
                            return "userspage";
                        }
                        else
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers LIMIT " + (userz-1)*setoff + ", " + setoff);
                            String xx="";
                            while(rs.next())
                            {
                                for(int i=1; i<11; i++) 
                                {
                                    dataList.add(rs.getString(i));
                                }
                                xx=rs.getString(1);
                            }

                            model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                            model.addAttribute("users", dataList);
                            model.addAttribute("prev", "<a style='text-decoration: none' href='getUsers?us="+ (userz-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                            
                            if(!xx.equals(""))
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='getUsers?us="+ ++userz +"'>Next</a>");
                            }
                            return "userspage";
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
                    if(rs!=null)
                    {
                        rs.close();
                    }
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
                    }
                    if(rsREP!=null)
                    {
                        rsREP.close();
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
    //perfect optimization 6: message to every people
    @RequestMapping(value="/generalinfo", method=RequestMethod.GET)
    public String generalInfo(HttpServletRequest req, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    HttpSession session, @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
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
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("eighteenthBeanObject", new GeneralInfoClass());
                        model.addAttribute("nineteenthBeanObject", new NewAdminClass());
                        model.addAttribute("display", "none");
                        return "adminInfoPage";
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
    //optimized 7
    @RequestMapping(value="/notifyallusers")//messages to all registered ppl
    public String notifyAll(HttpServletRequest req, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("eighteenthBeanObject")GeneralInfoClass gic, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("nineteenthBeanObject")NewAdminClass nac, HttpSession session)
    {
        ResultSet rsAdminCheck=null;
        PreparedStatement ps2=null;
        
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
                        String title=gic.getTitle();
                        String info=gic.getInfo().trim();
                        String action=gic.getAction();
                        
                        switch (action)
                        {
                            case "inform":
                            {
                                if(info.matches("\\s*"))
                                {
                                    model.addAttribute("info", "Please write something.");
                                }
                                else if(!info.matches("\\s*"))
                                {
                                    saveInfo(title, info);
                                    model.addAttribute("info", "Information disseminated.");
                                }
                                break;
                            }
                            
                            case "delete":
                            {
                                ps2=con.prepareStatement("DELETE FROM generalinformation");
                                ps2.executeUpdate();
                                model.addAttribute("info", "Old information successfully removed.");
                                break;
                            }
                        }
                        return "adminInfoPage";
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
                    if(ps2!=null)
                    {
                        ps2.close();
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
    //optimized 8
    @RequestMapping(value="/addNewAdmin")
    public String addNewAdmin(HttpServletRequest req, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("eighteenthBeanObject")GeneralInfoClass gic, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("nineteenthBeanObject")NewAdminClass nac, HttpSession session)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsREP=null;
        PreparedStatement ps3=null;
        
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
                        String newAdminFirstName=nac.getFirstName().trim();
                        String newAdminLastName=nac.getLastName().trim();
                        String newAdminUserName=nac.getUserName().trim();
                        String newAdminMobile=nac.getMobile().trim();
                        String newAdminPassword=nac.getPassword();                        
                        switch(nac.getAction())
                        {
                            case "add":
                            if(!newAdminFirstName.matches("\\s*") && !newAdminLastName.matches("\\s*") 
                            && !newAdminMobile.matches("\\s*") && !newAdminUserName.matches("\\s*") &&
                            !newAdminPassword.matches("\\s*"))
                            {
                                try
                                {
                                    rs=con.createStatement().executeQuery("SELECT * FROM admintable");
                                    while(rs.next())
                                    {
                                        String userNameCheck=rs.getString(1);
                                        String mobileCheck=rs.getString(4);
                                        if(newAdminUserName.equalsIgnoreCase(userNameCheck))
                                        {
                                            model.addAttribute("info", "Username already exists. Please try a different username");
                                            return "adminInfoPage";
                                        }
                                        if(newAdminPassword.length()<8)
                                        {
                                            model.addAttribute("info", "Password must be 8 characters or more.");
                                            return "adminInfoPage";
                                        }
                                        if(newAdminMobile.equals(mobileCheck))
                                        {
                                            model.addAttribute("info", "Mobile number is already registered. Please use a different mobile number");
                                            return "adminInfoPage";
                                        }
                                        if(!newAdminMobile.matches("[+][1-9][0-9]*[1-9]\\d{2}[1-9]\\d{6}"))
                                        {
                                            model.addAttribute("info", "Invalid mobile number.");
                                            return "adminInfoPage";
                                        }
                                    }
                                    
                                    rsREP=con.createStatement().executeQuery("SELECT username FROM registeredusers");
                                    while(rsREP.next())
                                    {
                                        String memberUsername=rsREP.getString(1);
                                        if(newAdminUserName.equalsIgnoreCase(memberUsername))
                                        {
                                            model.addAttribute("info", "Username already exists for a registered member. Please try a different username");
                                            return "adminInfoPage";
                                        }
                                    }
                                        
                                    saveNewAdmin(newAdminUserName, newAdminFirstName, newAdminLastName, newAdminMobile, newAdminPassword);
                                    model.addAttribute("info", "New Admin added.");
                                    return "adminInfoPage";
                                }                                        
                                catch (SQLException ex)
                                {
                                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else
                            {
                                model.addAttribute("info", "Please enter all fields. Thank you!");
                                return "adminInfoPage";
                            }                            
                            
                            case "delete":
                            {
                                if(!newAdminUserName.matches("\\s*"))
                                {
                                    if(!newAdminUserName.equals(admin))
                                    {
                                        rs=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='"+newAdminUserName+"'");
                                        while(rs.next())
                                        {
                                            if(newAdminUserName.equals(rs.getString(1)))
                                            {
                                                ps3=con.prepareStatement("DELETE FROM admintable WHERE username='"+newAdminUserName+"'");
                                                ps3.executeUpdate();
                                                model.addAttribute("info", "Admin removed.");
                                                return "adminInfoPage";
                                            }
                                        }
                                        model.addAttribute("info", "No admin by this username.");
                                        return "adminInfoPage";                                            
                                    }
                                    else
                                    {
                                        model.addAttribute("info", "Cannot execute command!");
                                        return "adminInfoPage";
                                    }
                                }
                                else
                                {
                                    model.addAttribute("info", "Please enter the admin's username you want to remove in the username input field.");
                                    return "adminInfoPage";    
                                }
                            }
                            
                            case "adminlist":
                            try
                            {
                                List<String> forUsername=new LinkedList<>();
                                List<String> forFirstname=new LinkedList<>();
                                List<String> forLastname=new LinkedList<>();
                                List<String> forMobile=new LinkedList<>();
                                List<String> forPassword=new LinkedList<>();
                                rs=con.createStatement().executeQuery("SELECT * FROM admintable");
                                while(rs.next())
                                {
                                    model.addAttribute("headermain", "Admin List");
                                    model.addAttribute("header1", "Admin Username");
                                    model.addAttribute("header2", "Firstname");
                                    model.addAttribute("header3", "Lastname");
                                    model.addAttribute("header4", "Mobile");
                                    model.addAttribute("header5", "Admin Password");
                                    model.addAttribute("display", "inline");
                                    
                                    if(rs.getString(1).equals(admin) && !username.equals(admin))//only main admin can see main admin, other admins cannnot see main admin so as not to tamper with things
                                    {
                                        continue;
                                    }
                                    forUsername.add(rs.getString(1));
                                    forFirstname.add(rs.getString(2));
                                    forLastname.add(rs.getString(3));
                                    forMobile.add(rs.getString(4));
                                    forPassword.add(rs.getString(5));                                        
                                }
                                model.addAttribute("forUsername", forUsername);
                                model.addAttribute("forFirstname", forFirstname);
                                model.addAttribute("forLastname", forLastname);
                                model.addAttribute("forMobile", forMobile);
                                model.addAttribute("forPassword", forPassword);
                                return "adminInfoPage";
                            }
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
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
                    if(rs!=null)
                    {
                        rs.close();
                    }
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
                    }
                    if(rsREP!=null)
                    {
                        rsREP.close();
                    }
                    if(ps3!=null)
                    {
                        ps3.close();
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
    
    private void saveInfo(String a, String b)
    {
        PreparedStatement ps2=null;
        
        try
        {
            ps2=con.prepareStatement("INSERT INTO generalinformation (title, information) VALUES(?,?)");
            ps2.setString(1, a);
            ps2.setString(2, b);
            ps2.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
            {
                try
                {
                    if(ps2!=null)
                    {
                        ps2.close();
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    private void saveNewAdmin(String a, String b, String c, String d, String e)
    {
        PreparedStatement ps3=null;
        
        try
        {
            ps3=con.prepareStatement("INSERT INTO admintable (username, firstname, lastname, mobile, password) VALUES(?,?,?,?,?)");
            ps3.setString(1, a);
            ps3.setString(2, b);
            ps3.setString(3, c);
            ps3.setString(4, d);
            ps3.setString(5, e);            
            ps3.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
            {
                try
                {
                    if(ps3!=null)
                    {
                        ps3.close();
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    //optimized 9
    @RequestMapping(value="/adminpasswordchange")
    public String adminPasswordChange(ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session)
    {
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
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("twentythirdBeanObject", new PasswordChangeClass());
                        return "adminpasswordupdate";
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
    //optimized 10
    @RequestMapping(value="/adminpasswordchangecontroller")
    public String adminPasswordChangeController(ModelMap model,HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("twentythirdBeanObject")PasswordChangeClass pcc, 
    @ModelAttribute("sixthBeanObject")InsertClass ic, HttpSession session)
    {
        ResultSet rsAdminCheck=null;
        PreparedStatement psAdminPasswordUpdate=null;
        
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
                        String userX=pcc.getUserName().trim();
                        String oldX=pcc.getOldPassword();
                        String newPass1=pcc.getNewPassword1();
                        String newPass2=pcc.getNewPassword2();                        
                        switch (pcc.getAction())
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
                                                        if(userX.equals(username))
                                                        {
                                                            if(oldX.equals(password))
                                                            {
                                                                session.setAttribute("password", newPass1);
                                                                psAdminPasswordUpdate=con.prepareStatement("UPDATE admintable SET "
                                                                + "password='"+newPass1+"' WHERE username='"+userX+"'");
                                                                psAdminPasswordUpdate.executeUpdate();                                                            
                                                                model.addAttribute("passwordinfo", "Password updated successfully");
                                                                return "adminpasswordupdate";
                                                            }
                                                            else
                                                            {
                                                                model.addAttribute("passwordinfo", "Current password did not match existing password.");
                                                                return "adminpasswordupdate";
                                                            }
                                                        }
                                                        else
                                                        {
                                                            model.addAttribute("passwordinfo", "Username did not match.");
                                                            return "adminpasswordupdate";
                                                        }                                                        
                                                    }
                                                    else
                                                    {
                                                        model.addAttribute("passwordinfo", "New passwords did not match.");
                                                        return "adminpasswordupdate";
                                                    }
                                                }
                                                else
                                                {
                                                    model.addAttribute("passwordinfo", "Please confirm new password");
                                                    return "adminpasswordupdate";
                                                }
                                            }
                                            else
                                            {
                                                model.addAttribute("passwordinfo", "Please enter new password");
                                                return "adminpasswordupdate";
                                            }
                                        }
                                        else
                                        {
                                            model.addAttribute("passwordinfo", "Please enter current password");
                                            return "adminpasswordupdate";
                                        }
                                }
                                else
                                {
                                    model.addAttribute("passwordinfo", "Please enter username");
                                    return "adminpasswordupdate";
                                }
                            }                            
                            case "cancel":
                            {
                                return "adminpage";
                            }
                        }                        
                        return "adminpasswordupdate";
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
                    if(psAdminPasswordUpdate!=null)
                    {
                        psAdminPasswordUpdate.close();
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
    //optimized 11
    @RequestMapping(value="/switcher")
    public String switcher(ModelMap model, HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, HttpSession session, RedirectAttributes ra)
    {
        ResultSet rsAdminCheck=null;
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        
        
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
                        return "redirect:/adminControl";
                    }
                }
                ra.addFlashAttribute("adid", ad.get(0));
                ra.addFlashAttribute("adimg", ad.get(1));
                return "redirect:/registeredMemberControl";
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
    
    //special for Ajax purpose (checking username on the fly for new admin registration)
    @RequestMapping(value="/adminUsernameCheck")
    @ResponseBody
    public String ajaxResponse(@RequestParam("sent")String admin_Username)
    {
        ResultSet rs=null;
        
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM admintable");                    
            while(rs.next())
            {                        
                if(admin_Username.equalsIgnoreCase(rs.getString(1)))
                {
                    return "exists";
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
    
    @RequestMapping(value="/freelanceController")
    public String freelancePage(ModelMap model, HttpSession session, HttpServletRequest req, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            String adminCrossCheck="SELECT * FROM admintable WHERE username='" + username + "'";
            try
            {
                rsAdminCheck=con.createStatement().executeQuery(adminCrossCheck);
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("eleventhBeanObject", new SearchPersonClass());
                        model.addAttribute("freelanceBloggerClass", new FreelanceBloggerClass());
                        return "freelance";
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
    
    @RequestMapping(value="/blogPostHandler")
    public String blogPostHandler(@ModelAttribute("freelanceBloggerClass")FreelanceBloggerClass fbc,
    ModelMap model, HttpSession session, HttpServletRequest req, RedirectAttributes ra, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsFreelanceCheck=null;
        PreparedStatement psFreelanceSave=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            String adminCrossCheck="SELECT * FROM admintable WHERE username='" + username + "'";
            try
            {
                rsAdminCheck=con.createStatement().executeQuery(adminCrossCheck);
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        String nameUser=fbc.getUsername().trim();
                        String[] categoryX=fbc.getCategory();
                        String category="";
                        
                        for(String count:categoryX)
                        {
                            category +=count +" ";
                        }
                        
                        if(!nameUser.matches("\\s+") && !nameUser.equals(""))
                        {
                            switch(fbc.getChoice())
                            {
                                case "add":
                                {
                                    rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+nameUser+"'");
                                    while(rs.next())
                                    {
                                        if(rs.getString(6)==null && rs.getString(7)==null)
                                        {
                                            if(!category.matches("\\s+") && !category.equals(""))
                                            {
                                                rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                                while(rsFreelanceCheck.next())
                                                {
                                                    model.addAttribute("errorXX", nameUser+" already exists in database.");
                                                    return "freelance";
                                                }
                                                saveToFreelanceTable(nameUser, category);
                                                updateRegUser_Freelance(nameUser, category);
                                                model.addAttribute("errorXX", nameUser+" added successfully.");
                                                return "freelance";
                                            }
                                            else
                                            {
                                                model.addAttribute("errorXX", "Enter category.");
                                                return "freelance";
                                            }
                                        }
                                    }
                                    model.addAttribute("errorXX", "Username not in database or suspended/banned.");
                                    return "freelance";
                                }
                                //break;
                                case "edit":
                                {
                                    if(!category.matches("\\s+") && !category.equals(""))
                                    {
                                        rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                        while(rsFreelanceCheck.next())
                                        {
                                            psFreelanceSave=con.prepareStatement("UPDATE freelancetable SET category='"+category+"' WHERE username='"+nameUser+"'");
                                            psFreelanceSave.executeUpdate();
                                                
                                            updateRegUser_Freelance(nameUser, category);
                                            model.addAttribute("errorXX", "Category updated successfully.");
                                            return "freelance";
                                        }
                                        model.addAttribute("errorXX", nameUser+" does not exist.");
                                        return "freelance";                                        
                                    }
                                    else
                                    {
                                        model.addAttribute("errorXX", "Enter category.");
                                        return "freelance";
                                    }
                                }
                                
                                case "ban":
                                {
                                    rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                    while(rsFreelanceCheck.next())
                                    {
                                        psFreelanceSave=con.prepareStatement("UPDATE freelancetable SET ban='BANNED' WHERE username='"+nameUser+"'");
                                        psFreelanceSave.executeUpdate();
                                            
                                        updateRegUser_Freelance(nameUser, "No");
                                        model.addAttribute("errorXX", nameUser+" has been banned.");
                                        return "freelance";
                                    }
                                    model.addAttribute("errorXX", nameUser+" does not exist.");
                                    return "freelance";
                                }
                                
                                case "block":
                                {
                                    rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                    while(rsFreelanceCheck.next())
                                    {
                                        psFreelanceSave=con.prepareStatement("UPDATE freelancetable SET block='BLOCKED' WHERE username='"+nameUser+"'");
                                        psFreelanceSave.executeUpdate();
                                            
                                        updateRegUser_Freelance(nameUser, "No");
                                        model.addAttribute("errorXX", nameUser+" has been blocked.");
                                        return "freelance";
                                    }
                                    model.addAttribute("errorXX", nameUser+" does not exist.");
                                    return "freelance";
                                }
                                
                                case "unban":
                                {
                                    rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                    while(rsFreelanceCheck.next())
                                    {
                                        psFreelanceSave=con.prepareStatement("UPDATE freelancetable SET ban='' WHERE username='"+nameUser+"'");
                                        psFreelanceSave.executeUpdate();
                                            
                                        updateRegUser_Freelance(nameUser, rsFreelanceCheck.getString(5));
                                        model.addAttribute("errorXX", nameUser+" has been unbanned.");
                                        return "freelance";                                                
                                        
                                    }
                                    model.addAttribute("errorXX", nameUser+" does not exist.");
                                    return "freelance";
                                }
                                
                                case "unblock":
                                {
                                    rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                    while(rsFreelanceCheck.next())
                                    {
                                        psFreelanceSave=con.prepareStatement("UPDATE freelancetable SET block='' WHERE username='"+nameUser+"'");
                                        psFreelanceSave.executeUpdate();
                                            
                                        updateRegUser_Freelance(nameUser, rsFreelanceCheck.getString(5));
                                        model.addAttribute("errorXX", nameUser+" has been unblocked.");
                                        return "freelance";
                                    }
                                    model.addAttribute("errorXX", nameUser+" does not exist.");
                                    return "freelance";
                                }
                                
                                case "delete":
                                {
                                    rsFreelanceCheck=con.createStatement().executeQuery("SELECT * FROM freelancetable WHERE username='"+nameUser+"'");
                                    while(rsFreelanceCheck.next())
                                    {
                                        psFreelanceSave=con.prepareStatement("DELETE FROM freelancetable WHERE username='"+nameUser+"'");
                                        psFreelanceSave.executeUpdate();
                                            
                                        updateRegUser_Freelance(nameUser, "No");
                                        model.addAttribute("errorXX", nameUser+" has been removed.");
                                        return "freelance";
                                    }
                                    model.addAttribute("errorXX", nameUser+" does not exist.");
                                    return "freelance";
                                }
                            }                                
                            return "redirect:freelanceController";    
                        }
                        else
                        {
                            model.addAttribute("errorXX", "Enter username.");
                            return "freelance";
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
                    if(rs!=null)
                    {
                        rs.close();
                    }
                    if(rsAdminCheck!=null)
                    {
                        rsAdminCheck.close();
                    }
                    if(rsFreelanceCheck!=null)
                    {
                        rsFreelanceCheck.close();
                    }
                    if(psFreelanceSave!=null)
                    {
                        psFreelanceSave.close();
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
    
    private void saveToFreelanceTable(String a, String b)
    {
        PreparedStatement psFreelanceSave=null;
        
        try
        {
            psFreelanceSave=con.prepareStatement("INSERT INTO freelancetable (username, ban, block, category) VALUES(?,?,?,?)");
            psFreelanceSave.setString(1, a);
            psFreelanceSave.setString(2, "");
            psFreelanceSave.setString(3, "");
            psFreelanceSave.setString(4, b);
            psFreelanceSave.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
            {
                try
                {
                    if(psFreelanceSave!=null)
                    {
                        psFreelanceSave.close();
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    private void updateRegUser_Freelance(String username, String yesorno)
    {
        PreparedStatement psFreelanceSave=null;
        
        try
        {
            psFreelanceSave=con.prepareStatement("UPDATE registeredusers SET sumblogger='"+yesorno+"' WHERE username='"+username+"'");
            psFreelanceSave.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(psFreelanceSave!=null)
                {
                    psFreelanceSave.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @RequestMapping(value="/categoryselection")
    public String categoryselection(ModelMap model, HttpSession session, HttpServletRequest req, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            String adminCrossCheck="SELECT * FROM admintable WHERE username='" + username + "'";
            try
            {
                rsAdminCheck=con.createStatement().executeQuery(adminCrossCheck);
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("eleventhBeanObject", new SearchPersonClass());
                        return "admincategorypage";
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
    
    @RequestMapping(value="/categoryListController")
    public String categoryListController(@RequestParam("pg")int pg, @RequestParam("action")String action, 
    ModelMap model, HttpSession session, HttpServletRequest req, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        if(username!=null)
        {
            String adminCrossCheck="SELECT * FROM admintable WHERE username='" + username + "'";
            try
            {
                rsAdminCheck=con.createStatement().executeQuery(adminCrossCheck);
                while(rsAdminCheck.next())
                {
                    if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                    && lastname.equals(rsAdminCheck.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("eleventhBeanObject", new SearchPersonClass());
                        model.addAttribute("fifteenthBeanObject", new AdminControlClass());
                        
                        model.addAttribute("t", action);
                        model.addAttribute("pg", pg); //for blog news list (back and forth purpose)
                        //model.addAttribute("pgn", 1); //for comment list (back and forth purpose)
                        
                        List<Integer> idList=new LinkedList<>();
                        List<String> writerList=new LinkedList<>();
                        List<String> titleList=new LinkedList<>();
                        List<String> dateList=new LinkedList<>();
                        List<String> coverImageList=new LinkedList<>();
                        List<String> likeList=new LinkedList<>();
                        List<String> viewList=new LinkedList<>();
                        List<String> statusList=new LinkedList<>();
                        
                        int setoff=25;//make it 25 later
                        int nID=0;
                        
                        if(pg==1)
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE tablex='"+action+"' ORDER BY postid DESC LIMIT " + 0 + ", " + setoff);
                            while(rs.next())
                            {
                                nID=rs.getInt(1);
                                idList.add(nID);
                                statusList.add(rs.getString(2));
                                dateList.add(rs.getString(4));
                                writerList.add(rs.getString(5));
                                titleList.add(rs.getString(6));
                                coverImageList.add(rs.getString(8));
                                likeList.add(rs.getString(9));
                                viewList.add(rs.getString(10));
                                
                            }
                            model.addAttribute("cList", idList);
                            model.addAttribute("statusX", statusList);
                            model.addAttribute("date", dateList);
                            model.addAttribute("writer", writerList);
                            model.addAttribute("title", titleList);
                            model.addAttribute("covImg", coverImageList);
                            model.addAttribute("like", likeList);
                            model.addAttribute("view", viewList);
                            
                            if(nID!=0)
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='categoryListController?pg="+ ++pg+"&action="+action+"'>Next</a>");
                            }
                        }
                        else
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE tablex='"+action+"' ORDER BY postid DESC LIMIT " + (pg-1)*setoff + ", " + setoff);
                            while(rs.next())
                            {
                                nID=rs.getInt(1);
                                idList.add(nID);
                                statusList.add(rs.getString(2));
                                dateList.add(rs.getString(4));
                                writerList.add(rs.getString(5));
                                titleList.add(rs.getString(6));
                                coverImageList.add(rs.getString(8));
                                likeList.add(rs.getString(9));
                                viewList.add(rs.getString(10));
                            }
                            model.addAttribute("cList", idList);
                            model.addAttribute("statusX", statusList);
                            model.addAttribute("date", dateList);
                            model.addAttribute("writer", writerList);
                            model.addAttribute("title", titleList);
                            model.addAttribute("covImg", coverImageList);
                            model.addAttribute("like", likeList);
                            model.addAttribute("view", viewList);
                            model.addAttribute("prev", "<a style='text-decoration: none' href='categoryListController?pg="+ (pg-1) +"&action="+action+"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                            
                            if(nID!=0)
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='categoryListController?pg="+ ++pg+"&action="+action+"'>Next</a>");
                            }
                        }
                        
                        switch(action)
                        {
                            case "arts":
                            {
                                model.addAttribute("section", "Arts/History Section");
                            }
                            break;
                            
                            case "writing":
                            {
                                model.addAttribute("section", "Creative Writing Section");
                            }
                            break;
                            
                            case "health":
                            {
                                model.addAttribute("section", "Health Tips Section");
                            }
                            break;
                            
                            case "diy":
                            {
                                model.addAttribute("section", "DIY Section");
                            }
                            break;
                            
                            case "entertainment":
                            {
                                model.addAttribute("section", "Entertainment Section");
                            }
                            break;
                            
                            case "relpol":
                            {
                                model.addAttribute("section", "Religion and Politics Section");
                            }
                            break;
                            
                            case "info":
                            {
                                model.addAttribute("section", "Information and Updates Section");
                            }
                            break;
                            
                            case "flt":
                            {
                                model.addAttribute("section", "Fashion, Lifestyle and Trends Section");
                            }
                            break;
                            
                            case "sports":
                            {
                                model.addAttribute("section", "World Sports Section");
                            }
                            break;
                        }
                        return "adminbloglistview";
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
    
    @RequestMapping(value="/blogpostmanipulate")
    public String blogpostManipulate(@RequestParam("pg")int pg, @RequestParam("t")String t, HttpServletRequest req, 
    @ModelAttribute("fifteenthBeanObject")AdminControlClass accs, RedirectAttributes ra, HttpSession session, 
    @ModelAttribute("eightBeanObject") WriteUs wu, @ModelAttribute("seventhBeanObject") WorkOnFile wof, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("secondBeanObject") EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        PreparedStatement ps=null;
        
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
                        try 
                        {
                            String name=accs.getName();              
                            String title=accs.getTitle();
                            //String date=accs.getDate();
                            model.addAttribute("unreadMessages", succ.getUnreadCount(admin));                
                            rs=con.createStatement().executeQuery("SELECT * FROM freelancetable");
                            while(rs.next())
                            {
                                switch(accs.getChoice())
                                {                                
                                    case "message":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("blogmessage", "Cannot Execute Command");
                                            return "redirect:categoryListController?pg="+pg+"&action="+t;
                                        }
                                        else
                                        {
                                            String replyTitle="RE: " + title;
                                            //String description="Posted by @" + name + " on: " + date;
                                            wu.setSubject(replyTitle);
                                            //wu.setDescription(description);
                                            wu.setSendTo(name);
                                            return "adminMessagePage";
                                        }
                                    }
                                    
                                    case "ban":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("blogmessage", "Cannot Execute Command");
                                        }
                                        else
                                        {
                                            ps=con.prepareStatement("UPDATE freelancetable SET ban='BANNED', "
                                            + "block='BLOCKED' WHERE username='" + name + "'");
                                            ps.executeUpdate();                                            
                                            ra.addFlashAttribute("blogmessage", "@" + name + " has been banned from Sumblogging.");
                                        }
                                        return "redirect:categoryListController?pg="+pg+"&action="+t;
                                    }
                                    
                                    case "block":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("blogmessage", "Cannot Execute Command");
                                        }
                                        else
                                        {
                                            ps=con.prepareStatement("UPDATE freelancetable SET block='BLOCKED' WHERE username='" + name + "'");
                                            ps.executeUpdate();                                
                                            ra.addFlashAttribute("blogmessage", "@" + name + " has been blocked from Sumblogging.");
                                        }
                                        return "redirect:categoryListController?pg="+pg+"&action="+t;                                   
                                    }
                            
                                    case "unban":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("blogmessage", "Cannot Execute Command");
                                        }
                                        else
                                        {
                                            ps=con.prepareStatement("UPDATE freelancetable SET ban='', "
                                            + "block='' WHERE username='" + name + "'");
                                            ps.executeUpdate();                                
                                            ra.addFlashAttribute("blogmessage", "@" + name + " can now Sumblog.");
                                        }
                                        return "redirect:categoryListController?pg="+pg+"&action="+t;
                                    }

                                    case "unblock":
                                    {
                                        if(name.equals(admin))
                                        {
                                            ra.addFlashAttribute("blogmessage", "Cannot Execute Command");
                                        }
                                        else
                                        {
                                            ps=con.prepareStatement("UPDATE freelancetable SET block='' WHERE username='" + name + "'");
                                            ps.executeUpdate();                                
                                            ra.addFlashAttribute("blogmessage", "@" + name + " is unblocked from Sumblogging.");
                                        }
                                        return "redirect:categoryListController?pg="+pg+"&action="+t;
                                    }
                                }
                            }                
                        }
                        catch (SQLException ex)
                        {
                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @RequestMapping(value="/checkwordlist")
    public String checkwordlist(ModelMap model, HttpSession session, HttpServletRequest req, 
    @ModelAttribute("eighteenthBeanObject")GeneralInfoClass gic, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, @ModelAttribute("nineteenthBeanObject")NewAdminClass nac, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @RequestParam("word")String wordInput, @RequestParam("category")String category, 
    @RequestParam("action")String action)
    {
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
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        model.addAttribute("eleventhBeanObject", new SearchPersonClass());
                        String word=wordInput.trim().toLowerCase();
                        String categoryList=category.toLowerCase()+".txt";
                        String fileLoc=pathFromPropertiesFile+"/docs/"+categoryList;
                        
                        if(!word.matches("\\s*"))
                        {
                            switch(action)
                            {
                                case "check":
                                {
                                    String allWords="";
                                    Path wordPath=Paths.get(fileLoc);
                                    List<String> wordLines=Files.readAllLines(wordPath);
                                
                                    for(int count=0; count<wordLines.size(); count++)
                                    {
                                        allWords += wordLines.get(count);
                                    }
            
                                    String[] wordSplit=allWords.split(",\\s*");
                                    for(String wordStatic:wordSplit)
                                    {
                                        if(wordStatic.equals(word))
                                        {
                                            model.addAttribute("result", "'"+word+"' exists in "+category+" list.");
                                            return "adminInfoPage";
                                        }                                    
                                    }
                                    model.addAttribute("result", "'"+word+"' does not exist in "+category+" list.");
                                    return "adminInfoPage";
                                }
                            
                                case "append":
                                {
                                    String allWords="";
                                    Path wordPath=Paths.get(fileLoc);
                                    List<String> wordLines=Files.readAllLines(wordPath);
                                
                                    for(int count=0; count<wordLines.size(); count++)
                                    {
                                        allWords += wordLines.get(count);
                                    }
                                    
                                    String[] wordSplit=allWords.split(",\\s*");
                                    for(String wordStatic:wordSplit)
                                    {
                                        if(wordStatic.equals(word))
                                        {
                                            model.addAttribute("result", "'"+word+"' exists in "+category+" list. Sorry, cannot duplicate.");
                                            return "adminInfoPage";
                                        }                                    
                                    }
                                    Files.write(wordPath, (word+", ").getBytes(), StandardOpenOption.APPEND);
                                    model.addAttribute("result", "'"+word.toLowerCase()+"' added to "+category+" list.");
                                    return "adminInfoPage";
                                }
                            
                                case "replace":
                                {
                                    if(username.equals(admin))
                                    {
                                        String allWords="";
                                        Path wordPath=Paths.get(fileLoc);
                                        List<String> wordLines=Files.readAllLines(wordPath);
                                
                                        for(int count=0; count<wordLines.size(); count++)
                                        {
                                            allWords += wordLines.get(count);
                                        }
            
                                        String[] token=word.split(",\\s*");
                                        String[] wordSplit=allWords.split(",\\s*");
                                        if(token.length==2)
                                        {
                                            for(String wordStatic:wordSplit)
                                            {
                                                if(wordStatic.equals(token[0]))
                                                {
                                                    if(!token[0].equals(token[1]))
                                                    {
                                                        allWords=allWords.replaceFirst("\\b"+ token[0] + "\\b", token[1]);
                                                        Files.write(wordPath, allWords.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                                                        model.addAttribute("result", "'"+token[0]+"' replaced with '"+token[1]+"' in "+category+" list.");
                                                        return "adminInfoPage";
                                                    }
                                                    else
                                                    {
                                                        model.addAttribute("result", "'"+token[0]+"' and '"+token[1]+"' are the same words.");
                                                        return "adminInfoPage";
                                                    }                                            
                                                }
                                            }
                                            model.addAttribute("result", "'"+token[0]+"' does not exist in "+category+" list.");
                                            return "adminInfoPage";
                                        }
                                        else
                                        {
                                            model.addAttribute("result", "Improper format, should  be: word, replacement");
                                            return "adminInfoPage";
                                        }                                
                                    }
                                    else
                                    {
                                        model.addAttribute("result", "Sorry cannot perform action");
                                        return "adminInfoPage";
                                    }
                                }
                            
                                case "remove":
                                {
                                    if(username.equals(admin))
                                    {
                                        String allWords="";
                                        Path wordPath=Paths.get(fileLoc);
                                        List<String> wordLines=Files.readAllLines(wordPath);
                                    
                                        for(int count=0; count<wordLines.size(); count++)
                                        {
                                            allWords += wordLines.get(count);
                                        }                                
                                        String[] wordSplit=allWords.split(",\\s*");
                                    
                                        for(String wordSplitCount:wordSplit)
                                        {
                                            if(word.equals(wordSplitCount))
                                            {
                                                allWords=allWords.replaceFirst("\\b"+ word+", " + "\\b", "");
                                                Files.write(wordPath, allWords.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                                                model.addAttribute("result", "'"+word+"' removed from "+category+" list.");
                                                return "adminInfoPage";
                                            }
                                        }
                                    }
                                    else
                                    {
                                        //model.addAttribute("result", "Sorry cannot perform action");
                                        return "adminInfoPage";
                                    }
                                }
                            }
                        }
                        else if(action.equals("properties"))//remove this when Sumpis goes online
                        {
                            model.addAttribute("result2", "Upload file location is "+pathFromPropertiesFile);
                            return "adminInfoPage";
                        }
                        else if(action.equals("wordlist"))
                        {
                            String allWords="";
                            Path wordPath=Paths.get(fileLoc);
                            List<String> wordLines=Files.readAllLines(wordPath);
                                
                            for(int count=0; count<wordLines.size(); count++)
                            {
                                allWords += wordLines.get(count);
                            }
                            model.addAttribute("result1", category+" LIST<br>Total: "+allWords.split(",\\s*").length+ " words.");
                            model.addAttribute("result2", "<div style='padding: 5px; text-align: justify; border: 1px solid green;'>"+allWords+"</div>");
                            return "adminInfoPage";
                        }
                        else if(action.equals("writetoproperties"))
                        {
                            URL str=AdminController.class.getClassLoader().getResource("/resources/config.properties");
                            model.addAttribute("result2", "Location of properties file is "+str);
                            return "adminInfoPage";
                        }
                        else
                        {
                            model.addAttribute("result", "Enter word.");
                            return "adminInfoPage";
                        }
                    }
                }
                session.invalidate();
                model.addAttribute("expired", "Unauthorized access. You have been logged out!!!");
            }
            catch (SQLException | IOException ex)
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
    
    @RequestMapping(value="/adpanel")
    public String adPanel(@RequestParam(value="pg", required=false)int pg, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("adClass")AdClass adc, HttpServletRequest req, HttpSession session, ModelMap model, RedirectAttributes ra)
    {
        ResultSet rsAdminCheck=null;
        PreparedStatement ps=null;
        
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
                        String choice=adc.getVar5();
                        if(adc.getVar6()!=0 && pg==0)
                        {
                            pg=adc.getVar6();
                        }
                        List<List<String>> showAd=adminShowAd(pg);
                        model.addAttribute("dispx", "none");
                        model.addAttribute("idList", showAd.get(0));
                        model.addAttribute("pubList", showAd.get(1));
                        model.addAttribute("apList", showAd.get(2));
                        model.addAttribute("bList", showAd.get(3));
                        model.addAttribute("xList", showAd.get(4));
                        model.addAttribute("dList", showAd.get(5));
                        model.addAttribute("pList", showAd.get(6));
                        model.addAttribute("sList", showAd.get(7));
                        //model.addAttribute("eList", showAd.get(8));
                        model.addAttribute("cList", showAd.get(8));
                        model.addAttribute("vList", showAd.get(9));
                        model.addAttribute("imgList", showAd.get(10));
                        model.addAttribute("landList", showAd.get(11));
                        model.addAttribute("tList", showAd.get(12));
                        model.addAttribute("pauseList", showAd.get(13));
                        model.addAttribute("pgList", showAd.get(14));
                        model.addAttribute("cpmList", showAd.get(15));
                        model.addAttribute("cpcList", showAd.get(16));
                        model.addAttribute("next", showAd.get(17));
                        model.addAttribute("prev", showAd.get(18));
                        model.addAttribute("adClass", new AdClass());
                        
                        if(choice!=null)
                        {
                            switch (choice)
                            {
                                case "apv":
                                {
                                    ps=con.prepareStatement("UPDATE adstable SET approve='ENABLED', display='inline' WHERE adid="+adc.getVar1());
                                    ps.executeUpdate();
                                    ra.addFlashAttribute("info", "Advert "+adc.getVar1()+" is approved!");
                                    return "redirect:/adpanel?pg="+pg;
                                }
                                case "msg":
                                {
                                    WriteUs wu=new WriteUs();
                                    wu.setSendTo(adc.getVar2());
                                    wu.setSubject("About Advert Placement");
                                    model.addAttribute("seventhBeanObject", new WorkOnFile());
                                    model.addAttribute("eightBeanObject", wu);
                                    
                                    return "adminMessagePage";
                                }
                                case "edit":
                                {
                                    model.addAttribute("dispx", "inline");
                                    break;
                                }
                                case "stp":
                                {
                                    ps=con.prepareStatement("UPDATE adstable SET approve='DISABLED', display='none' WHERE adid="+adc.getVar1());
                                    ps.executeUpdate();
                                    ra.addFlashAttribute("info", "Advert "+adc.getVar1()+" is stopped!");
                                    return "redirect:/adpanel?pg="+pg;
                                }
                                case "upd":
                                {
                                    int cpm=adc.getVar3();
                                    int cpc=adc.getVar4();
                                    String uName=adc.getVar2();
                                    ps=con.prepareStatement("UPDATE registeredusers SET cpm="+cpm+", cpc="+cpc+" WHERE username='"+uName+"'");
                                    ps.executeUpdate();
                                    ra.addFlashAttribute("info", "Pricing updated for "+uName);
                                    return "redirect:/adpanel?pg="+pg;
                                }
                            }
                        }
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        return "adminadspage";
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
    
    protected List<List<String>> adminShowAd(int pg)
    {
        ResultSet rs=null;
        List<List<String>> all=new LinkedList<>();
        List<String> idList=new LinkedList<>();
        List<String> apList=new LinkedList<>();
        List<String> pubList=new LinkedList<>();
        List<String> bList=new LinkedList<>();
        List<String> xList=new LinkedList<>();
        List<String> dList=new LinkedList<>();
        List<String> pList=new LinkedList<>();
        List<String> sList=new LinkedList<>();
        //List<String> eList=new LinkedList<>();
        List<String> cList=new LinkedList<>();
        List<String> vList=new LinkedList<>();
        List<String> imgList=new LinkedList<>();
        List<String> landList=new LinkedList<>();
        List<String> tList=new LinkedList<>();
        List<String> pauseList=new LinkedList<>();
        List<String> pgList=new LinkedList<>();
        List<String> cpmList=new LinkedList<>();
        List<String> cpcList=new LinkedList<>();
        List<String> next=new LinkedList<>();
        List<String> prev=new LinkedList<>();
        
        try
        {
            int setoff=30;
            int nID=0;
            if(pg==1)
            {
                rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username "
                + " ORDER BY adid ASC LIMIT 0,"+setoff);
                while(rs.next())
                {
                    nID=rs.getInt(1);
                    idList.add(String.valueOf(nID));
                    pubList.add(rs.getString(2));
                    apList.add(rs.getString(3));
                    bList.add(String.valueOf(rs.getInt(37)));
                    xList.add(rs.getString(14));
                    dList.add(String.valueOf(rs.getInt(15)));
                    pList.add(rs.getString(4));
                    sList.add(rs.getString(5));
                    //eList.add(rs.getString(6));
                    cList.add(String.valueOf(rs.getInt(7)));
                    vList.add(String.valueOf(rs.getInt(8)));
                    imgList.add(rs.getString(9));
                    landList.add(rs.getString(10));
                    tList.add(rs.getString(11));
                    pauseList.add(rs.getString(12));
                    
                    cpmList.add(String.valueOf(rs.getInt(38)));
                    cpcList.add(String.valueOf(rs.getInt(39)));
                }
                pgList.add(String.valueOf(pg));
                if(nID==0)
                {
                    next.add("");
                }
                else
                {
                    String nxt="<a href='adpanel?pg="+ ++pg+"'>Next</a>";
                    next.add(nxt);
                }
            }
            else
            {
                rs=con.createStatement().executeQuery("SELECT * FROM adstable INNER JOIN registeredusers ON adstable.username=registeredusers.username "
                + "LIMIT "+(pg-1)*setoff+","+setoff);
                while(rs.next())
                {
                    nID=rs.getInt(1);
                    idList.add(String.valueOf(nID));
                    pubList.add(rs.getString(2));
                    apList.add(rs.getString(3));
                    bList.add(String.valueOf(rs.getInt(37)));
                    xList.add(rs.getString(14));
                    dList.add(String.valueOf(rs.getInt(15)));
                    pList.add(rs.getString(4));
                    sList.add(rs.getString(5));
                    //eList.add(rs.getString(6));
                    cList.add(String.valueOf(rs.getInt(7)));
                    vList.add(String.valueOf(rs.getInt(8)));
                    imgList.add(rs.getString(9));
                    landList.add(rs.getString(10));
                    tList.add(rs.getString(11));
                    pauseList.add(rs.getString(12));
                    cpmList.add(String.valueOf(rs.getInt(38)));
                    cpcList.add(String.valueOf(rs.getInt(39)));
                }
                pgList.add(String.valueOf(pg));
                String prv="<a href='adpanel?pg="+(pg-1)+"'>Previous</a>";
                prev.add(prv);
                
                if(nID==0)
                {
                    next.add("");
                }
                else
                {
                    String nxt="<a href='adpanel?pg="+ ++pg+"'>Next</a>";
                    next.add(nxt);
                }
            }
            all.add(idList);
            all.add(pubList);
            all.add(apList);
            all.add(bList);
            all.add(xList);
            all.add(dList);
            all.add(pList);
            all.add(sList);
            //all.add(eList);
            all.add(cList);
            all.add(vList);
            all.add(imgList);
            all.add(landList);
            all.add(tList);
            all.add(pauseList);
            all.add(pgList);
            all.add(cpmList);
            all.add(cpcList);
            all.add(next);
            all.add(prev);
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
    
    @RequestMapping(value="/stat")
    public String statistics(@RequestParam("pg")int pg, @RequestParam(value="dateSearch", required=false)String dateSearch,
    HttpServletRequest req, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc,  ModelMap model, HttpSession session, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, 
    @ModelAttribute("fourthBeanObject")MemberClass mc)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        
        List<Integer>idList=new LinkedList<>();
        List<String>date=new LinkedList<>();
        List<String>page=new LinkedList<>();
        List<Integer>count=new LinkedList<>();
        
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
                        int setoff=15;
                        int nid=0;
                        
                        if(dateSearch!=null)
                        {
                            dateSearch=dateSearch.trim();
                            rs=con.createStatement().executeQuery("SELECT * FROM stats WHERE datex='"+dateSearch+"'");
                            while(rs.next())
                            {
                                nid=rs.getInt(1);
                                idList.add(nid);
                                date.add(rs.getString(2));
                                page.add(rs.getString(3));
                                count.add(rs.getInt(4));
                            }
                            
                            model.addAttribute("idList", idList);
                            model.addAttribute("date", date);
                            model.addAttribute("page", page);
                            model.addAttribute("count", count);
                        }
                        else if(pg==1)
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM stats ORDER BY statid DESC LIMIT 0,"+setoff);
                            while(rs.next())
                            {
                                nid=rs.getInt(1);
                                idList.add(nid);
                                date.add(rs.getString(2));
                                page.add(rs.getString(3));
                                count.add(rs.getInt(4));
                            }
                            
                            model.addAttribute("idList", idList);
                            model.addAttribute("date", date);
                            model.addAttribute("page", page);
                            model.addAttribute("count", count);
                            
                            if(nid!=0)
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='stat?pg="+ ++pg +"'>Next</>");
                            }
                        }
                        else
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM stats ORDER BY statid DESC LIMIT "+(pg-1)*setoff+","+setoff);
                            while(rs.next())
                            {
                                nid=rs.getInt(1);
                                idList.add(nid);
                                date.add(rs.getString(2));
                                page.add(rs.getString(3));
                                count.add(rs.getInt(4));
                            }
                            
                            model.addAttribute("idList", idList);
                            model.addAttribute("date", date);
                            model.addAttribute("page", page);
                            model.addAttribute("count", count);
                            
                            model.addAttribute("prev", "<a style='text-decoration: none' href='stat?pg="+ (pg-1) +"'>Previous</>");
                            
                            if(nid!=0)
                            {
                                model.addAttribute("next", "<a style='text-decoration: none' href='stat?pg="+ ++pg +"'>Next</>");
                            }
                        }
                        return "statpage";
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
}