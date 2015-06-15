package de.andreasgiemza.mangadownloader.sites.implementations.english;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaPandaAndReader;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaPanda extends MangaPandaAndReader implements Site {

	public MangaPanda() {
		super("Manga Panda", "http://www.mangapanda.com", Arrays
				.asList("English"), true);
	}
}
