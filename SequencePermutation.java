package org.coder;

public class SequencePermutation {

	private static final int mod = 1000000009;
	
	private Integer[][] mem = null;

	public int determineConfigurations(int N, int M) {
		mem = new Integer[N + 1][M + 1];
		for (int i = 0 ; i < N + 1 ; ++i) {
			mem[i][0] = 1;
		}
		for (int i = 0 ; i <M + 1 ; ++i) {
			mem[2][i] = 1;
		}
		return f(N, M);
	}
	
	private int f(int N, int M) {
		
		if (mem[N][M] != null) {
			return mem[N][M];
		}
		
		int result = 0;
		for (int i = 0 ; i <= M && i <= N - 1 ; ++i) {
			result = (result + f(N - 1, M - i)) % mod;
		}
		
		mem[N][M] = result;
		return result;
	}
	
	public static void main(String[] args) {
		
		SequencePermutation perm = new SequencePermutation();
		
		System.out.println(perm.determineConfigurations(2000, 2000));
	}
}