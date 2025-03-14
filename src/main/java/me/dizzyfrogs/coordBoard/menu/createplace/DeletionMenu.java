package me.dizzyfrogs.coordBoard.menu.createplace;

import me.dizzyfrogs.coordBoard.menu.MainMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.menu.Menu;

public class DeletionMenu extends Menu {

    private EditMenu editPlaceMenu;

    public DeletionMenu(EditMenu editPlaceMenu) {
        this.editPlaceMenu = editPlaceMenu;
        setTitle("&8&lAre you sure?");
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return switch (slot) {
            case 11 -> createConfirmItem();
            case 15 -> createCancelItem();
            default -> null;
        };
    }

    private ItemStack createConfirmItem() {
        return ItemCreator.of(CompMaterial.RED_WOOL)
                .name("&c&lConfirm Delete")
                .lore("", "&7Click to confirm deletion")
                .build().make();
    }

    private ItemStack createCancelItem() {
        return ItemCreator.of(CompMaterial.GRAY_WOOL)
                .name("&7&lCancel")
                .lore("", "&7Click to cancel deletion")
                .build().make();
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clickedItem) {
        switch (slot) {
            case 11:
                editPlaceMenu.deletePlace();
                new MainMenu(editPlaceMenu.plugin).displayTo(player);
                break;
            case 15:
                editPlaceMenu.displayTo(player);
                break;
        }
    }
}