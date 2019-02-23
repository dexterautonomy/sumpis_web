package com.teamwurkz.web;

import com.teamwurkz.domain.AdminBlogPostApproval;
import com.teamwurkz.domain.AdminControlClass;
import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.NewsPostClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MenuController
{
    @Autowired
    SignUpController succ;    
    @Autowired
    MessagingController msc;
    
    private Context ctx;
    private DataSource ds;
    private Connection con;
    
    @Value("${admin.name}")
    String admin;
    
    public MenuController()
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
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RequestMapping(value="/aboutus")
    public String menuAboutUs()
    {
        //statistics
        succ.statistics("ABOUTUS");
        return "aboutuspage";
    }
    
    @RequestMapping(value="/requestcontroller")//Just to bring the list of topics
    public String menuRequestController(@RequestParam("pg")int pg, @RequestParam("t")String t, 
    HttpSession session, HttpServletRequest req, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc)
    {
        ResultSet rs=null;
        
        try
        {
            session=req.getSession();
            List<Integer> idList=new LinkedList<>();
            List<String> dateList=new LinkedList<>();
            List<String> writerList=new LinkedList<>();
            List<String> titleList=new LinkedList<>();
            List<String> coverImageList=new LinkedList<>();
            List<String> likeList=new LinkedList<>();
            List<String> viewList=new LinkedList<>();
            
            model.addAttribute("t", t);
            model.addAttribute("pg", pg);
            
            switch(t)
            {
                case "writing":
                {
                    //statistics
                    succ.statistics("WRITING/ART");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Creative Writings");
                    model.addAttribute("blogheader", "Creative Writings");
                }
                break;
                case "arts":
                {
                    //statistics
                    succ.statistics("WRITING/ART");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Art/History");
                    model.addAttribute("blogheader", "Art and History");
                }
                break;
                case "health":
                {
                    //statistics
                    succ.statistics("HEALTH");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("HEALTH", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Healthy Living");
                    model.addAttribute("blogheader", "Health Tips");
                }
                break;
                case "diy":
                {
                    //statistics
                    succ.statistics("DIY");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("DIY", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "DIY Guides");
                    model.addAttribute("blogheader", "Do-It-Yourself");
                }
                break;
                case "relpol":
                {
                    //statistics
                    succ.statistics("RELIGION/POLITICS");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("RELIGION/POLITICS", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Religion/Politics");
                    model.addAttribute("blogheader", "World Religion and Politics");
                }
                break;
                case "entertainment":
                {
                    //statistics
                    succ.statistics("ENTERTAINMENT");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("ENTERTAINMENT", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Entertainment");
                    model.addAttribute("blogheader", "Entertainment");
                }
                break;
                case "sports":
                {
                    //statistics
                    succ.statistics("SPORTS");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("SPORTS", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "World Sports");
                    model.addAttribute("blogheader", "Sports");
                }
                break;
                case "info":
                {
                    //statistics
                    succ.statistics("TECH/UPDATES/INFO");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("TECH/UPDATES/INFO", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Updates");
                    model.addAttribute("blogheader", "Did You Know");
                }
                break;
                case "flt":
                {
                    //statistics
                    succ.statistics("LIFESTYLE/TRENDS/FASHION");
                    //Advert
                    List<List<String>> ad=succ.adAlgorithm("LIFESTYLE/TRENDS/FASHION", 6);
                    model.addAttribute("adid", ad.get(0));
                    model.addAttribute("adimg", ad.get(1));
                    
                    
                    model.addAttribute("blogpage", "Fashion, Lifestyle and Trends");
                    model.addAttribute("blogheader", "Fashion, Lifestyle and Trends");
                }
                break;                
            }
            
            int id=0;
            int setoff=12;            
            if(pg==1)
            {
                rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE tablex='"+t+"' ORDER BY postid DESC LIMIT " + 0 + ", " + setoff);
                while(rs.next())
                {
                    if(rs.getString(2).equals("ENABLED"))
                    {
                        id=rs.getInt(1);
                        idList.add(id);
                        dateList.add(rs.getString(4));
                        writerList.add(rs.getString(5));
                        titleList.add(rs.getString(6));
                        coverImageList.add(rs.getString(8));                    
                        likeList.add(rs.getString(9));
                        viewList.add(rs.getString(10));
                    }
                }
                model.addAttribute("n", idList);
                model.addAttribute("date", dateList);
                model.addAttribute("writer", writerList);
                model.addAttribute("title", titleList);
                model.addAttribute("coverImage", coverImageList);                
                model.addAttribute("like", likeList);
                model.addAttribute("view", viewList);
                
                if(id!=0)
                {
                    model.addAttribute("next", "<a style='text-decoration: none' href='requestcontroller?pg="+ ++pg+"&t="+t+"'>Next</a>");
                }
            }
            else
            {                
                rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE tablex='"+t+"' ORDER BY postid DESC LIMIT "+(pg-1)*setoff + ", " +setoff);
                while(rs.next())
                {
                    if(rs.getString(2).equals("ENABLED"))
                    {
                        id=rs.getInt(1);
                        idList.add(id);
                        dateList.add(rs.getString(4));
                        writerList.add(rs.getString(5));
                        titleList.add(rs.getString(6));
                        coverImageList.add(rs.getString(8));                    
                        likeList.add(rs.getString(9));
                        viewList.add(rs.getString(10));
                    }                    
                }
                model.addAttribute("n", idList);
                model.addAttribute("date", dateList);
                model.addAttribute("writer", writerList);
                model.addAttribute("title", titleList);
                model.addAttribute("coverImage", coverImageList);                
                model.addAttribute("like", likeList);
                model.addAttribute("view", viewList);
                
                model.addAttribute("prev", "<a style='text-decoration: none' href='requestcontroller?pg="+(pg-1)+"&t="+t+"'>Previous</a>&nbsp;&nbsp;&nbsp;");
                
                if(id!=0)
                {
                    model.addAttribute("next", "<a style='text-decoration: none' href='requestcontroller?pg="+ ++pg+"&t="+t+"'>Next</a>");
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
        return "blogpagez";
    }
    
    @RequestMapping(value="/handlerrequestcontroller")
    public String writingArtController(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn,
    @RequestParam("t")String t, @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc, ModelMap model, HttpSession session, HttpServletRequest req)
    {
        
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsLikers=null;
        PreparedStatement psIncrementViews=null;
        
        switch(t)
        {
            case "writing":
            {
                //statistics
                succ.statistics("WRITING/ART");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "arts":
            {
                //statistics
                succ.statistics("WRITING/ART");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "health":
            {
                //statistics
                succ.statistics("HEALTH");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("HEALTH", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "diy":
            {
                //statistics
                succ.statistics("DIY");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("DIY", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "relpol":
            {
                //statistics
                succ.statistics("RELIGION/POLITICS");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("RELIGION/POLITICS", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "entertainment":
            {
                //statistics
                succ.statistics("ENTERTAINMENT");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("ENTERTAINMENT", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "sports":
            {
                //statistics
                succ.statistics("SPORTS");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("SPORTS", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "info":
            {
                //statistics
                succ.statistics("TECH/UPDATES/INFO");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("TECH/UPDATES/INFO", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "flt":
            {
                //statistics
                succ.statistics("LIFESTYLE/TRENDS/FASHION");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("LIFESTYLE/TRENDS/FASHION", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;                
        }
        
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
            List<String> reportz=new LinkedList<>();
            List<String> pixList=new LinkedList<>();
            
            rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE postid="+n);//why not use INNER JOIN here to join blogcommentx table
            while(rs.next())
            {
                int views=rs.getInt(10);                    
                psIncrementViews=con.prepareStatement("UPDATE blogtable SET views="+ ++views+" WHERE postid="+n);
                psIncrementViews.executeUpdate();
                
                model.addAttribute("adminapproval", rs.getString(2));
                model.addAttribute("date", rs.getString(4));
                model.addAttribute("writer", rs.getString(5));
                model.addAttribute("headline", rs.getString(6));
                model.addAttribute("content", rs.getString(7));                
                model.addAttribute("coverimage", rs.getString(8));
                model.addAttribute("likesX", rs.getInt(9));
                model.addAttribute("viewsX", views);
                
                model.addAttribute("n", n);
                model.addAttribute("t", t);
                model.addAttribute("pg", pg);//for back to article list
                model.addAttribute("pgn", pgn);//for back to comment list
                    
                //this is for comments
                int setoff=15; //increase to 15 later
                int nID=0;
                if(pgn==1)//pagination
                {
                    rsLikers=con.createStatement().executeQuery("SELECT * FROM blogcommentx INNER JOIN registeredusers ON blogcommentx.username=registeredusers.username "
                    + "WHERE postid="+n+" AND approval='ENABLED' ORDER BY commentid ASC LIMIT "+0+","+setoff);
                    while(rsLikers.next())
                    {
                        nID=rsLikers.getInt(1);
                        cList.add(nID);
                        commentsUsername.add(rsLikers.getString(5));
                        commentsDate.add(rsLikers.getString(6));
                        mainComments.add(rsLikers.getString(7));
                        reportz.add(rsLikers.getString(8));
                        pixList.add(rsLikers.getString(20));
                    }
                    
                    model.addAttribute("cList", cList);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("pix", pixList);
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='rdr?pg="+pg+"&n="+n+"&pgn="+ ++pgn+"&t="+t+"'>Next</a>");
                    }
                }                    
                else
                {
                    rsLikers=con.createStatement().executeQuery("SELECT * FROM blogcommentx FROM blogcommentx INNER JOIN registeredusers ON blogcommentx.username=registeredusers.username "
                    + "WHERE postid="+n+" AND approval='ENABLED' ORDER BY commentid ASC LIMIT "+(pgn-1)*setoff+","+setoff);
                    while(rsLikers.next())
                    {
                        nID=rsLikers.getInt(1);
                        cList.add(nID);
                        commentsUsername.add(rsLikers.getString(5));
                        commentsDate.add(rsLikers.getString(6));
                        mainComments.add(rsLikers.getString(7));
                        reportz.add(rsLikers.getString(8));
                        pixList.add(rsLikers.getString(20));
                    }
                    
                    model.addAttribute("cList", cList);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("pix", pixList);
                        
                    model.addAttribute("prev", "<a style='text-decoration: none' href='rdr?pg="+pg+"&n="+n+"&pgn="+(pgn-1)+"&t="+t+"'>Previous</a>");
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='rdr?pg="+pg+"&n="+n+"&pgn="+ ++pgn+"&t="+t+"'>Next</a>");
                    }
                }
                    
                if(username!=null)
                {
                    rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                    while(rsAdminCheck.next())
                    {
                        if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                        && lastname.equals(rsAdminCheck.getString(3)))
                        {
                            model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                            model.addAttribute("eleventhBeanObject", new AdminControlClass());
                            model.addAttribute("adminBlogPostApproval", new AdminBlogPostApproval());
                            return "adminsumblogreaderpage";
                        }
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
                if(rsLikers!=null)
                {
                    rsLikers.close();
                }
                if(psIncrementViews!=null)
                {
                    psIncrementViews.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return "freeblogreader";
    }
    
    
    @RequestMapping(value="/rdr")//same as the method above, just that it does not increment views carelessly
    public String unwiseDiversion(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn,
    @RequestParam("t")String t, @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc, ModelMap model, HttpSession session, HttpServletRequest req)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsLikers=null;
        
        switch(t)
        {
            case "writing":
            {
                //statistics
                succ.statistics("WRITING/ART");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "arts":
            {
                //statistics
                succ.statistics("WRITING/ART");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "health":
            {
                //statistics
                succ.statistics("HEALTH");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("HEALTH", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "diy":
            {
                //statistics
                succ.statistics("DIY");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("DIY", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "relpol":
            {
                //statistics
                succ.statistics("RELIGION/POLITICS");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("RELIGION/POLITICS", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "entertainment":
            {
                //statistics
                succ.statistics("ENTERTAINMENT");
                //Advert
                List<List<String>> ad=succ.adAlgorithm("ENTERTAINMENT", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "sports":
            {
                List<List<String>> ad=succ.adAlgorithm("SPORTS", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "info":
            {
                List<List<String>> ad=succ.adAlgorithm("TECH/UPDATES/INFO", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;
            case "flt":
            {
                List<List<String>> ad=succ.adAlgorithm("LIFESTYLE/TRENDS/FASHION", 6);
                model.addAttribute("adid", ad.get(0));
                model.addAttribute("adimg", ad.get(1));
                
            }
            break;                
        }
        
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
            List<String> reportz=new LinkedList<>();
            List<String> pixList=new LinkedList<>();
            
            rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE postid="+n);            
            while(rs.next())
            {
                model.addAttribute("adminapproval", rs.getString(2));
                model.addAttribute("date", rs.getString(4));
                model.addAttribute("writer", rs.getString(5));
                model.addAttribute("headline", rs.getString(6));
                model.addAttribute("content", rs.getString(7));                
                model.addAttribute("coverimage", rs.getString(8));
                model.addAttribute("likesX", rs.getInt(9));
                model.addAttribute("viewsX", rs.getInt(10));
                
                model.addAttribute("n", n);
                model.addAttribute("t", t);
                model.addAttribute("pg", pg);//for back to article list
                model.addAttribute("pgn", pgn);//for back to comment list
                    
                //this is for comments
                int setoff=15; //make it 15 later
                int nID=0;
                if(pgn==1)//pagination
                {
                    rsLikers=con.createStatement().executeQuery("SELECT * FROM blogcommentx INNER JOIN registeredusers ON blogcommentx.username=registeredusers.username "
                    + "WHERE postid="+n+" AND approval='ENABLED' ORDER BY commentid ASC LIMIT "+0+","+setoff);
                    while(rsLikers.next())
                    {
                        nID=rsLikers.getInt(1);
                        cList.add(nID);
                        commentsUsername.add(rsLikers.getString(5));
                        commentsDate.add(rsLikers.getString(6));
                        mainComments.add(rsLikers.getString(7));
                        reportz.add(rsLikers.getString(8));
                        pixList.add(rsLikers.getString(20));
                    }
                        
                    model.addAttribute("cList", cList);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("pix", pixList);
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='rdr?pg="+pg+"&n="+n+"&pgn="+ ++pgn+"&t="+t+"'>Next</a>");
                    }
                }                    
                else
                {
                    rsLikers=con.createStatement().executeQuery("SELECT * FROM blogcommentx INNER JOIN registeredusers ON blogcommentx.username=registeredusers.username "
                    + "WHERE postid="+n+" AND approval='ENABLED' ORDER BY commentid ASC LIMIT "+(pgn-1)*setoff+","+setoff);            
                    while(rsLikers.next())
                    {
                        nID=rsLikers.getInt(1);
                        cList.add(nID);
                        commentsUsername.add(rsLikers.getString(5));
                        commentsDate.add(rsLikers.getString(6));
                        mainComments.add(rsLikers.getString(7));
                        reportz.add(rsLikers.getString(8));
                        pixList.add(rsLikers.getString(20));
                    }
                    
                    model.addAttribute("cList", cList);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("pix", pixList);
                        
                    model.addAttribute("prev", "<a style='text-decoration: none' href='rdr?pg="+pg+"&n="+n+"&pgn="+(pgn-1)+"&t="+t+"'>Previous</a>");
                        
                    if(nID!=0)
                    {
                        model.addAttribute("next", "<a style='text-decoration: none' href='rdr?pg="+pg+"&n="+n+"&pgn="+ ++pgn+"&t="+t+"'>Next</a>");
                    }
                }
                    
                if(username!=null)
                {
                    rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM admintable WHERE username='" + username + "'");
                    while(rsAdminCheck.next())
                    {
                        if(password.equals(rsAdminCheck.getString(5)) && firstname.equals(rsAdminCheck.getString(2)) 
                        && lastname.equals(rsAdminCheck.getString(3)))
                        {
                            model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                            model.addAttribute("eleventhBeanObject", new AdminControlClass());
                            model.addAttribute("adminBlogPostApproval", new AdminBlogPostApproval());
                            return "adminsumblogreaderpage";
                        }
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
                if(rsLikers!=null)
                {
                    rsLikers.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "freeblogreader";
    }
    
    @RequestMapping(value="/likeCounter")
    public String likeCounter(@RequestParam("pg")int pg, @RequestParam("n")int n, @RequestParam("pgn")int pgn, 
    @RequestParam("t")String t, @RequestParam("action")String action, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    ModelMap model, HttpSession session, @ModelAttribute("firstBeanObject")FieldClass fc, RedirectAttributes ra, 
    @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("adminBlogPostApproval")AdminBlogPostApproval abpa, 
    HttpServletRequest req)
    {
        ResultSet rs=null;
        ResultSet rsAdminCheck=null;
        ResultSet rsLikers=null;
        PreparedStatement psIncrementViews=null;
        
        try
        {
            session=req.getSession();
            String username=(String)session.getAttribute("username");
            
            if(username!=null)
            {
                switch(action)
                {
                    case "like":
                    {
                        rsLikers=con.createStatement().executeQuery("SELECT * FROM bloglikes WHERE postid="+n);
                        while(rsLikers.next())
                        {                            
                            if(username.equals(rsLikers.getString(3)) && rsLikers.getString(4).equals("LIKED"))                                
                            {
                                ra.addFlashAttribute("errorQQ", "You can only like once.");
                                return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
                            }
                        }
                        
                        if(username.equals(admin))//For promotion
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE postid="+n);
                            while(rs.next())
                            {
                                int likesXX=rs.getInt(9)+623;
                                int viewsXX=rs.getInt(10)+977;
                                psIncrementViews=con.prepareStatement("UPDATE blogtable SET likes="+ likesXX +", views="+ viewsXX +" WHERE postid="+n);
                                psIncrementViews.executeUpdate();
                            }
                            ra.addFlashAttribute("errorQQ", "Liked");
                        }
                        else
                        {
                            saveBlogLikes(n, username, "LIKED");
                            rs=con.createStatement().executeQuery("SELECT * FROM blogtable WHERE postid="+n);
                            while(rs.next())
                            {
                                int likesXX=rs.getInt(9);
                                psIncrementViews=con.prepareStatement("UPDATE blogtable SET likes="+ ++likesXX +" WHERE postid="+n);
                                psIncrementViews.executeUpdate();
                            }
                            ra.addFlashAttribute("errorQQ", "Liked");
                        }
                    }
                    break;
                    
                    case "comment":
                    {
                        switch(t)
                        {
                            case "writing":
                            {
                                //statistics
                                succ.statistics("WRITING/ART");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "arts":
                            {
                                //statistics
                                succ.statistics("WRITING/ART");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("WRITING/ART", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "health":
                            {
                                //statistics
                                succ.statistics("HEALTH");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("HEALTH", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "diy":
                            {
                                //statistics
                                succ.statistics("DIY");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("DIY", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "relpol":
                            {
                                //statistics
                                succ.statistics("RELIGION/POLITICS");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("RELIGION/POLITICS", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "entertainment":
                            {
                                //statistics
                                succ.statistics("ENTERTAINMENT");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("ENTERTAINMENT", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "sports":
                            {
                                //statistics
                                succ.statistics("SPORTS");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("SPORTS", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "info":
                            {
                                //statistics
                                succ.statistics("TECH/UPDATES/INFO");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("TECH/UPDATES/INFO", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;
                            case "flt":
                            {
                                //statistics
                                succ.statistics("LIFESTYLE/TRENDS/FASHION");
                                //Advert
                                List<List<String>> ad=succ.adAlgorithm("LIFESTYLE/TRENDS/FASHION", 6);
                                model.addAttribute("adid", ad.get(0));
                                model.addAttribute("adimg", ad.get(1));
                                
                            }
                            break;                
                        }
                        try
                        {
                            rsAdminCheck=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
                            while(rsAdminCheck.next())
                            {
                                if(rsAdminCheck.getString(7)==null)//FOR SUSPENSION PURPOSE
                                {
                                    model.addAttribute("pg", pg);
                                    model.addAttribute("n", n);
                                    model.addAttribute("pgn", pgn);
                                    model.addAttribute("t", t);
                                    model.addAttribute("fourteenthBeanObject", new NewsPostClass());
                                    model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                                    model.addAttribute("friendrequest", succ.getFriendRequest(username));
                                    return "tablePage";
                                }
                            }
                            ra.addFlashAttribute("errorQQ", "Try again later.");
                        }
                        catch (SQLException ex)
                        {
                            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
            }
            else
            {
                ra.addFlashAttribute("errorQQ", "Please login. Thank you!");
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
                if(rsLikers!=null)
                {
                    rsLikers.close();
                }
                if(psIncrementViews!=null)
                {
                    psIncrementViews.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "redirect:/rdr?pg="+pg+"&n="+n+"&pgn="+pgn+"&t="+t;
    }
    
    protected void saveLikes(int a, int b, String c, String d, String e, String f)
    {
        PreparedStatement ps=null;
        
        try
        {            
            ps=con.prepareStatement("INSERT INTO newsreact (postid, commentid, username, liked, disliked, shared) VALUES(?,?,?,?,?,?)");            
            ps.setInt(1, a);
            ps.setInt(2, b);
            ps.setString(3, c);
            ps.setString(4, d);
            ps.setString(5, e);
            ps.setString(6, f);
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    protected void saveBlogLikes(int a, String b, String c)
    {
        PreparedStatement ps=null;
        
        try
        {            
            ps=con.prepareStatement("INSERT INTO bloglikes (postid, username, liked) VALUES(?,?,?)");            
            ps.setInt(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
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
}