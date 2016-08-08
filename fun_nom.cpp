/*
 * funny_gnomes.cpp
 *
 *  Created on: Aug 8, 2016
 *      Author: ilya
 */
#include <iostream>
#include <vector>
#include <memory>

namespace {

short Adj[500][500];
using ui64 = unsigned long long;

class GnomIndex {
	constexpr static int index_size = 8;
	ui64 index[index_size];
	int gnomes_count;
public:
	GnomIndex(int id, int gnomes_count) noexcept : gnomes_count(gnomes_count) {
		if (id != -1) {
			index[id / 64] = 1 << (id% 64);
		}
	}

	void add(const GnomIndex& ids) {
		for (int i = 0 ; i < index_size ; ++i) {
			index[i] = index[i] | ids.index[i];
		}
	}

	std::unique_ptr<GnomIndex> copy() const {
		std::unique_ptr<GnomIndex> copy(new GnomIndex(-1, gnomes_count));
		for (int i = 0 ; i < index_size ; ++i) {
			copy->index[i] = this->index[i];
		}
		return copy;
	}

	class Iterator {
		const GnomIndex& parent;
		int _current;
	public:
		Iterator(const GnomIndex& parent) noexcept  : parent(parent), _current(-1) {
		}

		int current() const {
			return _current;
		}

		bool has_next() {
			while (_current < parent.gnomes_count) {
				++_current;
				if ((parent.index[_current / 64] >> (_current % 64)) & 0x01) {
					return true;
				}
			}
			return false;
		}
	};

	Iterator iterator() const {
		return Iterator(*this);
	}
};

std::unique_ptr<GnomIndex> first_ten[500][10];
std::unique_ptr<GnomIndex> power_of_ten[500][9];

}

int main() {
	int N;
	std::cin>>N;
	for (int i = 0 ; i < N ; ++i) {
		std::vector<int> row(N);
		for (int j = 0 ; j < N ; ++j) {
			std::cin>>Adj[i][j];
		}
	}

	for (int i = 0 ; i < N ; ++i) {
		first_ten[i][0].reset(new GnomIndex(i, N));
	}

	for (int s = 1 ; s < 10 ; ++s) {
		for (int i = 0 ; i < N ; ++i) {
			std::unique_ptr<GnomIndex> gnom_adj(new GnomIndex(-1, N));
			for (int j = 0 ; j < N ; ++j) {
				if (Adj[i][j]) {
					gnom_adj->add(*first_ten[j][s - 1]);
				}
			}
			first_ten[i][s].swap(gnom_adj);
		}
	}

	for (int i = 0 ; i < N ; ++i) {
		power_of_ten[i][0].swap(first_ten[i][9]);
	}

	for (int p = 1 ; p < 9 ; ++p) {
		for (int i = 0 ; i < N ; ++i) {
			std::unique_ptr<GnomIndex> edge_away(power_of_ten[i][p - 1]->copy());
			for (int s = 0 ; s < 10 ; ++s) {
				GnomIndex::Iterator edge_away_iter(edge_away->iterator());
				std::unique_ptr<GnomIndex> next_away(new GnomIndex(-1, N));
				while (edge_away_iter.has_next()) {
					next_away->add(*power_of_ten[edge_away_iter.current()][p - 1]);
				}
				edge_away.swap(next_away);
			}
			power_of_ten[i][p].swap(edge_away);
		}
	}

	int M;
	std::cin>>M;
	for (int i = 0 ; i < M ; ++i) {
		int k, x;
		std::cin>>k>>x;
	}
}
