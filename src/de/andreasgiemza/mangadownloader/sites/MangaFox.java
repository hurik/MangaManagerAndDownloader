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
package de.andreasgiemza.mangadownloader.sites;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Manga;
import java.io.IOException;
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
public class MangaFox implements Site {

    private final String baseUrl = "http://mangafox.me";

    @Override
    public List<Manga> getMangaList() {
        List<Manga> mangas = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(baseUrl + "/manga/")
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Elements rows = doc.select("div[class=left]").first().select("li");

            for (Element row : rows) {
                Element link = row.select("a[class^=series_preview]").first();

                if (link == null) {
                    continue;
                }

                mangas.add(new Manga(link.attr("href"), link.text()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) {
        List<Chapter> chapters = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(manga.getLink())
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Elements rows = doc.select("div[id=chapters]").first().select("li");

            for (Element row : rows) {
                chapters.add(new Chapter(row.select("a[class=tips]").first().attr("href"), row.select("a[class=tips]").first().text()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return chapters;
    }

    @Override
    public List<String> getChapterImageLinks(Chapter chapter) {
        List<String> images = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(chapter.getLink().endsWith("1.html") ? chapter.getLink() : chapter.getLink() + "1.html")
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Elements nav = doc.select("select[onchange=change_page(this)]").first()
                    .select("option");

            int pages = nav.size() - 1;

            for (int i = 1; i <= pages; i++) {
                if (i != 1) {
                    doc = Jsoup.connect(chapter.getLink().replace("1.html", "") + i + ".html")
                            .maxBodySize(10 * 1024 * 1024)
                            .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                            .get();
                }

                images.add(doc.select("img[id=image]").first().attr("src"));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return images;
    }
}
