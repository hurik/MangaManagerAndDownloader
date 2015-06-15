package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class RoseliaScanlations extends FoOlSlide implements Site {

	public RoseliaScanlations() {
		super("Roselia Scanlations", "http://www.roseliascans.com", Arrays
				.asList("English"), false, "http://reader.roseliascans.com",
				"/directory/");
	}
}
