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
package de.andreasgiemza.mangadownloader.sites.implementations.english;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Tapastic implements Site {

    private final String baseUrl = "http://tapastic.com";

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + "/browse/list");

        int max = Integer.parseInt(doc.select("div[class^=g-pagination-wrap]").first().select("a[class=page-num paging-btn g-act]").last().text());

        for (int i = 1; i <= max; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(baseUrl + "/browse/list?pageNumber=" + i);
            }

            Elements rows = doc.select("ul[class=page-list-wrap]").first().select("li");

            for (Element row : rows) {
                mangas.add(new Manga(row.select("a[class=title]").first().attr("href"), row.select("a[class=title]").first().text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + manga.getLink());

        Scanner scanner = new Scanner(doc.toString());

        String line = null;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.contains("episodeList")) {
                break;
            }
        }

        if (line == null) {
            return chapters;
        }

        line = line.split("episodeList : \\[")[1];
        line = line.substring(0, line.length() - 2);

        String[] jsons = line.replace("},{", "}},{{").split("\\},\\{");

        for (int i = 0; i < jsons.length; i++) {
            JsonObject jsonObject = new JsonParser().parse(jsons[i]).getAsJsonObject();
            chapters.add(new Chapter(jsonObject.get("id").toString(), "(" + i + ") " + jsonObject.get("title").toString()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> imageLinks = new LinkedList<>();

        String referrer = baseUrl + "/episode/" + chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        Elements images = doc.select("article[class^=ep-contents]").first().select("img[class=art-image]");

        for (Element image : images) {
            String link = image.attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            imageLinks.add(new Image(link, referrer, extension));
        }

        return imageLinks;
    }
}
