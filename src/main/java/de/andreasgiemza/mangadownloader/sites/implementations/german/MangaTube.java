package de.andreasgiemza.mangadownloader.sites.implementations.german;

import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class MangaTube extends FoOlSlide implements Site {

    public MangaTube() {
        super(
                "Manga-Tube",
                "http://www.manga-tube.org/",
                Arrays.asList("German"),
                false,
                "http://www.manga-tube.org/reader/",
                "latest/");
    }

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>(super.getMangaList());

        char[] alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        for (char bet : alphabet) {
            Document doc = JsoupHelper.getHTMLPage(baseUrl + "list/" + bet);

            Elements rows = doc.select("div[class=group]");

            for (Element row : rows) {
                Element link = row.select("div[class^=title]").first()
                        .select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.text()));
            }
        }

        return mangas;
    }
}
