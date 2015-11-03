/*
 * ceilmap.cpp
 *
 *  Created on: Nov 3, 2015
 *      Author: ilya
 */
#include <iostream>
#include <vector>
#include <cmath>

namespace {
	long computeDistance(int x1, int y1, int x2, int y2) {
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}

	struct Segment {

		int x1, y1, x2, y2;
		Segment(int x1, y1, x2, y2) {
			this->x1 = x1;
			this->x2 = x2;
			this->y1 = y1;
			this->y2 = y2;
		}
		long squareDistance() const {
			return computeDistance(x1, y1, x2, y2);
		}
		bool operator<(const Segment& other) {
			return this->squareDistance() < other.squareDistance();
		}
	};

	double findMaximum(const std::vector<int>& xs, const std::vector<int>& ys) {
		long maximumSquare = 0;
		for (size_t i = 0 ; i < xs.size() ; ++i) {
			for (size_t j = i + 1 ; j < xs.size() ; ++j) {
				long distance = computeDistance(xs[i], ys[i], xs[j], ys[j]);
				if (distance > maximumSquare) {
					maximumSquare = distance;
				}
			}
		}
		return std::sqrt((double)maximumSquare);
	}

	bool isIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		return false;
	}

	double computeFor4(const std::vector<int>& xs, const std::vector<int>& ys) {
		std::vector<Segment> segments;
		for (size_t i = 0 ; i < xs.size() ; ++i) {
			for (size_t j = i + 1 ; j < xs.size() ; ++j) {
				segments.push_back(Segment(xs[i], ys[i], xs[j], ys[j]));
			}
		}
		return 0.0;
	}
}

int main() {
	int T;
	std::cin>>T;
	for (int t = 0 ; t < T ; ++t) {
		int N;
		std::cin>>N;
		std::vector<int> xs(N);
		std::vector<int> ys(N);
		for (int n = 0 ; n < N ; ++n) {
			std::cin>>xs[n]>>ys[n];
		}
		if (N >= 5) {
			std::cout<<findMaximum(xs, ys)<<std::endl;
		} else {
			std::cout<<computeFor4(xs, ys)<<std::endl;
		}
	}
	return 0;
}
