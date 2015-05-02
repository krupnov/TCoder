package topcoder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class TennisTournament {
	public static void main(String[] args) throws NumberFormatException, IOException {
		Scanner reader = new Scanner(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.nextLine());
		for (int t = 0 ; t < T ; ++t) {
			String[] params = reader.nextLine().split(" ");
			int N = Integer.parseInt(params[0]);
			int M = Integer.parseInt(params[1]);
			double P = (double)Integer.parseInt(params[2]) / 100;
			
			int K = (int)(Math.log(N) / Math.log(2));
			int firstStepSize = (int)Math.pow(2, K / 2 + K % 2);
			
			int secondStepSize = N / firstStepSize;
			double[] secondStep = new double[secondStepSize];
			int nextPosition = -1;
			int[] neverPeople = new int[M];
			for (int i = 0 ; i < M ; ++i) {
				neverPeople[i] = reader.nextInt();
			}
			Arrays.sort(neverPeople);
			reader.nextLine();
			int m = 0;
			for (int i = 0 ; i < secondStepSize ; ++i) {
				double[] probabilities = new double[firstStepSize];
				if (nextPosition >= firstStepSize * i) {
					probabilities[nextPosition - firstStepSize * i] = 1;
				}
				while (m < M) {
					nextPosition = neverPeople[m] - 1;
					++m;
					if (nextPosition >= firstStepSize * (i + 1)) {
						break;
					}
					probabilities[nextPosition - firstStepSize * i] = 1;
				}
				
				while (probabilities.length > 1) {
					probabilities = nextRound(probabilities, P);
				}
				secondStep[i] = probabilities[0];
			}
			
			while (secondStep.length > 1) {
				secondStep = nextRound(secondStep, P);
			}
			
			System.out.println(secondStep[0] * 100);
			
		}
		
		reader.close();
	}

	private static double[] nextRound(double[] probabilities, double p) {
		double[] next = new double[probabilities.length / 2];
		for (int i = 0 ; i < probabilities.length ; i += 2) {
			double p1 = probabilities[i];
			double p2 = probabilities[i + 1];
			next[i / 2] = p1*p2 + p * (p2 - 2*p2*p1 + p1);
		}
		return next;
	}
}
