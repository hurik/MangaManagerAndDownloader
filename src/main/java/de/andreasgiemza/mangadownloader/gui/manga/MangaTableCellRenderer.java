package de.andreasgiemza.mangadownloader.gui.manga;

import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.helpers.FilenameHelper;
import java.awt.Color;
import java.awt.Component;
import java.nio.file.Files;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        Manga manga = ((MangaTableModel) table.getModel()).getMangaAt(table
                .convertRowIndexToModel(row));

        if (isSelected) {
            c.setBackground(UIManager.getColor("Table.selectionBackground"));
        } else if (Files.exists(FilenameHelper.buildMangaPath(manga))) {
            c.setBackground(Color.decode("#D2D2D2"));
        } else {
            c.setBackground(UIManager.getColor("Table.background"));
        }

        return c;
    }
}
