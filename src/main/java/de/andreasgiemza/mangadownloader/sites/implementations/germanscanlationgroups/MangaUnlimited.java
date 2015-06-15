package de.andreasgiemza.mangadownloader.sites.implementations.germanscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class MangaUnlimited extends FoOlSlide implements Site {

	public MangaUnlimited() {
		super("MangaUnlimited", "http://mangaunlimited.com/", Arrays
				.asList("German"), false, "http://reader.mangaunlimited.com",
				"/directory/");
	}
}
