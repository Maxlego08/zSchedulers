package fr.maxlego08.zscheduler.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DistantPlaceholder extends PlaceholderExpansion {

    private final LocalPlaceholder placeholder;

    /**
     * @param placeholder
     */
    public DistantPlaceholder(LocalPlaceholder placeholder) {
        super();
        this.placeholder = placeholder;
    }

    @Override
    public String getAuthor() {
        return this.placeholder.getPlugin().getDescription().getAuthors().get(0);
    }

    @Override
    public String getIdentifier() {
        return this.placeholder.getPrefix();
    }

    @Override
    public String getVersion() {
        return this.placeholder.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String params) {
        return this.placeholder.onRequest(p, params);
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return this.placeholder.onRequest(null, params);
    }
}
