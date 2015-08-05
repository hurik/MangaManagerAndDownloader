package de.andreasgiemza.mangadownloader.sites.implementations.english;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaEden;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaEdenEnglish extends MangaEden implements Site {

    public MangaEdenEnglish() {
        super(
                "Manga Eden",
                "http://www.mangaeden.com/en/",
                Arrays.asList("English"),
                false,
                "en-directory/");
    }
}
