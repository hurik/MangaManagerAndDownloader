package de.andreasgiemza.mangamanager.addsubscription;

import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.gui.manga.MangaTableModel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangasSelectionListener implements ListSelectionListener {

    private final AddSubscription addSubscription;
    private final JTable mangaListTable;

    public MangasSelectionListener(AddSubscription addSubscription,
            JTable mangaListTable) {
        this.addSubscription = addSubscription;
        this.mangaListTable = mangaListTable;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            Manga selectedManga = ((MangaTableModel) mangaListTable.getModel())
                    .getMangaAt(mangaListTable
                            .convertRowIndexToModel(mangaListTable
                                    .getSelectedRow()));

            addSubscription.mangaSelected(selectedManga);
        } catch (IndexOutOfBoundsException ex) {
        }
    }
}
