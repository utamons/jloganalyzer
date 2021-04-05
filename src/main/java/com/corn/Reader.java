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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

 /**
 * @author Oleg Z. (cornknight@gmail.com)
 */
public class Reader {
	private final Parameters parameters;
	private final List<File> files;
	long count = 0;

	public Reader(Parameters parameters) {
		this.parameters = parameters;
		files = parameters.getFiles();
	}

	public void go() throws IOException {
		if (files.size() > 1) {
			files.sort(Comparator.comparing(this::getFirstDate));
		}
		for (File f : files) {
			readFile(f);
		}
		if (parameters.isCount()) {
			System.out.println("Lines " + count);
		}
	}

	private void readFile(File file) throws IOException {
		Instant last = null;
		long hCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (parameters.getHead() != null && count == parameters.getHead())
					return;
				Instant date = getInstant(line);
				if (last == null && date != null && isDateFit(date))
					last = date;
				else if (last != null && date != null && !isDateFit(date))
					last = null;

				if (hCount == 1) {
					System.out.println("=============== END OF "+parameters.getHMatch()+" LINES AFTER MATCHED ONE =========");
					System.out.println();
					System.out.println("=============== NEXT MATCH OR EOF =========");
					hCount = 0;
				} else if (hCount>1) {
					System.out.println(line);
					hCount--;
				} else if(printFilteredLine(line, last, date) && parameters.getHMatch() != null) {
					hCount = parameters.getHMatch() + 1;
				}
			}
		}
	}

	private boolean printFilteredLine(String line, Instant last, Instant date) {
		if ((date == null && last == null) || (date != null && !isDateFit(date)))
			return false;
		else if (parameters.getGrep() != null && !line.contains(parameters.getGrep()))
			return false;
		else if (parameters.getRegexp() != null && !parameters.getRegexp().matcher(line).matches())
			return false;
		else if (parameters.isSilent()) {
			++count;
			return false;
		} else if (parameters.getPos() == null) {
			System.out.println(line);
		} else {
			String[]      segments      = line.split("[\\s+]+");
			StringBuilder str           = new StringBuilder();
			List<Integer> parametersPos = parameters.getPos();
			for (int pos : parametersPos) {
				if (segments.length >= pos)
					str.append(segments[pos - 1]).append(" ");
			}
			if (str.length() > 0)
				str.setLength(str.length() - 1);
			System.out.println(str.toString());
		}
		++count;
		return true;
	}

	private boolean isDateFit(Instant date) {
		if (parameters.getFrom() != null && parameters.getTo() != null)
			return date.isAfter(parameters.getFrom()) && date.isBefore(parameters.getTo());
		else if (parameters.getFrom() != null)
			return date.isAfter(parameters.getFrom());
		else if (parameters.getTo() != null)
			return date.isBefore(parameters.getTo());
		else
			return true;
	}

	private Instant getFirstDate(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			while ((line = br.readLine()) != null) {
				Instant date = getInstant(line);
				if (date != null)
					return date;
			}

			System.err.println("File " + file.getName() + " doesn't contain dates in format " + parameters.getDateFmt());
			return Instant.now();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return Instant.now();
		}
	}

	private Instant getInstant(String line) {
		final int     dateArrayLength = parameters.getDateFmt().split(" ").length;
		String[]      parts           = line.split(" ");
		StringBuilder dateStr         = new StringBuilder();
		if (parts.length >= dateArrayLength) {
			for (int i = 0; i < dateArrayLength; ++i) {
				dateStr.append(parts[i]).append(" ");
			}
			dateStr.setLength(dateStr.length() - 1);
			try {
				return parameters.parseDate(dateStr.toString());
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
}

