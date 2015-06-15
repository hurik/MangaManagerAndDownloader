package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class JapanzaiEcchi extends FoOlSlide implements Site {

	public JapanzaiEcchi() {
		super("Japanzai (Ecchi)", "http://japanzai.com", Arrays
				.asList("English"), false, "http://ecchi.japanzai.com",
				"/directory/");
	}
}
