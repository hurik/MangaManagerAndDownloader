package de.andreasgiemza.mangadownloader.helpers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.andreasgiemza.mangadownloader.data.Image;
import org.jsoup.Connection;

/**
 * @author chillikun
 * @year 2017
 *
 * This class helps in login to Batoto New Site and Retrieve Chapter Images etc ...
 */
public final class BatotoJsoupHelper {

    private final static int NUMBER_OF_TRIES = 5;
    private final static int MAX_BODY_SIZE = 0; // Unlimited (limited only by
    // RAM)

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0";
    private final static String USER_AGENT_MOBILE = "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19";

    private static Map<String, String> cookies;

    private BatotoJsoupHelper() {
    }
    
//    Static code to generate cookies for Batoto Login
    static
   {
        if ((cookies == null) || (cookies.isEmpty())) {
            try
            {           
                Connection.Response res =  Jsoup.connect("http://bato.to/forums/index.php?app=core&module=global&section=login&do=process")
                                                .data(new String[]{
                                                    "auth_key", "880ea6a14ea49e853634fbdc5015a024",
                                                    "referer", "https://bato.to/forums/", 
                                                    "ips_username", "liju23",
                                                    "ips_password", "T3mp4n0w",
                                                    "rememberMe", "1",
                                                    "anonymous", "1"})
                                                .method(Connection.Method.POST).maxBodySize(0).timeout(10000)
                                                .userAgent(USER_AGENT)
                                                .referrer("http://bato.to/forums/index.php?app=core&module=global&section=login").execute();
                cookies = res.cookies();
                
            }
            catch (Exception ex)
            {
                System.err.println("Unable to Login to Batoto. Error: " + ex.getMessage());
            }
        }
    }

    public static Document getHTMLPage(String url) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(url).maxBodySize(MAX_BODY_SIZE).timeout((i + 1) * 3000).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).cookies(cookies).referrer("https://bato.to/reader").get();
            } catch (Exception e) {
                System.err.println(
                        "Try " + (i + 1) + " of " + NUMBER_OF_TRIES + ". Link: " + url + ". Error: " + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageWithPost(String url, Map<String, String> post) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(url).maxBodySize(MAX_BODY_SIZE).timeout((i + 1) * 3000).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).cookies(cookies).referrer("https://bato.to/reader").data(post).post();
            } catch (Exception e) {
                System.err.println(
                        "Try " + (i + 1) + " of " + NUMBER_OF_TRIES + ". Link: " + url + ". Error: " + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageMobile(String url) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(url).maxBodySize(MAX_BODY_SIZE).timeout((i + 1) * 3000)
                        .userAgent(USER_AGENT_MOBILE).cookies(cookies).referrer("https://bato.to/reader").ignoreHttpErrors(true).get();
            } catch (Exception e) {
                System.err.println(
                        "Try " + (i + 1) + " of " + NUMBER_OF_TRIES + ". Link: " + url + ". Error: " + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImage(String imageLink, String referrer) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(imageLink).maxBodySize(MAX_BODY_SIZE).timeout((i + 1) * 3000).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).referrer(referrer).cookies(cookies).referrer("https://bato.to/reader").ignoreContentType(true).execute().bodyAsBytes();
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES + ". Link: " + imageLink + ". Error: "
                        + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImageWithFragment(Image images) throws Exception {
        byte[] imgByte1 = getImage(images.getLink(), images.getReferrer());
        byte[] imgByte2 = getImage(images.getLinkFragment(), images.getReferrer());

        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(imgByte1));
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(imgByte2));

        int width = Math.max(img1.getWidth(), img2.getWidth());
        int height = img1.getHeight() + img2.getHeight();

        // create a new buffer and draw two image into the new image
        BufferedImage combinedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = combinedImg.createGraphics();
        Color oldColor = g2.getColor();
        // fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        // draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight());
        g2.dispose();

        byte[] imageInByte;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(combinedImg, images.getExtension(), baos);
            baos.flush();
            imageInByte = baos.toByteArray();
        }

        return imageInByte;
    }

    // http://nanashi07.blogspot.de/2014/06/enable-ssl-connection-for-jsoup.html
    public static void deactivateCertificateCheck() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            System.err.println("JsoupHelper.deactivateCertificateCheck() error: " + ex.getMessage());
        }
    }

}
