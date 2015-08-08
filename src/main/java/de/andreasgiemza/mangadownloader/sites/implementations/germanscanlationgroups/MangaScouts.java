package de.andreasgiemza.mangadownloader.sites.implementations.germanscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class MangaScouts extends FoOlSlide implements Site {

    public MangaScouts() {
        super("Manga Scouts", "http://mangascouts.org",
                Arrays.asList("German"), false,
                "http://onlinereader.mangascouts.org", "/directory/");
    }
}
