package de.andreasgiemza.mangadownloader.sites.implementations.english;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaEden;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaEdenEnglish extends MangaEden implements Site {

	public MangaEdenEnglish() {
		super("Manga Eden", "http://www.mangaeden.com", Arrays
				.asList("English"), false, "/en-directory/");
	}
}
