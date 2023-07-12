package net.pl3x.commandbinds.configuration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.pl3x.commandbinds.gui.screen.ModMenuScreen;
import org.jetbrains.annotations.NotNull;

public class ModMenu implements ModMenuApi {
    @Override
    public @NotNull ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuScreen::new;
    }
}
