package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChibiMangaScanlation extends FoOlSlide implements Site {

	public ChibiMangaScanlation() {
		super("Chibi Manga Scanlation", "http://www.chibimanga.info", Arrays
				.asList("English"), false, "http://www.cmreader.info",
				"/directory/");
	}
}
