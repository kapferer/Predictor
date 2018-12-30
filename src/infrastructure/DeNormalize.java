package infrastructure;

/**
 * This classe provides the method for denormalization of data. It is needed for
 * backtransformation of predicted results.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class DeNormalize {

	/**
	 * The denormalization method.
	 * 
	 * @param toDeNormalize
	 * @param minimum
	 * @param maximum
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	double denormalize(double toDeNormalize, double minimum, double maximum, double lowerBound, double upperBound) {

		double denormalized;

		denormalized = ((maximum - minimum) / (upperBound - lowerBound)) * (toDeNormalize)
				- ((maximum - minimum) / (upperBound - lowerBound)) * lowerBound + minimum;

		return denormalized;
	}

}
