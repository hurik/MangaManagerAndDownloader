package de.andreasgiemza.mangadownloader.sites.implementations.englishscanlationgroups;

import java.util.Arrays;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class KireiCake extends FoOlSlide implements Site {

	public KireiCake() {
		super("Kirei Cake", "http://kireicake.com", Arrays.asList("English"),
				false, "http://reader.kireicake.com", "/reader/list/");
	}
}
