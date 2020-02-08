package com.corn;

import org.apache.commons.cli.ParseException;

import java.io.*;
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
			files.sort(Comparator.comparing(this::getFirstDate));
			System.out.println(files);
		}
	}

	@SuppressWarnings("UnnecessaryContinue")
	private Instant getFirstDate(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[]      parts   = line.split(" ");
				StringBuilder dateStr = new StringBuilder();
				for (int i = 0; i < parameters.getDateFmt().split(" ").length; ++i) {
					dateStr.append(parts[i]);
				}
				try {
					return parameters.parseDate(dateStr.toString());
				} catch (ParseException e) {
					continue;
				}
			}
			System.err.println("File "+file.getName()+" doesn't contain dates in format "+parameters.getDateFmt());
			return Instant.now();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return Instant.now();
		}
	}
}

