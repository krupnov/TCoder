package topcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
 
public class LittleElephant {
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T ; ++t) {
			String[] NM = reader.readLine().split(" ");
			int N = Integer.parseInt(NM[0]);
			int M = Integer.parseInt(NM[1]);
			
			int[][] A = new int[N][M];
			
			for (int i = 0 ; i < N ; ++i) {
				String nextLine = reader.readLine();
				for (int j = 0 ; j < M ; ++j) {
					A[i][j] = Character.getNumericValue(nextLine.charAt(j));
				}
			}
			int result = 0;
			for (int p = 0 ; p < 10 ; ++p) {
				int localMaximum = 0;
				int[][][][] pp = computeMaxPathes(A, N, M, p);
				for (int i = 0 ; i < 4 ; ++i) {
					int temp = pp[0][0][9][i];
					if (localMaximum < temp) {
						localMaximum = temp;
					}
				}
				if (localMaximum > result) {
					result = localMaximum;
				}
			}
			System.out.println(result);
		}
	}
 
	private static int[][][][] computeMaxPathes(int[][] A, int N, int M, int p) {
		int[][][][] maxPathes = new int[N][M][10][4];
		for (int n = N - 1; n >= 0 ; --n) {
			for (int m = M - 1 ; m >= 0 ; --m) {
				for (int i = 0 ; i < 10 ; ++i) {
					for (int j = 0 ; j < 4 ; ++j) {
						int current = A[n][m];
						if (j > 0) {
							current = (current + p) % 10;
						}
						if (j > 2) {
							current = (current + p) % 10;
						}
						
						ArrayList<int[]> nextCoords = new ArrayList<int[]>();
						ArrayList<int[]> additions = new ArrayList<int[]>();
						if (n < N - 1) {
							int[] e = new int[2];
							e[0] = n + 1;
							e[1] = m;
							nextCoords.add(e);
							int[] e2 = new int[2];
							if (j == 0 || j == 2) {
								e2[0] = 0;
								e2[1] = 2;
							}
							if (j == 1 || j == 3) {
								e2[0] = 1;
								e2[1] = 3;
							}
							additions.add(e2);
						}
						
						if (m < M - 1) {
							int[] e = new int[2];
							e[0] = n;
							e[1] = m + 1;
							nextCoords.add(e);
							int[] e2 = new int[2];
							if (j == 0 || j == 1) {
								e2[0] = 0;
								e2[1] = 1;
							}
							if (j == 2 || j == 3) {
								e2[0] = 2;
								e2[1] = 3;
							}
							additions.add(e2);
						}
						int result = current == i ? 1 : 0;
						for (int k = 0 ; k < nextCoords.size() ; ++k) {
							int[] addition = additions.get(k);
							int[] nextCoord = nextCoords.get(k);
							for (int jj = 0 ; jj < addition.length ; ++jj) {
								result = Math.max(result,
												(current == i ? 1 : 0) + maxPathes
												[nextCoord[0]][nextCoord[1]][i][addition[jj]]);
							}
						}
						if (current < i) {
							result = Math.max(result, maxPathes[n][m][current][j]);
						}
						maxPathes[n][m][i][j] = result;
					}
				}
			}
		}
		return maxPathes;
	}
}
