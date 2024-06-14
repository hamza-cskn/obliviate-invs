package mc.obliviate.inventory.pagination;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PaginationManager {

    private final Gui gui;
    private final LinkedList<Integer> slots = new LinkedList<>();
    private LinkedList<Icon> items = new LinkedList<>();
    private int page;

    public PaginationManager(Gui gui) {
        this.gui = gui;
    }

    /**
     * @return Registered pagination slots
     */
    public List<Integer> getSlots() {
        return this.slots;
    }

    /**
     * Registers pagination slots
     *
     * @param slots slots to be registered
     */
    public void registerPageSlots(Integer... slots) {
        this.registerPageSlots(Arrays.asList(slots));
    }

    /**
     * Registers pagination slots
     *
     * @param slots slots to be registered
     */
    public void registerPageSlots(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    /**
     * Registers pagination slots between
     * the numbers
     *
     * @param from small number
     * @param to   big number
     */
    public void registerPageSlotsBetween(int from, int to) {
        if (from > to) {
            registerPageSlotsBetween(to, from);
            return;
        }
        for (; from <= to; from++) {
            this.slots.add(from);
        }
    }

    /**
     * Unregisters pagination slots
     */
    public void unregisterAllPageSlots() {
        this.slots.clear();
    }

    /**
     * @return gui of the manager
     */
    public Gui getGui() {
        return this.gui;
    }

    /**
     * @return current page of the pagination
     */
    public int getCurrentPage() {
        return this.page;
    }

    /**
     * @param page new page number (page numbers starts from 0)
     * @return same instance
     */
    public PaginationManager setPage(int page) {
        this.page = page;
        return this;
    }

    /**
     * Increases page number by 1, if current page is not last page.
     *
     * @return same instance
     */
    public PaginationManager goNextPage() {
        if (this.page >= this.getLastPage()) return this;
        this.page += 1;
        return this;
    }

    /**
     * Decreases page number by 1, if current page is not first page.
     *
     * @return same instance
     */
    public PaginationManager goPreviousPage() {
        if (this.page <= 0) return this;
        this.page -= 1;
        return this;
    }

    /**
     * Sets page number to 0
     *
     * @return same instance
     */
    public PaginationManager goFirstPage() {
        this.page = 0;
        return this;
    }

    /**
     * Sets page number to last available page's number.
     *
     * @return same instance
     */
    public PaginationManager goLastPage() {
        this.page = getLastPage();
        return this;
    }

    /**
     * @return true if current page is last page.
     */
    public boolean isLastPage() {
        return this.page == getLastPage();
    }

    /**
     * @return true if current page is 0.
     */
    public boolean isFirstPage() {
        return this.page == 0;
    }

    /**
     * @return calculates last page's number
     */
    public int getLastPage() {
        if (this.slots.isEmpty() || this.items.isEmpty())
            return 0;
        
        int division = (int) Math.floor(this.items.size() / this.slots.size());
    
        if (this.items.size() % this.slots.size() == 0)
            return division - 1;
        return division;
    }

    /**
     * Registers new itemstack to paginate. You don't have any limit to register.
     */
    public void addItem(Icon... icons) {

        this.items.addAll(Arrays.asList(icons));

    }

    /**
     * Clear all items in pagination.
     */
    public void clearAllItems() {

        this.items.clear();

    }

    /**
     * @return All registered itemstacks
     */
    public List<Icon> getItems() {
        return this.items;
    }

    /**
     * Puts icons of the current page.
     */
    public void update() {
        if (this.page < 0) return;

        for (int slotNo = 0; slotNo < this.slots.size(); slotNo++) {
            int itemNo = slotNo + (this.page * this.slots.size());
            if (this.items.size() > itemNo) {
                this.gui.addItem(this.slots.get(slotNo), this.items.get(itemNo));
            } else {
                this.gui.addItem(null, this.slots.get(slotNo));
            }
        }
    }
}
