package xyz.oribuin.vouchers.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.manager.LocaleManager;
import xyz.oribuin.vouchers.model.Voucher;

public class GiveCommand extends RoseCommand {

    public GiveCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Voucher voucher, @Optional Integer amount, @Optional Player target) {
        LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        int amt = amount == null || amount <= 0 ? 1 : amount;

        if (target == null && (!(context.getSender() instanceof Player))) {
            locale.sendMessage(context.getSender(), "command-give-invalid-target");
            return;
        }

        if (target == null) {
            target = (Player) context.getSender();
        }

        voucher.give(target, amt);
        locale.sendMessages(context.getSender(), "command-give-succcess", StringPlaceholders.of(
                "amount", amt,
                "voucher", voucher.getId(),
                "target", target.getName()
        ));
    }

    @Override
    protected String getDefaultName() {
        return "give";
    }

    @Override
    public String getDescriptionKey() {
        return "command-give-description";
    }

    @Override
    public String getRequiredPermission() {
        return "vouchers.give";
    }

}
