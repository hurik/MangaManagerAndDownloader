package de.andreasgiemza.mangadownloader.sites.extend;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class FoOlSlide implements Site {

	private final String name;
	private final String url;
	private final List<String> language;
	private final Boolean watermarks;

	private final String baseUrl;
	private final String listUrl;
	public final Map<String, String> post = new HashMap<>();

	public FoOlSlide(String name, String url, List<String> language,
			Boolean watermarks, String baseUrl, String listUrl) {
		this.name = name;
		this.url = url;
		this.language = language;
		this.watermarks = watermarks;

		this.baseUrl = baseUrl;
		this.listUrl = listUrl;

		post.put("adult", "true"); // VortexScans adult check
		post.put("category", "1"); // SilentSkyScans Webton view
	}

	@Override
	public List<Manga> getMangaList() throws Exception {
		List<Manga> mangas = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPage(baseUrl + listUrl);

		int pages = 1;

		Element nav = doc.select("div[class=prevnext]").first();

		if (nav != null) {
			Element button = nav.select("a[class=gbutton fright]").first();
			if (button != null) {
				String[] pagesData = button.attr("href").split("/");
				pages = Integer.parseInt(pagesData[pagesData.length - 1]);
			}
		}

		for (int i = 1; i <= pages; i++) {
			if (i != 1) {
				doc = JsoupHelper.getHTMLPage(baseUrl + listUrl + i + "/");
			}

			Elements rows = doc.select("div[class=group]");

			for (Element row : rows) {
				Element link = row.select("div[class^=title]").first()
						.select("a").first();
				mangas.add(new Manga(link.attr("abs:href"), link.text()));
			}
		}
		
		Collections.sort(mangas);

		return mangas;
	}

	@Override
	public List<Chapter> getChapterList(Manga manga) throws Exception {
		List<Chapter> chapters = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPageWithPost(manga.getLink(), post);

		Elements groups = doc.select("div[class=group]");

		if (groups.size() == 0) {
			Elements rows = doc.select("div[class=element]");

			for (Element row : rows) {
				Element link = row.select("div[class=title]").first()
						.select("a").first();
				chapters.add(new Chapter(link.attr("abs:href"), link.text()));
			}
		} else {
			for (Element group : groups) {
				String volume = group.select("div[class=title]").first().text();

				Elements rows = group.select("div[class=element]");

				for (Element row : rows) {
					Element link = row.select("div[class=title]").first()
							.select("a").first();
					chapters.add(new Chapter(link.attr("abs:href"), volume
							+ " - " + link.text()));
				}
			}
		}

		return chapters;
	}

	@Override
	public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
		List<Image> images = new LinkedList<>();

		String referrer = chapter.getLink();
		Document doc = JsoupHelper.getHTMLPageWithPost(referrer, post);

		int pages = Integer.parseInt(doc
				.select("div[class=tbtitle dropdown_parent dropdown_right]")
				.first().select("div[class=text]").first().text()
				.replaceFirst("\\D*(\\d*).*", "$1"));

		for (int i = 1; i <= pages; i++) {
			if (i != 1) {
				referrer = chapter.getLink() + "page/" + i;
				doc = JsoupHelper.getHTMLPageWithPost(referrer, post);
			}

			String link = doc.select("img[class=open]").first().attr("src");
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
