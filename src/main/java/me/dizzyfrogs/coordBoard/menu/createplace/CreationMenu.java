package me.dizzyfrogs.coordBoard.menu.createplace;

import me.dizzyfrogs.coordBoard.CoordBoard;
import me.dizzyfrogs.coordBoard.place.PlaceBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.UUID;

public class CreationMenu extends Menu {

    private Menu previousMenu;
    String placeName;
    Location placeLocation;
    String placeWorld;
    Material placeIcon;
    boolean placeHidden = false;

    private final int NAME_SLOT = 11;
    private final int LOCATION_SLOT = 12;
    private final int ICON_SLOT = 13;
    private final int VISIBILITY_SLOT = 14;
    private final int SUBMIT_SLOT = 15;
    private final int CANCEL_SLOT = 26;

    CreationSession session;
    private Player viewer;
    PlaceBuilder builder = new PlaceBuilder();
    CoordBoard plugin;

    public CreationMenu(Menu previousMenu, Player viewer, CoordBoard plugin) {
        this.previousMenu = previousMenu;
        setTitle("&8&lCreate New Place");
        this.viewer = viewer;
        this.plugin = plugin;
        this.session = new CreationSession(viewer, this, plugin);
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return switch (slot) {
            case NAME_SLOT -> createNameItem();
            case LOCATION_SLOT -> createLocationItem();
            case ICON_SLOT -> createIconItem();
            case VISIBILITY_SLOT -> createVisibilityItem();
            case SUBMIT_SLOT -> createSubmitItem();
            case CANCEL_SLOT -> createCancelItem();
            default -> NO_ITEM;
        };
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clickedItem) {
        switch (slot) {
            case NAME_SLOT -> {
                session.startSettingName();
            }
            case LOCATION_SLOT -> {
                new SelectLocation(this, this).displayTo(player);
            }
            case ICON_SLOT -> {
                new SelectIcon(this).displayTo(player);
            }
            case VISIBILITY_SLOT -> {
                placeHidden = !placeHidden;
                this.displayTo(player);
            }
            case SUBMIT_SLOT -> {
                submitCreation(player.getUniqueId());
            }
            case CANCEL_SLOT -> {
                cancelCreation();
            }
        }
    }

    private void submitCreation(UUID owner) {
        if (placeName != null && placeLocation != null && placeIcon != null) {
            builder.setName(placeName);
            builder.setLocation(placeLocation);
            builder.setIcon(placeIcon);
            builder.setHidden(placeHidden);
            builder.setOwner(viewer.getUniqueId());
            builder.setDatePosted("");
            plugin.getPlaceManager().addPlace(owner, builder.build());
            tell(placeName + " &6has been posted!");
            previousMenu.displayTo(getViewer());
        }
    }

    private void cancelCreation() {
        previousMenu.displayTo(getViewer());
    }

    private ItemStack createNameItem() {
        if (placeName == null) {
            return ItemCreator.of(
                    CompMaterial.PAPER,
                    "&6&lPlace Name",
                    "",
                    "Set the name of the new place"
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
                    "Set the coordinates of the new place"
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
                    "Set the icon of the new place"
            ).glow(false).build().make();
        } else {
            return ItemCreator.of(
                    CompMaterial.fromItem(placeIcon.asItemType().createItemStack()),
                    "&6&lIcon",
                    "",
                    "Set the icon of the new place"
            ).glow(true).build().make();
        }
    }

    private ItemStack createVisibilityItem() {
        CompMaterial material = placeHidden ? CompMaterial.IRON_DOOR : CompMaterial.OAK_DOOR;
        return ItemCreator.of(
                material,
                (placeHidden ? "&c&lPrivate" : "&a&lPublic"),
                "",
                "Toggle the visibility of the new place"
        ).build().make();
    }

    private ItemStack createSubmitItem() {
        if (placeName == null || placeLocation == null || placeIcon == null) {
            return ItemCreator.of(
                    CompMaterial.GRAY_DYE,
                    "&7&lCan't Submit",
                    "",
                    "You must fill out all fields"
            ).glow(false).build().make();
        } else {
            return ItemCreator.of(
                    CompMaterial.LIME_DYE,
                    "&a&lSubmit",
                    "",
                    "Submit the new place"
            ).glow(true).build().make();
        }
    }

    private ItemStack createCancelItem() {
        return ItemCreator.of(
                CompMaterial.BARRIER,
                "&c&lCancel",
                "",
                "Cancel creating a new place"
        ).build().make();
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "Fill out the details of the new place,",
                "then click 'Submit' to post it."
        };
    }
}
