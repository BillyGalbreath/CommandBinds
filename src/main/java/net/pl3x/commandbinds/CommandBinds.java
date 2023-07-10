package net.pl3x.commandbinds;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.resources.ResourceLocation;
import net.pl3x.commandbinds.configuration.Config;
import net.pl3x.keyboard.Keyboard;

public class CommandBinds implements ClientModInitializer {
    public static final String MODID = "commandbinds";
    public static final ResourceLocation TEXTURES_LOCATION = new ResourceLocation(CommandBinds.MODID, "textures/textures.png");
    public static final int TEXTURES_WIDTH = 64;
    public static final int TEXTURES_HEIGHT = 64;

    @Override
    public void onInitializeClient() {
        Config.COMMAND_KEYBINDS.forEach(Keyboard::bindKey);
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
