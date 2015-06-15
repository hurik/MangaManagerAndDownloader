package de.andreasgiemza.mangadownloader.sites.extend;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaPandaAndReader implements Site {

	private final String name;
	private final String url;
	private final List<String> language;
	private final Boolean watermarks;

	public MangaPandaAndReader(String name, String url, List<String> language,
			Boolean watermarks) {
		this.name = name;
		this.url = url;
		this.language = language;
		this.watermarks = watermarks;
	}

	@Override
	public List<Manga> getMangaList() throws Exception {
		List<Manga> mangas = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPage(url + "/alphabetical");

		Elements cols = doc.select("div[class=series_col]");

		for (Element col : cols) {
			Elements rows = col.select("li");

			for (Element row : rows) {
				Element link = row.select("a").first();
				mangas.add(new Manga(link.attr("href"), link.text()));
			}
		}

		return mangas;
	}

	@Override
	public List<Chapter> getChapterList(Manga manga) throws Exception {
		List<Chapter> chapters = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPage(url + manga.getLink());

		Elements rows = doc.select("div[id=chapterlist]").first().select("tr");

		for (Element row : rows) {
			if (row.select("a").first() == null) {
				continue;
			}

			chapters.add(new Chapter(row.select("a").first().attr("href"), row
					.select("td").first().text()));
		}

		return chapters;
	}

	@Override
	public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
		List<Image> images = new LinkedList<>();

		String referrer = url + chapter.getLink();
		Document doc = JsoupHelper.getHTMLPage(referrer);

		Elements nav = doc.select("select[id=pageMenu]").first()
				.select("option");

		int pages = nav.size();

		for (int i = 0; i < pages; i++) {
			if (i != 0) {
				referrer = url + nav.get(i).attr("value");
				doc = JsoupHelper.getHTMLPage(referrer);
			}

			String link = doc.select("img[id=img]").first().attr("src");
			String extension = link.substring(link.length() - 3, link.length());

			images.add(new Image(link, referrer, extension));
		}

		return images;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public List<String> getLanguage() {
		return language;
	}

	@Override
	public Boolean hasWatermarks() {
		return watermarks;
	}
}
