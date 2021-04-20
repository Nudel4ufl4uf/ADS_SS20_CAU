import java.awt.*;
import java.util.Collections;

/**
 * A group of items. At most one item of each group can be included in the
 * knapsack.
 */
public class ItemGroup {

	/** The items in this group. */
	private final java.util.List<Item> items;

	/**
	 *
	 * Creates a new group that contains the specified items.
	 *
	 * @param theItems
	 *            list of items to group.
	 */
	public ItemGroup(final java.util.List<Item> theItems) {
		// Wrap the list in an unmodifiable list to prevent people from
		// changing it
		items = Collections.unmodifiableList(theItems);
	}

	/**
	 * Returns the list of items in this group. Any attempts to change the list will
	 * cause an {@link UnsupportedOperationException}, so don't bother.
	 * 
	 * @return the list of items.
	 */
	public List<Item> getItems() {
		return items;
	}

}
