/*
 * The MIT License
 *
 * Copyright 2015 Andreas Giemza <andreas@giemza.net>.
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
package de.andreasgiemza.mangadownloader.sites.extend;

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
public class MangaHere implements Site {

    private final String baseUrl;

    public MangaHere(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public List<Manga> getMangaList() throws IOException {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + "mangalist/");

        Elements cols = doc.select("div[class=list_manga]");

        for (Element col : cols) {
            Elements rows = col.select("li");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.attr("rel")));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws IOException {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        // Check if comic not there (One Piece - Colored) ...
        Elements pageNotFound = doc.select("div[class=detail_list]");
        if (pageNotFound.isEmpty()) {
            return chapters;
        }

        // Check if comic was licensed (One Piece) ...
        Elements licensed = doc.select("div[class=detail_list]").first().select("div[class=mt10 color_ff00 mb10]");
        if (!licensed.isEmpty()) {
            return chapters;
        }

        Elements rows = doc.select("div[class=detail_list]").first().select("ul").first().select("li");

        for (Element row : rows) {
            if (row.select("a").first() == null) {
                continue;
            }

            chapters.add(new Chapter(
                    row.select("a").first().attr("abs:href"),
                    buildChapterName(row)));
        }

        return chapters;
    }

    private String buildChapterName(Element row) {
        String front = row.select("a").first().text();
        String middle = row.select("span[class=mr6]").first().text();
        String end = row.select("span").first().text().replace(front + middle, "");

        if (!middle.isEmpty()) {
            front += " " + middle;
        }

        if (!end.isEmpty()) {
            front += " " + end;
        }

        return front;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws IOException {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[onchange=change_page(this)]").first()
                .select("option");

        for (Element page : nav) {
            if (page != nav.first()) {
                referrer = page.attr("value");
                if (!referrer.startsWith("http://")) {
                    referrer = baseUrl + referrer;
                }
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=image]").first().attr("src");
            String linkTemp = link.split("\\?v=")[0];
            String extension = linkTemp.substring(linkTemp.length() - 3, linkTemp.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }
}
