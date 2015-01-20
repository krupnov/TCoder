package org.coder;

public class SequencePermutation {

	private static final int mod = 1000000009;
	private int[][] mem = null;

	public int determineConfigurations(int N, int M) {
		mem = new int[N + 1][M + 1];
		for (int i = 0 ; i < N + 1 ; ++i) {
			mem[i][0] = 1;
		}
		for (int i = 0 ; i < M + 1 ; ++i) {
			mem[2][i] = 1;
		}
		
		for (int i = 3 ; i <= N ; ++i) {
			mem[i][1] = (mem[i - 1][0] + mem[i -1][1]) % mod;
			for (int j = 2 ; j <= M ; ++j) {
				
				mem[i][j] = ((mem[i][j -1] + mem[i - 1][j]) % mod - (j >= i ? mem[i - 1][j - i] : 0))  % mod;
			}
		}
		
		return mem[N][M] > 0 ? mem[N][M] : mod + mem[N][M];
	}

	public static void main(String[] args) {
		
		SequencePermutation perm = new SequencePermutation();
		
		System.out.println(perm.determineConfigurations(2000, 2000));
	}
}
