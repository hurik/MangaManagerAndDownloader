package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Japanzai extends FoOlSlide implements Site {

    public Japanzai() {
        super("Japanzai", "http://japanzai.com", Arrays.asList("English"),
                false, "http://reader.japanzai.com", "/directory/");
    }
}
