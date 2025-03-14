package me.dizzyfrogs.coordBoard.menu;

import me.dizzyfrogs.coordBoard.CoordBoard;
import me.dizzyfrogs.coordBoard.place.Place;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;

public class ListPlaces extends MenuPagged<Place> {

    private Menu previousMenu;
    private CoordBoard plugin;
    private Player viewer;

    public ListPlaces(Menu previousMenu, CoordBoard plugin, Player viewer) {
        super(previousMenu, plugin.getPlaceManager().getAllPlacesAllowed(viewer));
        this.previousMenu = previousMenu;
        this.plugin = plugin;
        this.viewer = viewer;

        setTitle("&8&lAll Places");
    }

    @Override
    protected ItemStack convertToItemStack(Place place) {
        if (place.getIcon() != null) {
            Location placeLoc = place.getLocation();
            int x = (int) placeLoc.getX();
            int y = (int) placeLoc.getY();
            int z = (int) placeLoc.getZ();

            String world = "";
            switch (placeLoc.getWorld().getEnvironment()) {
                case NORMAL -> world = "&2Overworld";
                case NETHER -> world = "&cNether";
                case THE_END -> world = "&eThe End";
            }
            ArrayList<String> lore = new ArrayList<>();
            lore.add("&6Coords: &a" + x + ", " + y + ", " + z);
            lore.add("&6Dimension: " + world);
            lore.add("&6Posted by: &e" + place.getOwnerName());
            if (viewer.hasPermission("coordboard.admin"))
                lore.add("&6Visibility: " + (place.isHidden() ? "&cPrivate" : "&aPublic"));
            lore.add("&6Posted on: &d" + place.getDatePosted());
            return ItemCreator.of(
                    CompMaterial.fromMaterial(place.getIcon()),
                    "&7" + place.getName(),
                    lore
            ).make();
        }
        return null;
    }

    @Override
    protected void onPageClick(Player player, Place place, ClickType clickType) {
        String world = "";
        switch (place.getLocation().getWorld().getEnvironment()) {
            case NORMAL -> world = "&2Overworld";
            case NETHER -> world = "&cNether";
            case THE_END -> world = "&eThe End";
        }
        tell("&6Details for &7" + place.getName(),
                "&6Coordinates: &a" + place.getLocation().getBlockX() + "&6, &a" + place.getLocation().getBlockY() + "&6, &a" + place.getLocation().getBlockZ(),
                "&6Dimension: " + world);
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "All public places are shown here,",
                "along with places that can only be seen by you.",
                "",
                "Click a place to send the coordinates to your chat."
        };
    }
}
