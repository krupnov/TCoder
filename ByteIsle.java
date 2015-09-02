package coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ByteIsle {
	
	private static class Interval {
		private final int left, right;
		private final boolean includeLeft, includeRight;
		
		public Interval(int left, int right,
				boolean includeLeft, boolean includeRight) {
			this.left = left;
			this.right = right;
			this.includeLeft = includeLeft;
			this.includeRight = includeRight;
		}

		public int getLeft() {
			return left;
		}

		public int getRight() {
			return right;
		}

		public Interval merge(Interval interval) {
			return new Interval(
					this.getLeft(), interval.getRight(), this.isIncludeLeft(), interval.isIncludeRight());
		}

		public boolean isIncludeLeft() {
			return includeLeft;
		}

		public boolean isIncludeRight() {
			return includeRight;
		}

		public boolean contains(Interval interval) {
			if ((this.left < interval.left || (this.left == interval.left && (this.includeLeft || !interval.includeLeft)))
					&& (this.right > interval.right || (this.right == interval.right && (this.includeRight || !interval.includeRight)))) {
				return true;
			}
			return false;
		}

		public boolean intersects(Interval interval) {
			if (this.left <= interval.left 
					&& (this.right > interval.left || (this.right == interval.left && this.includeRight && interval.includeLeft))) {
				return true;
			}
			if (this.right >= interval.right
					&& (this.left < interval.right || (this.left == interval.right && this.includeLeft && interval.includeRight))) {
				return true;
			}
			return false;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T; ++t) {
			int N = Integer.parseInt(reader.readLine());
			Interval[] intervals = new Interval[N];
			int[] points = new int[2*N];
			for (int i = 0 ; i < N ; ++i) {
				String[] bounderies = reader.readLine().split(" ");
				intervals[i] = new Interval(
						Integer.parseInt(bounderies[0]), Integer.parseInt(bounderies[1]),
						true, true);
				points[2*i] = intervals[i].getLeft();
				points[2*i + 1] = intervals[i].getRight();
			}
			
			Arrays.sort(points);
			ArrayList<Interval> collected = new ArrayList<Interval>();
			if (points.length > 0) {
				collected.add(new Interval(points[0], points[0], true, true));
			}
			for (int i = 0 ; i < points.length - 1; ++i) {
				if (points[i] == points[i+1]) {
					continue;
				}
				collected.add(new Interval(points[i], points[i+1], false, false));
				collected.add(new Interval(points[i+1], points[i+1], true, true));
			}
			Interval[] segmentTree = new Interval[2 * collected.size() - 1];
			buildTreeInternalNodes(collected, segmentTree, 0, collected.size()- 1, 0);
			
			int[] containCounters = new int[segmentTree.length];
			for (int i = 0 ; i < intervals.length ; ++i) {
				insertInterval(segmentTree, intervals[i], 0, containCounters);
			}
			
			int result = points[0] == 0 ? 0 : 1;
			int[] results = new int[segmentTree.length];
			result += countVariants(segmentTree, containCounters, results, 0, 0);
			System.out.println(result);
			StringBuffer firstResult = new StringBuffer(intervals.length);
			if (points[0] != 0) {
				for (int i = 0 ; i < intervals.length ; ++i) {
					firstResult.append("0");
				}
			} else {
				for (int i = 0 ; i < intervals.length ; ++i) {
					if (canSkip(segmentTree, results, intervals[i])) {
						skip(segmentTree, results, intervals[i], 0);
						firstResult.append("0");
					} else {
						firstResult.append("1");
					}
				}
			}
			System.out.println(firstResult.toString());
		}
		reader.close();
	}

	private static int skip(
			Interval[] segmentTree, int[] results,
			Interval interval, int current) {
		if (results[current] == 0) {
			return 0;
		}
		if (interval.contains(segmentTree[current])) {
			int temp = results[current];
			results[current] = 0;
			return temp;
		}
		
		int result = 0;
		if (interval.intersects(segmentTree[2 * current + 1])) {
			result += skip(segmentTree, results, interval, 2*current + 1);
		}
		if (interval.intersects(segmentTree[2 * current + 2])) {
			result += skip(segmentTree, results, interval, 2*current + 2);
		}
		results[current] -= result;
		return result;
	}

	private static boolean canSkip(Interval[] segmentTree, int[] results,
			Interval interval) {
		int skip = getSkip(segmentTree, results, interval, 0);
		return results[0] > skip;
	}
	
	private static int getSkip(
			Interval[] segmentTree, int[] results,
			Interval interval, int current) {
		if (results[current] == 0) {
			return 0;
		}
		
		if (interval.contains(segmentTree[current])) {
			return results[current];
		}
		int result = 0;
		if (interval.intersects(segmentTree[2 * current + 1])) {
			result += getSkip(segmentTree, results, interval, 2*current + 1);
		}
		if (interval.intersects(segmentTree[2 * current + 2])) {
			result += getSkip(segmentTree, results, interval, 2*current + 2);
		}

		return result;
	}

	private static int countVariants(Interval[] segmentTree,
			int[] containCounters, int[] results, int current, int sum) {
		sum += containCounters[current];
		if (2*current + 1 >= containCounters.length) {
			Interval sumPoint = new Interval(sum, sum, true, true);
			if (segmentTree[current].contains(sumPoint)) {
				results[current] = 1;
			}
			return results[current];
		}
		
		results[current] = countVariants(segmentTree, containCounters, results, 2 * current + 1, sum)
				+ countVariants(segmentTree, containCounters, results, 2 * current + 2, sum);
		
		return results[current];
	}

	private static int getMid(int s, int e) { 
		return s + (e -s)/2;
	}
	
	private static Interval buildTreeInternalNodes(ArrayList<Interval> collected,
			Interval[] segmentTree, int start, int end, int current) {
		if (start == end) {
			segmentTree[current] = collected.get(start);
		} else {
			int mid = getMid(start, end);
			segmentTree[current] = buildTreeInternalNodes(
					collected, segmentTree, start, mid, 2 * current + 1).merge(
							buildTreeInternalNodes(collected, segmentTree, mid+1, end, 2*current+2));
		}
		return segmentTree[current];
	}

	private static void insertInterval(Interval[] segmentTree,
			Interval interval, int current, int[] containCounters) {
		if (interval.contains(segmentTree[current])) {
			containCounters[current]++;
		} else {
			if (interval.intersects(segmentTree[2 * current + 1])) {
				insertInterval(segmentTree, interval, 2*current + 1, containCounters);
			}
			if (interval.intersects(segmentTree[2 * current + 2])) {
				insertInterval(segmentTree, interval, 2*current + 2, containCounters);
			}
		}
	}
}