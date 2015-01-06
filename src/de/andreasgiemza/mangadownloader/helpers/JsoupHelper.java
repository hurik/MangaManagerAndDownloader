/*
 * The MIT License
 *
 * Copyright 2014 Andreas Giemza <andreas@giemza.net>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.andreasgiemza.mangadownloader.helpers;

import de.andreasgiemza.mangadownloader.data.Image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class JsoupHelper {

    private JsoupHelper() {
    }

    public static Document getHTMLPage(String url) throws Exception {
        Exception ex = null;

        for (int i = 1; i <= 3; i++) {
            try {
                return Jsoup.connect(url)
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .get();
            } catch (Exception e) {
                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageWithPost(String url, Map<String, String> post) throws Exception {
        Exception ex = null;

        for (int i = 1; i <= 3; i++) {
            try {
                return Jsoup.connect(url)
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .data(post)
                        .post();
            } catch (Exception e) {
                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageMobile(String url) throws Exception {
        Exception ex = null;

        for (int i = 1; i <= 3; i++) {
            try {
                return Jsoup.connect(url)
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19")
                        .get();
            } catch (Exception e) {
                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImage(Image image) throws Exception {
        Exception ex = null;

        for (int i = 1; i <= 3; i++) {
            try {
                return Jsoup.connect(image.getLink())
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .referrer(image.getReferrer())
                        .ignoreContentType(true)
                        .execute()
                        .bodyAsBytes();
            } catch (Exception e) {
                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImageWithFragment(Image images) throws Exception {
        byte[] imgByte1 = null;
        byte[] imgByte2 = null;

        for (int i = 1; i <= 3; i++) {
            try {
                imgByte1 = Jsoup.connect(images.getLink())
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .referrer(images.getReferrer())
                        .ignoreContentType(true)
                        .execute()
                        .bodyAsBytes();
            } catch (Exception e) {
                throw e;
            }
        }

        for (int i = 1; i <= 3; i++) {
            try {
                imgByte2 = Jsoup.connect(images.getLinkFragment())
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .referrer(images.getReferrer())
                        .ignoreContentType(true)
                        .execute()
                        .bodyAsBytes();
            } catch (Exception e) {
                throw e;
            }
        }

        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(imgByte1));
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(imgByte2));

        int width = Math.max(img1.getWidth(), img2.getWidth());
        int height = img1.getHeight() + img2.getHeight();

        //create a new buffer and draw two image into the new image
        BufferedImage combinedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = combinedImg.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        //draw image
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
}
