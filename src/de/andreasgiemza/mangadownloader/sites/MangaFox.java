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
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
    public List<Manga> getMangaList() throws IOException {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + "/manga/");

        Elements rows = doc.select("div[class=left]").first().select("li");

        for (Element row : rows) {
            Element link = row.select("a[class^=series_preview]").first();

            if (link == null) {
                continue;
            }

            mangas.add(new Manga(link.attr("href"), link.text()));
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws IOException {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("div[id=chapters]").first().select("li");

        for (Element row : rows) {
            chapters.add(new Chapter(
                    row.select("a[class=tips]").first().attr("href"),
                    row.select("a[class=tips]").first().text() + (row.select("span[class=title nowrap]").first() == null ? "" : " - " + row.select("span[class=title nowrap]").first().text())));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws IOException {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink().endsWith("1.html") ? chapter.getLink() : chapter.getLink() + "1.html";
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[onchange=change_page(this)]").first()
                .select("option");

        int pages = nav.size() - 1;

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink().replace("1.html", "") + i + ".html";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=image]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }
}
