package mc.obliviate.inventory.pagination;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PaginationManager {

	private final Gui gui;
	private final LinkedList<Integer> slots = new LinkedList<>();
	private final LinkedList<Icon> items = new LinkedList<>();
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
	 * @param to big number
	 */
	public void registerPageSlotsBetween(int from, int to) {
		if (from > to) return;
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
	 *
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
	 *
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
	 *
	 * Sets page number to 0
	 *
	 * @return same instance
	 */
	public PaginationManager goFirstPage() {
		this.page = 0;
		return this;
	}

	/**
	 *
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
		if (this.slots.size() == 0) return 0;
		return Math.max(this.items.size() / this.slots.size() - 1, 0);
	}

	/**
	 * Registers new itemstack to paginate. You don't have any limit to register.
	 */
	public void addItem(Icon... icons) {
		this.items.addAll(Arrays.asList(icons));
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
		if (this.page < 0) return; // invalid page

		for (int i = 0; i < this.slots.size(); i++) {
			if (this.items.size() < i + 1) return;
			this.gui.addItem(this.slots.get(i), this.items.get(i + (this.page * this.slots.size())));
		}
	}
}
