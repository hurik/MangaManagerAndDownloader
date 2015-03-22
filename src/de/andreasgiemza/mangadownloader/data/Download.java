/*
 * The MIT License
 *
 * Copyright 2015 hurik.
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

import de.andreasgiemza.mangadownloader.sites.Site;
import java.util.Objects;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Download {

    private final int id;
    private final Site site;
    private final Manga manga;
    private final Chapter chapter;
    private State state = State.PENDING;
    private String message = "Pending";

    public Download(int id, Site site, Manga manga, Chapter chapter) {
        this.id = id;
        this.site = site;
        this.manga = manga;
        this.chapter = chapter;
    }

    public int getId() {
        return id;
    }

    public Site getSite() {
        return site;
    }

    public Manga getManga() {
        return manga;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.site);
        hash = 79 * hash + Objects.hashCode(this.manga);
        hash = 79 * hash + Objects.hashCode(this.chapter);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Download other = (Download) obj;
        if (!Objects.equals(this.site, other.site)) {
            return false;
        }
        if (!Objects.equals(this.manga, other.manga)) {
            return false;
        }
        return Objects.equals(this.chapter, other.chapter);
    }

    public enum State {

        PENDING, RUNNING, DONE, CANCELLED, ERROR;
    }
}
