package de.andreasgiemza.mangamanager;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SubscriptionsTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (((SubscriptionsTableModel) table.getModel()).getSubscription(table.convertRowIndexToModel(row)).getUnreadChapters() > 0) {
            component.setFont(component.getFont().deriveFont(Font.BOLD));
        }

        return component;
    }
}
