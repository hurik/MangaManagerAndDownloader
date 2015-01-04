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
package de.andreasgiemza.mangadownloader.gui.chapter;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.gui.Controller;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JTable;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterCheckBoxItemListener implements ItemListener {

    private final JTable chapterListTable;
    private final ChapterTableModel chapterTableModel;
    private final Controller controller;

    public ChapterCheckBoxItemListener(JTable chapterListTable, Controller controller) {
        this.chapterListTable = chapterListTable;
        chapterTableModel = (ChapterTableModel) chapterListTable.getModel();
        this.controller = controller;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            for (int i = 0; i < chapterListTable.getRowCount(); i++) {
                Chapter chapter = chapterTableModel.getChapterAt(chapterListTable.convertRowIndexToModel(i));

                if (!chapter.isAlreadyDownloaded()) {
                    chapter.setDownload(true);
                }
            }
            chapterTableModel.fireTableDataChanged();
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            controller.deactivateDownloads();
        }
    }
}
