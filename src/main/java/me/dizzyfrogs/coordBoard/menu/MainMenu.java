package me.dizzyfrogs.coordBoard.menu;

import me.dizzyfrogs.coordBoard.CoordBoard;
import me.dizzyfrogs.coordBoard.menu.createplace.CreationMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.SkullCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class MainMenu extends Menu {

    private final int CREATE_NEW_PLACE_SLOT = 11;
    private final int ALL_PLACES_SLOT = 13;
    private final int MY_PLACES_SLOT = 15;

    private CoordBoard plugin;

    public MainMenu(CoordBoard plugin) {
        setTitle("#717171&lCoord&8&lBoard");
        this.plugin = plugin;
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return switch (slot) {
            case CREATE_NEW_PLACE_SLOT -> createNewPlaceItem();
            case ALL_PLACES_SLOT -> allPostsItem();
            case MY_PLACES_SLOT -> myPostsItem();
            default -> NO_ITEM;
        };
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clickedItem) {
        switch(slot) {
            case CREATE_NEW_PLACE_SLOT:
                new CreationMenu(this, player, plugin).displayTo(player);
                break;
            case ALL_PLACES_SLOT:
                new ListPlaces(this, plugin, player).displayTo(player);
                break;
            case MY_PLACES_SLOT:
                new MyPlaces(this, plugin, player).displayTo(player);
                break;
            default:
                break;
        }
    }

    private ItemStack createNewPlaceItem() {
        return ItemCreator.of(
                CompMaterial.WRITABLE_BOOK,
                "&6&lCreate Place",
                "",
                "Click to post a new place"
        ).glow(true).build().make();
    }

    private ItemStack allPostsItem() {
        return ItemCreator.of(CompMaterial.PLAYER_HEAD,
                        "&6&lAll Places",
                        "",
                        "&7Click to view all places")
                .skullUrl("https://textures.minecraft.net/texture/25485031b37f0d8a4f3b7816eb717f03de89a87f6a40602aef52221cdfaf7488")
                .make();
    }

    private ItemStack myPostsItem() {
        return ItemCreator.of(
                        SkullCreator.itemFromUuid(getViewer().getUniqueId()))
                .name("&6&lMy Places")
                .lore("")
                .lore("&7Click to view your places")
                .make();
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "Plugin created by dizzyfrogs"
        };
    }
}
