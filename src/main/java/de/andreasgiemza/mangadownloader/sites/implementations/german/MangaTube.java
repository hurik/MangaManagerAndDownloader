package de.andreasgiemza.mangadownloader.sites.implementations.german;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class MangaTube extends FoOlSlide implements Site {

    public MangaTube() {
        super("Manga-Tube", "http://www.manga-tube.org/", Arrays
                .asList("German"), false, "http://www.manga-tube.org/reader",
                "/latest/");
    }
}
