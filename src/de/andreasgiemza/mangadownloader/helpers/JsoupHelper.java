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

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class JsoupHelper {

    private JsoupHelper() {
    }

    public static Document getHTMLPage(String url) throws IOException {
        for (int i = 1; i <= 3; i++) {
            try {
                return Jsoup.connect(url)
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .get();
            } catch (IOException e) {
            }
        }

        throw new IOException();
    }

    public static byte[] getImage(String url) throws IOException {
        for (int i = 1; i <= 3; i++) {
            try {
                return Jsoup.connect(url)
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .ignoreContentType(true)
                        .execute()
                        .bodyAsBytes();
            } catch (IOException e) {
            }
        }

        throw new IOException();
    }
}
