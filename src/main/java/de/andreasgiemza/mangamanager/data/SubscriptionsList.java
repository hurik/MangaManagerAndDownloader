package de.andreasgiemza.mangamanager.data;

import de.andreasgiemza.mangadownloader.options.Options;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SubscriptionsList {

    private SubscriptionsList() {
    }

    @SuppressWarnings("unchecked")
    public static void save(List<Subscription> subscriptions) {
        try {
            if (Files.exists(Options.INSTANCE.getSubscriptionsFile())) {
                Files.delete(Options.INSTANCE.getSubscriptionsFile());
            }

            try (FileOutputStream fout = new FileOutputStream(Options.INSTANCE.getSubscriptionsFile().toFile());
                    ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                oos.writeObject(subscriptions);
            }
        } catch (IOException ex) {
            Logger.getLogger(SubscriptionsList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Subscription> load() {
        List<Subscription> subscriptions = null;

        if (Files.exists(Options.INSTANCE.getSubscriptionsFile())) {
            try (FileInputStream fin = new FileInputStream(Options.INSTANCE.getSubscriptionsFile().toFile()); ObjectInputStream ois = new ObjectInputStream(fin)) {
                subscriptions = (LinkedList<Subscription>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SubscriptionsList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (subscriptions == null) {
            return new LinkedList<>();
        }

        return subscriptions;
    }
}
