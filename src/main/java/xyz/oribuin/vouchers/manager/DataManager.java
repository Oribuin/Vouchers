package xyz.oribuin.vouchers.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import xyz.oribuin.vouchers.migration._1_CreateInitialTables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataManager extends AbstractDataManager {

    private final Map<UUID, Integer> cachedUses = new HashMap<>();

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        super.reload();

        // Load all the uses from the database
        // (This is better than loading straight from the database when needed)
        this.cachedUses.clear();
        this.async(() -> this.databaseConnector.connect(connection -> {
            String select = "SELECT * FROM " + this.getTablePrefix() + "uses";
            try (PreparedStatement statement = connection.prepareStatement(select)) {
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    UUID voucher = UUID.fromString(rs.getString("voucher_id"));
                    int uses = rs.getInt("uses");

                    this.cachedUses.put(voucher, uses);
                }
            }
        }));
    }

    /**
     * Add a use to the voucher with the specified UUID for the specified amount of uses
     *
     * @param voucher The voucher to add the use to
     * @param uses    The amount of uses to add
     */
    public void addUse(UUID voucher, int uses) {
        this.cachedUses.put(voucher, uses);

        this.async(() -> this.databaseConnector.connect(connection -> {
            String insert = "REPLACE INTO " + this.getTablePrefix() + "uses (`voucher_id`, `uses`) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setString(1, voucher.toString());
                statement.setInt(2, uses);
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Get all the uses for a voucher
     *
     * @param voucher The voucher
     * @return The amount of uses
     */
    public int getUses(UUID voucher) {
        return this.cachedUses.getOrDefault(voucher, 1);
    }

    /**
     * Run a task asynchronously
     *
     * @param runnable The task to run
     */
    public void async(Runnable runnable) {
        this.rosePlugin.getServer().getScheduler().runTaskAsynchronously(this.rosePlugin, runnable);
    }

    @Override
    public List<Class<? extends DataMigration>> getDataMigrations() {
        return List.of(_1_CreateInitialTables.class);
    }

}
