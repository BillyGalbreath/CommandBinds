package net.pl3x.commandbinds;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.pl3x.commandbinds.configuration.Config;
import net.pl3x.commandbinds.mixin.KeyBindsScreenAccessor;
import net.pl3x.commandbinds.mixin.ScreenAccessor;
import net.pl3x.keyboard.Keyboard;

public class CommandBinds implements ClientModInitializer {
    public static final String MODID = "commandbinds";

    @Override
    public void onInitializeClient() {
        Config.COMMAND_KEYBINDS.forEach(Keyboard::bindKey);
    }

    public static void refreshKeyBindScreen() {
        if (Minecraft.getInstance().screen instanceof KeyBindsScreen screen) {
            KeyBindsScreenAccessor accessor = (KeyBindsScreenAccessor) screen;
            double scrollAmount = accessor.getKeyBindsList().getScrollAmount();
            ((ScreenAccessor) screen).invokeClearWidgets();
            accessor.invokeInit();
            accessor.getKeyBindsList().setScrollAmount(scrollAmount);
        }
    }
}
