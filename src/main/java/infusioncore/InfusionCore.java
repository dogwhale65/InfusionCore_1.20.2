package infusioncore.infusioncore_1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfusionCore_1 extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new GiveEffects(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
