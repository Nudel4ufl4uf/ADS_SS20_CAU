import java.util.ArrayList;

public class MedianSelector {

    /**
     * Computes and retrieves the lower median of the given array of pairwise
     * distinct numbers using the Median algorithm presented in the lecture.
     *
     * @param numbers array with pairwise distinct numbers.
     * @return the lower median.
     * @throw IllegalArgumentException if the array is {@code null} or empty.
     */
    public static int lowerMedian(int[] numbers) {

        // Check whether the number field does not exist or is empty
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException();
        }

        // Check if the number field only contains one number so it is the median itself
        if (numbers.length == 1) {
            return numbers[0];
        }
        return getMedian(numbers, (numbers.length + 1) / 2);
    }


    /**
     * The method "getMedian" calculates the value of the given median.
     *
     * @param numbers   the field to sort in.
     * @param medianPos the position of the field
     * @return the value of the median
     */
    public static int getMedian(int[] numbers, int medianPos) {
        // check if the given field is part of divided fields of length 5.
        if (numbers.length > 5) {
            //amount of fields of size five in the whole field.
            int numberOfFields = 0;

            // calculated numbers of fields to create.
            if (numbers.length % 5 != 0) {
                numberOfFields = (numbers.length / 5) + 1;
            } else {
                numberOfFields = (numbers.length / 5);
            }

            // field for the medians.
            int[] medianOfMedians = new int[numberOfFields];
            // field for the groups.
            int[] five = new int[5];
            // field for the rest numbers.
            int[] rest = new int[numbers.length % 5];
            // index for filling medianOfMedians field.
            int indexMediansOfMedians = 0;
            // index for the five field.
            int indexGroups = 0;

            // split numbers field into groups of five.
            for (int i = 0; i < numbers.length; i++) {

                if ((i % 5 == 0 && i != 0) || i == numbers.length - 1) {
                    //case for the last element in the list because it has to be copied separately.
                    if (i == numbers.length - 1 && rest.length == 0) {
                        five[indexGroups] = numbers[i];
                    }

                    // save the median of the group in the field for medians of medians.
                    medianOfMedians[indexMediansOfMedians] = lowerMedian(five);
                    // raise the index of the Medians.
                    indexMediansOfMedians += 1;
                    // reset index of "five" because new group starts.
                    indexGroups = 0;

                    // case for the field of the rest elements lower than five.
                    if ((numbers.length) - i == rest.length) {

                        // fill the rest field separately
                        for (int k = 0; k < rest.length; k++) {
                            rest[k] = numbers[i];
                            i += 1;
                        }
                        // save the median of the field rest.
                        medianOfMedians[indexMediansOfMedians] = lowerMedian(rest);
                    } else {
                        // get the first element for the group because of the current i for the calculating step.
                        five[indexGroups] = numbers[i];
                        indexGroups += 1;
                    }
                } else {
                    // copy the current element of numbers to group.
                    five[indexGroups] = numbers[i];
                    indexGroups += 1;
                }
            }

            //save the median of medians.
            int median = lowerMedian(medianOfMedians);
            //the List of element lower than the median element.
            ArrayList<Integer> L1 = createSplit(numbers, median, 0);
            //the List of element higher than the median element.
            ArrayList<Integer> L2 = createSplit(numbers, median, 1);
            //the current position of the median
            int k = L1.size() + 1;

            //check if the current median element is already in the correct position.
            if (medianPos == k) {
                return median;
            }
            //check if the current median element position is higher than the correct position and check the list of lower elements on the position of the median.
            else if (medianPos < k) {
                int[] numbers2 = convertIntegers(L1);
                int x = getMedian(numbers2, medianPos);
                return x;
            }
            //check if the current median element position is lower than the correct position and check the list of lower elements on the position median  minus the current.
            else if (medianPos > k) {
                int[] numbers2 = convertIntegers(L2);
                int x = getMedian(numbers2, medianPos - k);
                return x;
            }
        }
        // set the upper border for the field to sort
        int borderUp = numbers.length;
        // set the lower border for the field to sort
        int borderDown = 0;

        numbers = sort(numbers, borderDown, borderUp - 1);
        return numbers[medianPos - 1];
    }


    /**
     * The method "swap" swaps to integers in an array with the given positions.
     *
     * @param numbers field to swap in
     * @param i1      Integer 1
     * @param i2      Integer 2
     */
    public static void swap(int[] numbers, int i1, int i2) {
        int temp = numbers[i1];
        numbers[i1] = numbers[i2];
        numbers[i2] = temp;
    }

    /**
     * This method "createSplit" splits the given Field into to List with numbers bigger the pivot and lower the pivot.
     *
     * @param numbers    The field tom split
     * @param valuePivot The value of the medianOfMendians
     * @param direction
     * @return
     */
    public static ArrayList<Integer> createSplit(int[] numbers, int valuePivot, int direction) {
        //The list for the elements higher than the value of the pivot.
        ArrayList<Integer> higher = new ArrayList<Integer>();
        //The list for the elements lower than the value of the pivot.
        ArrayList<Integer> lower = new ArrayList<Integer>();

        //Check the demanded list: 0 = all elements higher in one list; 1= all elements lower in one list.
        if (direction == 1) {
            for (int i = 0; i < numbers.length; i++) {
                if (numbers[i] > valuePivot) {
                    higher.add(numbers[i]);
                }
            }
            return higher;
        }
        //Check the demanded list: 0 = all elements higher in one list; 1= all elements lower in one list.
        if (direction == 0) {
            for (int i = 0; i < numbers.length; i++) {
                if (numbers[i] < valuePivot) {
                    lower.add(numbers[i]);
                }
            }
            return lower;
        }
        return lower;
    }

    /**
     * The implementation of the sorting algorithm quicksort with O(n).
     *
     * @param A The field to sort
     * @param l left border
     * @param r right border
     * @return the sorted field
     */
    public static int[] sort(int[] A, int l, int r) {
        int x, j, i, temp;
        if (l < r) {
            x = A[l];i = l + 1;j = r;
            while (i <= j) {
                while (i <= j && A[i] <= x) {
                    i += 1;
                }
                while (i <= j && A[j] >= x) {
                    j -= 1;
                }
                if (i < j) {
                    temp = A[i];A[i] = A[j];A[j] = temp;
                }
            }
            i = i - 1;A[l] = A[i];A[i] = x;
            sort(A, l, i - 1);sort(A, i + 1, r);
        }
        return A;
    }

    /**
     * A method to convert an ArrayList into a field.
     *
     * @param integers The ArrayList
     * @return the converted field.
     */
    public static int[] convertIntegers(ArrayList<Integer> integers) {
        //create a field to convert in the list.
        int[] ret = new int[integers.size()];

        //go through the List and copy the elements.
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}

