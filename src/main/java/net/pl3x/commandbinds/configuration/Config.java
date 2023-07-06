package net.pl3x.commandbinds.configuration;

import java.util.ArrayList;
import java.util.List;
import net.pl3x.commandbinds.gui.CustomKey;
import org.lwjgl.glfw.GLFW;

public class Config {
    public static List<CustomKey> COMMAND_KEYBINDS = new ArrayList<>() {{
        add(new CustomKey(GLFW.GLFW_KEY_B, false, false, false, false, "back"));
        add(new CustomKey(GLFW.GLFW_KEY_H, false, false, false, false, "home"));
    }};
}
