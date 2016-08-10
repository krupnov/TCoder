/*
 * ch_gard.cpp
 *
 *  Created on: Aug 9, 2016
 *      Author: ilya
 */
#include <iostream>
#include <vector>
#include <utility>
#include <algorithm>
#include <sstream>

namespace {


template<typename T>
T abs(T x) {
	return x < 0 ? -1 * x : x;
}

struct Intersection {
	int SmallerIndex;
	int Left;
	int Right;

	bool operator < (const Intersection& that) const {
		return this->Left < that.Left;
	}
};

struct Line {
	int Initial;
	int Slope;
	int Index;

	bool intersect(Line const& that, Intersection& interval) const {
		if (this->Slope == that.Slope) {
			return false;
		}

		if (((this->Initial - that.Initial) < 0 && (that.Slope - this->Slope) > 0)
				|| ((this->Initial - that.Initial) > 0 && (that.Slope - this->Slope) < 0)) {
			return false;
		}

		if (abs(this->Initial - that.Initial) % abs(that.Slope - this->Slope) == 0) {
			interval.Left = interval.Right =  (this->Initial - that.Initial) / (that.Slope - this->Slope);
		} else {
			interval.Left = (this->Initial - that.Initial) / (that.Slope - this->Slope);
			interval.Right = interval.Left + 1;
		}
		interval.SmallerIndex = this->Index < that.Index ? this->Index : that.Index;
		return true;
	}

};

class OrderKeeper {
	int out_of_order_count;
	std::vector<bool> order;
	std::vector<bool> initialy_equale_next;
public:
	OrderKeeper(std::vector<Line> const& lines, bool less) noexcept : out_of_order_count(0), order(lines.size() - 1), initialy_equale_next(lines.size() - 1) {
		bool current_less = less;
		for (size_t i = 0 ; i < lines.size() - 1; ++i) {
			if (lines[i].Initial == lines[i + 1].Initial) {
				++out_of_order_count;
				order[i] = false;
				if (lines[i].Slope == lines[i + 1].Slope) {
					initialy_equale_next[i] = false;
				} else if (lines[i].Slope > lines[i + 1].Slope && current_less) {
					initialy_equale_next[i] = false;
				} else if (lines[i].Slope < lines[i + 1].Slope && !current_less) {
					initialy_equale_next[i] = false;
				} else {
					initialy_equale_next[i] = true;
				}
			} else if (lines[i].Initial > lines[i + 1].Initial && current_less) {
				++out_of_order_count;
				order[i] = false;
			} else if (lines[i].Initial < lines[i + 1].Initial && !current_less) {
				++out_of_order_count;
				order[i] = false;
			} else {
				order[i] = true;
			}
			current_less = !current_less;
		}
	}

	void swap(int left, int x) {
		if (x == 0) {
			if (initialy_equale_next[left]) {
				--out_of_order_count;
			}
			order[left] = initialy_equale_next[left];
			return;
		}
		if (order[left]) {
			++out_of_order_count;
		} else {
			--out_of_order_count;
		}
		order[left] = !order[left];
	}

	bool is_in_order() const {
		return out_of_order_count == 0;
	}
};

}

int main() {
	int T;
	std::cin>>T;
	for (int t = 0 ;t < T ; ++t) {
		int N;
		std::cin>>N;
		std::vector<Line> lines(N);
		for (int i = 0 ; i < N ; ++i) {
			std::cin>>lines[i].Initial>>lines[i].Slope;
			lines[i].Index = i;
		}
		OrderKeeper small_order(lines, true);
		OrderKeeper high_order(lines, false);
		std::vector<Intersection> intersections;
		for (size_t i = 0 ; i < lines.size() - 1 ; ++i) {
			Intersection inter;
			if (lines[i].intersect(lines[i + 1], inter)) {
				intersections.push_back(inter);
			}
		}
		std::sort(intersections.begin(), intersections.end());
		int next = 0;
		std::vector<std::pair<int, int>> intervals;
		for (size_t i = 0 ; i < intersections.size() ; ++i) {
			if (small_order.is_in_order() || high_order.is_in_order()) {
				intervals.push_back(std::make_pair(next, intersections[i].Right - 1));
			}
			small_order.swap(intersections[i].SmallerIndex, intersections[i].Right);
			high_order.swap(intersections[i].SmallerIndex, intersections[i].Right);
			while (i < intersections.size() - 1 && intersections[i].Left + 1 == intersections[i + 1].Right) {
				++i;
				small_order.swap(intersections[i].SmallerIndex, intersections[i].Right);
				high_order.swap(intersections[i].SmallerIndex, intersections[i].Right);
			}
			next = intersections[i].Left + 1;
		}
		if (small_order.is_in_order() || high_order.is_in_order()) {
			intervals.push_back(std::make_pair(next, -1));
		}
		if (intervals.size() == 0) {
			std::cout<<0;
			continue;
		}
		std::vector<std::pair<int, int>> glued;
		int left = intervals[0].first;
		for (size_t i = 0 ; i < intervals.size() - 1; ++i) {
			if (intervals[i].second != intervals[i + 1].first) {
				glued.push_back(std::make_pair(left, intervals[i].second));
				left = intervals[i + 1].first;
			}
		}
		glued.push_back(std::make_pair(left, intervals[intervals.size() - 1].second));
		std::cout<<glued.size()<<std::endl;
		for (const auto& inter : glued) {
			if (inter.second == -1) {
				std::cout<<inter.first<<" Inf"<<std::endl;
			} else {
				std::cout<<inter.first<<' '<<inter.second<<std::endl;
			}
		}
	}
}
