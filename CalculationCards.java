import java.util.ArrayList;

public class CalculationCards {

	public double getExpected(String[] cards) {
		ArrayList<Integer> simple = new ArrayList<Integer>();
		ArrayList<Integer> multi = new ArrayList<Integer>();

		for (String card : cards) {
			if (card.charAt(0) == '+') {
				simple.add(card.charAt(1) - '0');
			}
			if (card.charAt(0) == '-') {
				simple.add('0' - card.charAt(1));
			}
			if (card.charAt(0) == '*') {
				multi.add(card.charAt(1) - '0');
			}
		}
		
		double sum = 0;
		
		for (Integer value : simple) {
			sum += value;
		}
		
		int size = simple.size();
		
		for (Integer value : multi) {
			sum = sum * (value + size) / (size + 1);
		}
		
		return sum;
	}
}
