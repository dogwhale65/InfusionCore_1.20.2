package infusioncore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfusionCore extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new GiveEffects(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
