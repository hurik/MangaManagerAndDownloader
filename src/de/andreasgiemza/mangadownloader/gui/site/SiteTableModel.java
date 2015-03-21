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
package de.andreasgiemza.mangadownloader.gui.site;

import de.andreasgiemza.mangadownloader.data.MangaList;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SiteTableModel extends AbstractTableModel {

    private final List<Site> sites = SiteHelper.getSites();
    private final List<String> columnNames = Arrays.asList(
            "Name",
            "Language",
            "Watermarks",
            "Last List update");

    @Override
    public int getRowCount() {
        return sites.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2) {
            return Boolean.class;
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return sites.get(row).getName();
            case 1:
                return sites.get(row).getLanguage().toString().replace("[", "").replace("]", "");
            case 2:
                return sites.get(row).hasWatermarks();
            case 3:
                String time = MangaList.getLastListUpdate(sites.get(row));

                if (time != null) {
                    return time;
                } else {
                    return "no list present";
                }
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    public Site getSite(int row) {
        return sites.get(row);
    }

    public int getIndexOf(Site site) {
        return sites.indexOf(site);
    }
}
