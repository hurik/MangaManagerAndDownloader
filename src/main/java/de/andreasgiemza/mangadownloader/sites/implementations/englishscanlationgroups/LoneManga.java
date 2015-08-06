package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.ImageType;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.HTMLUnitHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.Serializable;
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
public class LoneManga implements Site, Serializable {

    private final String name = "LoneManga";
    private final String url = "http://lonemanga.com/";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = HTMLUnitHelper.getHTMLPage("http://lonemanga.com/directory/");

        Elements rows = doc.select("td[class=series]");

        for (Element row : rows) {
            Element data = row.select("a").first();

            if (data != null) {
                String link = data.attr("href");

                if (link.startsWith("http://lonemanga.com/manga")) {
                    mangas.add(new Manga(link, data.text()));
                }
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = HTMLUnitHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("td[class=series]");

        for (Element row : rows) {
            Element data = row.select("a").first();

            if (data != null) {
                String link = data.attr("href");

                if (link.startsWith("http://lonemanga.com/manga")) {
                    chapters.add(new Chapter(link, data.text()));
                }
            }
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        Document doc = HTMLUnitHelper.getHTMLPage(chapter.getLink());

        Integer pages = Integer.parseInt(doc.select("select[class=viewerPage]").first().select("option").last().html());

        for (int i = 0; i <= pages; i++) {
            if (i != 0) {
                doc = HTMLUnitHelper.getHTMLPage(chapter.getLink() + i + "/");
            }

            Element row = doc.select("tr[id=imageWrapper]").first();

            Element img = row.select("img").first();
            String link = img.attr("src");

            if (link.startsWith("//")) {
                link = link.replace("//", "http://");
            }
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, chapter.getLink(), extension, ImageType.HTMLUNIT));
        }

        // Only works sometime ... but much faster ...
//        ArrayList<NameValuePair> post = new ArrayList<>();
//        post.add(new NameValuePair("showAll", "true"));
//        post.add(new NameValuePair("design", "Show in Long Strip Mode"));
//
//        Document doc = HTMLUnitHelper.getHTMLPageWithPost(chapter.getLink(), post);
//
//        Elements rows = doc.select("tr[class=imageWrapper]");
//
//        for (Element row : rows) {
//            Element img = row.select("img").first();
//            String link = img.attr("src");
//
//            if (link.startsWith("//")) {
//                link = link.replace("//", "http://");
//            }
//            String extension = link.substring(link.length() - 3, link.length());
//
//            images.add(new Image(link, chapter.getLink(), extension, ImageType.HTMLUNIT));
//        }
        if (images.isEmpty()) {
            throw new Exception("No images found ...");
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
