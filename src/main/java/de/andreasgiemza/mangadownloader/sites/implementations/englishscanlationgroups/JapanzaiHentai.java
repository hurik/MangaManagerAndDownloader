package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class JapanzaiHentai extends FoOlSlide implements Site {

	public JapanzaiHentai() {
		super("Japanzai (Hentai)", "http://japanzai.com", Arrays
				.asList("English"), false, "http://h.japanzai.com",
				"/directory/");
	}
}
