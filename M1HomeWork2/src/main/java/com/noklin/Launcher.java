package com.noklin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class Launcher {

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {

		ClassInspector classInspector = new ClassInspector();
		System.out.println("Empty String size:\t" + classInspector.getInstanceSize(String::new) + " bytes");
		System.out.println("Empty Vector size:\t" + classInspector.getInstanceSize(Vector::new) + " bytes");
		System.out.println("Empty ArrayList size:\t" + classInspector.getInstanceSize(ArrayList::new) + " bytes");
		System.out.println("Empty LinkedList size:\t" + classInspector.getInstanceSize(LinkedList::new) + " bytes");
		System.out.println("Empty TreeSet size:\t" + classInspector.getInstanceSize(TreeSet::new) + " bytes");
		System.out.println("Empty TreeMap size:\t" + classInspector.getInstanceSize(TreeMap::new) + " bytes");
		System.out.println("Empty HashSet size:\t" + classInspector.getInstanceSize(HashSet::new) + " bytes");
		System.out.println("Empty HashMap size:\t" + classInspector.getInstanceSize(HashMap::new) + " bytes");
		System.out.println("Empty LinkedHashSet size:\t" + classInspector.getInstanceSize(LinkedHashSet::new) + " bytes");
		System.out.println("Empty LinkedHashMap size:\t" + classInspector.getInstanceSize(LinkedHashMap::new) + " bytes");
		determineSizeOfQuantity();
	}

	private static void determineSizeOfQuantity() throws InstantiationException, IllegalAccessException {
		ClassInspector classInspector = new ClassInspector();
		for (int i = 0; i < 10; i++) {
			final int itemCount = i;
			Long size = classInspector.getInstanceSize(()-> {
				List<Object> list = new ArrayList<>();
				for (int j = 0; j < itemCount; j++) 
					list.add(new Object());
				return list;
			});
			System.out.println("ArrayList item count: " + itemCount + " size: " + size);
		}
		System.out.println("Зависимость размера контейнера от количества элементов линейная.");
	}

}