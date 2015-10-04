package de.andreasgiemza.mangadownloader.sites.implementations.russian;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.StringReader;
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
public class ReadManga implements Site {

    private final String name = "ReadManga";
    private final String url = "http://readmanga.me";
    private final List<String> language = Arrays.asList("Russian");
    private final Boolean watermarks = true;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/list");

        for (int i = 0; i <= 10000; i = i + 70) {
            if (i != 0) {
                doc = JsoupHelper.getHTMLPage(url + "/list?offset=" + i);
            }

            Elements rows = doc.select("div[class=leftContent]").first().select("div[class=desc]").select("h3");

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

        Elements rows = doc.select("td[class=\" \"]");
        Elements rows2 = doc.select("td[class=extra-chapter]");
        for (Element row : rows) {
            Element link = row.select("a").first();
            chapters.add(new Chapter(manga, link.attr("abs:href"), link.text()));
        }
        for (Element row : rows2) {
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

        try (Scanner scanner = new Scanner(doc.html())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("var pictures = [")) {
                    line = line.split("var pictures = ")[1];
                    line = line.split(";")[0];

                    JsonReader jsonReader = new JsonReader(new StringReader(line));
                    jsonReader.setLenient(true);
                    Wrapper[] dataArray = new Gson().fromJson(jsonReader, Wrapper[].class);

                    for (Wrapper data : dataArray) {
                        String link = data.url;

                        String extension = link.substring(
                                link.length() - 3, link.length());

                        images.add(new Image(link,
                                referrer, extension));
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
    public List<String> getLanguage() {
        return language;
    }

    @Override
    public Boolean hasWatermarks() {
        return watermarks;
    }

    class Wrapper {

        public String url;
    }
}
