/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.searchmaster.model;

import com.xload.generic.WeightedItem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    StringBuilder builder = new StringBuilder();
    try {
      // Convert spaces to +, etc. to make a valid URL
      keyword = URLEncoder.encode(keyword, "UTF-8");
      URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=large&v=1.0&q=" + keyword);
      URLConnection connection = url.openConnection();
      connection.addRequestProperty("Referer", HTTP_REFERER);
      // Get the JSON response
      String line;
      InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
      BufferedReader reader = new BufferedReader(inputStream);
      while((line = reader.readLine()) != null) {
        builder.append(line);
      }
      String response = builder.toString();
      System.out.println("Search for '"+keyword+"' returned : "+response);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    System.out.println("End SearchGoogleAPIByKeyword : "+System.currentTimeMillis());
    return builder.toString();
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
