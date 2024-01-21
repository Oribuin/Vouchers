package xyz.oribuin.vouchers.action.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.action.Action;

public class PlayerAction extends Action {

    @Override
    public void run(Player player, String input) {
        Bukkit.dispatchCommand(player, this.parse(player, input));
    }

}
