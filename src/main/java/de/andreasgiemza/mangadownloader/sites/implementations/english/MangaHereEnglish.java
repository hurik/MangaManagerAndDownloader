package de.andreasgiemza.mangadownloader.sites.implementations.english;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaHere;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHereEnglish extends MangaHere implements Site, Serializable {

    public MangaHereEnglish() {
        super("Manga Here", "http://www.mangahere.co",
                Arrays.asList("English"), true);
    }
}
