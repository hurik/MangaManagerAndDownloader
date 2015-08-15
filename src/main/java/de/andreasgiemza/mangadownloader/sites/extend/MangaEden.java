package de.andreasgiemza.mangadownloader.sites.extend;

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
public class MangaEden implements Site {

    private final String name;
    private final String url;
    private final List<String> language;
    private final Boolean watermarks;

    private final String listUrl;

    public MangaEden(String name, String url, List<String> language,
            Boolean watermarks, String listUrl) {
        this.name = name;
        this.url = url;
        this.language = language;
        this.watermarks = watermarks;

        this.listUrl = listUrl;
    }

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + listUrl);

        Elements nav = doc.select("div[class=pagination pagination_bottom]")
                .first().select("a");

        int pages = Integer.parseInt(nav.get(nav.size() - 2).text());

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(url + listUrl + "?page=" + i);
            }

            Elements rows = doc.select("table[id=mangaList]").first()
                    .select("tbody").first().select("tr");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("table").first().select("tbody").first()
                .select("tr");

        for (Element row : rows) {
            Element link = row.select("a").first();
            chapters.add(new Chapter(manga, link.attr("abs:href"), link.text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("div[class^=pagination]").first().select("a");

        int pages = Integer.parseInt(nav.get(nav.size() - 2).text());

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink().substring(0,
                        chapter.getLink().length() - 2)
                        + i + "/";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=mainImg]").first().attr("src")
                    .replace("//", "http://");
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
