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
public class Mangajoy implements Site {

    private final String baseUrl = "http://mangajoy.com";

    @Override
    public List<Manga> getMangaList() {
        List<Manga> mangas = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(baseUrl + "/manga-list-all/")
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Elements rows = doc.select("div[id=sct_manga_list_all]").first().select("li");

            for (Element row : rows) {
                Element link = row.select("a").first();

                mangas.add(new Manga(link.attr("href"), link.attr("title")));
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

            Elements rows = doc.select("ul[class=chp_lst]").first().select("li");

            for (Element row : rows) {
                chapters.add(new Chapter(row.select("a").first().attr("href"), row.select("span[class=val]").first().text()));
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
            Document doc = Jsoup.connect(chapter.getLink())
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Elements nav = doc.select("div[class=wpm_nav_rdr]").first().select("select[onchange^=location.href='" + chapter.getLink() + "' + this.value + '/']").first()
                    .select("option");

            int pages = nav.size();

            for (int i = 1; i <= pages; i++) {
                if (i != 1) {
                    doc = Jsoup.connect(chapter.getLink() + (i) + "/")
                            .maxBodySize(10 * 1024 * 1024)
                            .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                            .get();
                }

                images.add(doc.select("div[class=prw]").first().select("img").attr("src"));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return images;
    }
}
