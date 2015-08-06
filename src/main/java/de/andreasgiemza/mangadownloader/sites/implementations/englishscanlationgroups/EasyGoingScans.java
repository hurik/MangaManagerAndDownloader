package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
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
public class EasyGoingScans implements Site, Serializable {

    private final String name = "Easy Going Scans";
    private final String url = "http://egscans.com";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    private final String baseUrl = "http://read.egscans.com/";

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl);

        Elements rows = doc.select("select[name=manga]").first()
                .select("option");

        for (Element row : rows) {
            if (row == rows.first()) {
                continue;
            }

            mangas.add(new Manga(baseUrl + row.attr("value"), row.text()));
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("select[name=chapter]").first()
                .select("option");

        for (Element row : rows) {
            chapters.add(new Chapter(manga.getLink() + "/" + row.attr("value"),
                    row.text()));
        }

        Collections.reverse(chapters);

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements jss = doc.select("script[type=text/javascript]");

        Element rightJs = null;

        for (Element js : jss) {
            if (js.toString().contains("img_url.push")) {
                rightJs = js;
                break;
            }
        }

        if (rightJs == null) {
            return images;
        }

        List<String> data = new LinkedList<>();

        try (Scanner scanner = new Scanner(rightJs.toString())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("img_url.push('")) {
                    data.add(line);
                }
            }
        }

        if (data.isEmpty()) {
            return images;
        }

        for (String line : data) {
            String link = baseUrl
                    + line.split("img_url\\.push\\(\\'")[1].split("\\' \\);")[0];
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
