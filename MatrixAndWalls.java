package topcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MatrixAndWalls {

	private static class Partition {
		public int count = 0;
		public Partition parent = null;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T; ++t) {
			String[] params = reader.readLine().split(" ");
			short N = Short.parseShort(params[0]);
			short M = Short.parseShort(params[1]);
			int Q = Integer.parseInt(params[2]);
			
			ArrayList<Short>[] queries = new ArrayList[Q];
			HashMap<Short, HashMap<Short, Integer>> xWalls = new HashMap<Short, HashMap<Short, Integer>>();
			HashMap<Short, HashMap<Short, Integer>> yWalls = new HashMap<Short, HashMap<Short, Integer>>();
			for (int q = 0 ; q < Q ; ++q) {
				String[] nextQuery = reader.readLine().split(" ");
				queries[q] = new ArrayList<Short>();
				short type = Short.parseShort(nextQuery[0]);
				parseQueries(queries, xWalls, yWalls, q, nextQuery, type);
			}
			
			Partition[][] partitions = new Partition[N][M];
			long maxPartitionSize = 1;
			for (short x = 0 ; x < N ; ++x) {
				for (short y = 0 ; y < M ; ++y) {
					if (partitions[x][y] == null) {
						partitions[x][y] = new Partition();
						partitions[x][y].count++;
					}
					if (!xWalls.containsKey(x) || !xWalls.get(x).containsKey(y)) {
						if (x < N - 1) {
							Partition partition = partitions[x+1][y];
							if (partition == null) {
								partitions[x+1][y] = getGrandParent(partitions[x][y]);
								partitions[x+1][y].count++;
								partition = partitions[x+1][y];
							} else {
								partition = mergePartitions(partition, partitions[x][y]);
							}
							if (partition.count > maxPartitionSize) {
								maxPartitionSize = partition.count;
							}
						}
					}
					if (!yWalls.containsKey(x) || !yWalls.get(x).containsKey(y)) {
						if (y < M - 1) {
							Partition partition = partitions[x][y+1];
							if (partition == null) {
								partitions[x][y+1] = getGrandParent(partitions[x][y]);
								partitions[x][y+1].count++;
								partition = partitions[x+1][y];
							} else  {
								partition = mergePartitions(partition, partitions[x][y]);
							}
							if (partition.count > maxPartitionSize) {
								maxPartitionSize = partition.count;
							}
						}
					}
				}
			}
			long result = 0;
			for (int q = Q -1 ; q >= 0 ; --q) {
				ArrayList<Short> query = queries[q];
				if (query.get(0) == 4) {
					result += maxPartitionSize;
				}
				if (query.get(0) == 3) {
					if (getGrandParent(partitions[query.get(1)][query.get(2)]) == 
							getGrandParent(partitions[query.get(3)][query.get(4)])) {
						result++;
					}
				}
				if (query.get(0) == 2) {
					short x = query.get(1);
					short y = query.get(2);
					xWalls.get(x).put(y, xWalls.get(x).get(y) - 1);
					if (xWalls.get(x).get(y) == 0) {
						Partition merged = mergePartitions(partitions[x][y], partitions[x + 1][y]);
						if (merged.count > maxPartitionSize) {
							maxPartitionSize = merged.count;
						}
					}
				}
				if (query.get(0) == 1) {
					short x = query.get(1);
					short y = query.get(2);
					yWalls.get(x).put(y, yWalls.get(x).get(y) - 1);
					if (yWalls.get(x).get(y) == 0) {
						Partition merged = mergePartitions(partitions[x][y], partitions[x][y + 1]);
						if (merged.count > maxPartitionSize) {
							maxPartitionSize = merged.count;
						}
					}
				}
			}
			System.out.println(result);
		}
	}

	private static void parseQueries(ArrayList<Short>[] queries,
			HashMap<Short, HashMap<Short, Integer>> xWalls,
			HashMap<Short, HashMap<Short, Integer>> yWalls, int q, String[] nextQuery,
			short type) {
		switch (type) {
		case 1: {
				queries[q].add(type);
				short x = Short.parseShort(nextQuery[1]);
				x--;
				queries[q].add(x);
				short y = Short.parseShort(nextQuery[2]);
				y--;
				queries[q].add(y);
				addWall(yWalls, x, y);
			}
			break;
		case 2: {
				queries[q].add(type);
				short x = Short.parseShort(nextQuery[1]);
				x--;
				queries[q].add(x);
				short y = Short.parseShort(nextQuery[2]);
				y--;
				queries[q].add(y);
				addWall(xWalls, x, y);
			}
			break;
		case 3: {
				queries[q].add(type);
				short x1 = Short.parseShort(nextQuery[1]);
				x1--;
				queries[q].add(x1);
				short y1 = Short.parseShort(nextQuery[2]);
				y1--;
				queries[q].add(y1);
				short x2 = Short.parseShort(nextQuery[3]);
				x2--;
				queries[q].add(x2);
				short y2 = Short.parseShort(nextQuery[4]);
				y2--;
				queries[q].add(y2);
			}
			break;
		case 4:
			queries[q].add(type);
		}
	}

	private static void addWall(HashMap<Short, HashMap<Short, Integer>> walls, short x, short y) {
		if (!walls.containsKey(x)) {
			walls.put(x, new HashMap<Short, Integer>());
		}
		if (!walls.get(x).containsKey(y)) {
			walls.get(x).put(y, 0);
		}
		walls.get(x).put(y, walls.get(x).get(y) + 1);
	}

	private static Partition mergePartitions(Partition first, Partition second) {
		first = getGrandParent(first);
		second = getGrandParent(second);
		if (first == second) {
			return first;
		}
		first.parent = second;
		second.count += first.count;
		return second;
	}
	
	private static Partition getGrandParent(Partition partition) {
		while (partition.parent != null) {
			partition = partition.parent;
		}
		return partition;
	}
}
