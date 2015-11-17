/*
 * golfcourse.cpp
 *
 *  Created on: Nov 17, 2015
 *      Author: ilya
 */
#include <iostream>
#include <vector>
#include <cmath>

typedef int elevation_type;

namespace {
	struct SegmentMeta {
		SegmentMeta(elevation_type max = 0, int count = 0) {
			this->count = count;
			this->max = max;
		}
		int count;
		elevation_type max;
	};

	SegmentMeta nullSegment(-1, -1);

	typedef std::vector<SegmentMeta> segment_tree;
	typedef std::vector<segment_tree> matrix;

	void buildSegmentTree(int node, int b, int e, const segment_tree& A, segment_tree& T) {
		if (e == b) {
			T[node] = A[e];
		} else {
			buildSegmentTree(2 * node, b, (e + b) / 2, A, T);
			buildSegmentTree(2 * node + 1, (e + b) / 2 + 1, e, A, T);

			if (T[2 * node].max > T[2 * node + 1].max) {
				T[node] = T[2 * node];
			} else if (T[2 * node].max < T[2 * node + 1].max) {
				T[node] = T[2 * node + 1];
			} else {
				T[node] = SegmentMeta(T[2 * node].max, T[2 * node].count + T[2 * node + 1].count);
			}
		}
	}

	SegmentMeta querySegmentTree(int i, int j, int node, int b, int e, const segment_tree& T) {
		if (i > e || j < b) {
			return nullSegment;
		}
		if (b >= i && e <= j) {
			return T[node];
		}

		SegmentMeta left = querySegmentTree(i, j, 2 * node, b, (b + e) / 2, T);
		SegmentMeta right = querySegmentTree(i, j, 2 * node + 1, (b + e) / 2 + 1, e, T);

		if (left.count == nullSegment.count) {
			return right;
		}
		if (right.count == nullSegment.count) {
			return left;
		}

		if (left.max == right.max) {
			return SegmentMeta(left.max, left.count + right.count);
		}

		return left.max > right.max ? left : right;;
	}

	int computeSegmentTreeSize(int N) {
		int log = 0;
		for (log = 0 ; 1 << log <= N ; ++log);
		return 1 << (log + 1);
	}

	segment_tree buildColumnTree(int i, int j, const matrix& segmentTrees, int e) {
		segment_tree columnTree(computeSegmentTreeSize(segmentTrees.size()));
		segment_tree column(segmentTrees.size());
		for (size_t i = 0 ; i < segmentTrees.size() ; ++i) {
			column[i] = querySegmentTree(i, j, 1, 0, e, segmentTrees[i]);
		}
		buildSegmentTree(1, 0, column.size() - 1, column, columnTree);
		return columnTree;
	}

}

int main() {
	int T;
	std::cin>>T;
	for (int t = 0 ; t < T ; ++t) {
		int M,N,K;
		std::cin>>M>>N>>K;
		matrix field(M);
		for (int m = 0 ; m < M ; ++m) {
			field[m] = segment_tree(N);
			for (int n = 0 ; n < N ; ++n) {
				std::cin>>field[m][n].max;
				field[m][n].count = 1;
			}
		}
		matrix rowTrees(M);
		for (int m = 0 ; m < M ; ++m) {
			rowTrees[m] = segment_tree(computeSegmentTreeSize(N));
			buildSegmentTree(1, 0, N - 1, field[m], rowTrees[m]);
		}
		std::cout<<"Case "<<t<<':'<<std::endl;
		matrix columnTrees(N - K + 1);
		for (int n = 0 ; n <= N - K ; ++n) {
			columnTrees[n] = buildColumnTree(n, n + K - 1, rowTrees, N - 1);
		}

		for (int m = 0 ; m <= M - K ; ++m) {
			for (int n = 0 ; n <= N - K ; ++n) {
				if (n > 0) {
					std::cout<<' ';
				}
				SegmentMeta result = querySegmentTree(m, m + K - 1, 1, 0, M - 1, columnTrees[n]);
				std::cout<<result.max<<'('<<result.count<<')';
			}
			std::cout<<std::endl;
		}
		std::cout<<std::endl;
	}
	return 0;
}
