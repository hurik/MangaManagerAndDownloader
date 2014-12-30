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
package de.andreasgiemza.mangadownloader.downloader;

import de.andreasgiemza.mangadownloader.chapters.Chapter;
import de.andreasgiemza.mangadownloader.helpers.Filename;
import de.andreasgiemza.mangadownloader.mangas.Manga;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.jsoup.Jsoup;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class Downloader {

    public static void download(Path currentDirectory, Site site, Manga selectedManga, List<Chapter> chapters) {
        int chapterCount = 0;
        for (Chapter chapter : chapters) {
            if (chapter.isDownload()) {
                chapterCount++;
            }
        }

        System.out.println("Downloading " + chapterCount + " chapters of " + selectedManga.getTitleShort() + " ...");

        int chapterDone = 1;

        for (Chapter chapter : chapters) {
            if (chapter.isDownload()) {
                System.out.println(chapterDone + " of " + chapterCount + " ...");

                List<String> imageLinks = site.getChapterImageLinks(chapter);

                String mangaTitle = Filename.checkForIllegalCharacters(selectedManga.getTitleShort());
                String chapterTitle = Filename.checkForIllegalCharacters(chapter.getTitle());

                Path mangaFile = currentDirectory.resolve("mangas")
                        .resolve(mangaTitle)
                        .resolve(chapterTitle + ".cbz");

                try {
                    if (!Files.exists(mangaFile.getParent())) {
                        Files.createDirectories(mangaFile.getParent());
                    }

                    if (Files.exists(mangaFile)) {
                        Files.delete(mangaFile);
                    }

                    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(mangaFile.toFile()))) {
                        for (int i = 0; i < imageLinks.size(); i++) {
                            String imageLink = imageLinks.get(i);
                            String extension = imageLink.substring(imageLink.length() - 3, imageLink.length());

                            ZipEntry ze = new ZipEntry((i + 1) + "." + extension);
                            zos.putNextEntry(ze);

                            byte[] image = Jsoup.connect(imageLink)
                                    .maxBodySize(10 * 1024 * 1024)
                                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                                    .ignoreContentType(true)
                                    .execute()
                                    .bodyAsBytes();

                            zos.write(image, 0, image.length);
                            zos.closeEntry();
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                }

                chapterDone++;
            }
        }
    }
}
