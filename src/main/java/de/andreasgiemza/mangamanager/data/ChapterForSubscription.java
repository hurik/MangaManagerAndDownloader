package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.data.Chapter;
import java.util.Calendar;
import java.util.Date;
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
    private final Date addedDate = Calendar.getInstance().getTime();
    private Boolean read = UNREAD;
    private Date readDate = null;

    ChapterForSubscription(Chapter chapter, boolean read) {
        link = chapter.getLink();
        title = chapter.getTitle();
        this.read = read;

        if (read == READ) {
            readDate = Calendar.getInstance().getTime();
        }
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        if (read == READ) {
            if (this.read == UNREAD) {
                this.read = READ;
                readDate = Calendar.getInstance().getTime();
            }
        } else {
            this.read = UNREAD;
            readDate = null;
        }
    }

    public Date getReadDate() {
        return readDate;
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
