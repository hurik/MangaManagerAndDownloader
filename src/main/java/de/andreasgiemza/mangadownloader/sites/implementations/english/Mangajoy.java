package de.andreasgiemza.mangadownloader.sites.implementations.english;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Mangajoy implements Site {

	private final String name = "Mangajoy";
	private final String url = "http://mangajoy.com";
	private final List<String> language = Arrays.asList("English");
	private final Boolean watermarks = false;

	@Override
	public List<Manga> getMangaList() throws Exception {
		List<Manga> mangas = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPage(url + "/manga-list-all/");

		Elements rows = doc.select("div[id=sct_manga_list_all]").first()
				.select("li");

		for (Element row : rows) {
			Element link = row.select("a").first();

			mangas.add(new Manga(link.attr("href"), link.attr("title")));
		}

		return mangas;
	}

	@Override
	public List<Chapter> getChapterList(Manga manga) throws Exception {
		List<Chapter> chapters = new LinkedList<>();

		Document doc = JsoupHelper.getHTMLPage(manga.getLink());

		Elements rows = doc.select("ul[class=chp_lst]").first().select("li");

		for (Element row : rows) {
			if (row.select("a").first() == null) {
				continue;
			}

			chapters.add(new Chapter(row.select("a").first().attr("href"), row
					.select("span[class=val]").first().text()));
		}

		return chapters;
	}

	@Override
	public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
		List<Image> images = new LinkedList<>();

		String referrer = chapter.getLink();
		Document doc = JsoupHelper.getHTMLPage(referrer);

		Elements nav = doc
				.select("div[class=wpm_nav_rdr]")
				.first()
				.select("select[onchange^=location.href='" + chapter.getLink()
						+ "' + this.value + '/']").first().select("option");

		int pages = nav.size();

		for (int i = 1; i <= pages; i++) {
			if (i != 1) {
				referrer = chapter.getLink() + (i) + "/";
				doc = JsoupHelper.getHTMLPage(referrer);
			}

			String link = doc.select("div[class=prw]").first().select("img")
					.attr("src");
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
