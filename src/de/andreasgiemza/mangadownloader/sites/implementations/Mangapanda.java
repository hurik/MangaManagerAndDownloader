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
package de.andreasgiemza.mangadownloader.sites.implementations;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
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
public class Mangapanda implements Site {

    private final String baseUrl = "http://www.mangapanda.com";

    @Override
    public List<Manga> getMangaList() throws IOException {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + "/alphabetical");

        Elements cols = doc.select("div[class=series_col]");

        for (Element col : cols) {
            Elements rows = col.select("li");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws IOException {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + manga.getLink());

        Elements rows = doc.select("div[id=chapterlist]").first().select("tr");

        for (Element row : rows) {
            if (row.select("a").first() == null) {
                continue;
            }

            chapters.add(new Chapter(row.select("a").first().attr("href"), row.select("td").first().text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws IOException {
        List<Image> images = new LinkedList<>();

        String referrer = baseUrl + chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[id=pageMenu]").first()
                .select("option");

        int pages = nav.size();

        for (int i = 0; i < pages; i++) {
            if (i != 0) {
                referrer = baseUrl + nav.get(i).attr("value");
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=img]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }
}
