package de.andreasgiemza.mangadownloader.data;

import com.thoughtworks.xstream.XStream;
import de.andreasgiemza.mangadownloader.helpers.FilenameHelper;
import de.andreasgiemza.mangadownloader.options.Options;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class MangaList {

    private final static String sourcesExtension = ".xml";
    private final static String sourcesCountExtension = ".txt";
    private final static XStream xstream = new XStream();

    private MangaList() {
    }

    public static void save(Site site, List<Manga> mangas) {
        try {
            Path sourceFile = Options.INSTANCE.getMangaListDir().resolve(
                    FilenameHelper.checkForIllegalCharacters(site.getName())
                    + sourcesExtension);

            Path sourceCountFile = Options.INSTANCE.getMangaListDir().resolve(
                    FilenameHelper.checkForIllegalCharacters(site.getName())
                    + sourcesCountExtension);

            if (!Files.exists(sourceFile.getParent())) {
                Files.createDirectory(sourceFile.getParent());
            }

            if (Files.exists(sourceFile)) {
                Files.delete(sourceFile);
            }

            if (Files.exists(sourceCountFile)) {
                Files.delete(sourceCountFile);
            }

            FileUtils.writeStringToFile(
                    sourceFile.toFile(),
                    xstream.toXML(mangas),
                    "UTF-8");

            FileUtils.writeStringToFile(
                    sourceCountFile.toFile(),
                    Integer.toString(mangas.size()));
        } catch (IOException ex) {
        }
    }

    public static List<Manga> load(Site site) {
        List<Manga> mangas = null;

        Path sourceFile = Options.INSTANCE.getMangaListDir().resolve(
                FilenameHelper.checkForIllegalCharacters(site.getName())
                + sourcesExtension);

        if (Files.exists(sourceFile)) {
            try {
                String data = FileUtils.readFileToString(
                        sourceFile.toFile(),
                        "UTF-8");

                mangas = (List<Manga>) xstream.fromXML(data);
            } catch (IOException ex) {
            }
        }

        if (mangas == null) {
            return new LinkedList<>();
        }

        return mangas;
    }

    public static String getLastListUpdate(Site site) {
        Path mangasListFile = Options.INSTANCE.getMangaListDir().resolve(
                FilenameHelper.checkForIllegalCharacters(site.getName())
                + sourcesExtension);

        try {
            return new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(Files
                    .getLastModifiedTime(mangasListFile).toMillis());
        } catch (IOException ex) {
            return null;
        }
    }

    public static int getMangaCount(Site site) {
        int mangaCount = 0;

        Path sourceCountFile = Options.INSTANCE.getMangaListDir().resolve(
                FilenameHelper.checkForIllegalCharacters(site.getName())
                + sourcesCountExtension);

        if (Files.exists(sourceCountFile)) {
            try {
                mangaCount = Integer.parseInt(FileUtils.readFileToString(sourceCountFile.toFile()));
            } catch (IOException ex) {
            }
        }

        return mangaCount;
    }

}
