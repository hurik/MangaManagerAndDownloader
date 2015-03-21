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
package de.andreasgiemza.mangadownloader.data;

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

    private MangaList() {
    }

    public static void save(Site site, List<Manga> mangas) {
        try {
            Path sourceFile = Options.INSTANCE.getMangaListDir().resolve(site.getName() + sourcesExtension);

            if (!Files.exists(sourceFile.getParent())) {
                Files.createDirectory(sourceFile.getParent());
            }

            if (Files.exists(sourceFile)) {
                Files.delete(sourceFile);
            }

            try (FileOutputStream fout = new FileOutputStream(sourceFile.toFile())) {
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(mangas);
            }
        } catch (IOException ex) {
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Manga> load(Site site) {
        List<Manga> mangas = new LinkedList<>();

        Path sourceFile = Options.INSTANCE.getMangaListDir().resolve(site.getName() + sourcesExtension);

        if (Files.exists(sourceFile)) {
            try (FileInputStream fin = new FileInputStream(sourceFile.toFile())) {
                ObjectInputStream ois = new ObjectInputStream(fin);
                mangas = (LinkedList<Manga>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
            }
        }

        return mangas;
    }

    public static String getLastListUpdate(Site site) {
        Path mangasListFile = Options.INSTANCE.getMangaListDir().resolve(site.getName() + sourcesExtension);

        try {
            return new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(Files.getLastModifiedTime(mangasListFile).toMillis());
        } catch (IOException ex) {
            return null;
        }
    }
}
