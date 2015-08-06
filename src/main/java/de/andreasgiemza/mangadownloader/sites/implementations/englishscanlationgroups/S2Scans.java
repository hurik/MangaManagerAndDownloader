package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class S2Scans extends FoOlSlide implements Site, Serializable {

    public S2Scans() {
        super("S2Scans", "http://s2smanga.com", Arrays.asList("English"),
                false, "http://reader.s2smanga.com", "/directory/");
    }
}
