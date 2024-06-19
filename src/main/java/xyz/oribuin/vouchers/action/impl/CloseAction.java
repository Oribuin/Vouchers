package xyz.oribuin.vouchers.action.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.action.Action;

public class CloseAction extends Action {

    @Override
    public void run(Player player, String input) {
        player.closeInventory();
    }

}
