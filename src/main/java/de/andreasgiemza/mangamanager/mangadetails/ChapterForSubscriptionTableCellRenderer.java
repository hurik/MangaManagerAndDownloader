package de.andreasgiemza.mangamanager.mangadetails;

import static de.andreasgiemza.mangamanager.data.ChapterForSubscription.UNREAD;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterForSubscriptionTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (((ChapterForSubscriptionTableModel) table.getModel()).getChapter(row).getRead() == UNREAD) {
            component.setFont(component.getFont().deriveFont(Font.BOLD));
        }

        return component;
    }
}
