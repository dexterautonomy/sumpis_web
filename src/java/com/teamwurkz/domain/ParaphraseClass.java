package com.teamwurkz.domain;
import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ParaphraseClass
{
    BreakIterator breakIterator;
    String mainText;
    List<String>list;
    
    public List<String> tokenizingTheInputText(String inputText)
    {
        list=new LinkedList<>();        
        breakIterator=BreakIterator.getSentenceInstance(Locale.ROOT);
        breakIterator.setText(inputText);
        int start=breakIterator.first();        
        for(int end=breakIterator.next(); end!=BreakIterator.DONE; start=end, end=breakIterator.next())
        {
            mainText=inputText.substring(start, end);
            list.add(mainText);
        }
        return list;
    }
}