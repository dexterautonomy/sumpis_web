package com.teamwurkz.web;

import com.teamwurkz.domain.EditClass;
import com.teamwurkz.domain.FieldClass;
import com.teamwurkz.domain.FileUploadClass;
import com.teamwurkz.domain.MemberClass;
import com.teamwurkz.domain.NewsPostClass;
import com.teamwurkz.domain.ParaphraseClass;
import com.teamwurkz.domain.SearchPersonClass;
import com.teamwurkz.domain.SignupClass;
import com.teamwurkz.domain.WorkOnFile;
import com.teamwurkz.domain.WriteUs;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Random;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController
{
    @Value("${advert.display}")//Properties file pending when we start allowing adverts. For now it is display: none
    String displayadvert;
    @Value("${imagepath.proploc}")
    String pathFromPropertiesFile;
    @Value("${admin.name}")
    String admin;
    @Autowired
    private ClientController cc;
    @Autowired
    private SignUpController succ;
    private DataSource ds;    
    private Connection con;
    private Context ctx;
    private final Charset ISO=Charset.forName("ISO-8859-1");
    private final Charset UTF=Charset.forName("UTF-8");
    
    public HomeController()
    {        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");            
            ctx=new InitialContext();
            ds=(DataSource)ctx.lookup("java:/comp/env/jdbc/sumpisdatabase");
            con=ds.getConnection();
        }        
        catch (SQLException | NamingException | ClassNotFoundException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String changed(List<String> entry)
    {
        String perSentenceParaphrase="";
        for(int i=0; i<entry.size(); i++)
        {
            perSentenceParaphrase += phraseReplace(phraseReplaceX(paraphraseSentence(entry.get(i))));
        }
        return perSentenceParaphrase;
    }
    
    private String phraseReplace(String fullSentence)//STAGE 1: gets column 2 and checks if sentence has any of the phrases in column 2 (extensive)
    {
        ResultSet rs=null;
        Random random=new Random();
    
        String replacement;
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM phrasereplacement");
            while(rs.next())
            {
                String[] morePhrase=rs.getString(2).split(",\\s*");//checks column 2 here and splits it
                        
                for(int count2=0; count2<morePhrase.length; count2++)
                {
                    String rp=morePhrase[count2];
                    if(fullSentence.toLowerCase().contains(rp))
                    {
                        int phraselength2=rp.length();
                        int phraseStartIndex2=fullSentence.toLowerCase().indexOf(rp);
                        //replacement=rs.getString(1);
                        
                        if(morePhrase.length==1)//stops infinite loop
                        {
                            replacement=morePhrase[random.nextInt(morePhrase.length)];
                        }
                        else
                        {
                            replacement=morePhrase[random.nextInt(morePhrase.length)];
                            while(replacement.equals(rp))
                            {
                                replacement=morePhrase[random.nextInt(morePhrase.length)];
                            }
                        }
                        if(Character.isUpperCase(fullSentence.charAt(phraseStartIndex2)) && 
                        Character.isUpperCase(fullSentence.charAt(phraseStartIndex2 + (phraselength2-1))))
                        {
                            replacement=replacement.toUpperCase();
                            rp=rp.toUpperCase();
                        }
                        else if(Character.isUpperCase(fullSentence.charAt(phraseStartIndex2)))
                        {
                            replacement=Character.toUpperCase(replacement.charAt(0)) + replacement.substring(1);
                            rp=Character.toUpperCase(rp.charAt(0)) + rp.substring(1);
                        }
                        fullSentence=fullSentence.replaceAll("\\b"+ rp + "\\b", replacement);
                    }
                }
            }
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fullSentence;
    }
    
    private String phraseReplaceX(String fullSentence)//STAGE 2: Normal phrase replace, checks from column 1
    {
        ResultSet rs=null;
        Random random=new Random();
        String[] wordArray=fullSentence.split("\\s+");
        String phraseList;
        String[] replaceList;
        String replacement;
        
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM phrasereplacement");                        
            while(rs.next())
            {
                phraseList=rs.getString(1);
                
                for(int count=0; count<wordArray.length; count++)
                {
                    int phraselength=phraseList.length(); //length of phrase
                    int phraseStartIndex=fullSentence.toLowerCase().indexOf(phraseList);
                    
                    if(fullSentence.toLowerCase().contains(phraseList))
                    {
                        replaceList=rs.getString(2).split(",\\s*");
                        replacement=replaceList[random.nextInt(replaceList.length)];
                        
                        if(Character.isUpperCase(fullSentence.charAt(phraseStartIndex)) && 
                        Character.isUpperCase(fullSentence.charAt(phraseStartIndex + (phraselength-1))))
                        {
                            phraseList=phraseList.toUpperCase();
                            replacement=replacement.toUpperCase();
                        }
                        else if(Character.isUpperCase(fullSentence.charAt(phraseStartIndex)))
                        {
                            phraseList=Character.toUpperCase(phraseList.charAt(0)) + phraseList.substring(1);
                            replacement=Character.toUpperCase(replacement.charAt(0)) + replacement.substring(1);
                        }
                        fullSentence=fullSentence.replaceAll("\\b"+ phraseList + "\\b", replacement);
                    }
                }
            }
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fullSentence;
    }
    
    private String sweetMethod(String sentence, String eachWord, int i)
    {    
        ResultSet rs=null;
        Random random=new Random();
        String[] splitSent=sentence.split("\\s+");
        String[] splitNewWords;
        String replaceNewWords;
        String wordBefore;
        String wordAfter;
        String wordAfterAfter;
        
        try
        {
            rs=con.createStatement().executeQuery("SELECT * FROM contextdatabase");
            while(rs.next())
            {
                if(eachWord.toLowerCase().equals(rs.getString(1)))
                {
                    if(capital_Letters_Test(eachWord))
                    {
                        //context paraphrase algorithm comes in here for words that have all their first letters capitalized (e.g BEAUTIFUL)                            
                        if(rs.getString(2)==null || rs.getString(2).equals(""))
                        {
                            if(rs.getString(5)==null || rs.getString(5).equals(""))
                            {
                                //this means the word can be used as an adjective or an adverb (double contextual usage) simply put [adjective vs adverb]
                                if(i==0 && splitSent.length==1) //first and only word in the sentence
                                {
                                    splitNewWords=rs.getString(3).split(",\\s*");
                                    replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                        
                                    sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                }                            
                                else if(i==0 && splitSent.length>1)//first word and not only word in the sentence
                                {
                                    wordAfter=splitSent[i+1];
                                
                                    if(adjective_Check(wordAfter) || adverb_Check(wordAfter))
                                    {
                                        if(verb_Check(wordAfter))
                                        {
                                            splitNewWords=rs.getString(4).split(",\\s*");
                                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                                
                                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                        }
                                    }
                                    else if(noun_Check(wordAfter))
                                    {
                                        splitNewWords=rs.getString(3).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                }
                                
                                //getting length of seentence and words in sentence
                                else if(i!=0 && i!=(splitSent.length-1))  //words inbetween
                                {
                                    wordAfter=splitSent[i+1];
                                    wordBefore=splitSent[i-1];

                                    if(adjective_Check(wordAfter) || adverb_Check(wordAfter) || verb_Check(wordAfter) || adverb_Check(wordBefore)
                                    || (verb_Check(wordBefore) && adverb_Check(wordAfter)))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                    else if((verb_Check(wordBefore) && wordAfter.equals("")) || (verb_Check(wordBefore) && !adverb_Check(wordAfter))
                                    || (verb_Check(wordBefore) && adjective_Check(wordAfter)) || noun_Check(wordAfter))
                                    {                                            
                                        splitNewWords=rs.getString(3).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                }
                                else if(i==(splitSent.length-1))    //last word
                                {
                                    wordBefore=splitSent[i-1];
                                    if(verb_Check(wordBefore))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                    else if(i-2>0)
                                    {
                                        String wordBefore2=splitSent[i-2];
                                        if(noun_Check(wordBefore2))
                                        {
                                            splitNewWords=rs.getString(3).split(",\\s*");
                                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                                
                                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                        }
                                    }
                                }
                            }                                
                            else
                            {
                                //this means the word can be used as a noun or a verb (double contextual usage) simply put [noun vs verb]
                                if(i==0 && splitSent.length==1) //first and only word in the sentence
                                {//collect from verb column
                                    splitNewWords=rs.getString(6).split(",\\s*");                                        
                                    replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                        
                                    sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);                                    
                                }
                                    
                                else if(i==0 && splitSent.length>2)//first word and not only word in the sentence //DONE
                                {                                                                        
                                    wordAfter=splitSent[i+1];
                                    wordAfterAfter=splitSent[i+2];
                                    
                                    if(verb_Check(wordAfter) || verb_Check(wordAfterAfter)) //Fight is good. Fight sometimes is good
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                        
                                    else if(adjective_Check(wordAfter) || adverb_Check(wordAfter)) // Fight good tonight. Fight beautifully tonight
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                }
                                    
                                else if(i==0 && splitSent.length==2)//first word and not only word in the sentence //DONE
                                {                                                                        
                                    wordAfter=splitSent[i+1];                                    
                                    if(verb_Check(wordAfter)) //Fight is good.
                                    {//collect from noun
                                        splitNewWords=rs.getString(5).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                        
                                    else if(adjective_Check(wordAfter) || adverb_Check(wordAfter)) // Fight good tonight. Fight beautifully tonight
                                    {//collect from verb
                                        splitNewWords=rs.getString(6).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                }
                                    
                                else if(i!=0 && i!=(splitSent.length-1))  //words inbetween //DONE
                                {
                                    wordAfter=splitSent[i+1];
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordBefore) || article_Check(wordBefore) || pronoun_Check(wordBefore)) //The fight was cool. The good fight was cool
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                    else if(adverb_Check(wordBefore) || adverb_Check(wordAfter) || noun_Check(wordBefore) ||
                                    preposition_Check(wordAfter)) //He slowly fights. He fights slowly. He fights like ....
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                }
                                else if(i==(splitSent.length-1))    //last word //DONE
                                {
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordBefore) || article_Check(wordBefore) || pronoun_Check(wordBefore)) //It was a seven good fights
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                    else if(adverb_Check(wordBefore) || noun_Check(wordBefore)) //I like the way he fights
                                    {
                                        splitNewWords=rs.getString(6).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                }
                            }
                        }
                        else
                        {
                            //this means the word has just one meaning(single contexual usage)
                            splitNewWords=rs.getString(2).split(",\\s*");
                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)].toUpperCase();                                    
                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                        }
                    }
                        
                    else if(Character.isUpperCase(eachWord.charAt(0)))
                    {
                        //context paraphrase algorithm comes in here for words that have their first letters capitalized (e.g Beautiful)                            
                        if(rs.getString(2)==null || rs.getString(2).equals(""))
                        {
                            if(rs.getString(5)==null || rs.getString(5).equals(""))
                            {
                                //this means the word can be used as an adjective or an adverb (double contextual usage) simply put [adjective vs adverb]
                                if(i==0 && splitSent.length==1) //first and only word in the sentence
                                {
                                    splitNewWords=rs.getString(3).split(",\\s*");
                                    replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                    String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                    sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                }
                                
                                else if(i==0 && splitSent.length>1)//first word and not only word in the sentence
                                {                                                                        
                                    wordAfter=splitSent[i+1];                                    
                                    if(adjective_Check(wordAfter) || adverb_Check(wordAfter))
                                    {
                                        if(verb_Check(wordAfter))
                                        {
                                            splitNewWords=rs.getString(4).split(",\\s*");
                                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                            String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                        }
                                    }
                                    else if(noun_Check(wordAfter))
                                    {
                                        splitNewWords=rs.getString(3).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }                                    
                                }                                
                                //getting length of seentence and words in sentence
                                else if(i!=0 && i!=(splitSent.length-1))  //words inbetween
                                {
                                    wordAfter=splitSent[i+1];
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordAfter) || adverb_Check(wordAfter) || verb_Check(wordAfter) || adverb_Check(wordBefore)
                                    || (verb_Check(wordBefore) && adverb_Check(wordAfter)))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                    else if((verb_Check(wordBefore) && wordAfter.equals("")) || (verb_Check(wordBefore) && !adverb_Check(wordAfter))
                                    || (verb_Check(wordBefore) && adjective_Check(wordAfter)) || noun_Check(wordAfter))
                                    {                                            
                                        splitNewWords=rs.getString(3).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                }
                                else if(i==(splitSent.length-1))    //last word
                                {
                                    wordBefore=splitSent[i-1];
                                    if(verb_Check(wordBefore))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }                                    
                                    else if(i-2>0)
                                    {
                                        String wordBefore2=splitSent[i-2];
                                        if(noun_Check(wordBefore2))
                                        {
                                            splitNewWords=rs.getString(3).split(",\\s*");
                                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                            String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                        }
                                    }
                                }
                            }                                
                            else
                            {
                                //this means the word can be used as a noun or a verb (double contextual usage) simply put [noun vs verb]
                                if(i==0 && splitSent.length==1) //first and only word in the sentence
                                {
                                    splitNewWords=rs.getString(6).split(",\\s*");                                        
                                    replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                    String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                    sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);                                    
                                }
                                    
                                else if(i==0 && splitSent.length>2)//first word and not only word in the sentence //DONE
                                {                                                                        
                                    wordAfter=splitSent[i+1];
                                    wordAfterAfter=splitSent[i+2];                                    
                                    if(verb_Check(wordAfter) || verb_Check(wordAfterAfter)) //Fight is good. Fight sometimes is good
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                        
                                    else if(adjective_Check(wordAfter) || adverb_Check(wordAfter)) // Fight good tonight. Fight beautifully tonight
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }                                    
                                }
                                    
                                else if(i==0 && splitSent.length==2)//first word and not only word in the sentence //DONE
                                {                                                                        
                                    wordAfter=splitSent[i+1];
                                    if(verb_Check(wordAfter)) //Fight is good. Fight sometimes is good
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                        
                                    else if(adjective_Check(wordAfter) || adverb_Check(wordAfter)) // Fight good tonight. Fight beautifully tonight
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }                                    
                                }
                                    
                                else if(i!=0 && i!=(splitSent.length-1))  //words inbetween //DONE
                                {
                                    wordAfter=splitSent[i+1];
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordBefore) || article_Check(wordBefore) || pronoun_Check(wordBefore)) //The fight was cool. The good fight was cool
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                    else if(adverb_Check(wordBefore) || adverb_Check(wordAfter) || noun_Check(wordBefore) ||
                                    preposition_Check(wordAfter)) //He slowly fights. He fights slowly. He fights like ....
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                }
                                else if(i==(splitSent.length-1))    //last word //DONE
                                {
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordBefore)  || article_Check(wordBefore) || pronoun_Check(wordBefore)) //It was a seven good fights
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                    else if(adverb_Check(wordBefore) || noun_Check(wordBefore)) //I like the way he fights
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                                    }
                                }
                            }
                        }
                        else
                        {
                            splitNewWords=rs.getString(2).split(",\\s*");
                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                            String replacement=Character.toUpperCase(replaceNewWords.charAt(0))+replaceNewWords.substring(1);
                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replacement);
                        }
                    }
                        
                    else
                    {
                        //context paraphrase algorithm comes in here for words that have their first letters in small letters (e.g beautiful)
                        if(rs.getString(2)==null || rs.getString(2).equals(""))
                        {
                            if(rs.getString(5)==null || rs.getString(5).equals(""))
                            {
                                //adjective vs adverb                                
                                //this means the word can be used as a an adjective or an adverb (double contextual usage) simply put [adjective vs adverb]
                                if(i==0 && splitSent.length==1) // first and only word in the sentence
                                {
                                    //if word is both an adjective and an adverb and word is the first and only word in the sentence, choose the adjectival replacements
                                    splitNewWords=rs.getString(3).split(",\\s*");
                                    replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                    sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                }                                
                                //getting length of sentence
                                else if(i==0 && splitSent.length>1)    //first word and not only word in the sentence
                                {                                    
                                    wordAfter=splitSent[i+1];                                    
                                    if(adjective_Check(wordAfter) || adverb_Check(wordAfter) || verb_Check(wordAfter))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                    else if(noun_Check(wordAfter))
                                    {
                                        splitNewWords=rs.getString(3).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                }                                
                                //getting length of sentence and words in sentence
                                else if(i!=0 && i!=(splitSent.length-1))  //words inbetween
                                {
                                    wordAfter=splitSent[i+1];
                                    wordBefore=splitSent[i-1];                                    
                                    if(adjective_Check(wordAfter) || adverb_Check(wordAfter) || verb_Check(wordAfter) || adverb_Check(wordBefore)
                                    || (verb_Check(wordBefore) && adverb_Check(wordAfter)))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                    else if((verb_Check(wordBefore) && wordAfter.equals("")) || (verb_Check(wordBefore) && !adverb_Check(wordAfter))
                                    || (verb_Check(wordBefore) && adjective_Check(wordAfter)) || noun_Check(wordAfter))
                                    {
                                        splitNewWords=rs.getString(3).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                }
                                else if(i==(splitSent.length-1))    //last word
                                {
                                    wordBefore=splitSent[i-1];                                    
                                    if(verb_Check(wordBefore))
                                    {
                                        splitNewWords=rs.getString(4).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                    else if(i-2>0)
                                    {
                                        wordBefore=splitSent[i-2];
                                        if(noun_Check(wordBefore))
                                        {
                                            splitNewWords=rs.getString(3).split(",\\s*");
                                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                        }
                                    }
                                }
                            }
                            else
                            {
                                //this means the word can be used as a noun or a verb (double contextual usage) simply put [noun vs verb]
                                if(i==0 && splitSent.length==1) //first and only word in the sentence
                                {
                                    splitNewWords=rs.getString(6).split(",\\s*");                                        
                                    replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                        
                                    sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                }
                                    
                                else if(i==0 && splitSent.length>2)//first word and not only word in the sentence //DONE
                                {
                                    wordAfter=splitSent[i+1];
                                    wordAfterAfter=splitSent[i+2];                                    
                                    if(verb_Check(wordAfter) || verb_Check(wordAfterAfter)) //Fight is good. Fight sometimes is good
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                        
                                    else if(adjective_Check(wordAfter) || adverb_Check(wordAfter)) // Fight good tonight. Fight beautifully tonight
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                }
                                    
                                else if(i==0 && splitSent.length==2)//first word and not only word in the sentence //DONE
                                {                                                                        
                                    wordAfter=splitSent[i+1];
                                    
                                    if(verb_Check(wordAfter)) //Fight is good. Fight sometimes is good
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                        
                                    else if(adjective_Check(wordAfter) || adverb_Check(wordAfter)) // Fight good tonight. Fight beautifully tonight
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");                                            
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }                                    
                                }
                                    
                                else if(i!=0 && i!=(splitSent.length-1))  //words inbetween //DONE
                                {
                                    wordAfter=splitSent[i+1];
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordBefore) || article_Check(wordBefore) || pronoun_Check(wordBefore)) //The fight was cool. The good fight was cool
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);                                            
                                    }
                                    else if(adverb_Check(wordBefore) || adverb_Check(wordAfter) || noun_Check(wordBefore) ||
                                    preposition_Check(wordAfter)) //He slowly fights. He fights slowly. He fights like ....
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                }
                                else if(i==(splitSent.length-1))    //last word //DONE
                                {
                                    wordBefore=splitSent[i-1];
                                    if(adjective_Check(wordBefore) || article_Check(wordBefore) || pronoun_Check(wordBefore)) //It was a seven good fights
                                    {//collect from noun column
                                        splitNewWords=rs.getString(5).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                    else if(adverb_Check(wordBefore) || noun_Check(wordBefore)) //I like the way he fights
                                    {//collect from verb column
                                        splitNewWords=rs.getString(6).split(",\\s*");
                                        replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];                                            
                                        sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                                    }
                                }
                            }                                
                        }
                        else
                        {
                            splitNewWords=rs.getString(2).split(",\\s*");
                            replaceNewWords=splitNewWords[random.nextInt(splitNewWords.length)];
                            sentence=sentence.replaceFirst("\\b"+ eachWord + "\\b", replaceNewWords);
                        }
                    }                        
                }
            }
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sentence;
    }
    
    //paraphrase method 2. This method is responsible for paraphrasing the tokenized sentence gotten from textarea
    private String paraphraseSentence(String sentence)
    {
        String[] splitSent=sentence.split("\\s+");
        String eachWord;
        int pos;
        for (int i=0; i<splitSent.length; i++)
        {
            if(splitSent[i].contains("\'"))
            {
                pos=splitSent[i].indexOf("\'");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else if(splitSent[i].endsWith("."))
            {
                pos=splitSent[i].indexOf(".");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else if(splitSent[i].endsWith(","))
            {
                pos=splitSent[i].indexOf(",");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else if(splitSent[i].endsWith("?"))
            {
                pos=splitSent[i].indexOf("?");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else if(splitSent[i].endsWith("!"))
            {
                pos=splitSent[i].indexOf("!");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else if(splitSent[i].endsWith(";"))
            {
                pos=splitSent[i].indexOf(";");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else if(splitSent[i].endsWith(":"))
            {
                pos=splitSent[i].indexOf(":");
                eachWord=splitSent[i].substring(0,pos);
                sentence=sweetMethod(sentence, eachWord, i);
            }
            else
            {
                eachWord=splitSent[i];
                sentence=sweetMethod(sentence, eachWord, i);
            }
        }
        return sentence;
    }
    
    private boolean capital_Letters_Test(String word)
    {
        for(int count=0; count<word.length(); count++)
        {            
            if(Character.isLowerCase(word.charAt(count)))
            {
                return false;
            }
        }
        return true;
    }
    
    private boolean article_Check(String article_Word)
    {
        String[] article={"the", "an", "a", "this", "that", "these", "those"};
        for (String word : article)
        {
            if(article_Word.equals(word))
            {
                return true;
            }
        }                                
        return false;
    }
    
    private boolean noun_Check(String noun_Word)
    {
        try 
        {
            String nouns="";        
            Path nouns_path=Paths.get(pathFromPropertiesFile+"/docs/nouns.txt");
            List<String> noun_Lines=Files.readAllLines(nouns_path);
            for(int count=0; count<noun_Lines.size(); count++)
            {
                nouns += noun_Lines.get(count);
            }            
            String[] noun_Split=nouns.split(",\\s*");
            for(String noun_Static:noun_Split)
            {
                if(noun_Word.equalsIgnoreCase(noun_Static))
                {
                    return true;
                }
            }            
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }                                
        return false;
    }
    
    private boolean pronoun_Check(String pronoun_Word)
    {
        try 
        {
            String pronouns="";        
            Path pronouns_path=Paths.get(pathFromPropertiesFile+"/docs/pronouns.txt");
            List<String> pronoun_Lines=Files.readAllLines(pronouns_path);
            for(int count=0; count<pronoun_Lines.size(); count++)
            {
                pronouns += pronoun_Lines.get(count);
            }            
            String[] pronoun_Split=pronouns.split(",\\s*");
            for(String pronoun_Static:pronoun_Split)
            {
                if(pronoun_Word.equalsIgnoreCase(pronoun_Static))
                {
                    return true;
                }
            }            
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }                                
        return false;
    }
    
    private boolean verb_Check(String verb_Word)
    {
        try
        {
            String verbs="";
            Path verbs_path=Paths.get(pathFromPropertiesFile+"/docs/verbs.txt");
            List<String> verb_Lines=Files.readAllLines(verbs_path);
            for(int count=0; count<verb_Lines.size(); count++)
            {
                verbs += verb_Lines.get(count);
            }            
            String[] verb_Split=verbs.split(",\\s*");
            for(String verb_Static:verb_Split)
            {
                if(verb_Word.equalsIgnoreCase(verb_Static))
                {
                    return true;
                }
            }            
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return false;
    }
    
    private boolean adverb_Check(String adverb_Word)
    {
        try
        {
            String adverbs="";
            Path adverbs_path=Paths.get(pathFromPropertiesFile+"/docs/adverbs.txt");
            List<String> adverb_Lines=Files.readAllLines(adverbs_path);
            for(int count=0; count<adverb_Lines.size(); count++)
            {
                adverbs += adverb_Lines.get(count);
            }            
            String[] adverb_Split=adverbs.split(",\\s*");
            for(String adverb_Static:adverb_Split)
            {
                if(adverb_Word.equalsIgnoreCase(adverb_Static))
                {
                    return true;
                }
            }            
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return false;
    }
    
    private boolean adjective_Check(String adjective_Word)
    {
        try
        {
            String adjectives="";
            Path adjectives_path=Paths.get(pathFromPropertiesFile+"/docs/adjectives.txt");
            List<String> adjective_Lines=Files.readAllLines(adjectives_path);
            for(int count=0; count<adjective_Lines.size(); count++)
            {
                adjectives += adjective_Lines.get(count);
            }            
            String[] adjective_Split=adjectives.split(",\\s*");
            for(String adjective_Static:adjective_Split)
            {
                if(adjective_Word.equalsIgnoreCase(adjective_Static))
                {
                    return true;
                }
            }            
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return false;
    }
    
    private boolean preposition_Check(String preposition_Word)
    {
        try
        {
            String prepositions="";
            Path prepositions_path=Paths.get(pathFromPropertiesFile+"/docs/prepositions.txt");        
            List<String> preposition_Lines=Files.readAllLines(prepositions_path);
            for(int count=0; count<preposition_Lines.size(); count++)
            {
                prepositions += preposition_Lines.get(count);
            }            
            String[] preposition_Split=prepositions .split(",\\s*");
            for(String preposition_Static:preposition_Split)
            {
                if(preposition_Word.equalsIgnoreCase(preposition_Static))
                {
                    return true;
                }
            }            
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }                                
        return false;
    }
    
    private String summary(List<String> entry, String sumLength)
    {
        Set<String> noDuplicateSet=new HashSet<>(entry);
        List<String> noDuplicateList=new LinkedList<>(noDuplicateSet);
        
        Random random=new Random();
        int summaryLength;
        String newSummary="";
        
        if(!sumLength.trim().matches("([0-9]*)") || sumLength.equals(""))    
        {
            newSummary="Please enter a real value for the number of sentence(s).";
        }        
        else
        {
            summaryLength=Integer.parseInt(sumLength.trim());
            int sentenceSize=noDuplicateList.size();        
            if(summaryLength<0)
            {
                newSummary="Wrong entry for number of sentence(s), number of sentences must be greater than zero.";
            }        
            else if(summaryLength==0)
            {
                newSummary="Please enter the number of sentence(s) you desire the summary to be. You cannot summarise to an empty sentence.";
            }        
            else if(summaryLength>0)
            {
                if(summaryLength>sentenceSize && (sentenceSize>1 || sentenceSize==1))
                {
                    newSummary="This is not a summary, you cannot summarize more than the number of sentences in a text.";
                }            
                else if(summaryLength>sentenceSize && sentenceSize==0)
                {
                    newSummary="No text to summarize.";
                }            
                else if(summaryLength<sentenceSize && sentenceSize==1)
                {
                    newSummary=noDuplicateList.get(0);
                }        
                else if(summaryLength<sentenceSize && sentenceSize>1)
                {
                    int count=1;
                    
                    newSummary=noDuplicateList.get(0);
                    while(count<summaryLength)
                    {
                        String randomSentence=noDuplicateList.get(random.nextInt(sentenceSize));
                        while(newSummary.contains(randomSentence))
                        {
                            randomSentence=noDuplicateList.get(random.nextInt(sentenceSize));
                        }
                        newSummary +=randomSentence;                            
                        count++;
                    }
                }            
                else if(summaryLength==sentenceSize && sentenceSize>1)
                {
                    newSummary="This is not a summary, you cannot summarize as the number of sentences in a text.";
                }
            }
        }
        
        return newSummary;            
    }

    private String streamlinedSummary(List<String> entry, String sumLength)
    {
        Set<String> noDuplicateSet=new HashSet<>(entry);
        List<String> noDuplicateList=new LinkedList<>(noDuplicateSet);
        
        Random random=new Random();
        int summaryLength;
        String newSummary="";        
        
        if(!sumLength.trim().matches("([0-9]*)") || sumLength.equals(""))
        {
            newSummary="Please enter a real value for the number of sentence(s).";
        }        
        else
        {
            summaryLength=Integer.parseInt(sumLength.trim());
            int sentenceSize=noDuplicateList.size();        
            if(summaryLength<0)
            {
                newSummary="Wrong entry for number of sentence(s), number of sentences must be greater than zero.";
            }        
            else if(summaryLength==0)
            {
                newSummary="Please enter the number of sentence(s) you desire the summary to be. You cannot summarise to an empty sentence.";
            }        
            else if(summaryLength>0)
            {
                if(summaryLength>sentenceSize && (sentenceSize>1 || sentenceSize==1))
                {
                    newSummary="This is not a summary, you cannot summarize more than the number of sentences in a text.";
                }            
                else if(summaryLength>sentenceSize && sentenceSize==0)
                {
                    newSummary="No text to summarize.";
                }            
                else if(summaryLength<sentenceSize && sentenceSize==1)
                {
                    newSummary=noDuplicateList.get(0);
                }        
                else if(summaryLength<sentenceSize && sentenceSize>1)
                {                    
                    int Sg=(int)sentenceSize/summaryLength;
                    int Gc=(int)summaryLength/Sg;
                    int TGc=(int)sentenceSize/Sg;
                    int moveP=0;
                    int skipX=0;                        
                    if(Gc!=0)
                    {
                        while(moveP<Sg)
                        {            
                            for(int y=0; y<Gc; y++)
                            {
                                String randomSentence=noDuplicateList.get(skipX + random.nextInt(TGc - 1));                                
                                while(newSummary.contains(randomSentence))
                                {
                                    randomSentence=noDuplicateList.get(skipX + random.nextInt(TGc - 1));
                                }
                                newSummary += randomSentence;                
                            }
                            moveP++;
                            skipX=TGc * moveP;
                        }
                    }        
                    else
                        newSummary="The streamlined summary algorithm could not process your summary length value, please try a larger value.";
                }
            
                else if(summaryLength==sentenceSize && sentenceSize>1)
                {
                    newSummary="This is not a summary, you cannot summarize as the number of sentences in a text.";
                }
            }
        }        
        return newSummary;            
    }
    
    private String rearrange(List<String> entry)
    {
        Random random=new Random();
        int count=0;
        String newSummary="";               
        while(count<entry.size())
        {
            String rearrangedSentence=entry.get(random.nextInt(entry.size()));            
            while(newSummary.contains(rearrangedSentence))
            {
                rearrangedSentence=entry.get(random.nextInt(entry.size()));
            }  
            newSummary +=rearrangedSentence;                            
            count++;
        }                
    return newSummary;            
    }

    private String summariseAndParaphrase(List<String> entry, String sumLength)
    {
        List<String> internalList=new LinkedList<>();        
        String internalSummary=summary(entry, sumLength);
        internalList.add(internalSummary);
        String paraphrasedSummary=changed(internalList);                    
        return paraphrasedSummary;            
    }
    
    private String streamlineAndParaphrase(List<String> entry, String sumLength)
    {
        List<String> internalList=new LinkedList<>();        
        String internalSummary=streamlinedSummary(entry, sumLength);
        internalList.add(internalSummary);
        String paraphrasedSummary=changed(internalList);                    
        return paraphrasedSummary;            
    }
    
    @RequestMapping(value="/")
    public String getHomePage(@ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    ModelMap model)//, HttpSession session, HttpServletRequest req)
    {
        //session=req.getSession();
        
        ResultSet rsGeneral=null;
        //statistics
        succ.statistics("VISITORS");
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        
        try
        {
            rsGeneral=con.createStatement().executeQuery("SELECT * FROM generalinformation");//maintenance/general info
            while(rsGeneral.next())
            {
                model.addAttribute("generalinfo", rsGeneral.getString(3));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if(rsGeneral!=null)
                {
                    rsGeneral.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "homepage";
    }
    
    //optimized 8
    @RequestMapping(value="/submit", method=RequestMethod.POST)
    public String getResult(@ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, ModelMap model, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, @ModelAttribute("fourthBeanObject")MemberClass mc, HttpServletRequest req, 
    HttpSession session)
    {
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        //ResultSet rs=null;
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        
        //Date d1=new Date();
        ParaphraseClass pc=new ParaphraseClass();
        List<String> sentence=pc.tokenizingTheInputText(fc.getField());
        String change="";
        int wordCount;
        
        switch (fc.getChoice())
        {
            case "getSentenceCount":
            {
                change=sentence.size() + " sentence(s).";
                break;
            }
            case "getWordCount":
            {
                if(fc.getField().matches("\\s*"))
                {
                    change="There is/are no sentence(s) present";
                }
                else
                {
                    wordCount=cc.wordCount(fc.getField());
                    change=wordCount+ " word(s).";
                }
                break;
            }
            case "paraphrase":
            {
                if(username!=null)
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        model.addAttribute("expired", "There is/are no sentence(s) present");
                    }
                    else
                    {
                        analytics(fc.getField()); //Very important for analytics(saving words that is not in our database yet)
                        /*session=req.getSession();
                        String username=(String)session.getAttribute("username");
                        if(username!=null)
                        {
                            try
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                                while(rs.next())
                                {
                                    String subscriptionDate=rs.getString(9);
                                    Date d2=succ.dateSuffix(subscriptionDate);
                                    if(d1.compareTo(d2)==-1) //extra condition here to check for expired subscription
                                    {*/
                                        change=changed(sentence);
                                    /*}
                                    else
                                    {
                                        model.addAttribute("expired", "<a href='premium' class='free2'>Click here and Go Premium</a>");
                                        return "homepage";
                                    }
                                }
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
                                }
                                catch (SQLException ex)
                                    {
                                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        else
                        {
                            model.addAttribute("expired", "<a href='signuppage' class='free2'>Click here and sign up</a>");
                        }*/
                    }
                }
                else
                {
                    model.addAttribute("exp", "<a href='signuppage' class='free2'>Please login or signup if you are a new user</a>");
                    return "homepage";
                }
                break;
            }
            case "summary":
            {
                if(username!=null)
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        model.addAttribute("expired", "There is/are no sentence(s) present");
                    }
                    else
                    {
                        analytics(fc.getField()); //Very important for analytics(saving words that is not in our database yet)
                        /*session=req.getSession();
                        String username=(String)session.getAttribute("username");
                        if(username!=null)
                        {
                            try
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                                while(rs.next())
                                {
                                    String subscriptionDate=rs.getString(9);
                                    Date d2=succ.dateSuffix(subscriptionDate);
                                    if(d1.compareTo(d2)==-1) //extra condition here to check for expired subscription
                                    {*/
                                        change=summary(sentence, fc.getLength());
                                    /*}
                                    else
                                    {
                                        model.addAttribute("expired", "<a href='premium' class='free2'>Click here and Go Premium</a>");
                                        return "homepage";
                                    }
                                }
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
                                }
                                catch (SQLException ex)
                                {
                                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        else
                        {
                            model.addAttribute("expired", "<a href='signuppage' class='free2'>Click here and sign up</a>");
                        }*/
                    }
                }
                else
                {
                    model.addAttribute("exp", "<a href='signuppage' class='free2'>Please login or signup if you are a new user</a>");
                    return "homepage";
                }
                break;
            }
            case "paraphrase_summary":
            {
                if(username!=null)
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        model.addAttribute("expired", "There is/are no sentence(s) present");
                    }
                    else
                    {
                        analytics(fc.getField()); //Very important for analytics(saving words that is not in our database yet)
                        /*session=req.getSession();
                        String username=(String)session.getAttribute("username");
                        if(username!=null)
                        {
                            try
                            {
                                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                                while(rs.next())
                                {
                                    String subscriptionDate=rs.getString(9);
                                    Date d2=succ.dateSuffix(subscriptionDate);
                                    if(d1.compareTo(d2)==-1) //extra condition here to check for expired subscription
                                    {*/
                                        change=summariseAndParaphrase(sentence, fc.getLength());
                                    /*}
                                    else
                                    {
                                        model.addAttribute("expired", "<a href='premium' class='free2'>Click here and Go Premium</a>");
                                        return "homepage";
                                    }
                                }
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
                                }
                                catch (SQLException ex)
                                {
                                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        else
                        {
                            model.addAttribute("expired", "<a href='signuppage' class='free2'>Click here and sign up</a>");
                        }*/
                    }
                }
                else
                {
                    model.addAttribute("exp", "<a href='signuppage' class='free2'>Please login or signup if you are a new user</a>");
                    return "homepage";
                }
                break;
            }
            case "rearrange":
            {
                if(username!=null)
                {
                    change=rearrange(sentence);
                }
                else
                {
                    model.addAttribute("expired", "<a href='signuppage' class='free2'>Please login or signup if you are a new user</a>");
                    return "homepage";
                }
                break;
            }
        }
        fc.setCount(sentence.size());
        fc.setField(new String(fc.getField().getBytes(ISO), UTF));
        ec.setField2(new String(change.getBytes(ISO), UTF));        
        List<String> outputSentence=pc.tokenizingTheInputText(new String(change.getBytes(ISO), UTF));
        ec.setCount2(outputSentence.size());
        return "homepage";
    }    
    
    @RequestMapping(value="/outputToInput", method=RequestMethod.POST)
    public String action(ModelMap map, @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        
        String content=new String(ec.getField2().getBytes(ISO), UTF);
        
        switch (ec.getAction())
        {
            case "resendToInput":
            if(!content.matches("\\s*"))
            {
                fc.setField(content);
                ec.setField2("");
                break;
            }
            else
            {
                model.addAttribute("fileUploadError", "Nothing to send back.");
                return "homepage";
            }                                        
            case "download":
                if(!content.matches("\\s*"))
                {
                    try
                    {
                        return "forward:/downloadcontroller?sent=" + URLEncoder.encode(content, "UTF-8");
                    }
                    catch (UnsupportedEncodingException ex)
                    {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    model.addAttribute("fileUploadError", "Cannot download an empty file.");
                    return "homepage";
                }
        }
        return "homepage";        
    }

    @RequestMapping(value="/downloadcontroller", method=RequestMethod.POST)
    public void downloadFile(@RequestParam("sent")String downloadableContent, HttpServletResponse res, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, @ModelAttribute("secondBeanObject")EditClass ec, 
    @ModelAttribute("firstBeanObject")FieldClass fc)
    {
        //res.setContentType("APPLICATION/OCTET-STREAM");
        res.setContentType("text/plain");
        res.addHeader("Content-Disposition", "attachment; filename=sumpis.txt");
        OutputStream out;
            
        try
        {
            byte[] bytes = new String(downloadableContent.getBytes(ISO), UTF).getBytes("UTF-8");
            out=res.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //optimized 5
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String uploadedFile1(@ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    HttpSession session, HttpServletRequest req)
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
            try
            {
                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                while(rs.next())
                {
                    /*String subscriptionDate=rs.getString(9);
                    Date d2=succ.dateSuffix(subscriptionDate);
                    if(new Date().compareTo(d2)==-1) //extra condition here to check for expired subscription
                    {*/
                        String fileToString="";
                        MultipartFile uploadedFile=fuc.getFile();
                        String nameOfFile=uploadedFile.getOriginalFilename();
                        ByteArrayInputStream fileStream=new ByteArrayInputStream(uploadedFile.getBytes());            
                        
                        if((nameOfFile.endsWith(".pdf") || nameOfFile.endsWith(".docx") || 
                        nameOfFile.endsWith(".doc") || nameOfFile.endsWith(".txt")))
                        {
                            if(uploadedFile.getSize()==0)
                            {
                                model.addAttribute("fileUploadError", "Empty file");
                            }
                            else if(uploadedFile.getSize()<=500000)//500kb
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
                                    fileToString = ext.getText();
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
                                model.addAttribute("fileUploadError", "File size exceeded!");
                            }
                        }
                        else
                        {
                            fileToString="File format not supported.";
                            model.addAttribute("fileUploadError", fileToString);
                        }
                        fc.setField(fileToString);
                        return "homepage";
                    /*}
                    else
                    {
                        model.addAttribute("fileUploadError", "Please Go Premium");
                    }*/
                }
            }
            catch (SQLException | IOException ex)
            {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                if(rs!=null)
                {
                    try
                    {
                        rs.close();
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    @RequestMapping(value="/signuppage", method=RequestMethod.GET)
    public String signuppage(@ModelAttribute("fourthBeanObject")MemberClass mc, ModelMap model)
    {        
        model.addAttribute("fifthBeanObject", new SignupClass());
        return "registration";
    }    
    //optimized 3
    @RequestMapping(value="/upload2", method=RequestMethod.POST)
    public String uploadedFile2(@ModelAttribute("thirdBeanObject") FileUploadClass fuc, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("secondBeanObject") EditClass ec, ModelMap model, @ModelAttribute("fourthBeanObject")MemberClass mc, 
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpServletRequest req, HttpSession session)
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
            try
            {
                model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                model.addAttribute("friendrequest", succ.getFriendRequest(username));
                rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                while(rs.next())
                {
                    /*String subscriptionDate=rs.getString(9);
                    Date d2=succ.dateSuffix(subscriptionDate);
                    if(new Date().compareTo(d2)==-1) //extra condition here to check for expired subscription
                    {*/
                        String fileToString="";
                        MultipartFile uploadedFile=fuc.getFile();
                        String nameOfFile=uploadedFile.getOriginalFilename();
                        ByteArrayInputStream fileStream=new ByteArrayInputStream(uploadedFile.getBytes());            
                        
                        if((nameOfFile.endsWith(".pdf") || nameOfFile.endsWith(".docx") || 
                        nameOfFile.endsWith(".doc") || nameOfFile.endsWith(".txt")))
                        {
                            if(uploadedFile.getSize()==0)
                            {
                                model.addAttribute("fileUploadError", "Empty file");
                            }
                            else if(uploadedFile.getSize()<=500000)//500kb
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
                                    fileToString = ext.getText();
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
                                model.addAttribute("fileUploadError", "File size exceeded!");
                            }
                        }
                        else
                        {
                            fileToString="File format not supported.";
                            model.addAttribute("fileUploadError", fileToString);
                        }
                        fc.setField(fileToString);
                    /*}
                    else
                    {
                        model.addAttribute("fileUploadError", "Please Go Premium");
                    }*/
                    model.addAttribute("displayadvert", displayadvert);
                    return "registeredMember";
                }
            }
            catch (SQLException | IOException ex)
            {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                if(rs!=null)
                {
                    try
                    {
                        rs.close();
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
    
    //optimized 2
    @RequestMapping(value="/outputToInput2", method=RequestMethod.POST)
    public String action2(ModelMap map, @ModelAttribute("secondBeanObject")EditClass ec, @ModelAttribute("firstBeanObject")FieldClass fc, 
    @ModelAttribute("fourthBeanObject")MemberClass mc, ModelMap model, @ModelAttribute("eleventhBeanObject")SearchPersonClass spc,
    HttpServletRequest req, @ModelAttribute("seventhBeanObject") WorkOnFile wof, @ModelAttribute("eightBeanObject")WriteUs wu, 
    @ModelAttribute("fourteenthBeanObject")NewsPostClass npc, HttpSession session)
    {
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");        
        if(username!=null)
        {
            String content=new String(ec.getField2().getBytes(ISO), UTF);
            
            switch (ec.getAction())
            {
                case "resendToInput":
                    if(!content.matches("\\s*"))
                    {
                        fc.setField(content);
                        ec.setField2("");
                        break;
                    }            
                    else
                    {
                        model.addAttribute("fileUploadError", "Nothing to send back.");
                        break;
                    }                                        
                case "download":
                    if(!content.matches("\\s*"))
                    {
                        try
                        {
                            content=content.replaceAll("<#", "");
                            content=content.replaceAll("#>", "");
                            return "forward:/downloadcontroller?sent=" + URLEncoder.encode(content, "UTF-8");
                        }
                        catch (UnsupportedEncodingException ex)
                        {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                
                    else
                    {
                        model.addAttribute("fileUploadError", "Cannot download an empty file.");
                        break;
                    }                    
                case "forward":
                    if(!content.matches("\\s*"))
                    {
                        wu.setWriteUp(content);
                        model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                        model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        return "message";
                    }                
                    else
                    {
                        model.addAttribute("fileUploadError", "Cannot forward an empty file.");
                        break;
                    }                    
                case "newspost":
                    if(!content.matches("\\s*"))
                    {
                        npc.setNewsPost(content);
                        model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                        model.addAttribute("friendrequest", succ.getFriendRequest(username));
                        return "sumpisnews";
                    }                
                    else
                    {
                        model.addAttribute("fileUploadError", "Cannot forward an empty file.");
                        break;
                    }
            }
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
            model.addAttribute("displayadvert", displayadvert);
            return "registeredMember";
        }
        else
        {
            ec.setField2("");
            model.addAttribute("expired", "Please login.");
            return "homepage";
        }                
    }
    
    //optimized 1
    @RequestMapping(value="/submit2", method=RequestMethod.POST)
    public String getResult2(@ModelAttribute("firstBeanObject")FieldClass fc, @ModelAttribute("secondBeanObject")EditClass ec, ModelMap model, 
    @ModelAttribute("thirdBeanObject")FileUploadClass fuc, @ModelAttribute("fourthBeanObject")MemberClass mc,
    @ModelAttribute("eleventhBeanObject")SearchPersonClass spc, HttpServletRequest req, HttpSession session)
    {    
        //ResultSet rs=null;
    
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        
        //Advert
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        if(username!=null)
        {
            //Date d1=new Date();
            ParaphraseClass pc=new ParaphraseClass();
            List<String> sentence=pc.tokenizingTheInputText(fc.getField());
            String change="";
            int wordCount;
            switch (fc.getChoice())
            {
                case "getSentenceCount":
                {
                    change=sentence.size() + " sentence(s).";
                    break;
                }
                case "getWordCount":
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        change="There is/are no sentence(s) present";
                    }
                    else
                    {
                        wordCount=cc.wordCount(fc.getField());
                        change=wordCount+ " word(s)";
                    }
                    break;
                }
                case "paraphrase":
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        model.addAttribute("expired", "There is/are no sentence(s) present");
                    }
                    else
                    {
                        analytics(fc.getField()); //Very important for analytics(saving words that is not in our database yet)
                        /*try
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                            while(rs.next())
                            {
                                String subscriptionDate=rs.getString(9);
                                Date d2=succ.dateSuffix(subscriptionDate);
                                if(d1.compareTo(d2)==-1)//extra condition here to check for expired subscription
                                {*/
                                    change=changed(sentence);
                                /*}
                                else
                                {
                                    model.addAttribute("expired", "<a href='premium' class='free2'>Click here and Go Premium</a>");
                                    return "registeredMember";
                                }
                            }
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
                            }
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }*/
                    }                    
                    break;
                }
                case "streamlinedsummary":
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        model.addAttribute("expired", "There is/are no sentence(s) present");
                    }
                    else
                    {
                        analytics(fc.getField()); //Very important for analytics(saving words that is not in our database yet)
                        /*try
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                            while(rs.next())
                            {
                                String subscriptionDate=rs.getString(9);
                                Date d2=succ.dateSuffix(subscriptionDate);
                                if(d1.compareTo(d2)==-1)//extra condition here to check for expired subscription
                                {*/
                                    change=streamlinedSummary(sentence, fc.getLength());
                                /*}
                                else
                                {
                                    model.addAttribute("expired", "<a href='premium' class='free2'>Click here and Go Premium</a>");
                                    return "registeredMember";
                                }
                            }
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
                            }
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }*/
                    }
                    break;
                }
                case "paraphrase_summary":
                {
                    if(fc.getField().matches("\\s*"))
                    {
                        model.addAttribute("expired", "There is/are no sentence(s) present");
                    }
                    else
                    {
                        analytics(fc.getField()); //Very important for analytics(saving words that is not in our database yet)
                        /*try
                        {
                            rs=con.createStatement().executeQuery("SELECT * FROM registeredusers WHERE username='"+username+"'");
                            while(rs.next())
                            {
                                String subscriptionDate=rs.getString(9);
                                Date d2=succ.dateSuffix(subscriptionDate);
                                if(d1.compareTo(d2)==-1)//extra condition here to check for expired subscription
                                {*/
                                    change=streamlineAndParaphrase(sentence, fc.getLength());
                                /*}
                                else
                                {
                                    model.addAttribute("expired", "<a href='premium' class='free2'>Click here and Go Premium</a>");
                                    return "registeredMember";
                                }
                            }
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
                            }
                            catch (SQLException ex)
                            {
                                Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }*/
                    }
                    break;
                }
                case "rearrange":
                {
                    change=rearrange(sentence);
                    break;
                }
            }        
            fc.setCount(sentence.size());
            fc.setField(new String(fc.getField().getBytes(ISO), UTF));
            ec.setField2(new String(change.getBytes(ISO), UTF));
            List<String> outputSentence=pc.tokenizingTheInputText(new String(change.getBytes(ISO), UTF));
            ec.setCount2(outputSentence.size());
            model.addAttribute("unreadMessages", succ.getUnreadCount(username));
            model.addAttribute("friendrequest", succ.getFriendRequest(username));
            model.addAttribute("displayadvert", displayadvert);
            return "registeredMember";
        }        
        else
        {
            model.addAttribute("expired", "Please login...");
            model.addAttribute("year", new SimpleDateFormat("yyyy").format(new Date()));
            return "homepage";
        }
    }
    
    @RequestMapping(value="/errorcontroller")
    public String error(ModelMap model, HttpServletRequest req, HttpSession session)
    {
        ResultSet rs=null;
        
        List<List<String>> ad=succ.adAlgorithm("GENERAL", 6);
        model.addAttribute("adid", ad.get(0));
        model.addAttribute("adimg", ad.get(1));
        
        
        session=req.getSession();
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        String firstname=(String)session.getAttribute("firstname");
        String lastname=(String)session.getAttribute("lastname");
        model.addAttribute("eleventhBeanObject", new SearchPersonClass());
        model.addAttribute("name", " @"+username+", ");
        if(username!=null)
        {
            String adminCrossCheck="SELECT * FROM admintable WHERE username='" + username + "'";
            try
            {
                rs=con.createStatement().executeQuery(adminCrossCheck);
                while(rs.next())
                {
                    if(password.equals(rs.getString(5)) && firstname.equals(rs.getString(2)) 
                    && lastname.equals(rs.getString(3)))
                    {
                        model.addAttribute("unreadMessages", succ.getUnreadCount(admin));
                        return "adminerrorpage";
                    }
                }
                model.addAttribute("unreadMessages", succ.getUnreadCount(username));
                model.addAttribute("friendrequest", succ.getFriendRequest(username));
                return "errorpage";
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
        model.addAttribute("fourthBeanObject", new MemberClass());
        return "error";
    }
    
    private void analytics(String sentence)//This is for saving words we don't have in our database yet
    {
        ResultSet rs=null;
        String[] split=sentence.split("\\s+");
        List<String> myList=new LinkedList<>(Arrays.asList(split));
        String wordsNotFound="";
        
        try
        {
            /*for(String eachWord:split)
            {
                rs=con.createStatement().executeQuery("SELECT * FROM contextdatabase WHERE word='"+eachWord+"'");
                while(rs.next())
                {
                    myList.remove(eachWord);
                }
            }*/
            
            for(String eachWord:split)
            {
                rs=con.createStatement().executeQuery("SELECT * FROM phrasereplacement WHERE phrases='"+eachWord+"'");
                while(rs.next())
                {
                    myList.remove(eachWord);
                }
            }
            
            for(String s:myList)
            {
                wordsNotFound += s+ " ";
            }
            
            if(wordsNotFound.length()!=0)
            {
                String hd="<div style='text-align: center; font-weight: bold; font-size: 20px; color: blue;'>Add To Database</div><br>";
                cc.saveMsg("User", admin, "<span style='background-color: yellow; color:red;'>Analytics</span>", hd+wordsNotFound, new UtilityClass().getDate(), "New");
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if(rs!=null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}