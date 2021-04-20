/**
 * An item that can be packed into a knapsack. Or not.
 */
public class Item {

	/** Profit gained by packing this item into the knapsack. */
	private final int profit;
	/** Weight of this item. */
	private final int weight;

	/**
	 * Creates a new instance with the given profit and weight.
	 * 
	 * @param profit
	 *            the item's profit.
	 * @param weight
	 *            the item's weight.
	 * @throws IllegalArgumentException
	 *             if any of the two parameters is negative.
	 */
	public Item(final int profit, final int weight) {
		if (profit < 0) {
			throw new IllegalArgumentException("profit cannot be negative.");
		}

		if (weight < 0) {
			throw new IllegalArgumentException("weight cannot be negative.");
		}

		this.profit = profit;
		this.weight = weight;
	}

	/**
	 * Returns the profit this item contributes when included in the knapsack.
	 */
	public int getProfit() {
		return profit;
	}

	/**
	 * Returns the weight of this item.
	 */
	public int getWeight() {
		return weight;
	}

}
