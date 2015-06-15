package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SilentSky extends FoOlSlide implements Site {

	public SilentSky() {
		super("Silent Sky", "http://www.silentsky-scans.net", Arrays
				.asList("English"), false, "http://reader.silentsky-scans.net",
				"/directory/");
	}
}
