package xyz.oribuin.vouchers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.manager.ConfigurationManager.Setting;
import xyz.oribuin.vouchers.manager.VoucherManager;
import xyz.oribuin.vouchers.model.Voucher;

public class VoucherListener implements Listener {

    private final VoucherManager manager;

    public VoucherListener(VoucherPlugin plugin) {
        this.manager = plugin.getManager(VoucherManager.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().isInteractable()) return;

        Voucher voucher = this.manager.getVoucher(event.getItem());
        if (voucher == null) return;
        event.setCancelled(true);

        if (!Setting.REDEEM_WHILE_CROUCHING.getBoolean() && event.getPlayer().isSneaking()) return;

        ItemStack item = event.getItem();
        if (voucher.redeem(event.getPlayer(), manager.getUniqueId(item))) {
            item.setAmount(item.getAmount() - 1);
        }
    }

}
