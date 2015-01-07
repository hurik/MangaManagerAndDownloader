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
package de.andreasgiemza.mangadownloader.sites;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.reflections.Reflections;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class SiteHelper {

    private final static String implementationsPackage
            = "de.andreasgiemza.mangadownloader.sites.implementations";
    private final static PrintStream noErr = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }
    });

    private SiteHelper() {
    }

    public static List<String> getSites() {
        List<String> sites = new LinkedList<>();

        PrintStream err = System.err;
        System.setErr(noErr);

        for (Class<? extends Site> site : new Reflections(implementationsPackage)
                .getSubTypesOf(Site.class)) {
            String[] packageAndName = site.getName().split("\\.");
            sites.add(packageAndName[packageAndName.length - 1]);
        }

        System.setErr(err);

        Collections.sort(sites, String.CASE_INSENSITIVE_ORDER);

        return sites;
    }

    public static Site getInstance(String source) {
        Site site = null;

        PrintStream err = System.err;
        System.setErr(noErr);

        for (Class<? extends Site> siteClasse : new Reflections(implementationsPackage)
                .getSubTypesOf(Site.class)) {
            if (siteClasse.getName().endsWith(source)) {
                try {
                    site = siteClasse.newInstance();
                    break;
                } catch (InstantiationException | IllegalAccessException ex) {
                }
            }
        }

        System.setErr(err);

        return site;
    }
}
