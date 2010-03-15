/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.searchmaster;

import com.xload.searchmaster.model.GoogleAPI;
import com.xload.generic.WeightedItem;
import com.xload.searchmaster.model.Master;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author madrax
 */
public class Facade {

  public static ArrayList<String> generateLSI(String keyword) {
    return GoogleAPI.generateLSI(keyword);
  }

  public static ArrayList<WeightedItem> analyzeResultsIndex(ArrayList<String> results) {
    return GoogleAPI.analyzeResultsIndex(results);
  }

  public static WeightedItem getResultsPerKeyword(String s) {
    return GoogleAPI.getResultsPerKeyword(s);
  }

  public static String SearchGoogleAPIByKeyword(String keyword) {
    return GoogleAPI.SearchGoogleAPIByKeyword(keyword);
  }

  public static ArrayList<String> getCandidatesFromResponse(String JSONResponse) {
    return GoogleAPI.getCandidatesFromResponse(JSONResponse);
  }

  public static Hashtable parseURL(String canidate, Hashtable hashResults) {
    return Master.parseURL(canidate, hashResults);
  }

  public static ArrayList<String> addLSIKeywords(ArrayList<String> keywordList, Hashtable hashResults, String text) {
    return Master.addLSIKeywords(keywordList, hashResults, text);
  }

}
