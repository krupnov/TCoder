import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
 
public class Main {
	
	private static int[] indexes;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		InputStreamReader inputStreamReader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		int t = Integer.parseInt(reader.readLine());
		for (int i = 0 ; i < t ; ++i) {
			String[] params = reader.readLine().split(" ");
			int k = Integer.parseInt(params[1]);
			int q = Integer.parseInt(params[2]);
			String s = reader.readLine();
			indexes = new int[s.length()];
			long[] left2right = left2Right(s, k);
			int[] candidates = preprocess(s, k);
			for (int j = 0 ; j < q ; ++j) {
				String[] query = reader.readLine().split(" ");
				int l = Integer.parseInt(query[0]) - 1;
				int r = Integer.parseInt(query[1]) - 1;
				int rightBorder = Arrays.binarySearch(candidates, r);
				if (rightBorder < 0) {
					rightBorder = -1 * rightBorder - 1;
					rightBorder = rightBorder == s.length() ? s.length() - 1 : rightBorder;
				}
				rightBorder = indexes[rightBorder];
				rightBorder = rightBorder > l ? rightBorder : l;
				long n = r - rightBorder + 1;
				long sum = n * (n + 1) / 2;
				if (rightBorder > l) {
					sum += candidates[rightBorder-1] - rightBorder + 2;
					sum += left2right[rightBorder-1];
					sum -= left2right[l];
				}
				System.out.println(sum);
			}
		}
	}
	
	private static int[] preprocess(String s, int k) {
		int[] candidates = new int[s.length()];
		int zeroes = 0;
		int ones = 0; 
		int j = -1;
		for (int i = 0 ; i < s.length() ; ++i) {
			while (j < s.length() - 1) {
				++j;
				if (s.charAt(j) == '0') {
					zeroes++;
				} else {
					ones++;
				}
				if (zeroes > k) {
					zeroes = k;
					--j;
					break;
				}
				if (ones > k) {
					ones = k;
					--j;
					break;
				}
			}
			candidates[i] = j;
			if (i > 0 && candidates[i] == candidates[i-1]) {
				indexes[i] = indexes[i-1];
			} else {
				indexes[i] = i;
			}
			if (s.charAt(i) == '0') {
				zeroes--;
			} else {
				ones--;
			}
		}
		
		return candidates;
	}
 
	private static long[] left2Right(String s, int k) {
		long[] result = new long[s.length()];
		
		long substringsCount = 0;
		int j = -1;
		int zeroes = 0 ;
		int ones = 0;
		
		for (int i = 0 ; i < s.length() ; ++i) {
			result[i] = substringsCount;
			while (j < s.length() - 1) {
				++j;
				if (s.charAt(j) == '0') {
					zeroes++;
				} else {
					ones++;
				}
				
				if (zeroes > k) {
					zeroes = k;
					--j;
					break;
				}
				if (ones > k) {
					ones = k;
					--j;
					break;
				}
			}
			
			if (s.charAt(i) == '0') {
				zeroes--;
			} else {
				ones--;
			}
			substringsCount += (j - i + 1);
		}
		
		return result;
	}
}
