package topcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Substrings {
	
	
	public static int main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int t = Integer.parseInt(reader.readLine());
		for (int i = 0 ; i < t ; ++i) {
			String[] params = reader.readLine().split(" ");
			int k = Integer.parseInt(params[1]);
			int q = Integer.parseInt(params[2]);
			String s = reader.readLine();
			int[] candidates = preprocess(s, k);
			for (int j = 0 ; j < q ; ++j) {
				String[] query = reader.readLine().split(" ");
				int l = Integer.parseInt(query[0]) - 1;
				int r = Integer.parseInt(query[1]) - 1;
				System.out.println(countQuery(candidates, l, r));
			}
		}
		return 0;
	}

	private static int countQuery(int[] candidates, int l, int r) {
		int result = 0;
		for (int i = l ; i <= r ; ++i) {
			int variants = 0;
			if (r < candidates[i]) {
				variants = r;
			} else {
				variants = candidates[i];
			}
			result += variants - i + 1;
		}
		return result;
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
			if (s.charAt(i) == '0') {
				zeroes--;
			} else {
				ones--;
			}
		}
		
		return candidates;
	}
}
