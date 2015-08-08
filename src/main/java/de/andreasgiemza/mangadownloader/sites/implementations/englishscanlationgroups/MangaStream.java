package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaStream implements Site {

    private final String name = "MangaStream";
    private final String url = "http://mangastream.com";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        Set<Manga> mangas = new HashSet<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/manga");

        Elements rows = doc.select("table[class=table table-striped]").first()
                .select("tr");

        for (Element row : rows) {
            if (row == rows.first()) {
                continue;
            }

            mangas.add(new Manga(row.select("a").first().attr("href"), row
                    .select("a").first().text()));
        }

        return new LinkedList<>(mangas);
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPageMobile(manga.getLink());

        Elements pages = doc.select("table[class=table table-striped]").first()
                .select("tr");

        for (Element page : pages) {
            if (page == pages.first()) {
                continue;
            }

            chapters.add(new Chapter(page.select("a").first().attr("href"),
                    page.select("a").first().text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> imageLinks = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        int max = Integer.parseInt(doc.select("div[class=btn-group]").last()
                .select("li").last().text().replace("Last Page (", "")
                .replace(")", ""));

        for (int i = 1; i <= max; i++) {
            if (i != 1) {
                referrer = chapter.getLink().substring(0,
                        chapter.getLink().length() - 1)
                        + i;
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=manga-page]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            imageLinks.add(new Image(link, referrer, extension));
        }

        return imageLinks;
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
