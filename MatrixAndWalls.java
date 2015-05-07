package coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class MatrixAndWalls {

	private static class Partition {
		public int count = 0;
		public Partition parent = null;
		public int level = 0;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T; ++t) {
			String[] params = reader.readLine().split(" ");
			short N = Short.parseShort(params[0]);
			short M = Short.parseShort(params[1]);
			int Q = Integer.parseInt(params[2]);
			
			LinkedList<short[]> queries = new LinkedList<short[]>();
			boolean[][] xWalls = new boolean[N][M];
			boolean[][] yWalls = new boolean[N][M];
			for (int q = 0 ; q < Q ; ++q) {
				String[] nextQuery = reader.readLine().split(" ");
				short type = Short.parseShort(nextQuery[0]);
				parseQueries(queries, xWalls, yWalls, nextQuery, type);
			}
			
			Partition[][] partitions = new Partition[N][M];
			int maxPartitionSize = 1;
			for (short x = 0 ; x < N ; ++x) {
				for (short y = 0 ; y < M ; ++y) {
					if (partitions[x][y] == null) {
						partitions[x][y] = new Partition();
						partitions[x][y].count++;
					}
					if (!xWalls[x][y]) {
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
					if (!yWalls[x][y]) {
						if (y < M - 1) {
							Partition partition = partitions[x][y+1];
							if (partition == null) {
								partitions[x][y+1] = getGrandParent(partitions[x][y]);
								partitions[x][y+1].count++;
								partition = partitions[x][y+1];
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
			Iterator<short[]> descendingIterator = queries.descendingIterator();
			while (descendingIterator.hasNext()) {
				short[] query = descendingIterator.next();
				if (query[0] == 4) {
					result += maxPartitionSize;
				}
				if (query[0] == 3) {
					if (getGrandParent(partitions[query[1]][query[2]]) == 
							getGrandParent(partitions[query[3]][query[4]])) {
						result++;
					}
				}
				if (query[0] == 1) {
					short x = query[1];
					short y = query[2];
					Partition merged = mergePartitions(partitions[x][y], partitions[x][y + 1]);
					if (merged.count > maxPartitionSize) {
						maxPartitionSize = merged.count;
					}
				}
				if (query[0] == 2) {
					short x = query[1];
					short y = query[2];
					Partition merged = mergePartitions(partitions[x][y], partitions[x + 1][y]);
					if (merged.count > maxPartitionSize) {
						maxPartitionSize = merged.count;
					}
				}
			}
			System.out.println(result);
		}
	}

	private static void parseQueries(LinkedList<short[]> queries, boolean[][] xWalls, boolean[][] yWalls, String[] nextQuery, short type) {
		switch (type) {
		case 1: {
				short x = Short.parseShort(nextQuery[1]);
				x--;
				short y = Short.parseShort(nextQuery[2]);
				y--;
				if (!yWalls[x][y]) {
					short[] query = new short[3];
					query[0] = type;
					query[1] = x;
					query[2] = y;
					yWalls[x][y] = true;
					queries.add(query);
				}
			}
			break;
		case 2: {
			short x = Short.parseShort(nextQuery[1]);
			x--;
			short y = Short.parseShort(nextQuery[2]);
			y--;
			if (!xWalls[x][y]) {
				short[] query = new short[3];
				query[0] = type;
				query[1] = x;
				query[2] = y;
				xWalls[x][y] = true;
				queries.add(query);
				}
			}
			break;
		case 3: {
				short[] query = new short[5];
				query[0] = type;
				short x1 = Short.parseShort(nextQuery[1]);
				x1--;
				query[1] = x1;
				short y1 = Short.parseShort(nextQuery[2]);
				y1--;
				query[2] = y1;
				short x2 = Short.parseShort(nextQuery[3]);
				x2--;
				query[3] = x2;
				short y2 = Short.parseShort(nextQuery[4]);
				y2--;
				query[4] = y2;
				queries.add(query);
			}
			break;
		case 4:
			short[] query = new short[1];
			query[0] = type;
			queries.add(query);
		}
	}

	private static Partition mergePartitions(Partition first, Partition second) {
		first = getGrandParent(first);
		second = getGrandParent(second);
		if (first == second) {
			return first;
		}
		if (first.level > second.level) {
			second.parent = first;
			first.count += second.count;
			return first;
		}
		if (first.level == second.level) {
			second.level++;
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
