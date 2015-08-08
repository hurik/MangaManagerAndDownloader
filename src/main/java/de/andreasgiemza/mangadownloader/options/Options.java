package de.andreasgiemza.mangadownloader.options;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public enum Options {

    INSTANCE;

    private final Properties properties = new Properties();
    private final Path optionsDir = Paths.get(System.getProperty("user.home"))
            .resolve(".MangaDownloader");
    private final Path optionsFile = optionsDir
            .resolve("MangaDownloader.properties");
    private final String mangasDir = "mangasDir";
    private final String selectedSource = "selectedSource";
    private final Path mangaListDir = optionsDir.resolve("sources");
    private final Path subscriptionsFile = optionsDir.resolve("subscriptions.xml");

    Options() {
        try {
            properties.load(new FileInputStream(optionsFile.toFile()));
        } catch (IOException ex) {
        }
    }

    public void setMangaDir(String mangaDir) {
        properties.setProperty(mangasDir, mangaDir);
    }

    public String getMangaDir() {
        String mangaDir = properties.getProperty(mangasDir, "");
        if (mangaDir.isEmpty()) {
            return Paths.get(System.getProperty("user.home")).resolve("Mangas")
                    .toString();
        } else {
            return mangaDir;
        }
    }

    public void setSelectedSource(String siteClass) {
        properties.setProperty(selectedSource, siteClass);
    }

    public String getSelectedSource() {
        return properties.getProperty(selectedSource, "");
    }

    public void saveOptions() {
        try {
            properties.store(new FileOutputStream(optionsFile.toFile()), null);
        } catch (IOException ex) {
        }
    }

    public void createOptionsDir() {
        if (!Files.exists(optionsDir)) {
            try {
                Files.createDirectory(optionsDir);
                Files.setAttribute(optionsDir, "dos:hidden", true);
            } catch (IOException ex) {
            }
        }
    }

    public Path getMangaListDir() {
        return mangaListDir;
    }

    public Path getSubscriptionsFile() {
        return subscriptionsFile;
    }
}
