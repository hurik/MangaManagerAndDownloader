package de.andreasgiemza.mangadownloader.sites;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import java.util.List;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public interface Site {

	public List<Manga> getMangaList() throws Exception;

	public List<Chapter> getChapterList(Manga manga) throws Exception;

	public List<Image> getChapterImageLinks(Chapter chapter) throws Exception;

	public String getName();

	public String getUrl();

	public List<String> getLanguage();

	public Boolean hasWatermarks();
}
