package topcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LittleElephant {
	
	private static Integer[][][][] memo;
	
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
				memo = new Integer[N][M][10][4];
				int localMaximum = 0;
				for (int i = 0 ; i < 4 ; ++i) {
					int temp = compute(N, M, A, p, 0, 0, 9, i);
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

	private static int compute(int N, int M, int[][] A, int p, int n, int m, int bound, int add) {
		if (memo[n][m][bound][add] != null) {
			return memo[n][m][bound][add];
		}

		int current = A[n][m];
		if (add > 0) {
			current = (current + p) % 10;
		}
		if (add > 2) {
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
			if (add == 0 || add == 2) {
				e2[0] = 0;
				e2[1] = 2;
			}
			if (add == 1 || add == 3) {
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
			if (add == 0 || add == 1) {
				e2[0] = 0;
				e2[1] = 1;
			}
			if (add == 2 || add == 3) {
				e2[0] = 2;
				e2[1] = 3;
			}
			additions.add(e2);
		}
		ArrayList<int[]> nextBounds = new ArrayList<int[]>();
		if (current == bound) {
			int[] e = new int[2];
			e[0] = current;
			e[1] = 1;
			nextBounds.add(e);
		}
		if (current > bound) {
			int[] e = new int[2];
			e[0] = bound;
			e[1] = 0;
			nextBounds.add(e);
		}
		if (current < bound) {
			int[] e = new int[2];
			e[0] = bound;
			e[1] = 0;
			nextBounds.add(e);
			e = new int[2];
			e[0] = current;
			e[1] = 1;
			nextBounds.add(e);
		}
		int result = current <= bound ? 1 : 0;
		for (int t = 0 ; t < nextBounds.size() ; ++t) {
			for (int k = 0 ; k < nextCoords.size() ; ++k) {
				int[] addition = additions.get(k);
				int[] nextCoord = nextCoords.get(k);
				for (int j = 0 ; j < addition.length ; ++j) {
					result = Math.max(result,
									nextBounds.get(t)[1] + compute(N, M, A, p,
									nextCoord[0],nextCoord[1],nextBounds.get(t)[0],addition[j]));
				}
			}
		}
		memo[n][m][bound][add] = result;
		return result;
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
						ArrayList<ArrayList<Integer>> additions = new ArrayList<ArrayList<Integer>>();
						if (n < N - 1) {
							int[] e = new int[2];
							e[0] = n + 1;
							e[1] = m;
							nextCoords.add(e);
							ArrayList<Integer> e2 = new ArrayList<Integer>();
							if (j == 0 || j == 2) {
								e2.add(0);
								e2.add(2);
							}
							if (j == 1 || j == 3) {
								e2.add(1);
								e2.add(3);
							}
							additions.add(e2);
						}
						
						if (m < M - 1) {
							int[] e = new int[2];
							e[0] = n;
							e[1] = m + 1;
							nextCoords.add(e);
							ArrayList<Integer> e2 = new ArrayList<Integer>();
							if (j == 0 || j == 1) {
								e2.add(0);
								e2.add(1);
							}
							if (j == 2 || j == 3) {
								e2.add(2);
								e2.add(3);
							}
							additions.add(e2);
						}
						ArrayList<int[]> nextCaps = new ArrayList<int[]>();
						if (current == i) {
							int[] e = new int[2];
							e[0] = current;
							e[1] = 1;
							nextCaps.add(e);
						}
						if (current > i) {
							int[] e = new int[2];
							e[0] = i;
							e[1] = 0;
							nextCaps.add(e);
						}
						if (current < i) {
							int[] e = new int[2];
							e[0] = i;
							e[1] = 0;
							nextCaps.add(e);
							e = new int[2];
							e[0] = current;
							e[1] = 1;
							nextCaps.add(e);
						}
						int result = current <= i ? 1 : 0;
						for (int t = 0 ; t < nextCaps.size() ; ++t) {
							for (int k = 0 ; k < nextCoords.size() ; ++k) {
								ArrayList<Integer> addition = additions.get(k);
								for (int jj = 0 ; jj < addition.size() ; ++jj) {
									int[] nextCoord = nextCoords.get(k);
									result = Math.max(result,
													nextCaps.get(t)[1] + maxPathes
													[nextCoord[0]][nextCoord[1]][nextCaps.get(t)[0]][addition.get(jj)]);
								}
							}
						}
						maxPathes[n][m][i][j] = result;
					}
				}
			}
		}
		return maxPathes;
	}
}
