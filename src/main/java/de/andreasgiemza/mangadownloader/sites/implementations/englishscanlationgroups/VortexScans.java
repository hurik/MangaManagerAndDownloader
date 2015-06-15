package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class VortexScans extends FoOlSlide implements Site {

	public VortexScans() {
		super("Vortex-Scans", "http://vortex-scans.com", Arrays
				.asList("English"), false, "http://reader.vortex-scans.com",
				"/directory/");
	}
}
