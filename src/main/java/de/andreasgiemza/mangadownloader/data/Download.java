package de.andreasgiemza.mangadownloader.data;

import java.util.Objects;

import de.andreasgiemza.mangadownloader.sites.Site;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Download {

	private final int id;
	private final Site site;
	private final Manga manga;
	private final Chapter chapter;
	private State state = State.PENDING;
	private String message = "Pending";

	public Download(int id, Site site, Manga manga, Chapter chapter) {
		this.id = id;
		this.site = site;
		this.manga = manga;
		this.chapter = chapter;
	}

	public int getId() {
		return id;
	}

	public Site getSite() {
		return site;
	}

	public Manga getManga() {
		return manga;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + Objects.hashCode(this.site);
		hash = 79 * hash + Objects.hashCode(this.manga);
		hash = 79 * hash + Objects.hashCode(this.chapter);
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
		final Download other = (Download) obj;
		if (!Objects.equals(this.site, other.site)) {
			return false;
		}
		if (!Objects.equals(this.manga, other.manga)) {
			return false;
		}
		return Objects.equals(this.chapter, other.chapter);
	}

	public enum State {

		PENDING, RUNNING, DONE, CANCELLED, ERROR;
	}
}
