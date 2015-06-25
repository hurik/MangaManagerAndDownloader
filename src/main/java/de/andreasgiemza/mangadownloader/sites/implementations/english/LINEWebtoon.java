package de.andreasgiemza.mangadownloader.sites.implementations.english;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
public class LINEWebtoon implements Site {

	private final String name = "LINE Webtoon";
	private final String url = "http://www.webtoons.com";
	private final List<String> language = Arrays.asList("English");
	private final Boolean watermarks = false;

	private final String urlMobile = "http://m.webtoons.com";

	@Override
	public List<Manga> getMangaList() throws Exception {
		Set<Manga> mangas = new HashSet<>();

		Document doc = JsoupHelper.getHTMLPage(url + "/dailySchedule");

		Elements rows = doc.select("li");

		for (Element row : rows) {
			Element link = row.select("a").first();
			Element mangaName = row.select("p[class=subj]").first();

			if (mangaName != null) {
				mangas.add(new Manga(link.attr("href"), mangaName.text()));
			}
		}

		return new LinkedList<>(mangas);
	}

	@Override
	public List<Chapter> getChapterList(Manga manga) throws Exception {
		List<Chapter> chapters = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPageMobile(manga.getLink().replace(
				url, urlMobile));

		Elements pages = doc.select("li[id^=episode]");

		for (Element page : pages) {
			chapters.add(new Chapter(page.select("a").first().attr("href")
					.replace(urlMobile, url), page
					.select("span[class=ellipsis]").first().text()));
		}

		return chapters;
	}

	@Override
	public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
		List<Image> imageLinks = new LinkedList<>();

		String referrer = chapter.getLink();
		Document doc = JsoupHelper.getHTMLPage(referrer);

		// Get pages linkes
		Elements images = doc.select("img[class=_images]");

		for (Element image : images) {
			String link = image.attr("data-url");
			String extension = link.substring(link.length() - 12,
					link.length() - 9);

			imageLinks.add(new Image(link, referrer, extension));
		}

		return imageLinks;
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
