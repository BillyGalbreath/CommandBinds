package net.pl3x.commandbinds;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.resources.ResourceLocation;
import net.pl3x.commandbinds.configuration.Config;
import net.pl3x.commandbinds.gui.CommandKey;
import net.pl3x.commandbinds.gui.screen.ModMenuScreen;
import net.pl3x.keyboard.Key;
import net.pl3x.keyboard.Keyboard;
import org.lwjgl.glfw.GLFW;

public class CommandBinds implements ClientModInitializer {
    public static final String CATEGORY_TITLE = "commandbinds.title";
    public static final String KEY_NAME = "commandbinds.keymap.command.%s";

    public static final ResourceLocation TEXTURES_LOCATION = new ResourceLocation("commandbinds", "textures/textures.png");
    public static final int TEXTURES_SIZE = 64;

    @Override
    public void onInitializeClient() {
        Config.reload();

        Keyboard.bindKey(new Key(CATEGORY_TITLE, "commandbinds.keymap.open_keybinds", GLFW.GLFW_KEY_K, client ->
                client.setScreen(new ModMenuScreen(client.screen))
        ));

        Config.COMMAND_KEYBINDS.forEach(Keyboard::bindKey);

        // we need at least 1 custom key for our category
        // to show up in the keybinds menu...
        if (Config.COMMAND_KEYBINDS.isEmpty()) {
            Keyboard.bindKey(new CommandKey(-1, ""));
        }
    }

    public static void refreshKeyBindScreen() {
        if (Minecraft.getInstance().screen instanceof KeyBindsScreen screen) {
            double scrollAmount = screen.keyBindsList.getScrollAmount();
            screen.clearWidgets();
            screen.init();
            screen.keyBindsList.setScrollAmount(scrollAmount);
        }
    }
}
