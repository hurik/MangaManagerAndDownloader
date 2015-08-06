package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SenseScans extends FoOlSlide implements Site, Serializable {

    public SenseScans() {
        super("Sense-Scans", "http://sensescans.com", Arrays.asList("English"),
                false, "http://reader.sensescans.com", "/reader/list/");
    }
}
