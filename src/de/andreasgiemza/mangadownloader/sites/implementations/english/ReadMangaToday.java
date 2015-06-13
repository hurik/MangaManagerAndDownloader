/*
 * The MIT License
 *
 * Copyright 2015 hurik.
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
package de.andreasgiemza.mangadownloader.sites.implementations.english;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author hurik
 */
public class ReadMangaToday implements Site {

    private final String name = "ReadManga Today";
    private final String url = "http://www.readmanga.today";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/manga-list/");

        Elements letters = doc.select("div[class=manga-letters]").first().select("a");

        for (Element letter : letters) {
            if (letter != letters.first()) {
                doc = JsoupHelper.getHTMLPage(letter.attr("href"));
            }

            Elements rows = doc.select("span[class=manga-item]");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("ul[class=chp_lst]").first().select("li");

        for (Element row : rows) {
            if (row.select("a").first() == null) {
                continue;
            }

            chapters.add(new Chapter(row.select("a").first().attr("href"), row.select("span[class=val]").first().text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements pages = doc.select("ul[class=list-switcher-2]").first().select("select[class=form-control input-sm jump-menu]").first().select("option");

        for (Element page : pages) {
            if (page != pages.first()) {
                referrer = page.attr("value");
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("div.page_chapter").first().select("img").attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<String> getLanguage() {
        return language;
    }

    @Override
    public Boolean hasWatermarks() {
        return watermarks;
    }

}
