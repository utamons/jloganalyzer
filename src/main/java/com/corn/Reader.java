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
	}

	private void readFile(File file) throws IOException {
		Instant last = null;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				Instant date = getInstant(line);
				if (last == null && date != null && isDateFit(date))
					last = date;
				printFilteredLine(line, last, date);
			}
		}
	}

	private void printFilteredLine(String line, Instant last, Instant date) {
		if ((date == null && last == null) || (date != null &&!isDateFit(date)))
			return;
		if (parameters.getGrepPattern() != null && !parameters.getGrepPattern().matcher(line).matches())
			return;
		if (parameters.getHead() != null && count == parameters.getHead())
			return;
		if (parameters.getPos() == null) {
			System.out.println(line);
			++count;
		} else {
			String[]      segments      = line.split("[\\s@&.?$+-]+");
			StringBuilder str           = new StringBuilder();
			List<Integer> parametersPos = parameters.getPos();
			for (int i = 0; i < parametersPos.size(); i++) {
				int pos = parametersPos.get(i);
				if (segments.length - 1 > pos)
					str.append(segments[i]).append(" ");
			}
			if (str.length() > 0)
				str.setLength(str.length() - 1);
			System.out.println(str.toString());
			++count;
		}
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

