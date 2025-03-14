package me.dizzyfrogs.coordBoard.menu.createplace;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.ButtonReturnBack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class SelectWorld extends Menu {

    private Menu previousMenu;
    private CreationMenu createPlaceMenu;
    private EditMenu editPlaceMenu;

    private final int OVERWORLD_SLOT = 11;
    private final int NETHER_SLOT = 13;
    private final int END_SLOT = 15;

    private ButtonReturnBack backButton;

    public SelectWorld(Menu previousMenu, CreationMenu createPlaceMenu) {
        this.previousMenu = previousMenu;
        this.createPlaceMenu = createPlaceMenu;
        setTitle("&8&lSelect Dimension");

        this.backButton = new ButtonReturnBack(previousMenu);
    }

    public SelectWorld(Menu previousMenu, EditMenu editPlaceMenu) {
        this.previousMenu = previousMenu;
        this.editPlaceMenu = editPlaceMenu;
        setTitle("&8&lSelect Dimension");

        this.backButton = new ButtonReturnBack(previousMenu);
    }

    @Override
    public ItemStack getItemAt(int slot) {
        switch (slot) {
            case OVERWORLD_SLOT:
                return createOverworldItem();
            case NETHER_SLOT:
                return createNetherItem();
            case END_SLOT:
                return createEndItem();
            case 26:
                return backButton.getItem();
            default:
                return NO_ITEM;
        }
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clickedItem) {
        switch (slot) {
            case OVERWORLD_SLOT:
                if (createPlaceMenu != null) {
                    createPlaceMenu.placeWorld = "world";
                    createPlaceMenu.session.startSettingLocation();
                }
                if (editPlaceMenu != null) {
                    editPlaceMenu.placeWorld = "world";
                    editPlaceMenu.session.startSettingLocation();
                }
                break;
            case NETHER_SLOT:
                if (createPlaceMenu != null) {
                    createPlaceMenu.placeWorld = "world_nether";
                    createPlaceMenu.session.startSettingLocation();
                }
                if (editPlaceMenu != null) {
                    editPlaceMenu.placeWorld = "world_nether";
                    editPlaceMenu.session.startSettingLocation();
                }
                break;
            case END_SLOT:
                if (createPlaceMenu != null) {
                    createPlaceMenu.placeWorld = "world_the_end";
                    createPlaceMenu.session.startSettingLocation();
                }
                if (editPlaceMenu != null) {
                    editPlaceMenu.placeWorld = "world_the_end";
                    editPlaceMenu.session.startSettingLocation();
                }
                break;
        }
    }

    private ItemStack createOverworldItem() {
        return ItemCreator.of(
                CompMaterial.GRASS_BLOCK,
                "&a&lOverworld",
                "",
                "Set the world to 'Overworld'"
        ).build().make();
    }

    private ItemStack createNetherItem() {
        return ItemCreator.of(
                CompMaterial.NETHERRACK,
                "&c&lNether",
                "",
                "Set the world to 'Nether'"
        ).build().make();
    }

    private ItemStack createEndItem() {
        return ItemCreator.of(
                CompMaterial.END_STONE,
                "&e&lThe End",
                "",
                "Set the world to 'The End'"
        ).build().make();
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "Select the dimension",
                "where the place is located."
        };
    }
}
