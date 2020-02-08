package com.corn;

import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) {
		try {
			Parameters parameters = new Parameters(args);
			Reader reader = new Reader(parameters);
			reader.go();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}
}
