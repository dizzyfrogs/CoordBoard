package me.dizzyfrogs.coordBoard.menu.createplace;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.menu.MenuPagged;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SelectIcon extends MenuPagged<Material> {

    CreationMenu creationMenu;
    EditMenu editPlaceMenu;

    public SelectIcon(CreationMenu creationMenu) {
        super(creationMenu, Arrays.stream(Material.values())
                .filter(material -> material != Material.AIR && material.isItem())
                .collect(Collectors.toList()));
        this.creationMenu = creationMenu;

        setTitle("&8&lSelect Icon");
    }

    public SelectIcon(EditMenu editPlaceMenu) {
        super(editPlaceMenu, Arrays.stream(Material.values())
                .filter(material -> material != Material.AIR && material.isItem())
                .collect(Collectors.toList()));
        this.editPlaceMenu = editPlaceMenu;

        setTitle("&8&lSelect Icon");
    }

    @Override
    protected ItemStack convertToItemStack(Material material) {
        ItemStack icon = new ItemStack(material);
        ItemMeta meta = icon.getItemMeta();

        if (meta == null) {
            return icon;
        }

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Click to select this icon");
        meta.setDisplayName("§6§l" + material.name().replace("_", " "));
        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
    }

    @Override
    protected void onPageClick(Player player, Material material, ClickType clickType) {
        if (creationMenu != null) {
            creationMenu.placeIcon = material;
            creationMenu.displayTo(player);
        }
        if (editPlaceMenu != null) {
            editPlaceMenu.placeIcon = material;
            editPlaceMenu.displayTo(player);
        }
    }

    @Override
    protected String[] getInfo() {
        return new String[] {
                "Select the icon for your place",
                "Click to select"
        };
    }
}
