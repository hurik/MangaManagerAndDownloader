package de.andreasgiemza.mangadownloader.data;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Image {

	private final String link;
	private final String linkFragment;
	private final String referrer;
	private final String extension;

	public Image(String link, String referrer, String extension) {
		this.link = link;
		this.referrer = referrer;
		this.extension = extension;
		this.linkFragment = null;
	}

	public Image(String link, String referrer, String extension,
			String linkFragment) {
		this.link = link;
		this.referrer = referrer;
		this.extension = extension;
		this.linkFragment = linkFragment;
	}

	public String getLink() {
		return link;
	}

	public String getLinkFragment() {
		return linkFragment;
	}

	public String getReferrer() {
		return referrer;
	}

	public String getExtension() {
		return extension;
	}
}
