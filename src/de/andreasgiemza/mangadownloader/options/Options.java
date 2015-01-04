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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public enum Options {

    INSTANCE;

    private final Path OPTIONS_FILE = Paths.get(System.getProperty("user.home")).resolve(".MangaDownloader");
    private final String MANGAS_DIR = "mangaasDir";
    private final String SELECTED_SOURCE = "selectedSource";
    private final Properties properties = new Properties();

    Options() {
        try {
            properties.load(new FileInputStream(OPTIONS_FILE.toFile()));
        } catch (IOException ex) {
        }
    }

    public void setMangaDir(String mangaDir) {
        properties.setProperty(MANGAS_DIR, mangaDir);
    }

    public String getMangaDir() {
        String mangaDir = properties.getProperty(MANGAS_DIR, "");
        if (mangaDir.isEmpty()) {
            return Paths.get("").toAbsolutePath().resolve("mangas").toString();
        } else {
            return mangaDir;
        }
    }

    public void setSelectedSource(String selectedSource) {
        properties.setProperty(SELECTED_SOURCE, selectedSource);
    }

    public String getSelectedSource() {
        return properties.getProperty(SELECTED_SOURCE, "");
    }

    public void saveOptions() {
        try {
            properties.store(new FileOutputStream(OPTIONS_FILE.toFile()), null);
        } catch (IOException ex) {
        }
    }
}
