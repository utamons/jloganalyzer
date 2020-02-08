package com.corn;

import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			Parameters parameters = new Parameters(args);
			Reader     reader     = new Reader(parameters);
			reader.go();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getClass() + " - " + e.getMessage());
		}
	}
}
