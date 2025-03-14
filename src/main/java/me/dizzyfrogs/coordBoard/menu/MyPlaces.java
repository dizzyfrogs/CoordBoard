package me.dizzyfrogs.coordBoard.menu;

import me.dizzyfrogs.coordBoard.CoordBoard;
import me.dizzyfrogs.coordBoard.menu.createplace.EditMenu;
import me.dizzyfrogs.coordBoard.place.Place;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class MyPlaces extends MenuPagged<Place> {

    private Menu previousMenu;
    private CoordBoard plugin;

    public MyPlaces(Menu previousMenu, CoordBoard plugin, Player viewer) {
        super(previousMenu, plugin.getPlaceManager().getPlaces(viewer.getUniqueId()));
        this.previousMenu = previousMenu;
        this.plugin = plugin;


        setTitle("&8&lMy Places");
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

            return ItemCreator.of(
                    CompMaterial.fromMaterial(place.getIcon()),
                    "&7" + place.getName(),
                    "&6Coords: &a" + x + ", " + y + ", " + z,
                    "&6Dimension: " + world,
                    "&6Visibility: " + (place.isHidden() ? "&cPrivate" : "&aPublic"),
                    "&6Posted on: &d" + place.getDatePosted()
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
        if (clickType.isLeftClick())
            tell("&6Details for &7" + place.getName(),
                    "&6Coordinates: &a" + place.getLocation().getBlockX() + "&6, &a" + place.getLocation().getBlockY() + "&6, &a" + place.getLocation().getBlockZ(),
                    "&6Dimension: " + world);
        if (clickType.isRightClick())
            new EditMenu(this, place, player, plugin).displayTo(player);
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "Left-click a place to send the coordinates to your chat.",
                "Right-click a place to edit it."
        };
    }
}
