package me.dizzyfrogs.coordBoard.place;

import org.bukkit.Location;
import org.bukkit.Material;
import java.util.UUID;

public class PlaceBuilder {

    private String name;
    private Location location;
    private String world;
    private UUID owner;
    private String datePosted;
    private boolean hidden;
    private Material icon;

    public PlaceBuilder() {
        this.name = "null";
        this.location = null;
        this.owner = null;
        this.datePosted = null;
        this.hidden = true;
        this.icon = null;
    }

    public PlaceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaceBuilder setLocation(Location location) {
        this.location = location;
        this.world = location.getWorld().getName();
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public PlaceBuilder setOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    public UUID getOwner() {
        return owner;
    }

    public PlaceBuilder setDatePosted(String datePosted) {
        this.datePosted = datePosted;
        return this;
    }

    public PlaceBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public PlaceBuilder setIcon(Material icon) {
        this.icon = icon;
        return this;
    }

    public Material getIcon() {
        return icon;
    }

    public Place build() {
        return new Place(name, location, owner, datePosted, hidden, icon);
    }
}