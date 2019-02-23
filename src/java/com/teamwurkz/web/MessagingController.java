package com.teamwurkz.web;

import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.FileUploadClass;
import com.teamwurkz.domain.FinalEdit;
import com.teamwurkz.domain.FirstHiddenFormClass;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.NewsPostClass;
import com.teamwurkz.domain.SearchPersonClass;
import com.teamwurkz.domain.WorkOnFile;
import com.teamwurkz.domain.WriteUs;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessagingController
{   
    @Autowired
    SignUpController succ;
    private Context ctx;
    protected DataSource ds;
    protected Connection con;
    
    @Value("${admin.name}")
    String admin;
    
    @Value("${advert.display}")//Properties file pending when we start allowing adverts. For now it is display: none
    String displayadvert;
    private final Charset ISO=Charset.forName("ISO-8859-1");
    private final Charset UTF=Charset.forName("UTF-8");
    
    public MessagingController()
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
            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //optimized 4
    @RequestMapping(value="/adminInbox", method=RequestMethod.GET)
    public String adminMessages(HttpServletRequest req, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    HttpSession session, @RequestParam("pg")int pg)
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
            try
            {
                model.addAttribute("pg", pg);
                model.addAttribute("display", "none");
                model.addAttribute("ninthBeanObject", new FinalEdit());
                model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                model.addAttribute("tenthBeanObject", new FirstHiddenFormClass());
                
                List<Integer> idList=new LinkedList<>();
                List<String> sender=new LinkedList<>();
                List<String> subject=new LinkedList<>();
                List<String> dates=new LinkedList<>();
                List<String> stat=new LinkedList<>();
                
                rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                while(rsAdminCheck.next())
                {
                    if(firstname.equals(rsAdminCheck.getString(2)) && lastname.equals(rsAdminCheck.getString(3)) &&
                    password.equals(rsAdminCheck.getString(5)))
                    {
                        try
                        {
                            int nID=0;
                            int setoff=30; //make 30 later
                            if(pg==1)
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE receiver ='"+admin+"' AND approval='ENABLED' ORDER BY msgid DESC LIMIT "+0+ ", " + setoff);//I put admin, so that other admins can see new msgs
                                while(rs.next())
                                {
                                    nID=rs.getInt(1);
                                    idList.add(nID);
                                    sender.add(rs.getString(2));
                                    subject.add(rs.getString(4));
                                    dates.add(rs.getString(6));
                                    stat.add(rs.getString(7));
                                }
                                
                                if(nID!=0)
                                {
                                    model.addAttribute("next", "<a style='text-decoration: none' href='adminInbox?pg="+ ++pg +"'>Next</a>");
                                }
                            }
                            else
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE receiver ='" + admin + "' AND approval='ENABLED' ORDER BY msgid DESC LIMIT "+(pg-1)*setoff+ ", " + setoff);            
                                while(rs.next())
                                {
                                    nID=rs.getInt(1);
                                    idList.add(nID);
                                    sender.add(rs.getString(2));
                                    subject.add(rs.getString(4));
                                    dates.add(rs.getString(6));
                                    stat.add(rs.getString(7));
                                }
                                model.addAttribute("prev", "<a style='text-decoration: none' href='adminInbox?pg="+ (pg-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                                
                                if(nID!=0)
                                {
                                    model.addAttribute("next", "<a style='text-decoration: none' href='adminInbox?pg="+ ++pg +"'>Next</a>");
                                }
                            }
                            session.setAttribute("cList", idList);
                            session.setAttribute("sender", sender);
                            session.setAttribute("subject", subject);
                            session.setAttribute("date", dates);
                            session.setAttribute("stat", stat);
                            return "adminInboxMessages";
                        }
                        catch (SQLException ex)
                        {
                            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    //optimized 5
    @RequestMapping(value="/userinbox", method=RequestMethod.GET)
    public String userMessages(HttpServletRequest req, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject") EditClass ec, HttpSession session, 
    @RequestParam("pg")int pg)
    {
        ResultSet rs=null;
        
        //statistics
        succ.statistics("INBOX");
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            model.addAttribute("pg", pg);
            model.addAttribute("display", "none");
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
            model.addAttribute("ninthBeanObject", new FinalEdit());
            model.addAttribute("tenthBeanObject", new FirstHiddenFormClass());
            
            List<Integer> idList=new LinkedList<>();
            List<String> sender=new LinkedList<>();
            List<String> subject=new LinkedList<>();
            List<String> dates=new LinkedList<>();
            List<String> stat=new LinkedList<>();
            try
            {
                int nID=0;
                int setoff=12;//make 12 later
                if(pg==1)
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE receiver ='" + username + "' AND approval='ENABLED' ORDER BY msgid DESC LIMIT "+0+ ", " + setoff);            
                    while(rs.next())
                    {
                        nID=rs.getInt(1);
                        idList.add(nID);
                        sender.add(rs.getString(2));
                        subject.add(rs.getString(4));
                        dates.add(rs.getString(6));
                        stat.add(rs.getString(7));
                    }
/**why session and not model, try with model. Answer: I used session so as to enable reading on the same page where the msgs are listed 
to allow for easy reading, with model, when you read a msg, the msg list disappears, so you have to reload to get back the list*/
                    
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='userinbox?pg="+ ++pg +"'>Next</a>");
                    }
                }
                else
                {
                    rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE receiver ='" + username + "' AND approval='ENABLED' ORDER BY msgid DESC LIMIT "+(pg-1)*setoff+ ", " + setoff);            
                    while(rs.next())
                    {
                        nID=rs.getInt(1);
                        idList.add(nID);
                        sender.add(rs.getString(2));
                        subject.add(rs.getString(4));
                        dates.add(rs.getString(6));
                        stat.add(rs.getString(7));
                    }
                    model.addAttribute("prev", "<a style='text-decoration: none' href='userinbox?pg="+ (pg-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                    
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='userinbox?pg="+ ++pg +"'>Next</a>");
                    }
                }
                session.setAttribute("cList", idList);
                session.setAttribute("sender", sender);
                session.setAttribute("subject", subject);
                session.setAttribute("date", dates);
                session.setAttribute("stat", stat);
                return "inboxMessages";
            }        
            catch (SQLException ex)
            {
                Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
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

    //optimized 6
    @RequestMapping(value="/adminFileReader")
    public String readClientMessages(HttpServletRequest req, ModelMap model, @ModelAttribute("tenthBeanObject")FirstHiddenFormClass fhfc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("eightBeanObject") WriteUs wu,
    @ModelAttribute("seventhBeanObject") WorkOnFile wof, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("secondBeanObject") EditClass ec, @ModelAttribute("fourteenthBeanObject")NewsPostClass npc, 
    HttpSession session, @RequestParam("admfr")int pg)
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
                    if(firstname.equals(rsAdminCheck.getString(2)) && lastname.equals(rsAdminCheck.getString(3))
                    && password.equals(rsAdminCheck.getString(5)))
                    {
                        FinalEdit fe=new FinalEdit();
                        model.addAttribute("ninthBeanObject", fe);
                        
                        try
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE msgid="+fhfc.getNid());            
                            while(rs.next())
                            {
                                String msg=rs.getString(5);
                                switch (fhfc.getChoice())
                                {
                                    case "read": 
                                    {
                                        ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                                        ps.executeUpdate();
                                        fe.setFileContent(msg);
                                        model.addAttribute("pg", pg);
                                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                                        model.addAttribute("fileContent", rs.getString(5));
                                        model.addAttribute("display", "inline");
                                        model.addAttribute("border", "1px solid grey");
                                        model.addAttribute("padding", "10px");
                                        model.addAttribute("radius", "5px");
                                    }
                                    break;
                        
                                    case "download":
                                    {
                                        ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                                        ps.executeUpdate();
                                        msg=msg.replaceAll("<pre>", "");
                                        msg=msg.replaceAll("</pre>", "");
                                        return "forward:/downloadcontroller?sent=" + URLEncoder.encode(msg, "UTF-8");                                
                                    }
                                        
                                    case "forwardto":
                                    {
                                        ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                                        ps.executeUpdate();
                                        
                                        msg=msg.replaceAll("<pre>", "<#");
                                        msg=msg.replaceAll("</pre>", "#>");
                                        wu.setWriteUp(msg);
                                        model.addAttribute("pg", pg);
                                        session.setAttribute("clients", adminList());
                                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                                        return "adminMessagePage";                                
                                    }
                                            
                                    case "delete": 
                                    {
                                        if(username.equals(admin))
                                        {
                                            ps=con.prepareStatement("UPDATE messages SET approval='DISABLED' WHERE msgid="+fhfc.getNid());
                                            ps.executeUpdate();
                                            return "redirect:/adminInbox?pg="+pg;
                                        }
                                        else
                                        {
                                            return "redirect:/adminInbox?pg="+pg;
                                        }
                                    }
                                                                            
                                    case "newspost":
                                    {
                                        ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                                        ps.executeUpdate();
                                        msg=msg.replaceAll("<pre>", "<#");
                                        msg=msg.replaceAll("</pre>", "#>");
                                        npc.setNewsPost(msg);
                                        model.addAttribute("pg", pg);
                                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                                        return "adminsumpisnews";
                                    }
                                }                
                            }            
                        }                
                        catch (SQLException | UnsupportedEncodingException ex)
                        {
                            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        model.addAttribute("pg", pg);
                        
                        if(pg!=1)
                        {
                            model.addAttribute("prev", "<a style='text-decoration: none' href='adminInbox?pg="+ (pg-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                        }
                        model.addAttribute("next", "<a style='text-decoration: none' href='adminInbox?pg="+ ++pg +"'>Next</a>");
                        return "adminInboxMessages";
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
    
    @RequestMapping(value="/filereader", method=RequestMethod.POST)
    public String readMessages(HttpServletRequest req, ModelMap model, @ModelAttribute("tenthBeanObject")FirstHiddenFormClass fhfc, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("eightBeanObject") WriteUs wu,
    @ModelAttribute("seventhBeanObject") WorkOnFile wof, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc,
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("secondBeanObject") EditClass ec, @ModelAttribute("fourteenthBeanObject")NewsPostClass npc, 
    HttpSession session, @RequestParam("fr")int pg)
    {
        ResultSet rs=null;
        PreparedStatement ps=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            FinalEdit fe=new FinalEdit();
            model.addAttribute("ninthBeanObject", fe);
            try
            {
                rs=con.createStatement().executeQuery("SELECT * FROM messages WHERE msgid="+fhfc.getNid());            
                while(rs.next())
                {
                    String msg=rs.getString(5);
                    switch (fhfc.getChoice())
                    {
                        case "read":
                        {
                            ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());                                
                            ps.executeUpdate();
                            fe.setFileContent(msg);// once you read this, it sets up for the 'further' controller to get

                            model.addAttribute("fileContent", msg);
                            model.addAttribute("display", "inline");
                            model.addAttribute("border", "1px solid grey");
                            model.addAttribute("padding", "10px");
                            model.addAttribute("radius", "5px");
                            model.addAttribute("pg", pg);
                            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                            model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        }
                        break;
                        
                        case "download": 
                        {
                            msg=msg.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "");
                            msg=msg.replaceAll("'/><br><br>", "");
                            msg=msg.replaceAll("<pre>", "");
                            msg=msg.replaceAll("</pre>", "");
                            ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                            ps.executeUpdate();
                            return "forward:/downloadcontroller?sent=" + URLEncoder.encode(msg, "UTF-8");
                        }
                    
                        case "forwardto":
                        {
                            msg=msg.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "<_");
                            msg=msg.replaceAll("'/><br><br>", "_>");
                            msg=msg.replaceAll("<pre>", "<#");
                            msg=msg.replaceAll("</pre>", "#>");
                            ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                            ps.executeUpdate();
                            wu.setWriteUp(msg);
                            session.setAttribute("colleagues", colleagueList(session));
                            model.addAttribute("pg", pg);
                            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                            model.addAttribute("friendrequest", succ.getFriendRequest(username));
                            return "message";
                        }
                        
                        case "delete":
                        {
                            ps=con.prepareStatement("UPDATE messages SET approval='DISABLED' WHERE msgid="+fhfc.getNid());
                            ps.executeUpdate();
                            return "redirect:/userinbox?pg="+pg;
                        }
                            
                        case "newspost":
                        {
                            msg=msg.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "<_");
                            msg=msg.replaceAll("'/><br><br>", "_>");
                            msg=msg.replaceAll("<pre>", "<#");
                            msg=msg.replaceAll("</pre>", "#>");
                            ps=con.prepareStatement("UPDATE messages SET unread='' WHERE msgid="+fhfc.getNid());
                            ps.executeUpdate();
                            npc.setNewsPost(msg);
                            model.addAttribute("pg", pg);
                            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                            model.addAttribute("friendrequest", succ.getFriendRequest(username));
                            return "sumpisnews";
                        }
                    }                
                }
                if(pg!=1)
                {
                    model.addAttribute("prev", "<a style='text-decoration: none' href='userinbox?pg="+ (pg-1) +"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                }
                model.addAttribute("next", "<a style='text-decoration: none' href='userinbox?pg="+ ++pg +"'>Next</a>");
                return "inboxMessages";
            }        
            catch (SQLException | UnsupportedEncodingException ex)
            {
                Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
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
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
        }
        return "homepage";        
    }
    
    public List adminList() //Doesn't have session because, admin has access to everybody.
    {
        ResultSet rs=null;
        
        List<String> clientList=new LinkedList<>();        
        try
        {
            rs=con.createStatement().executeQuery("SELECT username FROM registeredusers");
            while(rs.next())
            {
                clientList.add(rs.getString(1));
            }            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
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
        return clientList;
    }
    //optimized 1
    public List colleagueList(HttpSession session) //The session is to get a particular username and select their friends
    {
        ResultSet rs=null;
    
        String username=(String)session.getAttribute("username");        
        List<String> friendList=new LinkedList<>();
        friendList.add(admin);            
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM contactdb");                
            while(rs.next())
            {
                String userA=rs.getString(2);
                String userB=rs.getString(3);
                String confirmed=rs.getString(4);                    
                if(userA.equals(username) && confirmed.equals("confirmed"))
                {
                    friendList.add(userB);
                }                    
                if(userB.equals(username) && confirmed.equals("confirmed"))
                {
                    friendList.add(userA);
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return friendList;
    }
    //optimized 2
    @RequestMapping(value="/adminFurther", method=RequestMethod.POST)
    public String adminFurtherAction(@ModelAttribute("ninthBeanObject")FinalEdit fe, ModelMap model, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("eightBeanObject") WriteUs wu,
    @ModelAttribute("seventhBeanObject") WorkOnFile wof, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc,
    @ModelAttribute("tenthBeanObject")FirstHiddenFormClass fhfc, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("thirdBeanObject") FileUploadClass fuc, HttpServletRequest req, @ModelAttribute("fourteenthBeanObject")NewsPostClass npc, 
    HttpSession session)
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
                        String content=new String(fe.getFileContent().getBytes(ISO), UTF);
                        
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        switch (fe.getChoice())
                        {
                            case "download":
                                if(!content.matches("\\s*"))
                                {
                                    content=content.replaceAll("<pre>", "");
                                    content=content.replaceAll("</pre>", "");
                                    return "forward:/downloadcontroller?sent=" + URLEncoder.encode(content, "UTF-8");
                                }
                                else
                                {
                                    model.addAttribute("error", "Cannot download an empty file.");
                                    return "adminInboxMessages";
                                }
                            case "forwardTo":
                                if(!content.matches("\\s*"))
                                {
                                    content=content.replaceAll("<pre>", "<#");
                                    content=content.replaceAll("</pre>", "#>");
                                    wu.setWriteUp(content);
                                    return "adminMessagePage";
                                }        
                                else
                                {
                                    model.addAttribute("error", "Cannot send back empty content.");
                                }                    
                            case "newspost":
                                if(!content.matches("\\s*"))
                                {
                                    content=content.replaceAll("<pre>", "<#");
                                    content=content.replaceAll("</pre>", "#>");
                                    npc.setNewsPost(content);
                                    return "adminsumpisnews";
                                }
                                else
                                {
                                    model.addAttribute("error", "Cannot send back empty content.");
                                }
                        }
                        return "adminInboxMessages";
                    }
                }
                session.invalidate();
                model.addAttribute("expired", "Unauthorized access. You have been logged out!!!");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
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
    //optimized 3
    @RequestMapping(value="/further", method=RequestMethod.POST)
    public String furtherAction(@ModelAttribute("ninthBeanObject")FinalEdit fe, ModelMap model, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("eightBeanObject") WriteUs wu,
    @ModelAttribute("seventhBeanObject") WorkOnFile wof, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc,
    @ModelAttribute("tenthBeanObject")FirstHiddenFormClass fhfc, HttpServletRequest req, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("thirdBeanObject") FileUploadClass fuc, 
    @ModelAttribute("fourteenthBeanObject")NewsPostClass npc, HttpSession session)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            String content=new String(fe.getFileContent().getBytes(ISO), UTF);
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
        
            switch (fe.getChoice())
            {
                case "download":
                    if(!content.matches("\\s*"))
                    {
                        content=content.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "");
                        content=content.replaceAll("'/><br><br>", "");
                        content=content.replaceAll("<pre>", "");
                        content=content.replaceAll("</pre>", "");
                        try
                        {
                            return "forward:/downloadcontroller?sent=" + URLEncoder.encode(content, "UTF-8");
                        }
                        catch (UnsupportedEncodingException ex)
                        {
                            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                
                    else
                    {
                        model.addAttribute("sendingerror", "Cannot download an empty file.");
                        return "inboxMessages";
                    }                
                case "sendToInput":
                    if(!content.matches("\\s*"))
                    {
                        content=content.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "<_");
                        content=content.replaceAll("'/><br><br>", "_>");
                        content=content.replaceAll("<pre>", "<#");
                        content=content.replaceAll("</pre>", "#>");
                        fc.setField(content);
                        model.addAttribute("displayadvert", displayadvert);
                        return "registeredMember";
                    }                
                    else
                    {
                        model.addAttribute("sendingerror", "Cannot send back empty content.");
                        return "inboxMessages";
                    }                
                case "forwardTo":
                    if(!content.matches("\\s*"))
                    {
                        content=content.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "<_");
                        content=content.replaceAll("'/><br><br>", "_>");
                        content=content.replaceAll("<pre>", "<#");
                        content=content.replaceAll("</pre>", "#>");
                        wu.setWriteUp(content);
                        return "message";
                    }                
                    else
                    {
                        model.addAttribute("sendingerror", "Cannot send back empty content.");
                        return "inboxMessages";
                    }                    
                case "newspost":
                    if(!content.matches("\\s*"))
                    {
                        content=content.replaceAll("<br><br><img width='400' height='300' src='resources/images/general_Images/", "<_");
                        content=content.replaceAll("'/><br><br>", "_>");
                        content=content.replaceAll("<pre>", "<#");
                        content=content.replaceAll("</pre>", "#>");
                        npc.setNewsPost(content);
                        return "sumpisnews";
                    }                
                    else
                    {
                        model.addAttribute("sendingerror", "Cannot forward an empty file.");
                        return "inboxMessages";
                    }                
            }            
            return "inboxMessages";
        }        
        else
        {
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            model.addAttribute("expired", "Please login!");
            return "homepage";
        }
    }
}