package team.unnamed.gui.menu.util;

import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.item.action.ItemClickableAction;
import team.unnamed.gui.menu.type.SlottedMenuInventory;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Elb1to
 * @since 3/29/2024
 */
public final class SlottedMenuUtil {

    private SlottedMenuUtil() {
        throw new UnsupportedOperationException();
    }

    public static <E> Inventory createPage(Inventory delegate, SlottedMenuInventory<E> menuInventory) {
        int currentPage = menuInventory.getCurrentPage();
        Map<Character, ItemClickable> layoutItems = menuInventory.getLayoutItems();
        Map<Integer, ItemClickable> specificSlotItems = menuInventory.getSpecificSlotItems();
        int currentSlot = 0;

        specificSlotItems.forEach((key, item) -> {
            delegate.setItem(key, item.getItemStack());
            menuInventory.setItem(item);
        });

        for (String layoutLine : menuInventory.getLayoutLines()) {
            for (char c : layoutLine.toCharArray()) {
                ItemClickable itemClickable = null;

                switch (c) {
                    case 'n' -> itemClickable = getInteractPageItem(
                            currentPage < menuInventory.getMaxPages(),
                            currentPage, currentPage + 1, currentSlot,
                            menuInventory, menuInventory.getNextPageItem(),
                            menuInventory.getItemIfNoNextPage()
                    );
                    case 'p' -> itemClickable = getInteractPageItem(
                            currentPage > 1,
                            currentPage, currentPage - 1, currentSlot,
                            menuInventory, menuInventory.getPreviousPageItem(),
                            menuInventory.getItemIfNoPreviousPage()
                    );
                    default -> {
                        ItemClickable layoutItem = layoutItems.get(c);
                        if (layoutItem != null) {
                            itemClickable = layoutItem.clone(currentSlot);
                        }
                    }
                }

                if (itemClickable != null) {
                    delegate.setItem(itemClickable.getSlot(), itemClickable.getItemStack());
                    menuInventory.setItem(itemClickable);
                }

                currentSlot++;
            }
        }

        return delegate;
    }

    private static ItemClickable getInteractPageItem(
            boolean expression, int currentPage, int newPage,
            int currentSlot,
            SlottedMenuInventory<?> menuInventory,
            Function<Integer, ItemClickable> pageItem,
            ItemClickable orElseItem
    ) {
        ItemClickable itemClickable = null;
        if (expression) {
            itemClickable = pageItem.apply(currentPage)
                    .clone(currentSlot)
                    .clone(ItemClickableAction.single(event -> {
                        menuInventory.clearItems();
                        Inventory inventory = event.getClickedInventory();
                        inventory.clear();
                        createPage(inventory, menuInventory.clone(newPage));
                        return true;
                    }));
        } else {
            if (orElseItem != null) {
                itemClickable = orElseItem.clone(currentSlot);
            }
        }

        return itemClickable;
    }
}
