package de.andreasgiemza.mangadownloader.sites.implementations.english;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaPandaAndReader;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaReader extends MangaPandaAndReader implements Site {

	public MangaReader() {
		super("Manga Reader", "http://www.mangareader.net", Arrays
				.asList("English"), true);
	}
}
