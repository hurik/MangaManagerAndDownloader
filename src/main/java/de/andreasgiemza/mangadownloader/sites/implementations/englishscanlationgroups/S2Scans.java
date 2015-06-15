package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class S2Scans extends FoOlSlide implements Site {

	public S2Scans() {
		super("S2Scans", "http://s2smanga.com", Arrays.asList("English"),
				false, "http://reader.s2smanga.com", "/directory/");
	}
}
