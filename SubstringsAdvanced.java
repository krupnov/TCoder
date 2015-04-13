package topcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SubstringsAdvanced {
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		InputStreamReader inputStreamReader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		int t = Integer.parseInt(reader.readLine());
		for (int i = 0 ; i < t ; ++i) {
			String[] params = reader.readLine().split(" ");
			int k = Integer.parseInt(params[1]);
			int q = Integer.parseInt(params[2]);
			String s = reader.readLine();
			int[] left2right = left2Right(s, k);
			int[] right2Left = right2Left(s, k);
			for (int j = 0 ; j < q ; ++j) {
				String[] query = reader.readLine().split(" ");
				int l = Integer.parseInt(query[0]) - 1;
				int r = Integer.parseInt(query[1]) - 1;
				System.out.println(left2right[s.length() - 1] - left2right[l] - right2Left[r] + 1);
			}
		}
	}
	
	private static int[] right2Left(String s, int k) {
		int[] result = new int[s.length()];
		
		int substringsCount = 0;
		int j = s.length();
		int zeroes = 0 ;
		int ones = 0;
		
		for (int i = s.length() - 1 ; i >= 0 ; --i) {
			result[i] = substringsCount;
			while (j > 0) {
				--j;
				if (s.charAt(j) == '0') {
					zeroes++;
				} else {
					ones++;
				}
				
				if (zeroes > k) {
					zeroes = k;
					++j;
					break;
				}
				if (ones > k) {
					ones = k;
					++j;
					break;
				}
			}
			
			if (s.charAt(i) == '0') {
				zeroes--;
			} else {
				ones--;
			}
			substringsCount += i - j + 1;
		}
		
		return result;
	}

	private static int[] left2Right(String s, int k) {
		int[] result = new int[s.length()];
		
		int substringsCount = 0;
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
