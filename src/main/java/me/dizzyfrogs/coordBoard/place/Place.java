package me.dizzyfrogs.coordBoard.place;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class Place {
    private String name;
    private Location location;
    private String world;
    private UUID ownerUUID;
    private Material icon;
    private String datePosted;
    private boolean hidden;

    public Place(String name, Location location, UUID ownerUUID, String datePosted, boolean hidden, Material icon) {
        this.name = name;
        this.location = location;
        this.world = location.getWorld().getName();
        this.ownerUUID = ownerUUID;
        this.datePosted = datePosted;
        this.hidden = hidden;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getWorld() {
        return world;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getOwnerName() {
        return Bukkit.getOfflinePlayer(ownerUUID).getName();
    }

    public String getDatePosted() {
        return datePosted;
    }

    public Material getIcon() {
        return icon;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.world = location.getWorld().getName();
    }

    public void setOwner(UUID uuid) {
        this.ownerUUID = uuid;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
