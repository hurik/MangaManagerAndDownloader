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
public class Batoto implements Site {
    
    private final String baseUrl = "https://bato.to";
    private final int loadCount = 1000;
    
    @Override
    public List<Manga> getMangaList() throws IOException {
        List<Manga> mangas = new LinkedList<>();
        
        Document doc = JsoupHelper.getHTMLPage(baseUrl + "/comic/_/comics/?per_page=" + loadCount + "&st=0");
        
        int max = Integer.parseInt(doc.select("li[class=last]").first().select("a").first().attr("href").split("st=")[1]);
        
        for (int i = 0; i <= max; i += loadCount) {
            if (i != 0) {
                doc = JsoupHelper.getHTMLPage(baseUrl + "/comic/_/comics/?per_page=" + loadCount + "&st=" + i);
            }
            
            Elements rows = doc.select("table[class=ipb_table topic_list hover_rows]").first().select("tr");
            
            for (Element row : rows) {
                Elements cols = row.select("td");
                
                if (cols.size() != 7) {
                    continue;
                }
                
                mangas.add(new Manga(cols.get(1).select("a").first().attr("href"), cols.get(1).text()));
            }
        }
        
        return mangas;
    }
    
    @Override
    public List<Chapter> getChapterList(Manga manga) throws IOException {
        List<Chapter> chapters = new LinkedList<>();
        
        Document doc = JsoupHelper.getHTMLPage(manga.getLink());
        
        Elements rows = doc.select("table[class=ipb_table chapters_list]").first()
                .select("tr");
        
        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.size() != 5) {
                continue;
            }
            
            Element link = cols.get(0).select("a").first();
            
            String title = cols.get(0).text()
                    + " [" + cols.get(1).select("div").first().attr("title") + "]"
                    + " [" + cols.get(2).text() + "]";
            
            chapters.add(new Chapter(link.attr("href"), title));
        }
        
        return chapters;
    }
    
    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws IOException {
        List<Image> images = new LinkedList<>();
        
        String referrer = chapter.getLink() + "?supress_webtoon=t";
        Document doc = JsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        Elements pages = doc.select("div[class=moderation_bar rounded clear]").first()
                .select("ul").first()
                .select("li").get(3)
                .select("option");
        
        for (int i = 0; i < pages.size(); i++) {
            if (i != 0) {
                referrer = pages.get(i).attr("value") + "?supress_webtoon=t";
                doc = JsoupHelper.getHTMLPage(referrer);
            }
            
            String link = doc.select("img[id=comic_page]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());
            
            images.add(new Image(link, referrer, extension));
        }
        
        return images;
    }
}
