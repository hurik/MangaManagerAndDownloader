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
package de.andreasgiemza.mangadownloader.sitesdown;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MeinManga implements Site {

    private final String baseUrl = "http://www.meinmanga.com/";

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + "directory/all/");

        Elements rows = doc.select("table[class=chaplist]").first().select("tr");

        for (Element row : rows) {
            if (rows.indexOf(row) < 3) {
                continue;
            }

            Element link = row.select("a").first();
            mangas.add(new Manga(link.attr("href"), link.text()));
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        int pages = doc.select("div[class=gotopage]").first().select("a").size();

        if (pages > 1) {
            pages--;
        }

        for (int i = 0; i < pages; i++) {
            if (i != 0) {
                doc = JsoupHelper.getHTMLPage(manga.getLink() + i + "/");
            }

            Elements rows = doc.select("table[class=chaplist]").first().select("tr");

            for (Element row : rows) {
                if (row == rows.first()) {
                    continue;
                }

                Element link = row.select("a").first();

                chapters.add(new Chapter(link.attr("href"), link.text()));
            }
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[onchange=javascript:location.href=this.value + '.html';]").first()
                .select("option");

        int pages = nav.size();

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink() + i + ".html";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[class^=pic_fragment]").first().attr("src");
            String link_fragment = doc.select("img[class^=pic_fragment]").last().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension, link_fragment));
        }

        return images;
    }

}
