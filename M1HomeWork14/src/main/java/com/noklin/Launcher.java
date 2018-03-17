package com.noklin;

import java.util.Arrays;
import java.util.Random;

public class Launcher {
	
	public static void main(String[] args) {
		
		Integer[] mass = new Integer[150];
		Integer[] mass2 = new Integer[150];
		Random rand = new Random();
		for(int i = 0; i < mass.length; i++) {
			Integer val =  100 - rand.nextInt(200);
			mass[i] = val;
			mass2[i] = val;
		}
		FourThreadSorter sorter = new FourThreadSorter(mass); 
		sorter.sort((l,r) -> r-l); 
		Arrays.sort(mass2, (l,r) -> r-l);
		System.out.println(Arrays.equals(mass, mass2));
		System.out.println(Arrays.toString(mass));
		System.out.println(Arrays.toString(mass2));
		sorter.sort((l,r) -> l-r); 
		Arrays.sort(mass2, (l,r) -> l-r);
		System.out.println(Arrays.equals(mass, mass2));
		System.out.println(Arrays.toString(mass));
		System.out.println(Arrays.toString(mass2));
		
	}

}