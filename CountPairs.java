package org.coder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CountPairs {
	
	public static void main(String[] args) throws IOException {
		
		long pairCount = 0;
		File directory = new File(PairPartitioner.PARTITIONING_PATH);
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}
			
			System.out.println(file.getName());
			pairCount += processFile(file);
		}
		
		System.out.println(pairCount);
		
//		System.out.print(isNeighbours("ABCZZ", "ZABCZ"));
	}

	private static long processFile(File file) throws IOException {
		long pairCount = 0;
		
		List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));
		
		List<List<String>> lengthPartitioned = partitionByLength(lines);
		
		for (List<String> linesWithCloseLength : lengthPartitioned) {
			for (int i = 0 ; i < linesWithCloseLength.size() ; ++i) {
				for (int j = 0 ; j < linesWithCloseLength.size(); ++j) {
					if (isNeighbours(linesWithCloseLength.get(i), linesWithCloseLength.get(j))) {
						pairCount++;
					}
				}
			}
		}
		
		return pairCount;
	}

	private static List<List<String>> partitionByLength(List<String> lines) {
		
		SortedMap<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		for (String line : lines) {
			int lineMetric = getLineMetric(line);
			if (!map.containsKey(lineMetric)) {
				map.put(lineMetric, new ArrayList<String>());
			}
			map.get(lineMetric).add(line);
		}
		
		ArrayList<String> lengthPartitioned = null;
		int previousLength = -10;
		for (Integer length : map.keySet()) {
			if (previousLength != length - 1) {
				lengthPartitioned = new ArrayList<String>();
				result.add(lengthPartitioned);
			}
			lengthPartitioned.addAll(map.get(length));
			previousLength = length;
		}
		
		return result;
	}

	private static int getLineMetric(String line) {
		return line.length();
	}

	private static boolean isNeighbours(String first, String second) {
		if (Math.abs(first.length() - second.length()) > 1) {
			return false;
		}
		if (first.length() == second.length()) {
			return substitute(first, second);
		}
		if (first.length() > second.length()) {
			return insert(first, second);
		}
		return insert(second, first);
	}

	private static boolean insert(String longer, String shorter) {
		boolean missing = false;
		for (int i = 0 ; i < shorter.length() ; ++i) {
			if (longer.charAt(i + (missing ? 1 : 0)) != shorter.charAt(i)) {
				if (missing) {
					return false;
				}
				missing = true;
			}
		}
		return true;
	}

	private static boolean substitute(String first, String second) {
		boolean missing = false;
		for (int i = 0 ; i < first.length() ; ++i) {
			if (first.charAt(i) != second.charAt(i)) {
				if (missing) {
					return false;
				}
				missing = true;
			}
		}
		return true;
	}
}
