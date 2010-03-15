/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.searchmaster.model;

import com.xload.generic.WeightedItem;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import net.htmlparser.jericho.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author madrax
 */
public class Master {

  public static final int HASTABLE_DROP_INDEX = 0;
  public static final int MIN_WORD_LENGTH = 3;
  public static final boolean DIVE_FIRST_LEVEL = false;


  private static String textify(String url) {
    return textify(url, Master.DIVE_FIRST_LEVEL);
  }
  
  /*
   * Given a URL, gets all the text of the webpage.
   * If DIVE is true, it will also get the text for all the links on that webpage.
   *
   * @parms     url     Webpage to textify
   * @parms     dive    Dive 1st Level
   * @returns   String  Webpage text
   * @author madrax
   */
  private static String textify(String url, boolean dive) {
    HttpClient httpclient = new DefaultHttpClient();
    CookieStore cookieStore = (CookieStore) new BasicCookieStore();
    HttpContext localContext = new BasicHttpContext();
    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    HttpGet httpget = new HttpGet(url);
    HttpResponse response = null;
    try {
      response = httpclient.execute(httpget, localContext);
    } catch (IOException ex) {
      System.out.println("Master.textify error : "+ex.toString());
    }
    HttpEntity entity = response.getEntity();
    ByteArrayOutputStream sourceHtml = new ByteArrayOutputStream();
    try {
      entity.writeTo(sourceHtml);
    } catch (IOException ex) {
      System.out.println("Master.textify error : "+ex.toString());
    }
    Source source=new Source(sourceHtml.toString());
    source.fullSequentialParse();
    String webpageText = "";
//    webpageText += getTitle(source);
//    webpageText += getMetaValue(source,"description");
    if (getMetaValue(source,"keywords")!=null)
      webpageText += getMetaValue(source,"keywords");
    List<Element> linkElements=source.getAllElements(HTMLElementName.A);
    if (dive) {
      for (Element linkElement : linkElements) {
        String href=linkElement.getAttributeValue("href");
        if (href.indexOf("http://")==-1 && href.indexOf("https://")==-1) {
          href = url+href;
        }
        if (isValidLink(href)) {
          //String label=linkElement.getContent().getTextExtractor().toString();
          webpageText += textify(href, false);
        }
      }
    }
    //webpageText += source.getTextExtractor().setIncludeAttributes(true).toString();
    //webpageText = webpageText.replaceAll("[^a-zA-Z0-9ñÑáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛçÇàèìòùÀÈÌÒÙ ]", "");

    //String webpageTextAlt = Master.getAltText(source);    
    return webpageText;
  }
  private static String getTitle(Source source) {
    Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
    if (titleElement==null) return null;
    // TITLE element never contains other tags so just decode it collapsing whitespace:
    return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
  }

  private static String getMetaValue(Source source, String key) {
    for (int pos=0; pos<source.length();) {
      StartTag startTag=source.getNextStartTag(pos,"name",key,false);
      if (startTag==null) return null;
      if (startTag.getName()==HTMLElementName.META)
              return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
      pos=startTag.getEnd();
    }
    return null;
  }

  private static String getAltText(Source source) {
    AltTextExtractor textExtractor= new AltTextExtractor(source);
    return textExtractor.setIncludeAttributes(true).toString();
  }

  public static ArrayList<String> addLSIKeywords(ArrayList<String> keywordList, Hashtable hashResults, String keyword) {
    ArrayList<WeightedItem> sortedKeywords = new ArrayList<WeightedItem>();
    Enumeration e = hashResults.keys();
    while( e.hasMoreElements() ){
      String word = (String) e.nextElement();
      WeightedItem wItem = new WeightedItem(word,(Integer) hashResults.get(word));
      sortedKeywords.add(wItem);
    }
    Collections.sort(sortedKeywords, Collections.reverseOrder());
//    ArrayList<WeightedItem> sortedKeywords2 = (ArrayList<WeightedItem>) sortedKeywords.clone();
//    for (WeightedItem weightedItem : sortedKeywords) {
//      sortedKeywords2.remove(weightedItem);
//      for (WeightedItem weightedItem2 : sortedKeywords) {
//        keywordList.add(keyword+" "+weightedItem.getKey()+" ("+weightedItem.getWeight()+") "+weightedItem2.getKey()+" ("+weightedItem2.getWeight()+")");
//      }
//    }
    for (WeightedItem weightedItem : sortedKeywords) {
        keywordList.add(weightedItem.getKey()); // +" ("+weightedItem.getWeight()+")"
    }
    return keywordList;
  }


  /*
   * Given a URL, creates a Hashtable of weighted keywords, and adds them to a current hashtable
   *
   * @parms     url         Webpage to be parsed
   * @parms     hashResults Existng Hash
   * @returns   String      New hash
   * @author madrax
   */
  public static Hashtable parseURL(String url, Hashtable hashResults) {
    String webText = Master.textify(url);
    System.out.println(webText);
    String[] words = webText.split(",");
    for (String word : words) {
      if (word.length()>Master.MIN_WORD_LENGTH) {
        Integer i = (Integer) hashResults.get(word);
        if (i==null) i=0;
        i++;
        hashResults.put(word.toLowerCase(), i);
      }
    }
    Enumeration e = hashResults.keys();
    while( e.hasMoreElements() ){
      String word = (String) e.nextElement();
      Integer i = (Integer) hashResults.get(word);
      if (i<Master.HASTABLE_DROP_INDEX) {
        hashResults.remove(word);
      }
    }
    System.out.println(hashResults.toString());
    return hashResults;
  }

  private static boolean isValidLink(String urlString) {
    try {
      URL url = new URL(urlString);
      URLConnection connection =
      url.openConnection();
      if (connection instanceof HttpURLConnection) {
        HttpURLConnection httpConnection = (HttpURLConnection)connection;
        httpConnection.connect();
        int response = httpConnection.getResponseCode();
        InputStream is = httpConnection.getInputStream();
        byte[] buffer = new byte [256];
        while (is.read (buffer) != -1) {}
        is.close();
        if (response >= 100 && response < 300)
          return true;
      }
    }
    catch (IOException e) {
      //e.printStackTrace();
      System.out.println("Invalid link : "+urlString);
    }
    return false;
  }
}
