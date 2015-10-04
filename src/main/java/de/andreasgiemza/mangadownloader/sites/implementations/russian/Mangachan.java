package de.andreasgiemza.mangadownloader.sites.implementations.russian;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.ImageType;
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
 * @author yuuki
 */
public class Mangachan implements Site {

    private final String name = "Mangachan";
    private final String url = "http://mangachan.ru/";
    private final List language = Arrays.asList("Russian");
    private final Boolean watermarks = true;

    @Override
    public List getMangaList() throws Exception {
        List mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/catalog");

        Elements nav = doc.select("div[id=pagination]")
                .first().select("span").first().select("a");

        for (int i = 0; i <= 10000; i = i + 20) {
            if (i != 0) {
                doc = JsoupHelper.getHTMLPage(url + "/catalog" + "?offset=" + i);
            }

            Elements rows = doc.select("div[id=content]").first()
                    .select("div[class=content_row]").select("div[class=manga_row1]");

            for (Element row : rows) {
                Element link = row.select("a").get(1);
                mangas.add(new Manga(link.attr("abs:href"), link.text()));
            }
        }

        return mangas;

    }

    @Override
    public List getChapterList(Manga manga) throws Exception {
        List chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("div[class=manga]");

        for (Element row : rows) {
            Element link = row.select("a").first();
            chapters.add(new Chapter(manga, link.attr("abs:href"), link.text()));
        }

        return chapters;
    }

    @Override
    public List getChapterImageLinks(Chapter chapter) throws Exception {
        List images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        try (Scanner scanner = new Scanner(doc.html())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("\"fullimg\":")) {
                    line = line.split("\"fullimg\":\\[\"")[1];
                    line = line.split("\",\\]")[0];
                    line = line.split("\"\\]")[0]; // Only failsave if there is an end without the comma

                    String[] links = line.split("\",\"");

                    for (String link : links) {
                        link = link.replace("/im.", "/img.");
                        link = link.replace("/im2.", "/img2.");

                        String extension = link.substring(
                                link.length() - 3, link.length());

                        images.add(new Image(link,
                                referrer, extension, ImageType.HTMLUNIT));
                    }
                }
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
    public List getLanguage() {
        return language;
    }

    @Override
    public Boolean hasWatermarks() {
        return watermarks;
    }
}
