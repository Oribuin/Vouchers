package xyz.oribuin.vouchers.action.impl;

import dev.rosewood.rosegarden.utils.HexUtils;
import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.action.Action;

public class MessageAction extends Action {

    @Override
    public void run(Player player, String input) {
        player.sendMessage(this.parse(player, HexUtils.colorify(input)));
    }

}
