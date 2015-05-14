package topcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LittleElephant {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T ; ++t) {
			String[] NM = reader.readLine().split(" ");
			int N = Integer.parseInt(NM[0]);
			int M = Integer.parseInt(NM[1]);
			
			short[][] A = new short[N][M];
			
			for (int i = 0 ; i < N ; ++i) {
				String nextLine = reader.readLine();
				for (int j = 0 ; j < M ; ++j) {
					A[i][j] =(short) Character.getNumericValue(nextLine.charAt(j));
				}
			}
			short result = 0;
			for (int p = 0 ; p < 10 ; ++p) {
				short[][][][] maxPathes = computeMaxPathes(A, N, M, p);
				short localMaximum = 0;
				for (int i = 0 ; i < 4 ; ++i) {
					if (localMaximum < maxPathes[0][i][0][0]) {
						localMaximum = maxPathes[0][i][0][0];
					}
				}
				if (localMaximum > result) {
					result = localMaximum;
				}
			}
			System.out.println(result);
		}
	}

	private static short[][][][] computeMaxPathes(short[][] A, int N, int M, int p) {
		short[][][][] maxPathes = new short [10][4][N][M];
		
		return maxPathes;
	}
}
