package me.dizzyfrogs.coordBoard.menu.createplace;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.ButtonReturnBack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class SelectLocation extends Menu {

    private Menu previousMenu;
    private CreationMenu createPlaceMenu;
    private EditMenu editPlaceMenu;

    private final int CURRENT_LOCATION_SLOT = 12;
    private final int CUSTOM_LOCATION_SLOT = 14;

    private ButtonReturnBack backButton;

    public SelectLocation(Menu previousMenu, CreationMenu createPlaceMenu) {
        this.previousMenu = previousMenu;
        this.createPlaceMenu = createPlaceMenu;
        setTitle("&8&lSelect Location");

        this.backButton = new ButtonReturnBack(previousMenu);
    }

    public SelectLocation(Menu previousMenu, EditMenu editPlaceMenu) {
        this.previousMenu = previousMenu;
        this.editPlaceMenu = editPlaceMenu;
        setTitle("&8&lSelect Location");

        this.backButton = new ButtonReturnBack(previousMenu);
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return switch (slot) {
            case CURRENT_LOCATION_SLOT -> createCurrentLocationItem();
            case CUSTOM_LOCATION_SLOT -> createCustomLocationItem();
            case 26 -> backButton.getItem();
            default -> NO_ITEM;
        };
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clickedItem) {
        switch (slot) {
            case CURRENT_LOCATION_SLOT -> {
                if (createPlaceMenu != null) {
                    createPlaceMenu.placeLocation = player.getLocation();
                    createPlaceMenu.displayTo(player);
                }
                if (editPlaceMenu != null) {
                    editPlaceMenu.placeLocation = player.getLocation();
                    editPlaceMenu.displayTo(player);
                }
                tell("&aLocation set to your current location");
            }
            case CUSTOM_LOCATION_SLOT -> {
                if (createPlaceMenu != null)
                    new SelectWorld(this, createPlaceMenu).displayTo(player);
                if (editPlaceMenu != null)
                    new SelectWorld(this, editPlaceMenu).displayTo(player);
            }
        }
    }

    private ItemStack createCurrentLocationItem() {
        return ItemCreator.of(
                CompMaterial.COMPASS,
                "&6&lCurrent Location",
                "",
                "Set to your current location"
        ).build().make();
    }

    private ItemStack createCustomLocationItem() {
        return ItemCreator.of(
                CompMaterial.RECOVERY_COMPASS,
                "&6&lCustom Location",
                "",
                "Set a custom location for the new place"
        ).build().make();
    }

    @Override
    public String[] getInfo() {
        return new String[] {
                "Choose to use your current location,",
                "or set a custom location for the new place",
        };
    }
}
