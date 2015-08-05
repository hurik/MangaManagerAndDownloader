package de.andreasgiemza.mangadownloader.helpers;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import de.andreasgiemza.mangadownloader.data.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class HTMLUnitHelper {

    private final static int NUMBER_OF_TRIES = 5;
    private final static CookieManager cookieManager = new CookieManager();

    private HTMLUnitHelper() {
    }

    public static Document getHTMLPage(String url) throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
                webClient.setCookieManager(cookieManager);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);

                HtmlPage page = webClient.getPage(url);

                JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
                while (manager.getJobCount() > 0) {
                    Thread.sleep(500);
                }

                return Jsoup.parse(((HtmlPage) webClient.getCurrentWindow().getEnclosedPage()).getBody().asXml());
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                        + ". Link: " + url + ". Error: " + e.getMessage());

                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageWithPost(String url, ArrayList<NameValuePair> post) throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
                webClient.setCookieManager(cookieManager);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);

                WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);

                requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
                requestSettings.getRequestParameters().addAll(post);

                HtmlPage page = webClient.getPage(requestSettings);

                JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
                while (manager.getJobCount() > 0) {
                    Thread.sleep(500);
                }

                return Jsoup.parse(((HtmlPage) webClient.getCurrentWindow().getEnclosedPage()).asXml());
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                        + ". Link: " + url + ". Error: " + e.getMessage());

                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImage(Image image) throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
                webClient.setCookieManager(cookieManager);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.addRequestHeader("Referer", image.getReferrer());

                UnexpectedPage page = webClient.getPage(image.getLink());

                JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
                while (manager.getJobCount() > 0) {
                    Thread.sleep(500);
                }

                return IOUtils.toByteArray(page.getWebResponse().getContentAsStream());
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                        + ". Link: " + image.getLink() + ". Error: " + e.getMessage());

                ex = e;
            }
        }

        throw ex;
    }
}
