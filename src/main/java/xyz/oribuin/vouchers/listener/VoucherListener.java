package xyz.oribuin.vouchers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.manager.VoucherManager;
import xyz.oribuin.vouchers.model.Voucher;

public class VoucherListener implements Listener {

    private final VoucherPlugin plugin;

    public VoucherListener(VoucherPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null) return;

        Voucher voucher = this.plugin.getManager(VoucherManager.class).getVoucher(event.getItem());
        if (voucher == null) return;

        if (voucher.redeem(event.getPlayer())) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            event.setCancelled(true);
        }
    }

}
