package fr.kx2zh.minaria.hub.common.managers;

import com.google.gson.JsonObject;
import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.tools.JsonConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

public abstract class AbstractManager implements EntryPoints {

    protected final Hub hub;
    private String filename;

    public AbstractManager(Hub hub, String filename) {
        this(hub);
        this.filename = filename;
    }

    public AbstractManager(Hub hub) {
        this.hub = hub;

        hub.getEventBus().registerManager(this);
    }

    public void log(Level level, String message) {
        hub.getLogger().log(level, "[" + getClass().getSimpleName() + "] " + message);
    }

    protected JsonObject reloadConfiguration() {
        final File configuration = new File(hub.getDataFolder(), filename);

        if (!configuration.exists()) {
            try(PrintWriter writer = new PrintWriter(configuration)) {
                configuration.createNewFile();
                writer.println("{}");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return new JsonConfiguration(configuration).load();
    }
}
