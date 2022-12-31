package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class DysfunctionalIcon extends Icon {

    public DysfunctionalIcon(ItemStack item) {
        super(item);
    }

    public DysfunctionalIcon(Material material) {
        super(material);
    }

    @Nonnull
    @Override
    public Icon onDrag(Consumer<InventoryDragEvent> dragAction) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public Icon onClick(Consumer<InventoryClickEvent> clickAction) {
        throw new UnsupportedOperationException();
    }
}
