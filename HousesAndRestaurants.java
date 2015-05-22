package coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

public class HousesAndRestaurants {
	
	static class Edge implements Comparable<Edge> {
		public Edge(Building source, Building target, int weight) {
			this.source = source;
			this.target = target;
			this.weight = weight;
		}
		
		@Override
		public int compareTo(Edge o) {
			boolean thisSameType = this.source.partition.getParent() == this.target.partition.getParent();
			boolean thatSameType = o.source.partition.getParent() == o.target.partition.getParent();
			if (thisSameType && !thatSameType) {
				return -1;
			}
			if (!thisSameType && thatSameType) {
				return 1;
			}
			return o.weight - this.weight;
		}
		
		Building source;
		Building target;
		int weight;
	}
	
	static class Partition {
		
		public Partition(int rank, Partition parent) {
			this.rank = rank;
			this.parent = parent;
		}
		
		public Partition getParent() {
			Partition grandParent = this;
			while (grandParent.parent != null) {
				grandParent = grandParent.parent;
			}
			
			return grandParent;
		}
		
		int rank;
		Partition parent;
	}
	
	static class Building {
		public Building(Partition partition) {
			this.partition = partition;
		}
		
		Partition partition;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(reader.readLine());
		for (int t = 0 ; t < T ; ++t) {
			String[] nm = reader.readLine().split(" ");
			int N = Integer.parseInt(nm[0]);
			int M = Integer.parseInt(nm[1]);
			
			Partition house = new Partition(0, null);
			Partition rest = new Partition(0, null);
			
			Building[] buildings = new Building[N]; 
			
			String housesAndRest = reader.readLine();
			int houseCount = 0;
			for (int i = 0 ; i < housesAndRest.length() ; ++i) {
				char ch = housesAndRest.charAt(i);
				if (ch == 'H') {
					houseCount++;
					buildings[i] = new Building(house);
				} else {
					buildings[i] = new Building(rest);
				}
			}
			
			PriorityQueue<Edge> edges = new PriorityQueue<Edge>();
			
			for (int m = 0 ; m < M ; ++m) {
				String[] edge = reader.readLine().split(" ");
				edges.add(new Edge(
						buildings[Integer.parseInt(edge[0])],
						buildings[Integer.parseInt(edge[1])],
						Integer.parseInt(edge[2])));
			}
			
			while (houseCount > 0) {
				Edge nextEdge = edges.peek();
				if (nextEdge.source.partition == nextEdge.target.partition
						&& nextEdge.source.partition == rest) {
					continue;
				}
			}
		}
	}
}
