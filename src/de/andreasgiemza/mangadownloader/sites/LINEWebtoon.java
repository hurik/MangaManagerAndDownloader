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
package de.andreasgiemza.mangadownloader.sites;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class LINEWebtoon implements Site {
    
    private final String baseUrl = "http://www.webtoons.com";
    private final String baseUrlMobile = "http://m.webtoons.com";
    
    @Override
    public List<Manga> getMangaList() throws IOException {
        List<Manga> mangas = new LinkedList<>();
        
        Document doc = JsoupHelper.getHTMLPage(baseUrl + "/dailySchedule");
        
        Elements rows = doc.select("li");
        
        for (Element row : rows) {
            Elements data = row.select("h3");
            
            if (data.size() > 0) {
                mangas.add(new Manga(
                        row.select("a").first().attr("href"),
                        row.select("h3").first().text()));
            }
        }
        
        return new LinkedList<>(new HashSet<>(mangas));
    }
    
    @Override
    public List<Chapter> getChapterList(Manga manga) throws IOException {
        List<Chapter> chapters = new LinkedList<>();
        
        Document doc = JsoupHelper.getHTMLPageMobile(baseUrlMobile + manga.getLink());
        
        Elements pages = doc.select("li[id^=episode]");
        
        for (Element page : pages) {
            chapters.add(new Chapter(baseUrl + page.select("a").first().attr("href"), page.select("span[class=ellipsis]").first().text()));
        }
        
        return chapters;
    }
    
    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws IOException {
        List<Image> imageLinks = new LinkedList<>();
        
        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        Elements images = doc.select("img[class=_images]");
        
        for (Element image : images) {
            String link = image.attr("data-url");
            String extension = link.substring(link.length() - 12, link.length() - 9);
            
            imageLinks.add(new Image(link, referrer, extension));
        }
        
        return imageLinks;
    }
    
}
