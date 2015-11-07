package de.othr.mocker;

/**
 * Wrapper class to check whether a method was invoked exactly or at most/least
 * `i` times on a mocked object. 
 * 
 * @author Michael Neu
 */
final class RepeatCount {
	public static enum Operator { EQUALS, ATMOST, ATLEAST };
	
	private final int count;
	private final Operator operator;
	
	/**
	 * Initialize the count wrapper
	 * 
	 * @param count How often the method should have been called
	 */
	public RepeatCount(int count) {
		this(count, Operator.EQUALS);
	}
	

	/**
	 * Initialize the count wrapper
	 * 
	 * @param count How often the method should have been called
	 * @param operator How to compare a given value to the count
	 */
	public RepeatCount(int count, Operator operator) {
		this.count = count;
		this.operator = operator;
	}
	
	/**
	 * Compare the given value to the count of this object using the given
	 * compare operator.  
	 * 
	 * @param value How often the method was called
	 * @return Whether this matches the desired count
	 */
	public boolean matchesCount(int value) {
		switch (this.operator) {
			case ATMOST:
				return value <= this.count;
				
			case ATLEAST:
				return value >= this.count;
	
			default:
				return value == this.count;
		}
	}
	
	@Override
	public String toString() {
		return this.operator.name() + "(" + this.count + ")";
	}

	/**
	 * Get the defined count. 
	 * 
	 * @return
	 */
	public int getCount() {
		return this.count;
	}
}
