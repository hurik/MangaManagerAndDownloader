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

	public Chapter(String link, String title) {
		this.link = link;
		this.title = title;
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
