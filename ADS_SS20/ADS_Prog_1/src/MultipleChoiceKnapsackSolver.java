import java.util.List;

public class MultipleChoiceKnapsackSolver {
/*
    // Represents an impossible knapsack.
    private static final int IMPOSSIBLE = Integer.MAX_VALUE;

    // The knapsack's capacity.
    private final int capacity;
    // The groups of items.
    private final ads.set2.knapsack.ItemGroup[] itemGroups;
    // Maximum profit possible.
    private int maxProfit;
    // Our main data table which holds the min-weight knapsacks.
    private int[][] minKnapsacks;

    /**
     * Creates a new solver for the given problem instance.

    private MultipleChoiceKnapsackSolver(final ads.set2.knapsack.ItemGroup[] itemGroups, final int capacity) {
        this.capacity = capacity;
        this.itemGroups = itemGroups;

        init();
    }

    /**
     * Solves the multiple choice knapsack problem using a dynamic programming
     * algorithm based on the one introduced in the lecture.
     *
     * @param itemGroups
     *            list of non-empty item groups. All items have a profit {@code > 0.}
     * @param capacity
     *            the maximum weight allowed for the knapsack {@code >= 0}.
     * @return the maximum profit possible if at most one item of each group may
     *         be packed and the capacity may not be exceeded. If there is no such
     *         packing, {@code 0} is returned.

    public static int pack(final ads.set2.knapsack.ItemGroup[] itemGroups, final int capacity) {
        if (itemGroups.length == 0 || capacity <= 0) {
            return 0;
        } else {
            return new MultipleChoiceKnapsackSolver(itemGroups, capacity).pack();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Initialization

    /**
     * Initializes our main data tables.

    private void init() {
        // To define the size of the data tables we need to know the profit of all items combined
        maxProfit = computeMaxProfit(itemGroups);

        // Allocate space for the data tables. We need maxProfit + 1 rows here
        // because profits should run from 0 to maxProfit (incl)
        minKnapsacks = new int[itemGroups.length][maxProfit + 1];

        // We initialize the first column to IMPOSSIBLE and then check which items from the first
        // group might constitute a valid knapsack (note that there is no need to initialize the
        // table's first row since a zero-profit knapsack is always possible by not putting any
        // item into it)
        for (int p = 1; p <= maxProfit; p++) {
            minKnapsacks[0][p] = IMPOSSIBLE;
        }

        for (Item i : itemGroups[0].getItems()) {
            int p = i.getProfit();
            assert p <= maxProfit;

            // Always take the lightest knapsack
            if (i.getWeight() < minKnapsacks[0][p]) {
                minKnapsacks[0][p] = i.getWeight();
            }
        }
    }

    /**
     * Computes the maximum profit possible. This is the sum over the maximum profit
     * per item group.

    private static int computeMaxProfit(ItemGroup[] itemGroups) {
        int maxProfit = 0;

        for (ItemGroup group : itemGroups) {
            int maxProfitInGroup = 0;

            for (Item item : group.getItems()) {
                maxProfitInGroup = Math.max(maxProfitInGroup, item.getProfit());
            }

            maxProfit += maxProfitInGroup;
        }

        return maxProfit;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Packing

    /**
     * Computes and returns an optimal profit, that is, the maximum profit possible when packing
     * at most one item from each group and staying below the weight limit. If there is no admissible
     * packing, we return {@code 0}.

    private int pack() {
        // Fill the table's columns
        for (int i = 1; i < itemGroups.length; i++) {
            fillColumn(i);
        }

        // Find the best possible profit
        int profit = 0;
        for (int p = maxProfit; p >= 0; p--) {
            if (minKnapsacks[itemGroups.length - 1][p] <= capacity) {
                profit = p;
                break;
            }
        }

        return profit;
    }

    /**
     * Fills column {@code i} of our data tables.

    private void fillColumn(final int i) {
        List<Item> items = itemGroups[i].getItems();

        // Copy knapsacks from the previous column
        assert i > 0;
        for (int p = 1; p <= maxProfit; p++) {
            minKnapsacks[i][p] = minKnapsacks[i - 1][p];
        }

        // Iterate over our items and check whether we can pack better knapsacks
        for (Item item : items) {
            for (int p = item.getProfit(); p <= maxProfit; p++) {
                // Check if there is a previous knapsack we could use as a base
                int possibleBaseWeight = minKnapsacks[i-1][p - item.getProfit()];

                if (possibleBaseWeight != IMPOSSIBLE) {
                    // The weight we could achieve by packing the item into a knapsack of
                    // the previous iteration
                    int possibleWeight = minKnapsacks[i-1][p - item.getProfit()] + item.getWeight();

                    if (possibleWeight < minKnapsacks[i][p]) {
                        minKnapsacks[i][p] = possibleWeight;
                    }
                }
            }
        }
    }
    */
}
