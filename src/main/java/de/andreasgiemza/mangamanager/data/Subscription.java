package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Subscription implements Serializable {

    private final String siteClass;
    private final Manga manga;
    private final String filter;
    private final List<ChapterForSubscription> chapters = new LinkedList<>();

    public Subscription(String siteClass, Manga manga, String filter) {
        this.siteClass = siteClass;
        this.manga = manga;
        this.filter = filter;
    }

    public void newChapters(List<Chapter> chapterList, boolean selected) {
        Collections.reverse(chapterList);
        Collections.reverse(chapters);

        for (Chapter chapter : chapterList) {
            if (filter.isEmpty() || chapter.getTitle().contains(filter)) {
                ChapterForSubscription cfs = new ChapterForSubscription(chapter, selected);

                if (!chapters.contains(cfs)) {
                    chapters.add(cfs);
                }
            }
        }

        Collections.reverse(chapters);
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

    public List<ChapterForSubscription> getChapters() {
        return chapters;
    }

    public int getUnreadChapters() {
        int c = 0;

        for (ChapterForSubscription chapter : chapters) {
            if (chapter.getRead() == ChapterForSubscription.UNREAD) {
                c++;
            }
        }

        return c;
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
