package com.corn;

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

	public Reader(Parameters parameters) {
		this.parameters = parameters;
		files = parameters.getFiles();
	}

	public void go() {
		if (files.size() > 1) {
			System.out.println(files);
			files.sort(Comparator.comparing(this::getFirstDate));
			System.out.println(files);
		}
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

