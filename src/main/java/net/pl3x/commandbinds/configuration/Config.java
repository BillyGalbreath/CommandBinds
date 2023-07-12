package net.pl3x.commandbinds.configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.pl3x.commandbinds.gui.CommandKey;
import org.simpleyaml.configuration.file.YamlFile;

public class Config {
    public static final Path PATH = FabricLoader.getInstance().getGameDir().resolve("config").resolve("commandbinds.yml");
    private static final YamlFile CONFIG = new YamlFile(PATH.toFile());

    public static List<CommandKey> COMMAND_KEYBINDS = new ArrayList<>();

    public static void reload() {
        try {
            CONFIG.createOrLoad();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CONFIG.getMapList("keybinds").forEach(map -> {
            try {
                COMMAND_KEYBINDS.add(new CommandKey(Integer.parseInt(map.get("keycode").toString()), map.get("command").toString()));
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void save() {
        List<Map<String, Object>> list = new ArrayList<>();
        COMMAND_KEYBINDS.forEach(cmdKey -> {
            Map<String, Object> map = new HashMap<>();
            map.put("command", cmdKey.getCommand());
            map.put("keycode", cmdKey.getKeyCode());
            list.add(map);
        });

        CONFIG.set("keybinds", list);

        try {
            CONFIG.save(PATH.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
