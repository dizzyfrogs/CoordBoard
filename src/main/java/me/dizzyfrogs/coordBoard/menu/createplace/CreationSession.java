package me.dizzyfrogs.coordBoard.menu.createplace;

import me.dizzyfrogs.coordBoard.CoordBoard;
import me.dizzyfrogs.coordBoard.place.Place;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.mineacademy.fo.Common.tell;

public class CreationSession implements Listener {
    private final CoordBoard plugin;
    private final Player player;
    private final CreationMenu createPlaceMenu;
    private final EditMenu editPlaceMenu;
    private final Place place;

    private boolean isSettingName = false;
    private boolean isSettingLocation = false;
    private boolean editing = false;

    public CreationSession(Player player, CreationMenu createPlaceMenu, CoordBoard plugin) {
        this.player = player;
        this.createPlaceMenu = createPlaceMenu;
        this.editPlaceMenu = null;
        this.plugin = plugin;
        this.place = null;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public CreationSession(Player player, EditMenu editPlaceMenu, Place place, CoordBoard plugin) {
        this.player = player;
        this.createPlaceMenu = null;
        this.editPlaceMenu = editPlaceMenu;
        this.plugin = plugin;
        this.editing = true;
        this.place = place;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void startSettingName() {
        isSettingName = true;
        player.closeInventory();
        if (editing) {
            tell(player, "&eCurrent name: &7" + place.getName());
        }
        tell(player, "&ePlease enter the new name in chat.",
                "&eType \"&ccancel&e\" to go back.");
    }

    public void startSettingLocation() {
        isSettingLocation = true;
        player.closeInventory();
        tell(player, "&ePlease enter the location in chat,",
                "&eusing the format '&cx,y,z&e'",
                "&eType \"&ccancel&e\" to go back.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }

        String message = event.getMessage();
        if (editing) {
            handleEditingChatEvent(message);
        } else {
            handleCreationChatEvent(message);
        }
        event.setCancelled(true);
    }

    private void handleEditingChatEvent(String message) {
        if (isSettingName) {
            handleNameSettingForEditing(message);
            editPlaceMenu.displayTo(player);
        } else if (isSettingLocation) {
            handleLocationSettingForEditing(message);
            editPlaceMenu.displayTo(player);
        }
    }

    private void handleCreationChatEvent(String message) {
        if (isSettingName) {
            handleNameSettingForCreation(message);
            createPlaceMenu.displayTo(player);
        } else if (isSettingLocation) {
            handleLocationSettingForCreation(message);
            createPlaceMenu.displayTo(player);
        }
    }

    private void handleNameSettingForEditing(String message) {
        if (!message.equalsIgnoreCase("cancel")) {
            if (plugin.getPlaceManager().playerOwnsPlaceWithName(player.getUniqueId(), message)) {
                tell(player, "&cYou already have a place with that name.", "&cPlease choose a different name.");
                isSettingName = false;
                return;
            }
            place.setName(message);
            editPlaceMenu.builder.setName(place.getName());
        }
        isSettingName = false;
    }

    private void handleLocationSettingForEditing(String message) {
        if (message.equalsIgnoreCase("cancel")) {
            isSettingLocation = false;
        } else {
            Location location = parseLocation(message);
            if (location != null) {
                place.setLocation(location);
                editPlaceMenu.builder.setLocation(place.getLocation());
                isSettingLocation = false;
            }
        }
    }

    private void handleNameSettingForCreation(String message) {
        if (!message.equalsIgnoreCase("cancel")) {
            if (plugin.getPlaceManager().playerOwnsPlaceWithName(player.getUniqueId(), message)) {
                tell(player, "&cYou already have a place with that name.", "&cPlease choose a different name.");
                isSettingName = false;
                return;
            }
            createPlaceMenu.placeName = message;
            createPlaceMenu.builder.setName(createPlaceMenu.placeName);
        }
        isSettingName = false;
    }

    private void handleLocationSettingForCreation(String message) {
        if (message.equalsIgnoreCase("cancel")) {
            isSettingLocation = false;
        } else {
            Location location = parseLocation(message);
            if (location != null) {
                createPlaceMenu.placeLocation = location;
                createPlaceMenu.builder.setLocation(createPlaceMenu.placeLocation);
                isSettingLocation = false;
            }
        }
    }

    private Location parseLocation(String message) {
        String[] parts = message.split(",");
        if (parts.length == 3) {
            try {
                double x = parts[0].equals("~") ? player.getLocation().getX() : Double.parseDouble(parts[0]);
                double y = parts[1].equals("~") ? player.getLocation().getY() : Double.parseDouble(parts[1]);
                double z = parts[2].equals("~") ? player.getLocation().getZ() : Double.parseDouble(parts[2]);
                return new Location(Bukkit.getWorld(createPlaceMenu.placeWorld), x, y, z);
            } catch (NumberFormatException e) {
                tell(player, "&cInvalid location format. Please enter the location in the format 'x,y,z'.");
            }
        } else {
            tell(player, "&cInvalid location format. Please enter the location in the format 'x,y,z'.");
        }
        return null;
    }
}
