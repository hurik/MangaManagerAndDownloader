/*
 * The MIT License
 *
 * Copyright 2015 Andreas Giemza <andreas@giemza.net>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
    private final Path optionsDir = Paths.get(System.getProperty("user.home")).resolve(".MangaDownloader");
    private final Path optionsFile = optionsDir.resolve("MangaDownloader.properties");
    private final String mangasDir = "mangasDir";
    private final String selectedSource = "selectedSource";
    private final Path mangaListDir = optionsDir.resolve("sources");

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
            return Paths.get(System.getProperty("user.home")).resolve("Mangas").toString();
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
            getOptionsDir();
            properties.store(new FileOutputStream(optionsFile.toFile()), null);
        } catch (IOException ex) {
        }
    }

    public Path getOptionsDir() {
        if (!Files.exists(optionsDir)) {
            try {
                Files.createDirectory(optionsDir);
                Files.setAttribute(optionsDir, "dos:hidden", true);
            } catch (IOException ex) {
            }
        }

        return optionsDir;
    }

    public Path getMangaListDir() {
        return mangaListDir;
    }
}
