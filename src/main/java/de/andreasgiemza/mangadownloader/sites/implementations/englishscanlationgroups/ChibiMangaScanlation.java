package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChibiMangaScanlation extends FoOlSlide implements Site, Serializable {

    public ChibiMangaScanlation() {
        super("Chibi Manga Scanlation", "http://www.chibimanga.info", Arrays
                .asList("English"), false, "http://www.cmreader.info",
                "/directory/");
    }
}
