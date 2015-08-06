package de.andreasgiemza.mangadownloader.sites.implementations.germanscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class ThrillerBarkCafé extends FoOlSlide implements Site, Serializable {

    public ThrillerBarkCafé() {
        super("Thriller Bark Café", "http://www.thrillerbarkcafe.de/", Arrays
                .asList("German"), false, "http://reader.thrillerbarkcafe.de/",
                "/directory/");
    }
}
