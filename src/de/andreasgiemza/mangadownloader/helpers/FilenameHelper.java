/*
 * The MIT License
 *
 * Copyright 2014 Andreas Giemza <andreas@giemza.net>.
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
package de.andreasgiemza.mangadownloader.helpers;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.options.Options;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class FilenameHelper {

    private final static String windows = "[<>:\"/\\|?*]";
    private final static String os = System.getProperty("os.name").toLowerCase();

    private FilenameHelper() {
    }

    private static String checkForIllegalCharacters(String string) {
        if (os.contains("win")) {
            return string.replaceAll(windows, "_");
        } else {
            return string;
        }
    }

    private static String checkForValidDirectoryName(String string) {
        if (os.contains("win")) {
            while (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
            return string;
        } else {
            return string;
        }
    }

    public static Path buildChapterPath(Manga manga, Chapter chapter) {
        String mangaTitle = checkForIllegalCharacters(manga.getTitle());
        mangaTitle = checkForValidDirectoryName(mangaTitle);
        String chapterTitle = checkForIllegalCharacters(chapter.getTitle());

        return Paths.get(Options.INSTANCE.getMangaDir())
                .resolve(mangaTitle).resolve(chapterTitle + ".cbz");
    }
}
