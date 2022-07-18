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

	public List<Integer> getSlots() {
		return slots;
	}

	/**
	 * Registers pagination slots
	 *
	 * @param ints slots to be registered
	 */
	public void registerPageSlots(Integer... ints) {
		slots.addAll(Arrays.asList(ints));
	}

	/**
	 * Registers pagination slots between
	 * the numbers
	 */
	public void registerPageSlotsBetween(int from, int to) {
		if (from > to) return;
		for (; from <= to; from++) {
			slots.add(from);
		}
	}

	/**
	 * Unregisters pagination slots
	 */
	public void unregisterAllPageSlots() {
		slots.clear();
	}

	public Gui getGui() {
		return gui;
	}

	public int getCurrentPage() {
		return page;
	}

	public PaginationManager setPage(int page) {
		this.page = page;
		return this;
	}

	public PaginationManager openNextPage() {
		page += 1;
		return this;
	}

	public PaginationManager openPreviousPage() {
		page -= 1;
		return this;
	}

	public PaginationManager goFirstPage() {
		page = 0;
		return this;
	}

	public PaginationManager goLastPage() {
		page = getLastPage();
		return this;
	}

	public boolean isLastPage() {
		return page == getLastPage();
	}

	public boolean isFirstPage() {
		return page == 0;
	}

	public int getLastPage() {
		if (slots.size() == 0) return 0;
		return Math.max(items.size() / slots.size() - 1, 0);
	}

	public void addItem(Icon... icons) {
		items.addAll(Arrays.asList(icons));
	}

	public List<Icon> getItems() {
		return items;
	}

	public void update() {
		if (page < 0) return; // invalid page

		for (int i = 0; i < slots.size(); i++) {
			if (items.size() < i + 1) return;
			gui.addItem(slots.get(i), items.get(i + (page * slots.size())));
		}
	}
}
