package de.andreasgiemza.mangadownloader.sites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.andreasgiemza.mangadownloader.helpers.ClassesInPackageHelper;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class SiteHelper {

	private final static String implementationsPackage = "de.andreasgiemza.mangadownloader.sites.implementations";

	private SiteHelper() {
	}

	public static List<Site> getSites() {
		List<Site> sites = new LinkedList<>();

		try {
			ArrayList<Class<?>> clazzes = ClassesInPackageHelper
					.getClassesForPackageWithInterface(implementationsPackage,
							Site.class);

			for (Class<?> clazz : clazzes) {
				sites.add((Site) clazz.newInstance());
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException ex) {
		}

		Collections.sort(sites, new Comparator<Site>() {
			@Override
			public int compare(Site site1, Site site2) {
				return site1.getName().compareTo(site2.getName());
			}
		});

		return sites;
	}

	public static Site getInstance(String source) {
		try {
			ArrayList<Class<?>> clazzes = ClassesInPackageHelper
					.getClassesForPackageWithInterface(implementationsPackage,
							Site.class);

			for (Class<?> clazz : clazzes) {
				if (clazz.getSimpleName().equals(source)) {
					return (Site) clazz.newInstance();
				}
			}

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException ex) {
		}

		return null;
	}
}
