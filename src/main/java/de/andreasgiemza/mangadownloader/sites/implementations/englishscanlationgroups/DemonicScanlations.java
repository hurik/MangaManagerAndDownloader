package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class DemonicScanlations extends FoOlSlide implements Site {

	public DemonicScanlations() {
		super("Demonic Scanlations", "http://www.demonicscans.com", Arrays
				.asList("English"), false,
				"http://www.demonicscans.com/FoOlSlide", "/directory/");
	}

}
