package de.andreasgiemza.mangadownloader.sites.implementations.international;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.BatotoJsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.Arrays;
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

    private final String name = "Batoto";
    private final String url = "https://bato.to";
    private final List<String> language = Arrays.asList("International");
    private final Boolean watermarks = false;

    private final int loadCount = 1000;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = BatotoJsoupHelper.getHTMLPage(url
                + "/comic/_/comics/?per_page=" + loadCount + "&st=0");

        int max = Integer.parseInt(doc.select("li[class=last]").first()
                .select("a").first().attr("href").split("st=")[1]);

        for (int i = 0; i <= max; i += loadCount) {
            if (i != 0) {
                doc = BatotoJsoupHelper
                        .getHTMLPage(url + "/comic/_/comics/?per_page="
                                + loadCount + "&st=" + i);
            }

            Elements rows = doc
                    .select("table[class=ipb_table topic_list hover_rows]")
                    .first().select("tr");

            for (Element row : rows) {
                Elements cols = row.select("td");

                if (cols.size() != 7) {
                    continue;
                }

                mangas.add(new Manga(cols.get(1).select("a").first()
                        .attr("href"), cols.get(1).text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = BatotoJsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("table[class=ipb_table chapters_list]")
                .first().select("tr");

        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.size() != 5) {
                continue;
            }

            Element link = cols.get(0).select("a").first();

            String title = cols.get(0).text() + " ["
                    + cols.get(1).select("div").first().attr("title") + "]"
                    + " [" + cols.get(2).text() + "]";

            chapters.add(new Chapter(manga, link.attr("href"), title));

        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();
        String code = getChapterCode(chapter.getLink());
        String referrer = "http://bato.to/areader?id=" + code + "&p=1&supress_webtoon=t";
        
        Document doc = BatotoJsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        Elements pages = doc.select("div[class=moderation_bar rounded clear]")
                .first().select("ul").first().select("li").get(3)
                .select("option");

        for (int i = 0; i < pages.size(); i++) {
            if (i != 0) {
                // referrer = pages.get(i).attr("value") + "&supress_webtoon=t";
                referrer="http://bato.to/areader?id=" + code + "&p=" + String.valueOf(i + 1) + "&supress_webtoon=t";
                doc = BatotoJsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=comic_page]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }

    // Batoto Specific Chapter code
    private String getChapterCode(String url)
    {
        return url.substring(url.lastIndexOf("#") + 1);
      
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
