package xyz.oribuin.vouchers.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (event.useInteractedBlock() == Event.Result.DENY) return;

        Voucher voucher = this.plugin.getManager(VoucherManager.class).getVoucher(event.getItem());
        if (voucher == null) return;

        event.setCancelled(true);

        if (voucher.redeem(event.getPlayer())) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }

}
