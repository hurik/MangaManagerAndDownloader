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
package de.andreasgiemza.mangadownloader.data.gui.download;

import de.andreasgiemza.mangadownloader.data.Download;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class DownloadTableModel extends AbstractTableModel {

    private final List<Download> downloads;
    private final List<String> columnNames = Arrays.asList(
            "ID",
            "Site",
            "Manga",
            "Chapter",
            "Status"
    );

    public DownloadTableModel(List<Download> downloads) {
        this.downloads = downloads;
    }

    @Override
    public int getRowCount() {
        return downloads.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return downloads.get(row).getId();
            case 1:
                return downloads.get(row).getSite().getName();
            case 2:
                return downloads.get(row).getManga().getTitle();
            case 3:
                return downloads.get(row).getChapter().getTitle();
            case 4:
                return downloads.get(row).getMessage();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class;
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col);
    }
}
