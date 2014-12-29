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

import de.andreasgiemza.mangadownloader.chapters.Chapter;
import de.andreasgiemza.mangadownloader.images.Image;
import de.andreasgiemza.mangadownloader.mangas.Manga;
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
public class Mangacow implements Site {

    private final String baseUrl = "http://mangacow.co";

    @Override
    public List<Manga> updateMangaList() {
        List<Manga> mangas = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(baseUrl + "/manga-list/")
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Element div = doc.select("div[class=wpm_pag mng_lst tbn]").first();
            Elements rows = div.select("div[class^=nde]");

            for (Element row : rows) {
                Element link = row.select("a").first();

                mangas.add(new Manga(link.attr("href"), link.attr("title"), link.attr("title")));
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

            Element table = doc.select("ul[class=pgg]").first();

            int pages = 1;
            Elements rows;

            if (table != null) {
                rows = table.select("li");
                pages = Integer.parseInt(rows.get(rows.size() - 3).text());
            }

            for (int i = 1; i <= pages; i++) {
                doc = Jsoup.connect(manga.getLink() + "chapter-list/" + i + "/")
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .get();

                table = doc.select("ul[class=lst mng_chp]").first();
                rows = table.select("li");

                for (Element row : rows) {
                    Element link = row.select("a").first();

                    chapters.add(new Chapter(link.attr("href"), link.attr("title")));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return chapters;
    }

    @Override
    public List<Image> downloadChapter(Chapter chapter) {
        List<Image> images = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(chapter.getLink())
                    .maxBodySize(10 * 1024 * 1024)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .get();

            Element nav = doc.select("select[class=cbo_wpm_pag]").first();
            Elements nav_options = nav.select("option");

            int pages = nav_options.size();

            for (int i = 0; i < pages; i++) {
                doc = Jsoup.connect(chapter.getLink() + (i + 1) + "/")
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .get();

                String imageLink = doc.select("div[class=prw]").first().select("img").attr("src");
                String imageExtension = imageLink.substring(imageLink.length() - 3, imageLink.length());

                byte[] image = Jsoup.connect(imageLink)
                        .maxBodySize(10 * 1024 * 1024)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                        .ignoreContentType(true)
                        .execute().bodyAsBytes();

                images.add(new Image(image, imageExtension));
            }
        } catch (IOException ex) {
            Logger.getLogger(Batoto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return images;
    }
}
