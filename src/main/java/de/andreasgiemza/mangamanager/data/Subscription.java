package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Subscription implements Serializable {

    private final String siteClass;
    private final Manga manga;
    private final String filter;

    public Subscription(String siteClass, Manga manga, String filter) {
        this.siteClass = siteClass;
        this.manga = manga;
        this.filter = filter;
    }

    public Site getSite() {
        return SiteHelper.getInstance(siteClass);
    }

    public Manga getManga() {
        return manga;
    }

    public String getFilter() {
        return filter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.siteClass);
        hash = 37 * hash + Objects.hashCode(this.manga);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subscription other = (Subscription) obj;
        if (!Objects.equals(this.siteClass, other.siteClass)) {
            return false;
        }
        return Objects.equals(this.manga, other.manga);
    }
}
