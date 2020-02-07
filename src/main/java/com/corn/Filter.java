package com.corn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Oleg Z. (cornknight@gmail.com)
 */
public class Filter {
	private static      long   MAX_LINES = Long.MAX_VALUE;
	public static final String FMT       = "YYYY-MM-DD HH:MM:SS.SSS";

	public void filter() {
	/*	SimpleDateFormat sf    = new SimpleDateFormat(FMT);
		long             count = 0;

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
		}*/
	}


}

