package xyz.oribuin.vouchers.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.manager.VoucherManager;
import xyz.oribuin.vouchers.model.Voucher;

import java.util.List;

public class VoucherArgumentHandler extends ArgumentHandler<Voucher> {

    public VoucherArgumentHandler() {
        super(Voucher.class);
    }

    @Override
    public Voucher handle(CommandContext commandContext, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();

        Voucher result = VoucherPlugin.get().getManager(VoucherManager.class)
                .getVoucher(input);

        if (result != null) {
            return result;
        }

        throw new HandledArgumentException("argument-handler-voucher", StringPlaceholders.of("input", input));
    }

    @Override
    public List<String> suggest(CommandContext commandContext, Argument argument, String[] strings) {
        return VoucherPlugin.get().getManager(VoucherManager.class)
                .getVouchers()
                .keySet()
                .stream()
                .toList();
    }

}
