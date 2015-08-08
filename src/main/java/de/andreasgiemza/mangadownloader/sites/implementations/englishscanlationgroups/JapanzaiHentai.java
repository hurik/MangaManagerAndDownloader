package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class JapanzaiHentai extends FoOlSlide implements Site {

    public JapanzaiHentai() {
        super("Japanzai (Hentai)", "http://japanzai.com", Arrays
                .asList("English"), false, "http://h.japanzai.com",
                "/directory/");
    }
}
