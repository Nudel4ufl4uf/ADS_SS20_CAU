import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MedianSelectorTest {
	
	@Test
	public void testInvalidInput() {
		assertThrows(
				IllegalArgumentException.class,
				() -> MedianSelector.lowerMedian(null),
				"Your method doesn't throw the expected exception when we pass null.");
		
		assertThrows(
				IllegalArgumentException.class,
				() -> MedianSelector.lowerMedian(new int[0]),
				"Your method doesn't throw the expected exception when we pass an empty array.");
	}
	
	@Test
	public void testExtremelySmallArray() {
		performTests(new TestData(42, 42));
	}
	
	@Test
	public void testRatherSmallArray() {
		performTests(new TestData(42, 73, 42));
	}
	
	@Test
	public void testLectureExample() {
		performTests(new TestData(7, 3, 6, 9, 2, 10, 4, 5, 7, 1, 0, 11, 15, 13, 14, 12));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {20, 21, 22, 23, 24, 25})
	public void testRandomArrayOfLength(int length) {
		performTests(TestData.random(length));
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Actual Test Running

	private void performTests(TestData instance) {
		// Run algorithm
		int studentSolution = MedianSelector.lowerMedian(instance.copyData());
		
		// Check if the solution is correct
		assertEquals(instance.solution, studentSolution,
				"Lower median is incorrect for the following array:\n\n" + instance);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Test Data

	/**
	 * Encapsulates test data for a single test.
	 */
	private static class TestData {
		
		/** The array we're testing. */
		private final int[] data;
		/** The actual array item we're looking for. */
		private final int solution;
		
		/**
		 * Creates a new instance with the given data.
		 */
		private TestData(int solution, int... data) {
			this.solution = solution;
			this.data = data;
		}
		
		/**
		 * Creates random test data with an array of the given length
		 */
		private static TestData random(int length) {
			int[] array = new int[length];
			
			// Take a random start value between 1 and 4096
			int value = (int) (Math.random() * 4096) + 1;
			
			// Fill the array with numbers with a flexible step width
			for (int i = 0; i < length; ++i) {
				array[i] = value;
				value += (int) (Math.random() * 9 + 1);
			}
			
			// Select an item we're looking for and store its index and value
			int result = array[(array.length - 1) / 2];
			
			// Shuffle the data
			shuffleArray(array);

			return new TestData(result, array);
		}

		/**
		 * Shuffles the entries in an array, using the Durstenfeld shuffle.
		 */
		private static void shuffleArray(int[] array) {
			// Initialise source of entropy
			Random rnd = ThreadLocalRandom.current();
			for (int i = array.length - 1; i > 0; i--) {
				int index = rnd.nextInt(i + 1);
				
				// Simple swap
				int a = array[index];
				array[index] = array[i];
				array[i] = a;
			}
		}
		
		/**
		 * Returns a copy of our data array that can be fed to the student solution.
		 */
		public int[] copyData() {
			return Arrays.copyOf(data, data.length);
		}
		
		@Override
		public String toString() {
			return Arrays.toString(data);
		}
		
	}

}
