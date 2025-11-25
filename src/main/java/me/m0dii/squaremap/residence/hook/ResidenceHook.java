package me.m0dii.squaremap.residence.hook;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.World;
import me.m0dii.squaremap.residence.SquaremapResidence;

import java.util.*;

public class ResidenceHook {

    private final SquaremapResidence plugin;

    public ResidenceHook(SquaremapResidence plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieve the claims.
     *
     * @param  uuid The world UUID.
     * @return HashSet
     */
    public Set<ClaimedResidence> getResidences(UUID uuid) {
        World world = plugin.getServer().getWorld(uuid);

        if (world == null) {
            return null;
        }

        Set<ClaimedResidence> residences = new HashSet<>();

        plugin.getResidence().getResidenceManager().getResidences().values().forEach(residence -> {
            List<String> hiddenResidences = plugin.getSettings().getConfig().getStringList("settings.hidden.residences");

            if(hiddenResidences.stream().anyMatch(hidden -> hidden.equalsIgnoreCase(residence.getName()))) {
                return;
            }

            List<String> hiddenInWorlds = plugin.getSettings().getConfig().getStringList("settings.hidden.worlds");

            if(hiddenInWorlds.stream().anyMatch(hidden -> hidden.equalsIgnoreCase(residence.getWorldName()))) {
                return;
            }

            if (residence.getWorldName().equalsIgnoreCase(world.getName())) {
                residences.add(residence);
            }
        });

        return residences;
    }

    /**
     * Retrieve the residence ID.
     *
     * @param residence The residence.
     */
    public String getResidenceId(ClaimedResidence residence) {
        return residence.getName();
    }

    /**
     * Retrieve the residence ID as a slug.
     *
     * @param residence  The residence.
     * @param value The iterator.
     */
    public String getResidenceSlug(ClaimedResidence residence, int value) {
        return residence.getName() + (value == 0 ? "" : "-" + value);
    }

    /**
     * Retrieve the residence owner.
     *
     * @param residence The residence.
     */
    public String getResidenceOwner(ClaimedResidence residence) {
        return residence.getOwner();
    }
}
