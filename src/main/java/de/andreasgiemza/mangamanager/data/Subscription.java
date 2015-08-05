package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.sites.Site;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Subscription {

    private final Site site;
    private final Manga manga;
    private final String filter;

    public Subscription(Site site, Manga manga, String filter) {
        this.site = site;
        this.manga = manga;
        this.filter = filter;
    }

    public Site getSite() {
        return site;
    }

    public Manga getManga() {
        return manga;
    }

    public String getFilter() {
        return filter;
    }

}
