package team.unnamed.gui.menu.type;

import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.util.MenuUtil;
import team.unnamed.gui.menu.util.SlottedMenuUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static team.unnamed.validate.Validate.isNotNull;

/**
 * @author Elb1to
 * @since 3/29/2024
 */
public class SlottedMenuInventoryBuilder<E> extends StringLayoutMenuInventoryBuilder {

    private int entitySlotFrom;
    private int entitySlotTo;
    private Iterable<Integer> skippedSlots;
    private int itemsPerRow;
    private List<E> entities;
    private Map<Integer, ItemClickable> specificSlotItems = new HashMap<>();
    private Function<Integer, ItemClickable> previousPageItem;
    private Function<Integer, ItemClickable> nextPageItem;
    private ItemClickable itemIfNoEntities;
    private ItemClickable itemIfNoPreviousPage;
    private ItemClickable itemIfNoNextPage;

    protected SlottedMenuInventoryBuilder(String title) {
        super(title);
    }

    protected SlottedMenuInventoryBuilder(String title, int rows) {
        super(title, rows);
    }

    public SlottedMenuInventoryBuilder<E> bounds(int entitySlotFrom, int entitySlotTo) {
        this.entitySlotFrom = entitySlotFrom;
        this.entitySlotTo = entitySlotTo;
        return this;
    }

    public SlottedMenuInventoryBuilder<E> itemsPerRow(int itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
        return this;
    }

    public SlottedMenuInventoryBuilder<E> skippedSlots(Iterable<Integer> skippedSlots) {
        this.skippedSlots = skippedSlots;
        return this;
    }

    public SlottedMenuInventoryBuilder<E> skippedSlots(Integer... skippedSlots) {
        this.skippedSlots = Arrays.asList(skippedSlots);
        return this;
    }

    public SlottedMenuInventoryBuilder<E> entities(Collection<E> entities) {
        this.entities = new ArrayList<>(entities);
        return this;
    }

    public SlottedMenuInventoryBuilder<E> specificSlotItems(Map<Integer, ItemClickable> specificSlotItems) {
        this.specificSlotItems = specificSlotItems;
        return this;
    }

    public SlottedMenuInventoryBuilder<E> nextPageItem(Function<Integer, ItemClickable> nextPageItem) {
        this.nextPageItem = nextPageItem;
        return this;
    }

    public SlottedMenuInventoryBuilder<E> previousPageItem(Function<Integer, ItemClickable> previousPageItem) {
        this.previousPageItem = previousPageItem;
        return this;
    }

    public SlottedMenuInventoryBuilder<E> itemIfNoEntities(ItemClickable itemIfNoEntities) {
        this.itemIfNoEntities = isNotNull(itemIfNoEntities, "Item if no entities cannot be null.");
        return this;
    }

    public SlottedMenuInventoryBuilder<E> itemIfNoPreviousPage(ItemClickable itemIfNoPreviousPage) {
        this.itemIfNoPreviousPage = isNotNull(itemIfNoPreviousPage, "Item if no previos page cannot be null.");
        return this;
    }

    public SlottedMenuInventoryBuilder<E> itemIfNoNextPage(ItemClickable itemIfNoNextPage) {
        this.itemIfNoNextPage = isNotNull(itemIfNoNextPage, "Item if no next page cannot be null.");
        return this;
    }

    @Override
    public Inventory build() {
        isNotNull(entities, "Entities cannot be null.");
        isNotNull(nextPageItem, "Next page item cannot be null.");
        isNotNull(previousPageItem, "Previous page item cannot be null.");

        int nextIncrement = 9 - itemsPerRow;
        List<Integer> availableSlots = new ArrayList<>();
        int itemsPerRowCounter = 0;

        for (int i = entitySlotFrom; i <= entitySlotTo + 1; i++) {
            itemsPerRowCounter++;

            boolean isSkippedSlot = false;

            if (skippedSlots != null) {
                for (Integer skippedSlot : skippedSlots) {
                    if (i == skippedSlot) {
                        isSkippedSlot = true;
                        break;
                    }
                }
            }

            if (!isSkippedSlot) {
                availableSlots.add(i);
            }

            if (itemsPerRowCounter == itemsPerRow) {
                itemsPerRowCounter = 0;
                i += nextIncrement;
            }
        }

        SlottedMenuInventory<E> slottedMenuInventory = new SlottedMenuInventory<>(
                title, slots, items, openAction, closeAction, canIntroduceItems,
                canDragItems, entitySlotFrom, availableSlots.size(), availableSlots,
                entities, 1, layoutLines, layoutItems, specificSlotItems,
                previousPageItem, nextPageItem, itemIfNoEntities,
                itemIfNoPreviousPage, itemIfNoNextPage
        );

        Inventory inventory = MenuUtil.parseToInventory(slottedMenuInventory);
        return SlottedMenuUtil.createPage(inventory, slottedMenuInventory);
    }

    @Override
    protected SlottedMenuInventoryBuilder<E> back() {
        return this;
    }
}
