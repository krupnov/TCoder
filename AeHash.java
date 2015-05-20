package coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AeHash {
	
	private static final int mod = 1000000007;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T ; ++t) {
			String[] aev = reader.readLine().split(" ");
			int A = Integer.parseInt(aev[0]);
			int E = Integer.parseInt(aev[1]);
			int V = Integer.parseInt(aev[2]);
			
			int[][][] results = compute(A, E, V);
			
			System.out.println(results[A][E][V]);
		}
	}

	private static int[][][] compute(int A, int E, int V) {
		int[][][] memo = new int[A + 1][E + 1][V + 1];
		
		for (int e = 0 ; e < E + 1 ; ++e) {
			memo[0][e][0] = 1;
		}
		int vSum = 0;
		for (int a = 1 ; a <= A ; a *= 2) {
			vSum += a;
			if (vSum > V) {
				break;
			}
			memo[a][0][vSum] = 1;
		}
		
		for (int a = 1 ; a < A + 1 ; ++a) {
			for (int e = 1 ; e < E + 1 ; ++e) {
				for (int v = a ; v < V + 1 ; ++v) {
					memo[a][e][v] = next(a, v, e, memo);
				}
			}
		}
		
		return memo;
	}

	private static int next(int A, int V, int E, int[][][] memo) {
		int length = A + E;
		int firstHalf = length / 2 + (length % 2);
		int secondHalf = length / 2;
		int result = 0;
		for (int a = Math.max(0, firstHalf - E) ; a <= A && a <= firstHalf ; ++a) {
			int previous = result;
			for (int v = 0 ; v <= V - A ; ++v) {
				result = (result + memo[a][firstHalf - a][V - A] * memo[A - a][secondHalf - A + a][v]) % mod;
			}
			if (previous != result) {
				System.out.println("First half " + A + " - " + E + " - " + V + " : " + (result - previous));
			}
		}
		for (int a = Math.max(0, secondHalf - E) ; a <= A && a <= secondHalf ; ++a) {
			int previous = result;
			for (int v = 0 ; v < V - A ; ++v) {
				result = (result + memo[a][secondHalf - a][V - A] * memo[A - a][firstHalf - A + a][v]) % mod;
			}
			if (previous != result) {
				System.out.println("Second half " + A + " - " + E + " - " + V  + " : " + (result - previous));
			}
		}
		return result;
	}
}
