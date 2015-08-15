package de.andreasgiemza.mangadownloader.helpers;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import de.andreasgiemza.mangadownloader.data.Image;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try (WebClient webClient = createWebClient(url)) {
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
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try (WebClient webClient = createWebClient(url)) {
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
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try (WebClient webClient = createWebClient(image.getLink())) {
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

    private static WebClient createWebClient(final String url) {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        // http://stackoverflow.com/a/23482615/2246865 by Neil McGuigan
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        webClient.setCssErrorHandler(new SilentCssErrorHandler());

        webClient.setIncorrectnessListener(new IncorrectnessListener() {
            @Override
            public void notify(String arg0, Object arg1) {
            }
        });

        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setUseInsecureSSL(true);

        // http://stackoverflow.com/a/14227559/2246865 by Lee
        webClient.setWebConnection(new WebConnectionWrapper(webClient) {
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                if (request.getUrl().toString().contains(getDomainName(url))) {
                    return super.getResponse(request);
                } else {
                    return new StringWebResponse("", request.getUrl());
                }
            }
        });

        return webClient;
    }

    // http://stackoverflow.com/a/9608008/2246865 by Mike Samuel
    private static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            return "";
        }
    }
}
