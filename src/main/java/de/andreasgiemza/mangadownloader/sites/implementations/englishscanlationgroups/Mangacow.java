package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

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
public class Mangacow implements Site {

    private final String name = "Mangacow";
    private final String url = "http://mngcow.co/";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url
                + "/manga-list/all/any/name-az/");

        Element nav = doc.select("ul[class=sct_content]").first();

        int pages = 1;

        if (nav != null) {
            String data = nav.select("li").last().select("a").attr("href");
            String[] dataArray = data.split("/");

            pages = Integer.parseInt(dataArray[dataArray.length - 1]);
        }

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(url
                        + "/manga-list/all/any/name-az/" + i + "/");
            }

            Elements rows = doc.select("div[class=wpm_pag mng_lst tbn]")
                    .first().select("ul[id=wpm_mng_lst]").select("li");
            
            for (Element row : rows) {
                Element link = row.select("div[class=det]").first().select("a")
                        .first();
                mangas.add(new Manga(link.attr("href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Element nav = doc.select("ul[class=pgg]").first();

        int pages = 1;

        if (nav != null) {
            String data = nav.select("li").last().select("a").attr("href");
            String[] dataArray = data.split("/");

            pages = Integer.parseInt(dataArray[dataArray.length - 1]);
        }

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(manga.getLink() + "chapter-list/"
                        + i + "/");
            }

            Elements rows = doc.select("ul[class^=lst]").first().select("li");

            for (Element row : rows) {
                Element link = row.select("a").first();

                chapters.add(new Chapter(manga, link.attr("href"), link
                        .select("b[class=val]").first().text()));
            }
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[class=cbo_wpm_pag]").first()
                .select("option");

        int pages = nav.size();

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink() + (i) + "/";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("div[class=prw]").first().select("img")
                    .attr("src");
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
