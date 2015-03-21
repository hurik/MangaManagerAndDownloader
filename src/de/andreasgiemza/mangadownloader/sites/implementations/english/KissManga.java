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

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.Arrays;
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
public class KissManga implements Site {

    private final String name = "KissManga";
    private final String url = "http://kissmanga.com";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/MangaList");

        int numberOfPages = Integer.parseInt(doc.select("div[class=pagination pagination-left]").first()
                .select("li").last().select("a").attr("page"));

        for (int page = 1; page <= numberOfPages; page++) {
            if (page != 1) {
                doc = JsoupHelper.getHTMLPage(url + "/MangaList?page=" + page);
            }

            Elements rows = doc.select("table[class=listing]").first().select("tr");

            for (Element row : rows) {
                Element td = row.select("td").first();

                if (td == null) {
                    continue;
                }

                Element link = td.select("a").first();

                mangas.add(new Manga(link.attr("href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + manga.getLink());

        Elements rows = doc.select("table[class=listing]").first().select("tr");

        for (Element row : rows) {
            Element td = row.select("td").first();

            if (td == null) {
                continue;
            }

            Element link = td.select("a").first();

            chapters.add(new Chapter(link.attr("href"), link.text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = url + chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements scripts = doc.select("script");

        for (Element script : scripts) {
            String scriptText = script.html();

            if (scriptText.contains("var lstImages = new Array();")) {
                try (Scanner scanner = new Scanner(scriptText)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();

                        if (line.contains("lstImages.push")) {
                            String link = line.split("lstImages\\.push\\(\"")[1].split("\\?imgmax")[0];
                            String extension = link.substring(link.length() - 3, link.length());

                            images.add(new Image(link + "?imgmax=9999", referrer, extension));
                        }
                    }
                }

                break;
            }
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
