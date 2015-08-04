package de.andreasgiemza.mangadownloader.data;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Manga implements Serializable, Comparable<Manga> {

    private static final long serialVersionUID = 1L;
    private final String link;
    private final String title;

    public Manga(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + " (" + link + ")";
    }

    @Override
    public int compareTo(Manga manga) {
        return title.compareToIgnoreCase(manga.getTitle());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.link);
        hash = 79 * hash + Objects.hashCode(this.title);
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
        final Manga other = (Manga) obj;
        if (!Objects.equals(this.link, other.link)) {
            return false;
        }
        return Objects.equals(this.title, other.title);
    }
}
