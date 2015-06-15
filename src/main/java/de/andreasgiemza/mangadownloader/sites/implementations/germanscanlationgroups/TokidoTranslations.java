package de.andreasgiemza.mangadownloader.sites.implementations.germanscanlationgroups;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class TokidoTranslations extends FoOlSlide implements Site {

	public TokidoTranslations() {
		super("Tokido Translations", "http://tokido-scans.blogspot.de/", Arrays
				.asList("German"), false, "http://tokido.bplaced.net/Reader/",
				"/directory/");
	}
}
