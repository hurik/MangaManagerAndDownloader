package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SenseScans extends FoOlSlide implements Site {

	public SenseScans() {
		super("Sense-Scans", "http://sensescans.com", Arrays.asList("English"),
				false, "http://reader.sensescans.com", "/reader/list/");
	}
}
