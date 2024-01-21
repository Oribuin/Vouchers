package xyz.oribuin.vouchers.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import xyz.oribuin.vouchers.manager.VoucherManager;
import xyz.oribuin.vouchers.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherArgumentHandler extends RoseCommandArgumentHandler<Voucher> {

    public VoucherArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, Voucher.class);
    }

    @Override
    protected Voucher handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) throws HandledArgumentException {
        String input = argumentParser.next();

        Voucher result = this.rosePlugin.getManager(VoucherManager.class).getVoucher(input);
        if (result != null) {
            return result;
        }

        throw new HandledArgumentException("argument-handler-voucher", StringPlaceholders.of("input", input));
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();

        return new ArrayList<>(this.rosePlugin.getManager(VoucherManager.class).getVouchers().keySet());
    }

}
