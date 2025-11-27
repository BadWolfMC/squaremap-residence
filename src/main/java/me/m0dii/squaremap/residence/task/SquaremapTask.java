package me.m0dii.squaremap.residence.task;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import me.m0dii.squaremap.residence.SquaremapResidence;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Rectangle;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SquaremapTask extends BukkitRunnable {
    private final UUID world;
    private final SimpleLayerProvider provider;
    private final SquaremapResidence plugin;

    private boolean stop;

    public SquaremapTask(SquaremapResidence plugin, UUID world, SimpleLayerProvider provider) {
        this.plugin = plugin;
        this.world = world;
        this.provider = provider;
    }

    @Override
    public void run() {
        if (stop) {
            cancel();
        }

        updateResidences();
    }

    /**
     * Update the residences.
     */
    private void updateResidences() {
        provider.clearMarkers();

        Set<ClaimedResidence> residences = plugin.getResidenceHook()
            .getResidences(this.world);

        if (residences == null || residences.isEmpty()) {
            return;
        }

        residences.forEach(this::handleResidence);
    }

    /**
     * Handle the residence markers.
     *
     * @param residence The residence.
     */
    private void handleResidence(ClaimedResidence residence) {
        World world = plugin.getServer().getWorld(residence.getWorldName());
        List<String> tooltip = plugin.getSettings().getConfig().getStringList("settings.tooltip.residence");

        int i = 0;

        for (CuboidArea region : residence.getAreaArray()) {
            Location min = region.getLowLocation();
            min.setY(world.getMinHeight());

            Location max = region.getHighLocation();
            max.setY(world.getMaxHeight());

            Rectangle rectangle = Marker.rectangle(
                Point.of(min.getBlockX(), min.getBlockZ()),
                Point.of(max.getBlockX() + 1D, max.getBlockZ() + 1D)
            );

            MarkerOptions.Builder options = MarkerOptions
                .builder()
                .strokeColor(plugin.getSettings().getStrokeColor())
                .strokeWeight(plugin.getSettings().getConfig().getInt("settings.style.stroke.weight"))
                .strokeOpacity(plugin.getSettings().getConfig().getDouble("settings.style.stroke.opacity"))
                .fillColor(plugin.getSettings().getFillColor())
                .fillOpacity(plugin.getSettings().getConfig().getDouble("settings.style.fill.opacity"))
                .clickTooltip(
                    String.join("", tooltip)
                        .replace("{id}", plugin.getResidenceHook().getResidenceId(residence))
                        .replace("{owner}", plugin.getResidenceHook().getResidenceOwner(residence))
                );

            rectangle.markerOptions(options);

            Key marker = Key.of(
                "residence_" +
                    world.getName() +
                    "_residence_" +
                    plugin.getResidenceHook().getResidenceSlug(residence, i)
            );

            provider.addMarker(marker, rectangle);

            i++;
        }
    }

    /**
     * Disable the task.
     */
    public void disable() {
        cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
