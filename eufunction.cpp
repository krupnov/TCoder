/*
 * eufunction.cpp
 *
 *  Created on: Dec 2, 2015
 *      Author: ilya
 */
#include <iostream>

typedef unsigned long long number;

namespace {
	int F(number x)  {
		int digitSum = 0;
		while (x != 0) {
			digitSum += x % 10;
			x = x / 10;
		}
		return digitSum > 9 ? F(digitSum) : digitSum;
	}

	number computePeriodSum(number Al, number D, int period) {
		number sum = 0;
		for (int i = 0 ; i < period ; ++i) {
			sum += F(Al + i * D);
		}
		return sum;
	}
}

int main() {
	int T;
	std::cin>>T;
	for (int t = 0 ; t < T ; ++t) {
		number A,D,L,R;
		std::cin>>A>>D>>L>>R;
		int period = 9;
		if (D % 3 == 0) {
			period = 3;
		}
		if (D % 9 == 0) {
			period = 1;
		}
		--L; --R;
		number periodSum = computePeriodSum(A, D, period);
		number left = (L / period) * period;
		number right = (R / period + 1) * period;
		number fullCycles = (right - left) / period;
		number result = periodSum * fullCycles;
		result -= computePeriodSum(A, D, L - left);
		result -= computePeriodSum(A + (R + period - right + 1) * D, D, right - R - 1);
		std::cout<<result<<std::endl;
	}
}
