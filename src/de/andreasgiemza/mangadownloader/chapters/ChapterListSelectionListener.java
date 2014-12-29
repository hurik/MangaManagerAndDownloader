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
package de.andreasgiemza.mangadownloader.chapters;

import de.andreasgiemza.mangadownloader.Gui;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterListSelectionListener implements ListSelectionListener {

    final private Gui gui;
    final private JTable chapterListTable;
    private Chapter lastSelected;

    public ChapterListSelectionListener(Gui gui, JTable chapterListTable) {
        this.gui = gui;
        this.chapterListTable = chapterListTable;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            Chapter selectedChapter = ((ChapterTableModel) chapterListTable.getModel()).getChapterAt(chapterListTable.convertRowIndexToModel(chapterListTable.getSelectedRow()));

            if (selectedChapter != lastSelected) {
                gui.getChapters(selectedChapter);
            }

            lastSelected = selectedChapter;
        } catch (IndexOutOfBoundsException ex) {
        }
    }
}
