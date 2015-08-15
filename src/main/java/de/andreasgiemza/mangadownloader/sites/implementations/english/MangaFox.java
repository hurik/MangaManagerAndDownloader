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
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaFox implements Site {

    private final String name = "Manga Fox";
    private final String url = "http://mangafox.me";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = true;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/manga/");

        Elements rows = doc.select("div[class=left]").first().select("li");

        for (Element row : rows) {
            Element link = row.select("a[class^=series_preview]").first();

            if (link == null) {
                continue;
            }

            mangas.add(new Manga(link.attr("href"), link.text()));
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("div[id=chapters]").first().select("li");

        for (Element row : rows) {
            chapters.add(new Chapter(manga, 
                    row.select("a[class=tips]").first().attr("href"),
                    row.select("a[class=tips]").first().text()
                    + (row.select("span[class=title nowrap]").first() == null ? ""
                            : " - "
                            + row.select(
                                    "span[class=title nowrap]")
                            .first().text())));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink().endsWith("1.html") ? chapter
                .getLink() : chapter.getLink() + "1.html";
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[onchange=change_page(this)]").first()
                .select("option");

        int pages = nav.size() - 1;

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink().replace("1.html", "") + i
                        + ".html";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=image]").first().attr("src");
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
