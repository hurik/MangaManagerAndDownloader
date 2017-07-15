package de.andreasgiemza.mangadownloader.helpers;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;

import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;


/**
 *
 * @author chillikun
 */
public class CookieManager {

    private static HashMap<String, HashMap<String, String>> host2cookies = new HashMap<String, HashMap<String, String>>();

    public static String[] getcookies(String url) throws Exception {
        
        return null;

    }

    private static void loadCookiesByHost(String url, Connection con) {
        try {
            String host = gethost(url);
            if (host2cookies.containsKey(host)) {
                HashMap<String, String> cookies = host2cookies.get(host);
                for (Entry<String, String> cookie : cookies.entrySet()) {
                    con.cookie(cookie.getKey(), cookie.getValue());
                }
            }
        } catch (Throwable t) {
            // MTMT move to log
            System.err.println(t.toString() + ":: Error loading cookies to: " + url);
        }
    }

    private static void storeCookiesByHost(String url, Connection con) {
        try {
            String host = gethost(url);
            HashMap<String, String> cookies = host2cookies.get(host);
            if (cookies == null) {
                cookies = new HashMap<String, String>();
                host2cookies.put(host, cookies);
            }
            cookies.putAll(con.response().cookies());
        } catch (Throwable t) {
            // MTMT move to log
            System.err.println(t.toString() + ":: Error saving cookies from: " + url);
        }
    }
    
    private static String gethost(String url) throws MalformedURLException{
//    URI uri = new URI(url);
//    String domain = uri.getHost();
//    return domain.startsWith("www.") ? domain.substring(4) : domain;
    
       URL aURL = new URL(url);
       return aURL.getHost();
}
    
    
}
