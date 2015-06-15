package de.andreasgiemza.mangadownloader.sites.implementations.germanscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class ThrillerBarkCafé extends FoOlSlide implements Site {

	public ThrillerBarkCafé() {
		super("Thriller Bark Café", "http://www.thrillerbarkcafe.de/", Arrays
				.asList("German"), false, "http://reader.thrillerbarkcafe.de/",
				"/directory/");
	}
}
