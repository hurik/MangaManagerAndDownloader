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
package de.andreasgiemza.mangadownloader.mangas;

import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaListSearchDocumentListener implements DocumentListener {

    private final JTextField mangaListSearchTextField;
    private final TableRowSorter<MangaTableModel> mangaTableRowSorter;

    public MangaListSearchDocumentListener(JTextField mangaListSearchTextField, TableRowSorter<MangaTableModel> mangaTableRowSorter) {
        this.mangaListSearchTextField = mangaListSearchTextField;
        this.mangaTableRowSorter = mangaTableRowSorter;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changed();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changed();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changed();

    }

    private void changed() {
        final String searchText = mangaListSearchTextField.getText();

        if (searchText.length() == 0) {
            mangaTableRowSorter.setRowFilter(null);
        } else if (searchText.length() > 0) {
            final String[] searchTextAray = searchText.split(" ");

            String regexExpression = "(?i)";

            for (String word : searchTextAray) {
                if (word.length() > 0) {
                    regexExpression += "(?=.*" + Pattern.quote(word) + ")";
                }
            }

            mangaTableRowSorter.setRowFilter(RowFilter.regexFilter(regexExpression));
        }
    }
}
