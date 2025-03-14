package me.dizzyfrogs.coordBoard.place;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlaceManager {
    private final Map<UUID, List<Place>> placesMap;
    private final File placesFile;
    private final YamlConfiguration placesConfig;

    public PlaceManager(JavaPlugin plugin) {
        // Initialize the places map to store places for each player UUID
        placesMap = new HashMap<>();

        // Create the file to store places data if it doesn't exist
        placesFile = new File(plugin.getDataFolder(), "places.yml");
        if (!placesFile.exists()) {
            try {
                placesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the existing data from the places.yml file
        placesConfig = YamlConfiguration.loadConfiguration(placesFile);
        loadPlaces();
    }

    public void addPlace(UUID uuid, Place place) {
        // Add a new place for the given UUID
        place.setDatePosted(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
        List<Place> playerPlaces = placesMap.computeIfAbsent(uuid, k -> new ArrayList<>());
        playerPlaces.add(place);
        savePlaces(); // Save the updated places map to the YML file
    }

    public void removePlace(UUID uuid, Place place) {
        // Remove a place for the given UUID
        List<Place> playerPlaces = placesMap.get(uuid);
        if (playerPlaces != null) {
            playerPlaces.remove(place);
            savePlaces(); // Save the updated places map to the YML file
        }
    }

    public void updatePlace(UUID owner, Place oldPlace, Place updatedPlace) {
        List<Place> playerPlaces = placesMap.get(owner);
        if (playerPlaces != null) {
            for (Place place : playerPlaces) {
                if (place.getName().equals(oldPlace.getName())) {
                    place.setName(updatedPlace.getName());
                    place.setLocation(updatedPlace.getLocation());
                    place.setIcon(updatedPlace.getIcon());
                    place.setHidden(updatedPlace.isHidden());
                    savePlaces();
                    break;
                }
            }
        }
    }

    public List<Place> getPlaces(UUID uuid) {
        // Get all places for the given UUID
        if (placesMap.containsKey(uuid)) {
            return placesMap.get(uuid);
        }
        return new ArrayList<>();
    }

    public List<Place> getAllPlacesAllowed(Player player) {
        // Get all places for all UUIDs
        List<Place> places = new ArrayList<>();
        for (List<Place> playerPlaces : placesMap.values()) {
            for (Place place : playerPlaces) {
                if (!place.isHidden() || player.hasPermission("coordboard.admin") || player.getUniqueId().equals(place.getOwnerUUID()))
                    places.add(place);
            }
        }
        return places;
    }

    public Place getPlace(UUID owner, String name) {
        // Get a place by owner's UUID and name
        List<Place> playerPlaces = placesMap.get(owner);
        if (playerPlaces != null) {
            for (Place place : playerPlaces) {
                if (place.getName().equalsIgnoreCase(name))
                    return place;
            }
        }
        return null;
    }

    public List<Place> getAllPlaces() {
        // Get all places for all UUIDs
        List<Place> allPlaces = new ArrayList<>();
        for (List<Place> playerPlaces : placesMap.values()) {
            allPlaces.addAll(playerPlaces);
        }
        return allPlaces;
    }

    public List<Place> getPlacesByOwner(UUID ownerUUID) {
        // Get all places for the given owner UUID
        List<Place> places = new ArrayList<>();
        for (List<Place> playerPlaces : placesMap.values()) {
            for (Place place : playerPlaces) {
                if (place.getOwnerUUID().equals(ownerUUID))
                    places.add(place);
            }
        }
        return places;
    }

    public boolean playerOwnsPlaceWithName(UUID owner, String name) {
        List<Place> playerPlaces = placesMap.get(owner);
        if (playerPlaces != null) {
            for (Place place : playerPlaces) {
                if (place.getName().equalsIgnoreCase(name))
                    return true;
            }
        }
        return false;
    }

    public void reload() {
        // Clear the current data and reload from the YML file
        placesMap.clear();
        loadPlaces();
    }

    public void loadPlaces() {
        for (String key : placesConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            List<Place> playerPlaces = new ArrayList<>();

            List<Map<String, Object>> placesDataList = (List<Map<String, Object>>) placesConfig.getList(key);
            if (placesDataList != null) {
                for (Map<String, Object> placeData : placesDataList) {
                    String name = (String) placeData.get("name");
                    String iconName = (String) placeData.get("icon");
                    Material icon = iconName != null ? Material.getMaterial(iconName) : null;
                    boolean hidden = (Boolean) placeData.get("hidden");
                    String datePosted = (String) placeData.get("datePosted");

                    Map<String, Object> locationData = (Map<String, Object>) placeData.get("location");
                    String worldName = (String) locationData.get("world");
                    double x = (Double) locationData.get("x");
                    double y = (Double) locationData.get("y");
                    double z = (Double) locationData.get("z");

                    Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

                    // Load owner's UUID
                    String ownerUUIDString = (String) placeData.get("ownerUUID");
                    UUID ownerUUID = ownerUUIDString != null ? UUID.fromString(ownerUUIDString) : null;

                    // Create a Place object
                    Place place = new Place(name, location, ownerUUID, datePosted, hidden, icon);
                    playerPlaces.add(place);
                }
            }
            placesMap.put(uuid, playerPlaces);
        }
    }


    public void savePlaces() {
        for (Map.Entry<UUID, List<Place>> entry : placesMap.entrySet()) {
            UUID uuid = entry.getKey();
            List<Place> playerPlaces = entry.getValue();
            List<Map<String, Object>> placesDataList = new ArrayList<>();

            for (Place place : playerPlaces) {
                // Create a map to hold the data of the Place object
                Map<String, Object> placeData = new HashMap<>();
                placeData.put("name", place.getName());
                placeData.put("icon", place.getIcon() != null ? place.getIcon().name() : null);
                placeData.put("hidden", place.isHidden());
                placeData.put("datePosted", place.getDatePosted());
                placeData.put("ownerUUID", place.getOwnerUUID().toString());

                // Create a nested map for the world and coordinates
                Map<String, Object> locationData = new HashMap<>();
                locationData.put("world", place.getLocation().getWorld().getName());
                locationData.put("x", place.getLocation().getX());
                locationData.put("y", place.getLocation().getY());
                locationData.put("z", place.getLocation().getZ());

                placeData.put("location", locationData);
                placesDataList.add(placeData);
            }

            // Save placesDataList to placesConfig under the UUID
            placesConfig.set(uuid.toString(), placesDataList);
        }

        try {
            placesConfig.save(placesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
