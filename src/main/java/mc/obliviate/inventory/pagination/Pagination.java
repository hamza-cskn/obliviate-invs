package mc.obliviate.inventory.pagination;

import mc.obliviate.inventory.GUI;
import mc.obliviate.inventory.Icon;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Pagination {

	private final GUI gui;
	private final LinkedList<Integer> slots = new LinkedList<>();
	private final LinkedList<Icon> items = new LinkedList<>();
	private int page;

	public Pagination(GUI gui) {
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
	public void registerItemSlots(Integer... ints) {
		slots.addAll(Arrays.asList(ints));
	}

	/**
	 * Registers pagination slots between
	 * the numbers
	 */
	public void registerSlotsBetween(int from, int to) {
		if (from > to) return;
		for (; from <= to; from++) {
			slots.add(from);
		}
	}

	/**
	 * Unregisters pagination slots
	 */
	public void unregisterAllSlots() {
		slots.clear();
	}

	public GUI getGui() {
		return gui;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void nextPage() {
		page += 1;
	}

	public void previousPage() {
		page -= 1;
	}

	public void firstPage() {
		page = 0;
	}

	public void lastPage() {
		page = getLastPage();
	}

	public boolean isLastPage() {
		return page == getLastPage();
	}

	public boolean isFirstPage() {
		return page == 0;
	}

	public int getLastPage() {
		int m = (int) Math.ceil((double) items.size() / slots.size()) - 1;
		return m != -1 ? m : 0;
	}

	public void addIcon(Icon... icons) {
		items.addAll(Arrays.asList(icons));
	}

	public List<Icon> getItems() {
		return items;
	}

	public void update() {

		if (page < 0) return;
		if (items.size() < (slots.size() + 1)) return;

		for (int i = 0; i < slots.size(); i++) {
			if (items.size() < i + 1) return;
			gui.addItem(slots.get(i), items.get(i + (page * slots.size())));
		}
	}
}
