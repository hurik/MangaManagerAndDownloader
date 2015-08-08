package de.andreasgiemza.mangamanager.data;

import com.thoughtworks.xstream.XStream;
import de.andreasgiemza.mangadownloader.options.Options;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SubscriptionsList {

    private final static XStream xstream = new XStream();

    private SubscriptionsList() {
    }

    @SuppressWarnings("unchecked")
    public static void save(List<Subscription> subscriptions) {
        try {
            if (Files.exists(Options.INSTANCE.getSubscriptionsFile())) {
                Files.delete(Options.INSTANCE.getSubscriptionsFile());
            }

            FileUtils.writeStringToFile(
                    Options.INSTANCE.getSubscriptionsFile().toFile(),
                    xstream.toXML(subscriptions),
                    "UTF-8");
        } catch (IOException ex) {
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Subscription> load() {
        List<Subscription> subscriptions = null;

        if (Files.exists(Options.INSTANCE.getSubscriptionsFile())) {
            try {
                String data = FileUtils.readFileToString(
                        Options.INSTANCE.getSubscriptionsFile().toFile(),
                        "UTF-8");

                subscriptions = (List<Subscription>) xstream.fromXML(data);
            } catch (IOException ex) {
            }
        }

        if (subscriptions == null) {
            return new LinkedList<>();
        }

        return subscriptions;
    }
}
