package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class DemonicScanlations extends FoOlSlide implements Site, Serializable {

    public DemonicScanlations() {
        super("Demonic Scanlations", "http://www.demonicscans.com", Arrays
                .asList("English"), false,
                "http://www.demonicscans.com/FoOlSlide", "/directory/");
    }

}
