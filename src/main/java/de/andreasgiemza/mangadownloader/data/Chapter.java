package de.andreasgiemza.mangadownloader.data;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Chapter {

    private final String link;
    private final String title;
    private boolean alreadyDownloaded = false;
    private boolean download = false;

    public Chapter(Manga manga, String link, String title) {
        this.link = link;
        this.title = cleanTitle(manga, title);
    }

    private String cleanTitle(Manga manga, String title) {
        // Remove not needed information
        title = title.replace("Chapters", "");
        title = title.replace(manga.getTitle(), "");

        // Use abbreviations of volume and chapter
        title = title.replace("Volume", "Vol.");
        title = title.replace("Chapter", "Ch.");

        title = title.trim();
        if (title.startsWith("-")) {
            title = title.substring(1);
        }

        return title.trim();
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isAlreadyDownloaded() {
        return alreadyDownloaded;
    }

    public void setAlreadyDownloaded(boolean alreadyDownloaded) {
        this.alreadyDownloaded = alreadyDownloaded;
    }

    @Override
    public String toString() {
        return title + " (" + link + ")";
    }
}
