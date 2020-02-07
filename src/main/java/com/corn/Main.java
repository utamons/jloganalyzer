package com.corn;

import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) {
		try {
			Parameters parameters = new Parameters(args);
			System.out.println("args = " + parameters.toString());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}

	}
}
