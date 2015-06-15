package de.andreasgiemza.mangadownloader.sites.implementations.italian;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaEden;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaEdenItalian extends MangaEden implements Site {

	public MangaEdenItalian() {
		super("Manga Eden (Italian)", "http://www.mangaeden.com", Arrays
				.asList("Italian"), true, "/it-directory/");
	}
}
