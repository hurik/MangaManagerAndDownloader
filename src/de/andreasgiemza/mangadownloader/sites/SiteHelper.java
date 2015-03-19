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

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class SiteHelper {

    private final static String implementationsPackage = "de.andreasgiemza.mangadownloader.sites.implementations";

    private SiteHelper() {
    }

    public static List<String> getSites() {
        List<String> sites = new LinkedList<>();

        try {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();

            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith(implementationsPackage)) {
                    final Class<?> clazz = info.load();
                    String[] packageAndName = clazz.getName().split("\\.");
                    sites.add(packageAndName[packageAndName.length - 1]);
                }
            }

            Collections.sort(sites, String.CASE_INSENSITIVE_ORDER);
        } catch (IOException ex) {
        }

        return sites;
    }

    public static Site getInstance(String source) {
        Site site = null;

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith(implementationsPackage)) {
                    final Class<?> clazz = info.load();

                    if (clazz.getName().endsWith(source)) {
                        try {
                            site = (Site) clazz.newInstance();
                            break;
                        } catch (InstantiationException | IllegalAccessException ex) {
                        }
                    }
                }
            }
        } catch (IOException ex) {
        }

        return site;
    }
}
