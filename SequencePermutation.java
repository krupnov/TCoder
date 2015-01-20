package org.coder;

624672242

19154

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
			for (int j = 1 ; j <= M ; ++j) {
				for (int k = 0 ; k <= j && k <= i -1 ; ++k) {
					mem[i][j] = (mem[i][j] + mem[i - 1][j - k]) % mod;
				}
			}
		}
		
		return mem[N][M];
	}
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		SequencePermutation perm = new SequencePermutation();
		
		System.out.println(perm.determineConfigurations(2000, 2000));
		long endTime = System.nanoTime();
		
		System.out.println((endTime - startTime) / 1000000);
	}
}
