package com.corn;

/*
 *     This file is part of JLoganalyzer.
 *
 *     JLoganalyzer is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     JLoganalyzer is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with JLoganalyzer.  If not, see <https://www.gnu.org/licenses/>.
 */

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
