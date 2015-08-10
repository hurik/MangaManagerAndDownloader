package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;
import static de.andreasgiemza.mangamanager.data.ChapterForSubscription.READ;
import static de.andreasgiemza.mangamanager.data.ChapterForSubscription.UNREAD;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Subscription {
    
    private final String siteClass;
    private final Manga manga;
    private final String filter;
    private Date lastUpdate;
    private final List<ChapterForSubscription> chapters = new LinkedList<>();
    
    public Subscription(String siteClass, Manga manga, String filter, List<Chapter> chapters, boolean selected) {
        this.siteClass = siteClass;
        this.manga = manga;
        this.filter = filter;
        
        getNewChapters(chapters, selected);
    }
    
    public final void getNewChapters(List<Chapter> chapterList, boolean selected) {
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
        
        lastUpdate = Calendar.getInstance().getTime();
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
    
    public Date getLastUpdate() {
        return lastUpdate;
    }
    
    public String getLastReadChapter() {
        ChapterForSubscription lastReadChapter = null;
        Date lastReadDate = new Date(0);
        
        for (ChapterForSubscription chapter : chapters) {
            if (chapter.getRead() == READ && chapter.getReadDate() != null && chapter.getReadDate().compareTo(lastReadDate) > 0) {
                lastReadChapter = chapter;
                lastReadDate = chapter.getReadDate();
            }
        }
        
        if (lastReadChapter == null) {
            for (ChapterForSubscription chapter : chapters) {
                if (chapter.getRead() == READ) {
                    lastReadChapter = chapter;
                    break;
                }
            }
        }
        
        if (lastReadChapter == null) {
            return null;
        } else {
            return lastReadChapter.getTitle();
        }
        
    }
    
    public void markAllAsRead() {
        Collections.reverse(chapters);
        
        for (ChapterForSubscription chapter : chapters) {
            chapter.setRead(READ);
        }
        
        Collections.reverse(chapters);
        
        lastUpdate = Calendar.getInstance().getTime();
    }
    
    public List<ChapterForSubscription> getChapters() {
        return chapters;
    }
    
    public int getUnreadChapters() {
        int c = 0;
        
        for (ChapterForSubscription chapter : chapters) {
            if (chapter.getRead() == UNREAD) {
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
    
    @Override
    public String toString() {
        return getManga().getTitle() + " (" + siteClass + ") [" + chapters.size() + "/" + getUnreadChapters() + "]";
    }
    
}
