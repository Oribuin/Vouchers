package xyz.oribuin.vouchers.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.command.argument.VoucherArgumentHandler;
import xyz.oribuin.vouchers.manager.LocaleManager;
import xyz.oribuin.vouchers.model.Voucher;

public class GiveCommand extends BaseRoseCommand {

    public GiveCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context, Voucher voucher, Integer amount, Player target) {
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
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("give")
                .descriptionKey("command-give-description")
                .permission("vouchers.give")
                .arguments(ArgumentsDefinition.builder()
                        .required("voucher", new VoucherArgumentHandler())
                        .optional("amount", ArgumentHandlers.INTEGER)
                        .optional("target", ArgumentHandlers.PLAYER)
                        .build())
                .build();

    }

}
