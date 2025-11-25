package me.m0dii.squaremap.residence.config;

import me.m0dii.squaremap.residence.SquaremapResidence;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.util.List;

public class SettingsConfig {
    private final SquaremapResidence plugin;
    private FileConfiguration settings;
    private File settingsFile;

    public SettingsConfig(SquaremapResidence plugin) {
        this.plugin = plugin;
        this.register();
    }

    /**
     * Register the settings config.
     */
    public void register() {
        settingsFile = new File(plugin.getDataFolder(), "settings.yml");

        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            plugin.saveResource("settings.yml", false);
        }

        settings = YamlConfiguration.loadConfiguration(settingsFile);

        setDefaults();
    }

    /**
     * Set the settings config defaults.
     */
    public void setDefaults() {
        settings.addDefault("settings.update-interval", 300);

        settings.addDefault("settings.hidden.residences", List.of("residence-name-to-hide"));
        settings.addDefault("settings.hidden.worlds", List.of("worlds-to-hide-residences"));

        settings.addDefault("settings.control.label", "Residence");
        settings.addDefault("settings.control.show", true);
        settings.addDefault("settings.control.hide-by-default", false);

        settings.addDefault("settings.style.stroke.color", colorToHex(Color.GREEN));
        settings.addDefault("settings.style.stroke.weight", 1);
        settings.addDefault("settings.style.stroke.opacity", 1.0D);

        settings.addDefault("settings.style.fill.color", colorToHex(Color.GREEN));
        settings.addDefault("settings.style.fill.opacity", 0.2D);

        settings.addDefault("settings.tooltip.residence", List.of(
            "Residence Name: <span style=\"font-weight:bold;\">{id}</span><br />",
            "Residence Owner: <span style=\"font-weight:bold;\">{owner}</span><br />"
        ));
        settings.addDefault("settings.tooltip.none", "None");
        settings.addDefault("settings.tooltip.unknown", "Unknown");
        settings.addDefault("settings.tooltip.everyone", "Everyone");

        settings.options().copyDefaults(true);

        try {
            settings.save(settingsFile);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Get the stroke color.
     *
     * @return Color
     */
    public Color getStrokeColor() {
        return hexToColor(
            settings.getString("settings.style.stroke.color")
        );
    }

    /**
     * Get the fill color.
     *
     * @return Color
     */
    public Color getFillColor() {
        return hexToColor(
            settings.getString("settings.style.fill.color")
        );
    }

    /**
     * Convert the specified color to hex.
     *
     * @param  color The color.
     * @return String
     */
    private String colorToHex(Color color) {
        return Integer.toHexString(color.getRGB() & 0x00FFFFFF);
    }

    /**
     * Convert the specified hex to a color.
     *
     * @param  hex The hex.
     * @return Color
     */
    private Color hexToColor(String hex) {
        if (hex == null) {
            return Color.RED;
        }

        int rgb = (int) Long.parseLong(
            hex.replace("#", ""),
            16
        );

        return new Color(rgb);
    }

    /**
     * Retrieve the settings config.
     *
     * @return FileConfiguration
     */
    public FileConfiguration getConfig() {
        return settings;
    }
}
