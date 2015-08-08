package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class RedHawkScanlations extends FoOlSlide implements Site {

    public RedHawkScanlations() {
        super("Red Hawk Scanlations", "http://www.redhawkscans.com", Arrays
                .asList("English"), false, "http://manga.redhawkscans.com",
                "/reader/list/");
    }
}
