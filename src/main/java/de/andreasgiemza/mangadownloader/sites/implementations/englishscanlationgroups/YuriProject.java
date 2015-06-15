package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class YuriProject extends FoOlSlide implements Site {

	public YuriProject() {
		super("Yuri Project", "http://yuriproject.net", Arrays
				.asList("English"), false, "http://reader.yuriproject.net",
				"/directory/");
	}
}
