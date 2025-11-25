package me.m0dii.squaremap.residence;

import com.bekvon.bukkit.residence.Residence;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.m0dii.squaremap.residence.config.SettingsConfig;
import me.m0dii.squaremap.residence.hook.ResidenceHook;
import me.m0dii.squaremap.residence.hook.SquaremapHook;

public class SquaremapResidence extends JavaPlugin {
    public static SquaremapResidence plugin;
    private SettingsConfig settingsConfig;
    private SquaremapHook squaremapHook;
    private Residence residence;
    private ResidenceHook residenceHook;

    @Override
    public void onEnable() {
        plugin = this;

        Plugin residencePlugin = getServer().getPluginManager().getPlugin("Residence");

        if (residencePlugin != null) {
            this.residence = Residence.getInstance();
        } else {
            getLogger().severe("Residence plugin not found! Disabling SquaremapResidence.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerSettings();
        registerHooks();
    }

    @Override
    public void onDisable() {
        if (squaremapHook != null) {
            squaremapHook.disable();
        }
    }

    /**
     * Register the plugin hooks.
     */
    public void registerHooks() {
        squaremapHook = new SquaremapHook(this);
        residenceHook = new ResidenceHook(this);
    }

    /**
     * Register the plugin config.
     */
    public void registerSettings() {
        settingsConfig = new SettingsConfig(this);
    }

    /**
     * Retrieve the plugin config.
     *
     * @return SettingsConfig
     */
    public SettingsConfig getSettings() {
        return settingsConfig;
    }

    /**
     * Retrieve the Residence instance.
     *
     * @return Residence
     */
    public Residence getResidence() {
        return residence;
    }

    /**
     * Retrieve the ResidenceHook hook instance.
     *
     * @return ResidenceHook
     */
    public ResidenceHook getResidenceHook() {
        return residenceHook;
    }
}
