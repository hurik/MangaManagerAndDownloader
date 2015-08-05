package de.andreasgiemza.mangadownloader.data;

import de.andreasgiemza.mangadownloader.helpers.FilenameHelper;
import de.andreasgiemza.mangadownloader.options.Options;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class MangaList {

    private final static String sourcesExtension = ".list";
    private final static String sourcesCountExtension = ".count";

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

            try (FileOutputStream fout = new FileOutputStream(sourceFile.toFile());
                    ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                oos.writeObject(mangas);
            }

            try (FileOutputStream fout = new FileOutputStream(sourceCountFile.toFile());
                    ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                oos.writeObject(mangas.size());
            }
        } catch (IOException ex) {
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Manga> load(Site site) {
        List<Manga> mangas = null;

        Path sourceFile = Options.INSTANCE.getMangaListDir().resolve(
                FilenameHelper.checkForIllegalCharacters(site.getName())
                + sourcesExtension);

        if (Files.exists(sourceFile)) {
            try (FileInputStream fin = new FileInputStream(sourceFile.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fin)) {
                mangas = (LinkedList<Manga>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
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

    @SuppressWarnings("unchecked")
    public static int getMangaCount(Site site) {
        int mangaCount = 0;

        Path sourceCountFile = Options.INSTANCE.getMangaListDir().resolve(
                FilenameHelper.checkForIllegalCharacters(site.getName())
                + sourcesCountExtension);

        if (Files.exists(sourceCountFile)) {
            try (FileInputStream fin = new FileInputStream(sourceCountFile.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fin)) {
                mangaCount = (int) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
            }
        }

        return mangaCount;
    }

}
