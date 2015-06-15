package de.andreasgiemza.mangadownloader.sites.implementations.españolenglishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class CaféConLenin extends FoOlSlide implements Site {

	public CaféConLenin() {
		super("Café con Lenin", "http://cafeconlenin.com", Arrays.asList(
				"Español", "English"), false, "http://reader.cafeconlenin.com",
				"/directory/");
	}
}
