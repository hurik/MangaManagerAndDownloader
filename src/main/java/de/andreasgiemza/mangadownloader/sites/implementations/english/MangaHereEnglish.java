package de.andreasgiemza.mangadownloader.sites.implementations.english;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaHere;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHereEnglish extends MangaHere implements Site {

	public MangaHereEnglish() {
		super("Manga Here", "http://www.mangahere.co",
				Arrays.asList("English"), true);
	}
}
