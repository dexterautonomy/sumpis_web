package com.teamwurkz.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AndroidController
{
    @Value("${imagepath.proploc}")
    String pathFromPropertiesFile;
    @Autowired
    SignUpController succ;    
    @Autowired
    ClientController ccAndroid;
    @Autowired
    MenuController mk;
    @Autowired
    LoginClass lc;
    private DataSource dsAndroid;
    private Connection conAndroid;
    private Statement sAndroid;
    private ResultSet rsAndroid1;
    private ResultSet rsAndroid2;
    private ResultSet rsAndroid3;
    
    private PreparedStatement psAndroid1;
    private PreparedStatement psAndroid2;
    private Context ctxAndroid;    
    @Value("${admin.name}")
    String admin;
    
    //private final Charset ISO=Charset.forName("ISO-8859-1");
    //private final Charset UTF=Charset.forName("UTF-8");
    
    public AndroidController()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");            
            ctxAndroid=new InitialContext();
            dsAndroid=(DataSource)ctxAndroid.lookup("java:/comp/env/jdbc/sumpisdatabase");
            conAndroid=dsAndroid.getConnection();            
            sAndroid=conAndroid.createStatement();
        }        
        catch (ClassNotFoundException | NamingException | SQLException ex) 
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RequestMapping(value="/extlogin/{username}/{password}", method=RequestMethod.POST)
    @ResponseBody
    public String androidLogin(@PathVariable("username")String username, @PathVariable("password")String password)
    {
        Date d1=new Date();
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM registeredusers");                
            while(rsAndroid1.next())
            {    
                if(username.equals(rsAndroid1.getString(1)))
                {                  
                    if(password.equals(rsAndroid1.getString(5)))
                    {
                        if(rsAndroid1.getString(6)==null)
                        {
                            String subscriptionDate=rsAndroid1.getString(9);
                            Date d2=new SimpleDateFormat("dd MMMM yyyy,  hh:mm:ss a").parse(subscriptionDate);
                                    
                            //check for subscription
                            if(d1.compareTo(d2)==-1) //you have subscription
                            {
                                //check if user with live subscription is a sumblogger
                                rsAndroid2=sAndroid.executeQuery("SELECT * FROM freelancetable WHERE username='"+username+"'");
                                while(rsAndroid2.next())
                                {
                                    if(username.equals(rsAndroid2.getString(2)) && rsAndroid2.getString(3).equals("") && rsAndroid2.getString(4).equals(""))
                                    {
                                        return "s_s";//sumblogger and subscription alive
                                    }
                                }
                                return "n_s";//not sumblogger but subscription dey
                            }
                            else if(d1.compareTo(d2)==1) //subscription has expired
                            {
                                //check if user with expired subscription is a sumblogger
                                rsAndroid2=sAndroid.executeQuery("SELECT * FROM freelancetable WHERE username='"+username+"'");
                                while(rsAndroid2.next())
                               {
                                    if(username.equals(rsAndroid2.getString(2)) && rsAndroid2.getString(3).equals("") && rsAndroid2.getString(4).equals(""))
                                    {
                                        return "s_n";//sumblogger and no subscription
                                    }
                                }
                                return "n_n";//not sumblogger and subscription NO dey
                            }
                        }
                        else if(rsAndroid1.getString(6).equals("suspended"))
                        {
                            return "login_err";
                        }                                
                    }                    
                    else
                    {
                        return "pswd_err";
                    }
                }
            }
            return "uname_err";
        }        
        catch (SQLException | ParseException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @RequestMapping(value="androidnews", method=RequestMethod.POST)
    @ResponseBody
    public String extNews()
    {
        String all="";
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM newstable ORDER BY serial DESC LIMIT "+0+","+30);
            while(rsAndroid1.next())
            {                        
                if(rsAndroid1.getString(7).equals("verified"))
                {
                    String title=rsAndroid1.getString(2);
                    String date=rsAndroid1.getString(5);
                    String both=title + "\r\n\r\n" + date;
                    
                    all +=both + "#";
                    
                    /*String title=rs.getString(2);
                    String date=rs.getString(5);
                    String both=title + "##" + date;
                    
                    all +=both + "//";*/
                    
                    //all +=rs.getString(2)+"//"+rs.getString(5)+"//";
                }
            }    
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }
    
    /*
    @RequestMapping(value="androidblog/{title}/{table}", method=RequestMethod.POST)
    @ResponseBody
    public String extBlog(@PathVariable("title")String title, @PathVariable("table")String blogTable)
    {
        String all=title+ "<";
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM "+blogTable+" ORDER BY serial DESC LIMIT "+0+","+30);
            while(rsAndroid1.next())
            {
                if(rsAndroid1.getString(11).equals("verified"))
                {
                    String blogtitle=rsAndroid1.getString(3);
                    String date=rsAndroid1.getString(5);
                    String both=blogtitle + "\r\n\r\n" + date;
                    
                    all +=both + "#";
                }
            }    
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }
    
    @RequestMapping(value="andwx")
    public String androidWebView(ModelMap model, @RequestParam("news")String headline, @RequestParam("username")String username,   
    @RequestParam("date")String date, @RequestParam("id")int pageNumb, @RequestParam("pgn")int pgnX)
    {
        try
        {
            List<String> headlinelist=new LinkedList<>();
            List<String> commentsUsername=new LinkedList<>();
            List<String> commentsDate=new LinkedList<>();
            List<String> mainComments=new LinkedList<>();
            List<String> commentLikes=new LinkedList<>();
            List<String> commentsDislikes=new LinkedList<>();
            List<String> commentsShares=new LinkedList<>();
            List<String> quotez=new LinkedList<>();
            List<String> reportz=new LinkedList<>();
            List<String> device=new LinkedList<>();
            
            int setoff=3;
            
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM newstable WHERE title='"+headline+"' AND datetime='"+date+"'");            
            while(rsAndroid1.next())
            {
                model.addAttribute("writer", rsAndroid1.getString(6));
                model.addAttribute("headline", headline);
                model.addAttribute("date", date);
                model.addAttribute("story", rsAndroid1.getString(3));
                model.addAttribute("source", rsAndroid1.getString(4));
                model.addAttribute("id", pageNumb);
                model.addAttribute("current", pageNumb);
                model.addAttribute("pgn", pgnX);
                    
                    //this is for comments
                String title="";
                if(pgnX==1)//pagination
                {
                    rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE headline='"+headline+"' AND datex='"+date+"' "
                    + "AND approval='approved' ORDER BY serial ASC LIMIT "+0+","+setoff);
                    while(rsAndroid2.next())
                    {
                        title=rsAndroid2.getString(4);
                        headlinelist.add(title);
                        commentsUsername.add(rsAndroid2.getString(2));
                        commentsDate.add(rsAndroid2.getString(6));
                        mainComments.add(rsAndroid2.getString(7));
                        commentLikes.add(rsAndroid2.getString(9));
                        commentsDislikes.add(rsAndroid2.getString(10));
                        commentsShares.add(rsAndroid2.getString(11));
                        quotez.add(rsAndroid2.getString(12));
                        reportz.add(rsAndroid2.getString(13));
                        device.add(rsAndroid2.getString(15));
                    }
                    
                    model.addAttribute("usernameAndroid", username);
                    model.addAttribute("title", headlinelist);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("commentLikes", commentLikes);
                    model.addAttribute("commentsDislikes", commentsDislikes);
                    model.addAttribute("commentsShares", commentsShares);
                    model.addAttribute("quotez", quotez);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("device", device);
                        
                    if(title.equals(""))
                    {
                        model.addAttribute("next", "");
                    }
                    else
                    {
                        model.addAttribute("next", "<a href='andwx?news="+headline+"&username="+username+"&date="+date+"&id="+pageNumb+"&pgn="+ ++pgnX+"'>Next</a>");
                    }
                }
                else
                {
                    rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE headline='"+headline+"' AND datex='"+date+"' "
                    + "AND approval='approved' ORDER BY serial ASC LIMIT "+(pgnX-1)*setoff+","+setoff);            
                    while(rsAndroid2.next())
                    {
                        title=rsAndroid2.getString(4);
                        headlinelist.add(title);
                        commentsUsername.add(rsAndroid2.getString(2));
                        commentsDate.add(rsAndroid2.getString(6));
                        mainComments.add(rsAndroid2.getString(7));
                        commentLikes.add(rsAndroid2.getString(9));
                        commentsDislikes.add(rsAndroid2.getString(10));
                        commentsShares.add(rsAndroid2.getString(11));
                        quotez.add(rsAndroid2.getString(12));
                        reportz.add(rsAndroid2.getString(13));
                        device.add(rsAndroid2.getString(15));
                    }
                    
                    model.addAttribute("usernameAndroid", username);
                    model.addAttribute("title", headlinelist);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("commentLikes", commentLikes);
                    model.addAttribute("commentsDislikes", commentsDislikes);
                    model.addAttribute("commentsShares", commentsShares);
                    model.addAttribute("quotez", quotez);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("device", device);
                    
                    model.addAttribute("prev", "<a href='andwx?news="+headline+"&username="+username+"&date="+date+"&id="+pageNumb+"&pgn="+(pgnX-1)+"'>Previous</a>");
                        
                    if(title.equals(""))
                    {
                        model.addAttribute("next", "");
                    }
                    else
                    {
                        model.addAttribute("next", "<a href='andwx?news="+headline+"&username="+username+"&date="+date+"&id="+pageNumb+"&pgn="+ ++pgnX+"'>Next</a>");
                    }
                }
            }    
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!username.equals(""))
        {
            return "androidnewspage";
        }
        else
            return "androidnewspageplain";
    }
    
    
    @RequestMapping(value="/androidCommentAction", method=RequestMethod.POST)
    public String commentAction(@RequestParam("writer")String writer, @RequestParam("username")String username, 
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("id")int pageNumb, 
    @RequestParam("pgn")int pgnX, @RequestParam("date2")String date2, @RequestParam("writer2")String writer2, 
    @RequestParam("subcomment")String subcomment, @RequestParam("choice")String choice, ModelMap model)
    {
        int kk=0;
            switch(choice)
            {
                case "like":
                {
                    try
                    {
                        rsAndroid1=sAndroid.executeQuery("SELECT * FROM liketable WHERE title='"+headline+"' AND dateofpost='"+date2+"' AND writer='"+writer2+"'");
                        while(rsAndroid1.next())
                        {
                            if(username.equals(rsAndroid1.getString(2)) && rsAndroid1.getString(6).equals("LIKED"))
                            {
                                return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                            }
                            else if(username.equals(rsAndroid1.getString(2)) && rsAndroid1.getString(7).equals("DISLIKED"))
                            {
                                return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                            }
                        }
                        mk.saveLikes(username, writer2, headline, date2, "LIKED", "", "");
                        
                        rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE username='"+writer2+"' AND datey='"+date2+"'");
                        while(rsAndroid2.next())
                        {
                            kk=rsAndroid2.getInt(9);                            
                        }                        
                        ccAndroid.updateSubComment(writer2, "liked", ++kk, date2);
                        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                case "dislike":
                {
                    try
                    {
                        rsAndroid1=sAndroid.executeQuery("SELECT * FROM liketable WHERE title='"+headline+"' AND dateofpost='"+date2+"' AND writer='"+writer2+"'");
                        while(rsAndroid1.next())
                        {
                            if(username.equals(rsAndroid1.getString(2)) && rsAndroid1.getString(7).equals("DISLIKED"))
                            {
                                return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                            }
                            else if(username.equals(rsAndroid1.getString(2)) && rsAndroid1.getString(6).equals("LIKED"))
                            {
                                return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                            }
                        }
                        mk.saveLikes(username, writer2, headline, date2, "", "DISLIKED", "");
                        
                        rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE username='"+writer2+"' AND datey='"+date2+"'");
                        while(rsAndroid2.next())
                        {
                            kk=rsAndroid2.getInt(10);                            
                        }
                        ccAndroid.updateSubComment(writer2, "disliked", ++kk, date2);
                        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                case "share":
                {
                    try
                    {
                        rsAndroid1=sAndroid.executeQuery("SELECT * FROM liketable WHERE title='"+headline+"' AND dateofpost='"+date2+"' AND writer='"+writer2+"'");
                        while(rsAndroid1.next())
                        {
                            if(username.equals(rsAndroid1.getString(2)) && rsAndroid1.getString(8).equals("SHARED"))
                            {
                                return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                            }
                        }
                        mk.saveLikes(username, writer2, headline, date2, "", "", "SHARED");
                        
                        rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE username='"+writer2+"' AND datey='"+date2+"'");
                        while(rsAndroid2.next())
                        {
                            kk=rsAndroid2.getInt(11);                            
                        }
                        ccAndroid.updateSubComment(writer2, "shared", ++kk, date2);
                        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                case "quote":
                {
                    /model.addAttribute("writer", writer);
                    model.addAttribute("username", username);
                    model.addAttribute("headline", headline);
                    model.addAttribute("date", date);
                    model.addAttribute("pageNumb", pageNumb);
                    model.addAttribute("pgn", pgnX);
                    model.addAttribute("commenterdate", date2);
                    model.addAttribute("commenter", writer2);
                    model.addAttribute("space", "@"+writer2 +" ["+ date2+"]");
                    model.addAttribute("subcomment", subcomment);
                    return "quotePageAndroid";/
                    
    
                    try
                    {
                        rsAndroid1=sAndroid.executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
                        while(rsAndroid1.next())
                        {
                            if(username.equals(rsAndroid1.getString(1)))
                            {                        
                                if(rsAndroid1.getString(7)==null)//FOR SUSPENSION PURPOSE
                                {
                                    model.addAttribute("writer", writer);
                                    model.addAttribute("username", username);
                                    model.addAttribute("headline", headline);
                                    model.addAttribute("date", date);
                                    model.addAttribute("pageNumb", pageNumb);
                                    model.addAttribute("pgn", pgnX);
                                    model.addAttribute("commenterdate", date2);
                                    model.addAttribute("commenter", writer2);
                                    model.addAttribute("space", "@"+writer2 +" ["+ date2+"]");
                                    model.addAttribute("subcomment", subcomment);
                                    return "quotePageAndroid";
                                }
                                else
                                {
                                    return "androidbanpage";
                                }
                            }
                        }
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(AndroidController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                case "report":
                {
                    try
                    {
                        String badStoryCommentOrQuote="";
                        rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE username='"+writer2+"' AND datey='"+date2+"'");
                        while(rsAndroid2.next())
                        {
                            badStoryCommentOrQuote=rsAndroid2.getString(7);
                            kk=rsAndroid2.getInt(13);                            
                        }
                        ccAndroid.updateSubComment(writer2, "report", ++kk, date2);
                        String reportLink="<a style=\"font-weight: bold; text-align: center; color: purple\" href=\"getstory?headline="
                        +headline+"&writer="+writer+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"\">Report Link</a>";
                        
                        ccAndroid.saveClientWork(username, admin, "Reporting @"+writer2, "<div style=\"font-weight: bold; font-size:22px; text-align: center; color: red\">"
                        +headline+"</div>"+ badStoryCommentOrQuote +"<br><br>"+ reportLink, reportLink, new Date().toString(), "unread");
                                                
                        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }
        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&pgn="+pgnX+"&username="+username;
    }
    
    @RequestMapping(value="/submitQuoteAndroid", method=RequestMethod.POST)
    public String submitquoteandroid(@RequestParam("writer")String writer, @RequestParam("username")String username,
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("id")String id, 
    @RequestParam("pgn")String pgn, @RequestParam("date2")String date2, @RequestParam("space")String space, 
    @RequestParam("choice")String action, @RequestParam("quotedtext")String qoutedtext, @RequestParam("quotedtext2")String qoutedtext2,
    @RequestParam("qoutingtext")String qoutingtext, ModelMap model)
    {        
        String quoteDetails="<div style=\"background-color: rgb(255,215,229); padding: 0px; border-radius: 2px; margin-bottom: 5px;\">"
        + "<div style=\"background-color: rgb(255,215,229); border-radius: 2px; padding: 3px; border-bottom: 1px solid grey; font-weight: bold;\">"+space+"</div>"
        + "<div style=\"text-align: justify; padding: 5px;\">"+qoutedtext+"</div>"
        + "</div>";
        
        switch(action)
        {
            case "post_Story":
            {
                if(qoutedtext.matches("\\s*"))
                {
                    break;
                }
                else if(!qoutedtext2.contains(qoutedtext.trim()))
                {
                    break;
                }
                else if(qoutingtext.matches("\\s*"))
                {
                    break;
                }
                else
                {
                    ccAndroid.saveComments(username, writer, headline, date, date2, qoutingtext, "approved", quoteDetails, "", "Mobile");
                }
            }
        }
        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+id+"&username="+username+"&pgn="+pgn;
    }
    
    @RequestMapping(value="/androidCommentaryController", method=RequestMethod.POST)
    public String commentaryAndroid(@RequestParam("writer")String writer, @RequestParam("username")String username, 
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("id")int pageNumb, 
    @RequestParam("pgn")int pgnX, ModelMap model)
    {
        try
        {
            model.addAttribute("writer", writer);
            model.addAttribute("username", username);
            model.addAttribute("headline", headline);
            model.addAttribute("date", date);
            model.addAttribute("id", pageNumb);
            model.addAttribute("pgn", pgnX);
            
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
            while(rsAndroid1.next())
            {
                if(username.equals(rsAndroid1.getString(1)))
                {                        
                    if(rsAndroid1.getString(7)==null)//FOR SUSPENSION PURPOSE
                    {
                        return "commentaryPageAndroid";
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&username="+username+"&pgn="+pgnX;
        return "androidbanpage";
    }
    
    @RequestMapping(value="/androidSubmitCommentary", method=RequestMethod.POST)
    public String submitcommentaryAndroid(@RequestParam("writer")String writer, @RequestParam("username")String username, 
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("id")int pageNumb, 
    @RequestParam("pgn")int pgnX, @RequestParam("date2")String date2, @RequestParam("comment")String comment, 
    @RequestParam("choice")String choice, ModelMap model)
    {            
        switch(choice)
        {
            case "post_Story":
            {
                if(!comment.matches("\\s*"))
                {
                    ccAndroid.saveComments(username, writer, headline, date, date2, comment, "approved", "", "", "Mobile");
                }
                else
                    break;
            }
        }
        return "redirect:/andwx?news="+headline+"&date="+date+"&id="+pageNumb+"&username="+username+"&pgn="+pgnX;
    }

    
    
    @RequestMapping(value="/andblog")
    public String androidBlogView(@RequestParam("username")String username, @RequestParam("blog")String headline, 
    @RequestParam("date")String date, @RequestParam("id")int id, @RequestParam("pgn")int pgn, 
    @RequestParam("table")String table, ModelMap model)
    {
        try
        {            
            List<String> headlinelist=new LinkedList<>();
            List<String> commentsUsername=new LinkedList<>();
            List<String> commentsDate=new LinkedList<>();
            List<String> mainComments=new LinkedList<>();
            List<String> commentLikes=new LinkedList<>();
            List<String> commentsDislikes=new LinkedList<>();
            List<String> commentsShares=new LinkedList<>();
            List<String> quotez=new LinkedList<>();
            List<String> reportz=new LinkedList<>();
            List<String> device=new LinkedList<>();
            
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM "+table+" WHERE title='"+headline+"' AND dateofpost='"+date+"'");            
            while(rsAndroid1.next())
            {
                int likeXX=rsAndroid1.getInt(7);
                int viewsXX=rsAndroid1.getInt(8);
                
                psAndroid1=conAndroid.prepareStatement("UPDATE "+table+" SET views="+ ++viewsXX+" WHERE title='"+headline+"' AND dateofpost='"+date+"'");
                psAndroid1.executeUpdate();
                
                    //this is for calculating like-view ratio(instantly) from the view side when you view or click on the story
                float ratio=(float)likeXX/viewsXX;
                psAndroid2=conAndroid.prepareStatement("UPDATE "+table+" SET like_view_ratio="+ratio+" WHERE title='"+headline+"' AND dateofpost='"+date+"'");
                psAndroid2.executeUpdate();
                
                model.addAttribute("username", username);
                model.addAttribute("writer", rsAndroid1.getString(2));
                model.addAttribute("headline", headline);
                model.addAttribute("content", rsAndroid1.getString(4));
                model.addAttribute("date", date);
                //model.addAttribute("coverimage", rsAndroid1.getString(6));                    
                //model.addAttribute("ratioX", rsAndroid1.getString(9));
                //model.addAttribute("adminapproval", rsAndroid1.getString(11));
                //model.addAttribute("likeObject", new LikeObject());
                model.addAttribute("likesX", likeXX);
                model.addAttribute("viewsX", viewsXX);
                model.addAttribute("table", table);
                //model.addAttribute("pageX", pageX);
                model.addAttribute("id", id);//for back to comment list
                model.addAttribute("pgn", pgn);
                    
                    //this is for comments
                    int setoff=2;
                    String title="";
                    if(id==1)  //pagination
                    {
                        rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE headline='"+headline+"' AND datex='"+date+"' "
                        + "AND approval='approved' AND tablex='"+table+"' ORDER BY serial ASC LIMIT "+0+","+setoff);
                        while(rsAndroid2.next())
                        {
                            title=rsAndroid2.getString(4);                            
                            headlinelist.add(title);
                            commentsUsername.add(rsAndroid2.getString(2));
                            commentsDate.add(rsAndroid2.getString(6));
                            mainComments.add(rsAndroid2.getString(7));
                            commentLikes.add(rsAndroid2.getString(9));
                            commentsDislikes.add(rsAndroid2.getString(10));
                            commentsShares.add(rsAndroid2.getString(11));
                            quotez.add(rsAndroid2.getString(12));
                            reportz.add(rsAndroid2.getString(13));
                            device.add(rsAndroid2.getString(15));
                        }
                        
                        model.addAttribute("title", headlinelist);
                        model.addAttribute("commentsUsername", commentsUsername);
                        model.addAttribute("commentsDate", commentsDate);
                        model.addAttribute("mainComments", mainComments);
                        model.addAttribute("commentLikes", commentLikes);
                        model.addAttribute("commentsDislikes", commentsDislikes);
                        model.addAttribute("commentsShares", commentsShares);
                        model.addAttribute("quotez", quotez);
                        model.addAttribute("reportz", reportz);
                        model.addAttribute("device", device);
                        
                        if(title.equals(""))
                        {
                            model.addAttribute("next", "");
                        }
                        else
                        {
                            model.addAttribute("next", "<a href='androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+ ++id+"&table="+table+"&pgn="+pgn+"'>Next</a>");
                        }
                        
                        //if(!username.equals(""))
                        //{
                        //    return "androidfreeblogreader";
                        //}
                    }                    
                    else
                    {
                        rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE headline='"+headline+"' AND datex='"+date+"' "
                        + "AND approval='approved' AND tablex='"+table+"' ORDER BY serial ASC LIMIT "+(id-1)*setoff+","+setoff);            
                        while(rsAndroid2.next())
                        {
                            title=rsAndroid2.getString(4);                            
                            headlinelist.add(title);
                            commentsUsername.add(rsAndroid2.getString(2));
                            commentsDate.add(rsAndroid2.getString(6));
                            mainComments.add(rsAndroid2.getString(7));
                            commentLikes.add(rsAndroid2.getString(9));
                            commentsDislikes.add(rsAndroid2.getString(10));
                            commentsShares.add(rsAndroid2.getString(11));
                            quotez.add(rsAndroid2.getString(12));
                            reportz.add(rsAndroid2.getString(13));
                            device.add(rsAndroid2.getString(15));
                        }
                    
                        model.addAttribute("title", headlinelist);
                        model.addAttribute("commentsUsername", commentsUsername);
                        model.addAttribute("commentsDate", commentsDate);
                        model.addAttribute("mainComments", mainComments);
                        model.addAttribute("commentLikes", commentLikes);
                        model.addAttribute("commentsDislikes", commentsDislikes);
                        model.addAttribute("commentsShares", commentsShares);
                        model.addAttribute("quotez", quotez);
                        model.addAttribute("reportz", reportz);
                        model.addAttribute("device", device);
                        
                        model.addAttribute("prev", "<a href='androiidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+(pgn-1)+"&table="+table+"&pgn="+pgn+"'>Previous</a>");
                        
                        if(title.equals(""))
                        {
                            model.addAttribute("next", "");
                        }
                        else
                        {                            
                            model.addAttribute("next", "<a href='androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+ ++pgn+"&table="+table+"&pgn="+pgn+"'>Next</a>");
                        }
                        
                        //if(!username.equals(""))
                        //{
                          //  return "androidfreeblogreader";
                        //}
                }   
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!username.equals(""))
        {
            return "androidfreeblogreader";
        }
        else
            return "androidfreeblogreaderplain";
    }
    
    @RequestMapping(value="/androidrdr")//same as the method above, just that it does not increment views carelessly
    public String unwiseDiversionAndroid(@RequestParam("username")String username, @RequestParam("blog")String headline, 
    @RequestParam("date")String date, @RequestParam("id")int id, @RequestParam("pgn")int pgn, @RequestParam("table")String table, 
    ModelMap model)
    {
        try
        {            
            List<String> headlinelist=new LinkedList<>();
            List<String> commentsUsername=new LinkedList<>();
            List<String> commentsDate=new LinkedList<>();
            List<String> mainComments=new LinkedList<>();
            List<String> commentLikes=new LinkedList<>();
            List<String> commentsDislikes=new LinkedList<>();
            List<String> commentsShares=new LinkedList<>();
            List<String> quotez=new LinkedList<>();
            List<String> reportz=new LinkedList<>();
            List<String> device=new LinkedList<>();
            
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM "+table+" WHERE title='"+headline+"' AND dateofpost='"+date+"'");            
            while(rsAndroid1.next())
            {
                int likeXX=rsAndroid1.getInt(7);
                int viewsXX=rsAndroid1.getInt(8);
                float ratio=(float)likeXX/viewsXX;
                
                model.addAttribute("username", username);
                model.addAttribute("writer", rsAndroid1.getString(2));
                model.addAttribute("headline", headline);
                model.addAttribute("content", rsAndroid1.getString(4));
                model.addAttribute("date", date);
                //model.addAttribute("coverimage", rsAndroid1.getString(6));                    
                //model.addAttribute("ratioX", rsAndroid1.getString(9));
                //model.addAttribute("remittance", rsAndroid1.getString(10));
                //model.addAttribute("adminapproval", rsAndroid1.getString(11));
                //model.addAttribute("likeObject", new LikeObject());
                model.addAttribute("likesX", likeXX);
                model.addAttribute("viewsX", viewsXX);
                model.addAttribute("table", table);
                //model.addAttribute("pageX", pageX);
                //model.addAttribute("backToMenuList", pageNumb);//for back to article list
                model.addAttribute("id", id);//for back to comment list
                model.addAttribute("pgn", pgn);
                    
                    //this is for comments
                int setoff=2;
                String title="";
                if(id==1)//pagination
                {
                    rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE headline='"+headline+"' AND datex='"+date+"' "
                    + "AND approval='approved' AND tablex='"+table+"' ORDER BY serial ASC LIMIT "+0+","+setoff);
                    while(rsAndroid2.next())
                    {
                        title=rsAndroid2.getString(4);                            
                        headlinelist.add(title);
                        commentsUsername.add(rsAndroid2.getString(2));
                        commentsDate.add(rsAndroid2.getString(6));
                        mainComments.add(rsAndroid2.getString(7));
                        commentLikes.add(rsAndroid2.getString(9));
                        commentsDislikes.add(rsAndroid2.getString(10));
                        commentsShares.add(rsAndroid2.getString(11));
                        quotez.add(rsAndroid2.getString(12));
                        reportz.add(rsAndroid2.getString(13));
                        device.add(rsAndroid2.getString(15));
                    }
                        
                    model.addAttribute("title", headlinelist);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("commentLikes", commentLikes);
                    model.addAttribute("commentsDislikes", commentsDislikes);
                    model.addAttribute("commentsShares", commentsShares);
                    model.addAttribute("quotez", quotez);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("device", device);
                        
                    if(title.equals(""))
                    {
                        model.addAttribute("next", "");
                    }
                    else
                    {                            
                        model.addAttribute("next", "<a href='androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+ ++id+"&pgn="
                        +pgn+"&table="+table+"'>Next</a>");
                    }
                    
                    //if(!username.equals(""))
                    //{
                      //  return "androidfreeblogreader";
                    //}
                }                    
                else
                {
                    rsAndroid2=sAndroid.executeQuery("SELECT * FROM comments WHERE headline='"+headline+"' AND datex='"+date+"' "
                    + "AND approval='approved' AND tablex='"+table+"' ORDER BY serial ASC LIMIT "+(id-1)*setoff+","+setoff);            
                    while(rsAndroid2.next())
                    {
                       title=rsAndroid2.getString(4);                            
                        headlinelist.add(title);
                        commentsUsername.add(rsAndroid2.getString(2));
                        commentsDate.add(rsAndroid2.getString(6));
                        mainComments.add(rsAndroid2.getString(7));
                        commentLikes.add(rsAndroid2.getString(9));
                        commentsDislikes.add(rsAndroid2.getString(10));
                        commentsShares.add(rsAndroid2.getString(11));
                        quotez.add(rsAndroid2.getString(12));
                        reportz.add(rsAndroid2.getString(13));
                        device.add(rsAndroid2.getString(15));
                    }
                    
                    model.addAttribute("title", headlinelist);
                    model.addAttribute("commentsUsername", commentsUsername);
                    model.addAttribute("commentsDate", commentsDate);
                    model.addAttribute("mainComments", mainComments);
                    model.addAttribute("commentLikes", commentLikes);
                    model.addAttribute("commentsDislikes", commentsDislikes);
                    model.addAttribute("commentsShares", commentsShares);
                    model.addAttribute("quotez", quotez);
                    model.addAttribute("reportz", reportz);
                    model.addAttribute("device", device);
                        
                    model.addAttribute("prev", "<a href='androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+(id-1)+"&pgn="
                    +pgn+"&table="+table+"'>Previous</a>");
                    
                    if(title.equals(""))
                    {
                        model.addAttribute("next", "");
                    }
                    else
                    {
                        model.addAttribute("next", "<a href='androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+ ++id+"&pgn="
                        +pgn+"&table="+table+"'>Next</a>");
                    }
                    
                    //if(!username.equals(""))
                    //{
                    //    return "androidfreeblogreader";
                    //}
                }                
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!username.equals(""))
        {
            return "androidfreeblogreader";
        }
        else
            return "androidfreeblogreaderplain";
    }
  

    @RequestMapping(value="androidcommentactiondx", method=RequestMethod.POST)
    public String androidCommentActionDx(@RequestParam("username")String username, @RequestParam("writer")String writer, 
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("table")String table,  
    @RequestParam("id")int id, @RequestParam("pgn")int pgn, @RequestParam("date2")String date2, ModelMap model, 
    @RequestParam("choice")String choice, @RequestParam("writer2")String writer2)
    {
        int kk=0;
        switch(choice)
        {
            case "report":
            {
                try
                {
                    String badStoryComment="";//to increment the report count
                    rsAndroid3=sAndroid.executeQuery("SELECT * FROM comments WHERE username='"+writer2+"' AND datey='"+date2+"'");
                    while(rsAndroid3.next())
                    {
                        badStoryComment=rsAndroid3.getString(7);
                        kk=rsAndroid3.getInt(13);                            
                    }
                    ccAndroid.updateSubComment(writer2, "report", ++kk, date2);
                    String reportLink="<a style=\"font-weight: bold; text-align: center; color: purple\" href=\"handlerrequestcontroller?headline="
                    +headline+"&date="+date+"&id="+id+"&writer="+writer+"&table="+table+"&pageNumb="+pgn+"\">Report Link</a>";
                    
                    ccAndroid.saveClientWork(username, admin, "Reporting @"+writer2, "<div style=\"font-weight: bold; font-size:22px; text-align: center; color: red\">"
                    +headline+"</div>"+ badStoryComment +"<br><br>"+ reportLink, reportLink, new Date().toString(), "unread");
                       
                    //return "redirect:/androidrdr?blog="+headline+"&date="+date+"&id="+id+"&writer="+writer+"&pgn="+pgn+"&table="+table;
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }                
        }
        return "redirect:/androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+id+"&writer="+writer+"&pgn="+pgn+"&table="+table;
    }
  
    @RequestMapping(value="/androidLikeCounter")
    public String androidLikeCounter(@RequestParam("username")String username, @RequestParam("writer")String writer, 
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("table")String table,  
    @RequestParam("id")int id, @RequestParam("pgn")int pgn, ModelMap model, @RequestParam("choice")String choice)
    {
        
        try
        {
            switch(choice)
            {
                case "like":
                {
                    rsAndroid1=sAndroid.executeQuery("SELECT * FROM liketable");
                    while(rsAndroid1.next())
                    {                            
                        if(username.equals(rsAndroid1.getString(2)) && writer.equals(rsAndroid1.getString(3)) && 
                        headline.equals(rsAndroid1.getString(4)) && date.equals(rsAndroid1.getString(5)) && 
                        rsAndroid1.getString(6).equals("LIKED"))                                
                        {
                            return "redirect:/androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+id+"&pgn="+pgn+"&table="+table;
                        }
                    }
                    
                    
                    rsAndroid2=sAndroid.executeQuery("SELECT * FROM "+table);
                    while(rsAndroid2.next())
                    {
                        if(writer.equals(rsAndroid2.getString(2)) && headline.equals(rsAndroid2.getString(3)) && date.equals(rsAndroid2.getString(5)))
                        {
                            int likesXX=rsAndroid2.getInt(7);
                                //int viewsXX=rs.getInt(8);
                            psAndroid1=conAndroid.prepareStatement("UPDATE "+table+" SET likes="+ ++likesXX+" WHERE username='"+writer+"' AND title='"+headline+"' AND dateofpost='"+date+"'");
                            psAndroid1.executeUpdate();
                                
                            mk.saveLikes(username, writer, headline, date, "LIKED", "", "");
                            return "redirect:/androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+id+"&pgn="+pgn+"&table="+table;
                        }
                    }
                }
                break;
                
                case "comment":
                {
                    model.addAttribute("username", username);
                    model.addAttribute("writer", writer);
                    model.addAttribute("headline", headline);
                    model.addAttribute("date", date);
                    model.addAttribute("table", table);
                    model.addAttribute("id", id);
                    model.addAttribute("pgn", pgn);
                    try
                    {
                        rsAndroid3=sAndroid.executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
                        while(rsAndroid3.next())
                        {
                            if(username.equals(rsAndroid3.getString(1)))
                            {                        
                                if(rsAndroid3.getString(7)==null)//FOR SUSPENSION PURPOSE
                                {
                                    return "androidSumblogComment";
                                }
                            }
                        }
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return "androidfreeblogreader";
        //return "redirect:/androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+id+"&pgn="+pgn+"&table="+table;
        
        return "redirect:/androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+id+"&pgn="+pgn+"&table="+table;
    }
  

    @RequestMapping(value="/androidblogcommentary", method=RequestMethod.POST)
    public String blogcommentary(@RequestParam("username")String username, @RequestParam("writer")String writer, 
    @RequestParam("headline")String headline, @RequestParam("date")String date, @RequestParam("table")String table,  
    @RequestParam("id")int id, @RequestParam("pgn")int pgn, @RequestParam("date2")String date2, @RequestParam("blogcomment")String blogcomment, 
    ModelMap model, @RequestParam("choice")String choice)
    {
        switch(choice)
        {
            case "post_Story":
            {
                ccAndroid.saveComments(username, writer, headline, date, date2, blogcomment, "approved", "", table, "Mobile");
            }
        }
        return "redirect:/androidrdr?blog="+headline+"&username="+username+"&date="+date+"&id="+id+"&pgn="+pgn+"&table="+table;
    }
    
    @RequestMapping("andfriend/{username}")
    @ResponseBody
    public String androidFriend(@PathVariable("username")String username)
    {
        String friendList=admin+", ";            
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM contactdb");                
            while(rsAndroid1.next())
            {
                String userA=rsAndroid1.getString(2);
                String userB=rsAndroid1.getString(3);
                String confirmed=rsAndroid1.getString(4);
                
                if(userA.equals(username) && confirmed.equals("confirmed"))
                {
                    friendList += userB + ", ";
                }                    
                if(userB.equals(username) && confirmed.equals("confirmed"))
                {
                    friendList += userA + ", ";
                }
            }               
        }            
        catch (SQLException ex)
        {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return friendList;
    }
    
    @RequestMapping("andsend")
    @ResponseBody
    public String andSend(@RequestParam("username")String username, @RequestParam("recipient")String recipients, 
    @RequestParam("subject")String subject, @RequestParam("msg")String message)
    {
        UtilityClass uc=new UtilityClass();
        String success="";
        if(recipients.contains(","))
        {
            String[] recipnt=recipients.split(",\\s*");
            for(String count:recipnt)
            {
                try
                {
                    String recipient_name=count.trim();
                    if(!success.contains(recipient_name))
                    {
                        if(recipient_name.equals(admin))
                        {
                            ccAndroid.saveClientWork(username, recipient_name, subject, message, "Mobile", uc.getDate(), "unread");
                            ccAndroid.saveClientWork("<span style=\"color: red; font-weight: bold;\">OUTBOX</span>", username, 
                            "Message sent to: @"+recipient_name, message, "Mobile", uc.getDate(), "");
                            success +=admin+" ";
                        }
                        else
                        {
                            rsAndroid1=sAndroid.executeQuery("SELECT * FROM registeredusers WHERE username='"+recipient_name+"'");
                            while(rsAndroid1.next())
                            {
                                ccAndroid.saveClientWork(username, recipient_name, subject, message, "Mobile", uc.getDate(), "unread");
                                ccAndroid.saveClientWork("<span style=\"color: red; font-weight: bold;\">OUTBOX</span>", username, 
                                "Message sent to: @"+recipient_name, message, "Mobile", uc.getDate(), "");
                                success +=recipient_name+" ";
                            }
                        }
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(AndroidController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        }
        else
        {
            try
            {
                String recipient_name=recipients.trim();
                if(recipient_name.equals(admin))
                {
                    ccAndroid.saveClientWork(username, recipient_name, subject, message, "Mobile", uc.getDate(), "unread");
                    ccAndroid.saveClientWork("<span style=\"color: red; font-weight: bold;\">OUTBOX</span>", username, 
                    "Message sent to: @"+recipient_name, message, "Mobile", uc.getDate(), "");
                    success=recipient_name;
                }
                else
                {
                    rsAndroid1=sAndroid.executeQuery("SELECT * FROM registeredusers WHERE username='"+recipient_name+"'");
                    while(rsAndroid1.next())
                    {
                        ccAndroid.saveClientWork(username, recipient_name, subject, message, "Mobile", uc.getDate(), "unread");
                        ccAndroid.saveClientWork("<span style=\"color: red; font-weight: bold;\">OUTBOX</span>", username, 
                        "Message sent to: @"+recipient_name, message, "Mobile", uc.getDate(), "");
                    
                        success=recipient_name;
                    }
                }                
            }
            catch (SQLException ex)
            {
                Logger.getLogger(AndroidController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
    
    @RequestMapping("removeColleague")
    @ResponseBody
    public String removeColleague(@RequestParam("username")String username, @RequestParam("colleague")String colleague)
    {
        String success="";
        String[] col=colleague.split(",");
        for(String count:col)
        {
            try
            {
                rsAndroid1=sAndroid.executeQuery("SELECT * FROM contactdb WHERE sender='"+count+"' AND receiver='"+username+"'");
                if(rsAndroid1.next())
                {
                    psAndroid1=conAndroid.prepareStatement("DELETE FROM contactdb WHERE " + "sender='" + count + "' AND receiver='" + username + "'");
                    psAndroid1.executeUpdate();
                    success +=count+ " ";
                }
                else
                {
                    psAndroid2=conAndroid.prepareStatement("DELETE FROM contactdb WHERE " + "sender='" + username + "' AND receiver='" + count + "'");
                    psAndroid2.executeUpdate();
                    success +=count+ " ";
                }
            }                
            catch (SQLException ex)
            {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
    
    @RequestMapping("banCheck/{username}")
    @ResponseBody
    public String banCheck(@PathVariable("username")String username)
    {
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");//not necessary but for suspension purpose so that stupid commenters can be stopped from making further comments
            while(rsAndroid1.next())
            {
                if(username.equals(rsAndroid1.getString(1)))
                {                        
                    if(rsAndroid1.getString(7)==null) //checking suspension
                    {
                        return "clean";
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AndroidController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "banned";
    }
    
    @RequestMapping("andnewspost")
    @ResponseBody
    public String andNewsPost(@RequestParam("u")String username, @RequestParam("t")String title, @RequestParam("q")String content)
    {
        UtilityClass uc=new UtilityClass();
        String date=uc.getDate();
        ccAndroid.saveStory(title, content, "sumpisnews?pg=1", date, username, "unverified");
        ccAndroid.saveClientWork("<span style=\"color: blue; font-weight: bold;\">NEWSPOST</span>", username, title, content, "News posted by: @"+username, date, "");
        return "posted";                                                    
    }
    
    @RequestMapping("sumblogCheck/{username}")
    @ResponseBody
    public String sumblogCheck(@PathVariable("username")String username)
    {
        String niche="--Select-- ";
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM freelancetable WHERE username='"+username+"'");
            while(rsAndroid1.next())
            {
                if(rsAndroid1.getString(3).equals("") && rsAndroid1.getString(4).equals(""))
                {
                    
                    niche += rsAndroid1.getString(5);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AndroidController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return niche;
    }
  
    @RequestMapping("andblogpost")
    @ResponseBody
    public String andBlogPost(@RequestParam("c")String category, @RequestParam("u")String username, 
    @RequestParam("t")String title, @RequestParam("q")String content)
    {
        UtilityClass uc=new UtilityClass();
        String date=uc.getDate();
        
        try 
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM freelancetable WHERE username='"+username+"'");
            while(rsAndroid1.next())
            {
                if(!rsAndroid1.getString(3).equals("BANNED"))
                {
                    if(!rsAndroid1.getString(4).equals("BLOCKED"))
                    {                                                        
                        ccAndroid.saveClientWork("<span style=\"color: green; font-weight: bold;\">BLOGPOST</span>", username, title, content, "Blogpost by: @"+username, date, "");
                        String coverImageName="03.jpg"; //change this later. All blog posts from mobile will have a default image based on category, use corel draw to design it
                        switch(category)
                        {
                            case "writing":
                            {
                                lc.saveNiches("writing", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "arts":
                            {
                                lc.saveNiches("arts", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "literature":
                            {
                                lc.saveNiches("arts", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "health":
                            {
                                lc.saveNiches("health", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "how_tos":
                            {
                                lc.saveNiches("howtodo", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "entertainment":
                            {
                                lc.saveNiches("entertainment", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "religion":
                            {
                                lc.saveNiches("religionpolitics", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "politics":
                            {
                                lc.saveNiches("religionpolitics", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "information":
                            {
                                lc.saveNiches("information", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "updates":
                            {
                                lc.saveNiches("information", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "fashion":
                            {
                                lc.saveNiches("flt", username, title, content, date, coverImageName);
                            }
                            break;
                                                            
                            case "lifestyle":
                            {
                                lc.saveNiches("flt", username, title, content, date, coverImageName);
                            }
                            break;
                        
                            case "trends":
                            {
                                lc.saveNiches("flt", username, title, content, date, coverImageName);
                            }
                            break;
                        }
                        return "posted";
                    }
                    else
                    {
                        return "blocked";
                    }
                }
                else
                {
                    return "banned";
                }                    
            }
        }
        catch (SQLException | IllegalStateException ex)
        {
            Logger.getLogger(LoginClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }


    @RequestMapping("andinbox/{username}/{p}")
    public String andInbox(@PathVariable("username")String username, @PathVariable("p")int page, 
    ModelMap model)
    {
        List<String> sx=new LinkedList<>();
        List<String> nx=new LinkedList<>();
        List<String> dsc=new LinkedList<>();
        List<String> datex=new LinkedList<>();
        List<String> stat=new LinkedList<>();
        
        model.addAttribute("username", username);
        try
        {
            String descriptor;
            int setoff=12;
            if(page==1)
            {
                rsAndroid1=sAndroid.executeQuery("SELECT * FROM office WHERE receiver ='" + username + "' ORDER BY serialnumber DESC LIMIT "+0+ ", " + setoff);            
                while(rsAndroid1.next())
                {
                    descriptor=rsAndroid1.getString(6);
                    stat.add(rsAndroid1.getString(8));
                    dsc.add(descriptor);
                    sx.add(rsAndroid1.getString(2));
                    nx.add(rsAndroid1.getString(4));
                    datex.add(rsAndroid1.getString(7));
                }
                model.addAttribute("sender", sx);
                model.addAttribute("name", nx);
                model.addAttribute("date", datex);
                model.addAttribute("statux", stat);
                model.addAttribute("desc", dsc);
                model.addAttribute("page", page);
            }
            else
            {
                rsAndroid1=sAndroid.executeQuery("SELECT * FROM office WHERE receiver ='" + username + "' ORDER BY serialnumber DESC LIMIT "+(page-1)*setoff+ ", " + setoff);            
                while(rsAndroid1.next())
                {
                    descriptor=rsAndroid1.getString(6);
                    stat.add(rsAndroid1.getString(8));
                    dsc.add(descriptor);
                    sx.add(rsAndroid1.getString(2));
                    nx.add(rsAndroid1.getString(4));
                    datex.add(rsAndroid1.getString(7));
                }
                model.addAttribute("sender", sx);
                model.addAttribute("name", nx);
                model.addAttribute("date", datex);
                model.addAttribute("statux", stat);
                model.addAttribute("desc", dsc);
                model.addAttribute("page", page);
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "androidInboxMessages";
    }


    @RequestMapping(value="andinboxreader", method=RequestMethod.POST)
    public String androidInboxReader(@RequestParam("username")String username, @RequestParam("title")String title, 
    @RequestParam("date")String date, @RequestParam("page")int page, @RequestParam("choice")String choice, RedirectAttributes ra, 
    ModelMap model)
    {
        try
        {
            rsAndroid1=sAndroid.executeQuery("SELECT * FROM office WHERE receiver='"+username+"'");            
            while(rsAndroid1.next())
            {
                switch (choice)
                {
                    case "read":  
                    {
                        if(title.equals(rsAndroid1.getString(4)) && date.equals(rsAndroid1.getString(7))) 
                        {
                            psAndroid1=conAndroid.prepareStatement("UPDATE office SET unread='' WHERE date='" + date + "' AND receiver='"+username+"'");                                
                            psAndroid1.executeUpdate();
                            //ra.addAttribute("unreadMessages", succ.getUnreadCount(username));
                            //ra.addAttribute("friendrequest", succ.getFriendRequest(username));
                            //ra.addFlashAttribute("fileContent", rsAndroid1.getString(5));
                            
                            String back="<a href=andinbox/"+username+"/"+page+">Back</a>";
                            model.addAttribute("back", back);
                            model.addAttribute("fileContent", rsAndroid1.getString(5));
                            return "androidInboxMessages";
                        }
                    }
                    break;
                    
                    case "delete":
                    {
                        if(title.equals(rsAndroid1.getString(4)) && date.equals(rsAndroid1.getString(7))) 
                        {
                            psAndroid1=conAndroid.prepareStatement("DELETE FROM office WHERE filename='" + title + "' AND date='" + date + "' AND receiver='"+username+"'");
                            psAndroid1.executeUpdate();
                        }
                    }
                    break;
                }                
            }
        }        
        catch (SQLException ex)
        {
            Logger.getLogger(MessagingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:andinbox/"+username+"/"+page;
    }
*/
}