package team.unnamed.gui.menu.type;

import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Elb1to
 * @since 3/29/2024
 */
public class SlottedMenuInventory<E> extends DefaultMenuInventory {

    private final int entitySlotFrom;
    private final int availableEntitySlots;
    private final List<Integer> availableSlots;
    private final int maxPages;
    private final List<E> entities;
    private final int currentPage;
    private final List<String> layoutLines;
    private final Map<Character, ItemClickable> layoutItems;
    private final Map<Integer, ItemClickable> specificSlotItems;
    private final Function<Integer, ItemClickable> previousPageItem;
    private final Function<Integer, ItemClickable> nextPageItem;
    private final ItemClickable itemIfNoEntities;
    private final ItemClickable itemIfNoPreviousPage;
    private final ItemClickable itemIfNoNextPage;

    protected SlottedMenuInventory(
            String title, int slots,
            List<ItemClickable> items,
            Predicate<Inventory> openAction,
            Predicate<Inventory> closeAction,
            boolean canIntroduceItems,
            boolean canDragItems,
            int entitySlotFrom, int availableEntitySlots,
            List<Integer> availableSlots, List<E> entities, int currentPage, List<String> layoutLines,
            Map<Character, ItemClickable> layoutItems,
            Map<Integer, ItemClickable> specificSlotItems,
            Function<Integer, ItemClickable> previousPageItem,
            Function<Integer, ItemClickable> nextPageItem,
            ItemClickable itemIfNoEntities,
            ItemClickable itemIfNoPreviousPage,
            ItemClickable itemIfNoNextPage
    ) {
        super(title, slots, items, openAction, closeAction, canIntroduceItems, canDragItems);
        this.entitySlotFrom = entitySlotFrom;
        this.availableEntitySlots = availableEntitySlots;
        this.availableSlots = availableSlots;
        this.specificSlotItems = specificSlotItems;
        this.maxPages = (int) Math.ceil(entities.size() / (double) availableEntitySlots);
        this.entities = entities;
        this.currentPage = currentPage;
        this.layoutLines = layoutLines;
        this.layoutItems = layoutItems;
        this.previousPageItem = previousPageItem;
        this.nextPageItem = nextPageItem;
        this.itemIfNoEntities = itemIfNoEntities;
        this.itemIfNoPreviousPage = itemIfNoPreviousPage;
        this.itemIfNoNextPage = itemIfNoNextPage;
    }

    public int getEntitySlotFrom() {
        return entitySlotFrom;
    }

    public List<Integer> getAvailableSlots() {
        return availableSlots;
    }

    public int getAvailableEntitySlots() {
        return availableEntitySlots;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<E> getEntities() {
        return entities;
    }

    public Map<Integer, ItemClickable> getSpecificSlotItems() {
        return specificSlotItems;
    }

    public List<String> getLayoutLines() {
        return layoutLines;
    }

    public Map<Character, ItemClickable> getLayoutItems() {
        return layoutItems;
    }

    public Function<Integer, ItemClickable> getPreviousPageItem() {
        return previousPageItem;
    }

    public Function<Integer, ItemClickable> getNextPageItem() {
        return nextPageItem;
    }

    public ItemClickable getItemIfNoEntities() {
        return itemIfNoEntities;
    }

    public ItemClickable getItemIfNoPreviousPage() {
        return itemIfNoPreviousPage;
    }

    public ItemClickable getItemIfNoNextPage() {
        return itemIfNoNextPage;
    }

    public SlottedMenuInventory<E> clone(int page) {
        return new SlottedMenuInventory<>(
                title, slots, items, openAction, closeAction, canIntroduceItems,
                canDragItems, entitySlotFrom, availableEntitySlots, availableSlots,
                entities, page, layoutLines, layoutItems, specificSlotItems,
                previousPageItem, nextPageItem, itemIfNoEntities,
                itemIfNoPreviousPage, itemIfNoNextPage
        );
    }
}
