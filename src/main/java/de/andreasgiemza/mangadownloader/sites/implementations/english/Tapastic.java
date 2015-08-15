package de.andreasgiemza.mangadownloader.sites.implementations.english;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.StringReader;
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
public class Tapastic implements Site {

    private final String name = "Tapastic";
    private final String url = "http://tapastic.com";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/browse?browse=ALL");

        int max = Integer.parseInt(doc.select("div[class^=g-pagination-wrap]")
                .first()
                .select("a[class=paging-btn page-num g-act force mln size-up]")
                .first().text());

        for (int i = 1; i <= max; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(url + "/browse?pageNumber=" + i
                        + "&browse=ALL");
            }

            Elements rows = doc.select("ul[class=page-list-wrap]").first()
                    .select("li");

            for (Element row : rows) {
                mangas.add(new Manga(row.select("a[class=title]").first()
                        .attr("href"), row.select("a[class=title]").first()
                        .text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + manga.getLink());

        String line = null;

        try (Scanner scanner = new Scanner(doc.toString())) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.contains("episodeList")) {
                    break;
                }
            }
        }

        if (line == null) {
            return chapters;
        }

        line = line.replace("        episodeList : ", "").replace(";", "")
                .trim();

        JsonReader jsonReader = new JsonReader(new StringReader(line));
        jsonReader.setLenient(true);

        Wrapper[] dataArray = new Gson().fromJson(jsonReader, Wrapper[].class);

        for (int i = 0; i < dataArray.length; i++) {
            chapters.add(new Chapter(manga, dataArray[i].id, "(" + i + ") "
                    + dataArray[i].title));
        }

        Collections.reverse(chapters);

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> imageLinks = new LinkedList<>();

        String referrer = url + "/episode/" + chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        Elements images = doc.select("article[class^=ep-contents]").first()
                .select("img[class=art-image]");

        for (Element image : images) {
            String link = image.attr("src");
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

    class Wrapper {

        public String id;
        public String title;
    }
}
