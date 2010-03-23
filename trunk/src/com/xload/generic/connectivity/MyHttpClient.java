/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.generic.connectivity;

import com.xload.generic.AppProperties;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author SG0894180
 */
public class MyHttpClient {

  public static DefaultHttpClient getClient() {
      DefaultHttpClient httpclient = new DefaultHttpClient();

      //Credentials credentials = new  UsernamePasswordCredentials("sg0894180", "edcvfr4");
      String  proxy_url      = AppProperties.getProperty(AppProperties.PROXY_URL);
      Integer proxy_port     = Integer.parseInt(AppProperties.getProperty(AppProperties.PROXY_PORT));
      String  proxy_user     = AppProperties.getProperty(AppProperties.PROXY_USER);
      String  proxy_password = AppProperties.getProperty(AppProperties.PROXY_PASSWORD);
      
      httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(proxy_url, proxy_port),
                new UsernamePasswordCredentials(proxy_user, proxy_password));

      HttpHost proxy = new HttpHost(proxy_url, proxy_port);

      List authPrefs = new ArrayList();
        authPrefs.add(AuthPolicy.NTLM);

      httpclient.getParams().setParameter(AuthPolicy.NTLM, authPrefs);
      httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

      return httpclient;

  }

}
