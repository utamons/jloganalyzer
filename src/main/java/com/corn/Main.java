package com.corn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
	public static final String FMT = "YYYY-MM-DD HH:MM:SS.SSS";

	public static void main(String[] args) throws IOException {
		SimpleDateFormat sf       = new SimpleDateFormat(FMT);
		long             maxLines = Long.MAX_VALUE;
		if (args.length == 3) {
			maxLines = Integer.parseInt(args[2]);
			System.out.println("Max lines = " + maxLines);
		}

		try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
			long   count = 0;
			String line;
			while ((line = br.readLine()) != null) {
				if (maxLines != 0 && count >= maxLines) {
					break;
				}
				try {
					Date     from  = sf.parse(args[0]);
					String[] parts = line.split(" ");
					if (line.trim().length() > 0 && parts.length < 2) {
						++count;
						System.out.println(line);
					} else if (line.trim().length() > 0) {
						String dateStr = parts[0] + " " + parts[1];
						Date   dt      = sf.parse(dateStr);
						if (dt.getTime() > from.getTime()) {
							++count;
							System.out.println(line);
						}
					}
				} catch (ParseException e) {
					++count;
					System.out.println(line);
				}
			}

			System.out.println("Lines " + count);
		}
	}
}
