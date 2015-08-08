package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.data.Chapter;
import java.util.Objects;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterForSubscription {

    public final static boolean READ = true;
    public final static boolean UNREAD = false;

    private final String link;
    private final String title;
    private Boolean read = UNREAD;

    ChapterForSubscription(Chapter chapter, boolean read) {
        link = chapter.getLink();
        title = chapter.getTitle();
        this.read = read;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean readed) {
        this.read = readed;
    }

    @Override
    public String toString() {
        return title + " (" + (read ? "READ" : "UNREAD") + ") [" + link + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.link);
        hash = 59 * hash + Objects.hashCode(this.title);
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
        final ChapterForSubscription other = (ChapterForSubscription) obj;
        if (!Objects.equals(this.link, other.link)) {
            return false;
        }
        return Objects.equals(this.title, other.title);
    }

}
