package coder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Hashtable;

public class PageRank {
	
	private static double epsilone = 0.01;
	private static double beta = 0.8;
	public static String BASE_PATH = "/home/ilya/Downloads/";

	public static void main(String[] args) throws IOException {
		InputStream input = new FileInputStream(BASE_PATH + "web-Google.txt");
		InputStreamReader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
		BufferedReader buffer = new BufferedReader(reader);
		//skip header
		for (int i = 0 ; i < 4 ; ++i) {
			buffer.readLine();
		}
		
		Hashtable<Integer, Integer> adjacencyList = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Hashtable<Integer, Double>> inwardLinks = 
				new Hashtable<Integer, Hashtable<Integer, Double>>(adjacencyList.size());
		Hashtable<Integer, Double> r = new Hashtable<Integer, Double>();
		String nextLine = null;
		int maxNode = 0;
		while ((nextLine = buffer.readLine()) != null) {
			String[] parts = nextLine.split("\t");
			int from = Integer.valueOf(parts[0]);
			int to = Integer.valueOf(parts[1]);
			if (!adjacencyList.containsKey(from)) {
				adjacencyList.put(from, 0);
			}
			
			adjacencyList.put(from, adjacencyList.get(from) + 1);
			if (!inwardLinks.containsKey(to)) {
				inwardLinks.put(to, new Hashtable<Integer, Double>());
			}
			
			inwardLinks.get(to).put(from, beta);
			if (maxNode < from + 1) {
				maxNode = from + 1;
			}
			if (maxNode < to + 1) {
				maxNode = to + 1;
			}
			r.put(from, 1.0);
			r.put(to, 1.0);
		}
		
		for (int node : r.keySet()) {
			r.put(node, r.get(node) / r.size());
		}
		Hashtable<Integer, Double> rNew = r;
		do {
			r = rNew;
			rNew = makeStep(r, inwardLinks, adjacencyList);
		} while (distance(r, rNew) > epsilone);
		
		buffer.close();
		System.out.println(r.get(99));
	}

	private static double distance(Hashtable<Integer, Double> r, Hashtable<Integer, Double> rNew) {
		double distance = 0;
		for (int node : r.keySet()) {
			distance += Math.abs(r.get(node) - rNew.get(node));
		}
		return distance;
	}

	private static Hashtable<Integer, Double> makeStep(
			Hashtable<Integer, Double> r,
			Hashtable<Integer, Hashtable<Integer, Double>> inwardLinks,
			Hashtable<Integer,Integer> adjacencyList) {
		Hashtable<Integer, Double> rNew = new Hashtable<Integer, Double>(r.size());
		double sum = 0;
		for (int node : r.keySet()) {
			double newValue = 0;
			if (inwardLinks.containsKey(node)) {
				for (int from : inwardLinks.get(node).keySet()) {
					newValue += r.get(from) * inwardLinks.get(node).get(from) / adjacencyList.get(from);
				}
			}
			newValue += (1 - beta) / r.size();
			sum += newValue;
			rNew.put(node, newValue);
		}
		
		if (sum < 1) {
			for (int node : rNew.keySet()) {
				rNew.put(node, rNew.get(node) + (1 - sum) / rNew.size());
			}
		}
		
		return rNew;
	}
}
