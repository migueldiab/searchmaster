/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.searchmaster.model;

import com.xload.generic.WeightedItem;
import com.xload.generic.connectivity.MyHttpClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;      // JSON library from http://www.json.org/java/
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author madrax
 */
public class GoogleAPI {

  // Put your website here
  private final static String HTTP_REFERER = "http://www.example.com/";

  /*
   * Given a seed Keyword will search Google top results, parse each link's text
   * and create a weighted list of words related to the seed, and return it sorted
   * by importance.
   *
   * @parms     keyword     Seed Keyword
   * @returns   ArrayList   List of best LSIs
   * @author madrax
   */
  public static ArrayList<String> generateLSI(String keyword) {
    return null;
  }

  /*
   * Searches Google for any given keyword and returns the JSON output as a string
   *
   * @parms     keyword     Seed Keyword
   * @returns   String      JSON Output
   * @author madrax
   */
  public static String SearchGoogleAPIByKeyword(String keyword) {
    System.out.println("Start SearchGoogleAPIByKeyword : "+System.currentTimeMillis());
    String stringResponse = null;
    try {
      // Convert spaces to +, etc. to make a valid URL
      keyword = URLEncoder.encode(keyword, "UTF-8");

      String url = "http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=large&v=1.0&lr=lang_pt&cr=countryBR&q=" + keyword;
      DefaultHttpClient httpclient = MyHttpClient.getClient();
      CookieStore cookieStore = (CookieStore) new BasicCookieStore();
      HttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
      HttpGet httpget = new HttpGet(url);
      HttpResponse response = null;
      try {
        response = httpclient.execute(httpget, localContext);
      } catch (IOException ex) {
        System.out.println("GoogleAPI.SearchGoogleAPIByKeyword error : "+ex.toString());
      }
      HttpEntity entity = response.getEntity();
      ByteArrayOutputStream sourceHtml = new ByteArrayOutputStream();
      try {
        entity.writeTo(sourceHtml);
      } catch (IOException ex) {
        System.out.println("Master.textify error : "+ex.toString());
      }
      stringResponse=sourceHtml.toString();

      // Get the JSON response
      System.out.println("Search for '"+keyword+"' returned : "+stringResponse);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    System.out.println("End SearchGoogleAPIByKeyword : "+System.currentTimeMillis());
    return stringResponse;
  }

  /*
   * Given a Google API JSON Input, gets a list of Candidate Links for that response.
   *
   * @parms     response    JSON Input
   * @returns   ArrayList   List of URLs
   * @author madrax
   */
  public static ArrayList<String> getCandidatesFromResponse(String response) {
    ArrayList<String> candidates = new ArrayList<String>();
    try {
      JSONObject jsonResponse = new JSONObject(response);
      String totalResults = jsonResponse.getJSONObject("responseData").getJSONObject("cursor").getString("estimatedResultCount");
      JSONArray jsonArrayOfResults = jsonResponse.getJSONObject("responseData").getJSONArray("results");
      for (int i = 0; i < jsonArrayOfResults.length(); i++) {
        JSONObject j = jsonArrayOfResults.getJSONObject(i);
        //String title = j.getString("titleNoFormatting");
        System.out.println("Candidate : "+j.getString("url"));
        String url = j.getString("url");
        candidates.add(url);
      }
    } catch (JSONException ex) {
      Logger.getLogger(GoogleAPI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return candidates;
  }

  public static ArrayList<WeightedItem> analyzeResultsIndex(ArrayList<String> results) {
    ArrayList<WeightedItem> newResults = new ArrayList<WeightedItem>();
    for (String string : results) {
      String response = GoogleAPI.SearchGoogleAPIByKeyword(string);
      int resultIndex = GoogleAPI.getTotalResults(response);
      WeightedItem wItem = new WeightedItem(string, resultIndex);
      newResults.add(wItem);
    }
    return newResults;
  }

  private static int getTotalResults(String response) {
    try {
      JSONObject jsonResponse = new JSONObject(response);
      String totalResults = jsonResponse.getJSONObject("responseData").getJSONObject("cursor").getString("estimatedResultCount");
      return Integer.parseInt(totalResults);
    } catch (JSONException ex) {
      Logger.getLogger(GoogleAPI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }

  public static WeightedItem getResultsPerKeyword(String s) {
    String response = GoogleAPI.SearchGoogleAPIByKeyword(s);
    int resultIndex = GoogleAPI.getTotalResults(response);
    WeightedItem wItem = new WeightedItem(s, resultIndex);
    return wItem;
  }

}
