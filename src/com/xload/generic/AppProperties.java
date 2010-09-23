/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.generic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author SG0894180
 */
public class AppProperties {
  public static final String PROXY_ENABLED = "proxy_enabled";
  public static final String PROXY_URL = "proxy_url";
  public static final String PROXY_PORT = "proxy_port";
  public static final String PROXY_USER = "proxy_user";
  public static final String PROXY_PASSWORD = "proxy_pass";

  public static String getProperty(String property_name) {

    Properties props = new Properties();
    String value = "";
    //try retrieve data from file
    try {
      props.load(new FileInputStream(System.getProperty("user.dir")+"\\src\\SearchMaster.properties"));
      value = props.getProperty(property_name);
    }
    //catch exception in case properties file does not exist
    catch(IOException e)
    {
      e.printStackTrace();
    }
    return value;
  }
  
}
