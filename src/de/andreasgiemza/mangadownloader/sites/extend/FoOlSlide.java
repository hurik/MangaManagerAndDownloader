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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class FoOlSlide implements Site {

    private final String baseUrl;
    private final String listUrl;
    private final Map<String, String> post = new HashMap<>();

    public FoOlSlide(String baseUrl, String listUrl) {
        this.baseUrl = baseUrl;
        this.listUrl = listUrl;

        post.put("adult", "true"); // VortexScans adult check
        post.put("category", "1"); // SilentSkyScans Webton view
    }

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + listUrl);

        int pages = 1;

        Element nav = doc.select("div[class=prevnext]").first();

        if (nav != null) {
            String[] pagesData = nav.select("a[class=gbutton fright]").first().attr("href").split("/");
            pages = Integer.parseInt(pagesData[pagesData.length - 1]);
        }

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(baseUrl + listUrl + i + "/");
            }

            Elements rows = doc.select("div[class=group]");

            for (Element row : rows) {
                Element link = row.select("div[class=title]").first().select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.text()));
            }
        }

        return new LinkedList<>(new HashSet<>(mangas));
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPageWithPost(manga.getLink(), post);

        Elements rows = doc.select("div[class=element]");

        for (Element row : rows) {
            Element link = row.select("div[class=title]").first().select("a").first();
            chapters.add(new Chapter(link.attr("abs:href"), link.text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPageWithPost(referrer, post);

        int pages = Integer.parseInt(doc.select("div[class=tbtitle dropdown_parent dropdown_right]").first()
                .select("div[class=text]").first().text().replaceFirst("\\D*(\\d*).*", "$1"));

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink() + "page/" + i;
                doc = JsoupHelper.getHTMLPageWithPost(referrer, post);
            }

            String link = doc.select("img[class=open]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }
}
