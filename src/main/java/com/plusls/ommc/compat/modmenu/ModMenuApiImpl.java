package com.plusls.ommc.compat.modmenu;

import com.plusls.ommc.OhMyMinecraftClientReference;
import com.plusls.ommc.gui.GuiConfigs;
import top.hendrixshen.magiclib.compat.modmenu.ModMenuCompatApi;

public class ModMenuApiImpl implements ModMenuCompatApi {
    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return (screen) -> {
            GuiConfigs gui = GuiConfigs.getInstance();
            //#if MC > 11903 && MC < 12000
            gui.setParent(screen);
            //#else
            //$$ gui.setParentGui(screen);
            //#endif
            return gui;
        };
    }

    @Override
    public String getModIdCompat() {
        return OhMyMinecraftClientReference.getCurrentModIdentifier();
    }
}
