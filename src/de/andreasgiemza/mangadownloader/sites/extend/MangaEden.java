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
public class MangaEden implements Site {

    private final String baseUrl;
    private final String listUrl;

    public MangaEden(String baseUrl, String list) {
        this.baseUrl = baseUrl;
        this.listUrl = list;
    }

    @Override
    public List<Manga> getMangaList() throws IOException {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + listUrl);

        Elements nav = doc.select("div[class=pagination pagination_bottom]").first().select("a");

        int pages = Integer.parseInt(nav.get(nav.size() - 2).text());

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(baseUrl + listUrl + "?page=" + i);
            }

            Elements rows = doc.select("table[id=mangaList]").first().select("tbody").first().select("tr");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws IOException {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("table").first().select("tbody").first().select("tr");

        for (Element row : rows) {
            Element link = row.select("a").first();
            chapters.add(new Chapter(link.attr("abs:href"), link.text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws IOException {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("div[class^=pagination]").first().select("a");

        int pages = Integer.parseInt(nav.get(nav.size() - 2).text());

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink().substring(0, chapter.getLink().length() - 2) + i + "/";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=mainImg]").first().attr("src").replace("//", "http://");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }
}
