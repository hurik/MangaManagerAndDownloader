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
package de.andreasgiemza.mangadownloader.sites.notworking;

import de.andreasgiemza.mangadownloader.chapters.Chapter;
import de.andreasgiemza.mangadownloader.mangas.Manga;
import de.andreasgiemza.mangadownloader.sites.Batoto;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class LINEWebtoon implements Site {

    private final String baseUrl = "http://www.webtoons.com";
    private final String baseUrlMobile = "http://m.webtoons.com";

    @Override
    public List<Manga> getMangaList() {
        List<Manga> mangas = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(baseUrl + "/dailySchedule")
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Elements rows = doc.select("li");

            for (Element row : rows) {
                Elements data = row.select("h3");

                if (data.size() > 0) {
                    String title = row.select("h3").first().text() + " [" + row.select("p[class=author]").first().text() + "]";

                    if (row.select("span[class=txt_ico_completed2]").size() != 1) {
                        title += " [Ongoing]";
                    } else {
                        title += " [Complete]";
                    }

                    mangas.add(new Manga(
                            row.select("a").first().attr("abs:href"),
                            title,
                            row.select("h3").first().text()));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LINEWebtoon.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new LinkedList<>(new HashSet<>(mangas));
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) {
        List<Chapter> chapters = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(manga.getLink().replace(baseUrl, baseUrlMobile))
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19")
                    .get();

            Elements pages = doc.select("li[id^=episode]");

            for (Element page : pages) {
                chapters.add(new Chapter(baseUrl + page.select("a").first().attr("href"), page.select("span[class=ellipsis]").first().text()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return chapters;
    }

    @Override
    public List<String> getChapterImageLinks(Chapter chapter) {
        List<String> imageLinks = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(chapter.getLink())
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            // Get pages linkes
            Elements images = doc.select("img[class=_images]");

            for (Element image : images) {
                imageLinks.add(image.attr("data-url"));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return imageLinks;
    }
}
