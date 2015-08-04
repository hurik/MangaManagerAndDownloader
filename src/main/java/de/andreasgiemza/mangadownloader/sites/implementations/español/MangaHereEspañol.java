package de.andreasgiemza.mangadownloader.sites.implementations.español;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaHere;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHereEspañol extends MangaHere implements Site {

    public MangaHereEspañol() {
        super("Manga Here (Español)", "http://es.mangahere.co", Arrays
                .asList("Español"), false);
    }
}
