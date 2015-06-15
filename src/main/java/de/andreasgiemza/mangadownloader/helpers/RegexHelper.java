package de.andreasgiemza.mangadownloader.helpers;

import java.util.regex.Pattern;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class RegexHelper {

	private RegexHelper() {
	}

	public static String build(String string) {
		final String[] searchTextAray = string.split(" ");

		String regexExpression = "(?i)";

		for (String word : searchTextAray) {
			if (word.length() > 0) {
				regexExpression += "(?=.*" + Pattern.quote(word) + ")";
			}
		}

		return regexExpression;
	}
}
