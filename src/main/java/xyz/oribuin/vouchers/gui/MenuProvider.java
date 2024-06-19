package xyz.oribuin.vouchers.gui;


import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.gui.impl.ConfirmMenu;

import java.util.HashMap;
import java.util.Map;

public enum MenuProvider {
    ;

    private final static Map<Class<? extends PluginMenu>, PluginMenu> menuCache = new HashMap<>();

    static {
        menuCache.put(ConfirmMenu.class, new ConfirmMenu(VoucherPlugin.get()));

        menuCache.forEach((aClass, pluginMenu) -> pluginMenu.load());
    }

    public static void reload() {
        menuCache.forEach((aClass, pluginMenu) -> pluginMenu.load());
    }

    /**
     * Get the instance of the menu.
     *
     * @param <T> the type of the menu.
     * @return the instance of the menu.
     */
    @SuppressWarnings("unchecked")
    public static <T extends PluginMenu> T get(Class<T> menuClass) {
        if (menuCache.containsKey(menuClass)) {
            return (T) menuCache.get(menuClass);
        }

        try {
            T menu = menuClass.getDeclaredConstructor().newInstance();
            menu.load();
            menuCache.put(menuClass, menu);
            return menu;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + menuClass.getName(), e);
        }
    }

}