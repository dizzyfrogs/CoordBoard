package me.dizzyfrogs.coordBoard.menu.createplace;

import me.dizzyfrogs.coordBoard.CoordBoard;
import me.dizzyfrogs.coordBoard.place.Place;
import me.dizzyfrogs.coordBoard.place.PlaceBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.UUID;

public class EditMenu extends Menu {

    Menu previousMenu;
    private Place place;
    private Player viewer;
    CoordBoard plugin;

    String placeName;
    public Location placeLocation;
    public String placeWorld;
    public Material placeIcon;
    boolean placeHidden = false;

    // Similar slots as in CreatePlaceMenu
    private final int NAME_SLOT = 11;
    private final int LOCATION_SLOT = 12;
    private final int ICON_SLOT = 13;
    private final int VISIBILITY_SLOT = 14;
    private final int CONFIRM_SLOT = 15;
    private final int DELETE_SLOT = 0;
    private final int CANCEL_SLOT = 26;

    public CreationSession session;

    public PlaceBuilder builder = new PlaceBuilder();

    public EditMenu(Menu previousMenu, Place place, Player viewer, CoordBoard plugin) {
        this.previousMenu = previousMenu;
        this.place = place;
        this.viewer = viewer;
        this.plugin = plugin;
        this.session = new CreationSession(viewer, this, place, plugin);

        this.placeName = place.getName();
        this.placeLocation = place.getLocation();
        this.placeIcon = place.getIcon();
        this.placeHidden = place.isHidden();
        setTitle("&8&lEdit Place");
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return switch (slot) {
            case NAME_SLOT -> createNameItem();
            case LOCATION_SLOT -> createLocationItem();
            case ICON_SLOT -> createIconItem();
            case VISIBILITY_SLOT -> createVisibilityItem();
            case CONFIRM_SLOT -> createConfirmItem();
            case CANCEL_SLOT -> createCancelItem();
            case DELETE_SLOT -> createDeleteItem();
            default -> NO_ITEM;
        };
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clickedItem) {
        switch (slot) {
            case NAME_SLOT:
                session.startSettingName();
                break;
            case LOCATION_SLOT:
                new SelectLocation(this, this).displayTo(player);
                break;
            case ICON_SLOT:
                new SelectIcon(this).displayTo(player);
                break;
            case VISIBILITY_SLOT:
                placeHidden = !placeHidden;
                this.displayTo(player);
                break;
            case CONFIRM_SLOT:
                updatePlace(place.getOwnerUUID());
                previousMenu.displayTo(player);
                break;
            case DELETE_SLOT:
                new DeletionMenu(this).displayTo(player);
                break;
            case CANCEL_SLOT:
                previousMenu.displayTo(viewer);
                break;
        }
    }

    void deletePlace() {
        plugin.getPlaceManager().removePlace(place.getOwnerUUID(), place);
        tell(place.getName() + " &6has been deleted!");
        previousMenu.displayTo(getViewer());
    }

    private void updatePlace(UUID owner) {
        builder.setName(placeName);
        builder.setLocation(placeLocation);
        builder.setIcon(placeIcon);
        builder.setHidden(placeHidden);
        builder.setOwner(place.getOwnerUUID());
        builder.setDatePosted(place.getDatePosted());
        plugin.getPlaceManager().updatePlace(owner, place, builder.build());
        tell(builder.getName() + " &6has been updated!");
        previousMenu.displayTo(getViewer());
    }

    private ItemStack createNameItem() {
        if (placeName == null) {
            return ItemCreator.of(
                    CompMaterial.PAPER,
                    "&6&lPlace Name",
                    "",
                    "Edit the name of the place"
            ).glow(false).build().make();
        } else {
            return ItemCreator.of(
                    CompMaterial.PAPER,
                    "&6&lPlace Name",
                    "",
                    "Currently: &7" + placeName
            ).glow(true).build().make();
        }
    }

    private ItemStack createLocationItem() {
        if (placeLocation == null) {
            return ItemCreator.of(
                    CompMaterial.COMPASS,
                    "&6&lLocation",
                    "",
                    "Edit the coordinates of the place"
            ).glow(false).build().make();
        } else {
            String worldType;
            switch (placeLocation.getWorld().getEnvironment()) {
                case NORMAL -> worldType = "&2Overworld";
                case NETHER -> worldType = "&cNether";
                case THE_END -> worldType = "&eThe End";
                default -> worldType = "&7Unknown";
            }
            return ItemCreator.of(
                    CompMaterial.COMPASS,
                    "&6&lLocation",
                    "",
                    "Currently: &a" + (int) placeLocation.getX() + ", " + (int) placeLocation.getY() + ", " + (int) placeLocation.getZ(),
                    "Dimension: " + worldType
            ).glow(true).build().make();
        }
    }

    private ItemStack createIconItem() {
        if (placeIcon == null) {
            return ItemCreator.of(
                    CompMaterial.PAINTING,
                    "&6&lIcon",
                    "",
                    "Edit the icon of the place"
            ).glow(false).build().make();
        } else {
            return ItemCreator.of(
                    CompMaterial.fromItem(placeIcon.asItemType().createItemStack()),
                    "&6&lIcon",
                    "",
                    "Edit the icon of the place"
            ).glow(true).build().make();
        }
    }

    private ItemStack createVisibilityItem() {
        CompMaterial material = placeHidden ? CompMaterial.IRON_DOOR : CompMaterial.OAK_DOOR;
        return ItemCreator.of(
                material,
                (placeHidden ? "&c&lPrivate" : "&a&lPublic"),
                "",
                "Toggle the visibility of the place"
        ).build().make();
    }

    private ItemStack createConfirmItem() {
        if (placeName == null || placeLocation == null || placeIcon == null) {
            return ItemCreator.of(
                    CompMaterial.GRAY_DYE,
                    "&7&lCan't Confirm",
                    "",
                    "You must fill out all fields"
            ).glow(false).build().make();
        } else {
            return ItemCreator.of(
                    CompMaterial.LIME_DYE,
                    "&a&lConfirm Changes",
                    "",
                    "Confirm changes and submit"
            ).glow(true).build().make();
        }
    }

    private ItemStack createDeleteItem() {
        return ItemCreator.of(
                CompMaterial.RED_DYE,
                "&c&lDelete",
                "",
                "Delete the place"
        ).build().make();
    }

    private ItemStack createCancelItem() {
        return ItemCreator.of(
                CompMaterial.BARRIER,
                "&c&lCancel",
                "",
                "Cancel editing place"
        ).build().make();
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "Edit the details of the place",
                "you have posted"
        };
    }
}
