package de.andreasgiemza.mangadownloader.sites.implementations.españolenglishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class CaféConIRST extends FoOlSlide implements Site, Serializable {

    public CaféConIRST() {
        super("Café con IRST", "http://cafeconirst.com/", Arrays.asList(
                "Español", "English"), false, "http://reader.cafeconirst.com/",
                "/directory/");
    }
}
