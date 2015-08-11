package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class OneTimeScans extends FoOlSlide implements Site {

    public OneTimeScans() {
        super("One Time Scans",
                "http://onetimescans.com/",
                Arrays.asList("English"),
                false,
                "http://onetimescans.com/foolslide/",
                "directory/");
    }
}
