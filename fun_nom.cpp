/*
 * funny_gnomes.cpp
 *
 *  Created on: Aug 8, 2016
 *      Author: ilya
 */
#include <iostream>
#include <memory>
#include <sstream>
#include <cstring>

namespace {

template <typename T, size_t N>
inline
size_t SizeOfArray( const T(&)[ N ] ) {
	return N;
}


short Adj[500][500];
using ui64 = unsigned long long;

class GnomIndex {
	static constexpr ui64 one = 1;
	constexpr static int index_size = 8;
	ui64 index[index_size];
	int gnomes_count;
public:
	GnomIndex(int id, int gnomes_count) noexcept : gnomes_count(gnomes_count) {
		std::memset(index, 0, SizeOfArray(index) * sizeof(ui64));
		if (id != -1) {
			index[id / 64] = one << (id % 64);
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
			++_current;
			while (_current < parent.gnomes_count) {
				if ((parent.index[_current / 64] >> (_current % 64)) & one) {
					return true;
				}
				++_current;
			}
			return false;
		}
	};

	Iterator iterator() const {
		return Iterator(*this);
	}
};

std::unique_ptr<GnomIndex> first_ten[500][11];
std::unique_ptr<GnomIndex> power_of_ten[500][9][10];

}

int main() {
	int N;
	std::cin>>N;
	for (int i = 0 ; i < N ; ++i) {
		for (int j = 0 ; j < N ; ++j) {
			std::cin>>Adj[i][j];
		}
	}

	for (int i = 0 ; i < N ; ++i) {
		first_ten[i][0].reset(new GnomIndex(i, N));
	}

	for (int s = 1 ; s < 11 ; ++s) {
		for (int i = 0 ; i < N ; ++i) {
			std::unique_ptr<GnomIndex> gnom_adj(new GnomIndex(-1, N));
			for (int j = 0 ; j < N ; ++j) {
				if (Adj[i][j] == 1) {
					gnom_adj->add(*first_ten[j][s - 1]);
				}
			}
			first_ten[i][s].swap(gnom_adj);
		}
	}

	for (int i = 0 ; i < N ; ++i) {
		power_of_ten[i][0][0].swap(first_ten[i][10]);
	}

	for (int p = 1 ; p < 9 ; ++p) {
		for (int i = 0 ; i < N ; ++i) {
			std::unique_ptr<GnomIndex> edge_away(power_of_ten[i][p - 1][0]->copy());
			for (int s = 1 ; s < 10 ; ++s) {
				GnomIndex::Iterator edge_away_iter(edge_away->iterator());
				std::unique_ptr<GnomIndex> next_away(new GnomIndex(-1, N));
				while (edge_away_iter.has_next()) {
					next_away->add(*power_of_ten[edge_away_iter.current()][p - 1][0]);
				}
				auto level = next_away->copy();
				power_of_ten[i][p - 1][s].swap(level);
				edge_away.swap(next_away);
			}
			power_of_ten[i][p][0].swap(edge_away);
		}
	}

	int M;
	std::cin>>M;
	for (int i = 0 ; i < M ; ++i) {
		int k, x;
		std::cin>>k>>x;
		--x;
		std::unique_ptr<GnomIndex> edge(first_ten[x][k % 10]->copy());
		k = k / 10;
		int ten_pow = 0;
		while (k > 0 && edge->iterator().has_next()) {
			if ((k % 10) != 0) {
				auto iter(edge->iterator());
				std::unique_ptr<GnomIndex> next_step(new GnomIndex(-1, N));
				while (iter.has_next()) {
					next_step->add(*power_of_ten[iter.current()][ten_pow][(k - 1) % 10]);
				}
				edge.swap(next_step);
			}

			k = k / 10;
			++ten_pow;
		}
		auto result(edge->iterator());
		std::stringstream string_stream;
		int count = 0;
		while (result.has_next()) {
			if (count > 0) {
				string_stream<<' ';
			}
			string_stream<<result.current() + 1;
			++count;
		}
		if (count > 0) {
			std::cout<<count<<std::endl<<string_stream.str()<<std::endl;
		} else {
			std::cout<<0<<std::endl<<-1<<std::endl;
		}
	}
}
