package xyz.oribuin.vouchers.gui.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.vouchers.gui.MenuItem;
import xyz.oribuin.vouchers.gui.PluginMenu;
import xyz.oribuin.vouchers.model.Voucher;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfirmMenu extends PluginMenu {

    public ConfirmMenu(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    /**
     * Open the confirm redeem GUI for the player
     *
     * @param player  The player to open the GUI for
     * @param voucher The voucher to redeem
     * @param item    The item to redeem
     */
    public void open(Player player, Voucher voucher, ItemStack item) {
        Gui gui = this.createGUI(player);
        AtomicBoolean hasRan = new AtomicBoolean(false);

        CommentedConfigurationSection extraItems = this.config.getConfigurationSection("extra-items");
        if (extraItems != null) {
            for (String key : extraItems.getKeys(false)) {
                MenuItem.create(this.config)
                        .path("extra-items." + key)
                        .player(player)
                        .place(gui);
            }
        }

        // Confirm the redeem
        MenuItem.create(this.config)
                .path("confirm-redeem")
                .player(player)
                .action(event -> {
                    if (hasRan.get()) return;
                    hasRan.set(true);

                    if (voucher.redeem(player)) {
                        item.setAmount(item.getAmount() - 1);
                    }

                    gui.close(player);
                }).place(gui);

        // Cancel the redeem
        MenuItem.create(this.config)
                .path("cancel-redeem")
                .player(player)
                .action(event -> gui.close(player))
                .place(gui);

        // Display the voucher, maybe
        Object voucherSlot = this.config.get("gui-settings.voucher-slot");
        if (voucherSlot instanceof Integer slot) {
            gui.setItem(slot, new GuiItem(voucher.getDisplay().clone()));
        }

        gui.open(player);
    }

    @Override
    public String getMenuName() {
        return "confirm-menu";
    }

}
