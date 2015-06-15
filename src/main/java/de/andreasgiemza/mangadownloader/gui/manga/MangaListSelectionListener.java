package de.andreasgiemza.mangadownloader.gui.manga;

import de.andreasgiemza.mangadownloader.MangaDownloader;
import de.andreasgiemza.mangadownloader.data.Manga;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaListSelectionListener implements ListSelectionListener {

	private final MangaDownloader mangaDownloader;
	private final JTable mangaListTable;

	public MangaListSelectionListener(MangaDownloader mangaDownloader,
			JTable mangaListTable) {
		this.mangaDownloader = mangaDownloader;
		this.mangaListTable = mangaListTable;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		try {
			Manga selectedManga = ((MangaTableModel) mangaListTable.getModel())
					.getMangaAt(mangaListTable
							.convertRowIndexToModel(mangaListTable
									.getSelectedRow()));

			mangaDownloader.mangaSelected(selectedManga);
		} catch (IndexOutOfBoundsException ex) {
		}
	}
}
