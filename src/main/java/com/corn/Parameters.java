package com.corn;

import org.apache.commons.cli.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.System.exit;

/**
 * @author Oleg Z. (cornknight@gmail.com)
 */
public class Parameters {

	public static final String FROM             = "f";
	public static final String TO               = "t";
	public static final String HEAD             = "h";
	public static final String FILE             = "file";
	public static final String POS              = "p";
	public static final String GREP             = "g";
	public static final String HELP             = "help";
	public static final String COUNT_LINES      = "cl";
	public static final String FMT              = "fmt";
	public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss.SSS";

	private final Instant       from;
	private final Instant       to;
	private final long          head;
	private final String        file;
	private final List<Integer> pos;
	private final boolean       count;
	private final String        grep;
	private final String        dateFmt;

	public Parameters(String[] args) throws ParseException {
		Options options = new Options()
				.addOption(Option.builder(FROM).longOpt("from").hasArg().desc("The date/time to read log from").argName("date_from").build())
				.addOption(Option.builder(TO).longOpt("to").hasArg().desc("The date/time to read log to").argName("date_to").build())
				.addOption(Option.builder(HEAD).longOpt("head").hasArg().desc("Only print the first head <lines>").argName("lines").build())
				.addOption(Option.builder(FILE).longOpt(FILE).hasArg().desc("Path to the log file").argName("path").build())
				.addOption(Option.builder(POS).longOpt("pos").hasArg().desc("Prints only given <segments> of line in %1%2.. format").argName("segments").build())
				.addOption(Option.builder(GREP).longOpt("grep").hasArg().desc("Prints only lines containing <string>").argName("string").build())
				.addOption(Option.builder(HELP).longOpt(HELP).hasArg(false).desc("Help").argName("string").build())
				.addOption(Option.builder(COUNT_LINES).longOpt("count-lines").hasArg(false).desc("Counts printing lines").build())
				.addOption(Option.builder(FMT).longOpt("time-format").hasArg().desc("Date/time format (using Java DateTimeFormatter). By default - "+DEFAULT_DATE_FMT).argName("format").build());

		CommandLineParser parser = new DefaultParser();
		CommandLine       cmd    = parser.parse(options, args);

		if (cmd.hasOption(HELP) || args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("jloganalyzer -file logfile [options]", options);
			exit(0);
		}

		if (cmd.hasOption(HEAD))
			head = Long.parseLong(cmd.getOptionValue(HEAD));
		else
			head = 0L;

		if (cmd.hasOption(FILE))
			file = cmd.getOptionValue(FILE);
		else
			throw new ParseException("Path to the log file is required");

		count = cmd.hasOption(COUNT_LINES);

		if (cmd.hasOption(POS))
			pos = parsePos(cmd.getOptionValue(POS));
		else
			pos = null;

		if (cmd.hasOption(GREP))
			grep = cmd.getOptionValue(GREP);
		else
			grep = null;

		if (cmd.hasOption(FMT))
			dateFmt = cmd.getOptionValue(FMT);
		else
			dateFmt = DEFAULT_DATE_FMT;

		if (cmd.hasOption(FROM))
			from = parseDate(cmd.getOptionValue(FROM));
		else
			from = null;

		if (cmd.hasOption(TO))
			to = parseDate(cmd.getOptionValue(TO));
		else
			to = null;
	}

	private List<Integer> parsePos(String posStr) {
		String[] pos = posStr.substring(1).split("%");
		if (pos.length == 0)
			throw new RuntimeException("Empty pos");
		else {
			return Arrays.stream(pos).map(Integer::parseInt).collect(Collectors.toList());
		}
	}

	private Instant parseDate(String dt) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFmt, Locale.US);
		LocalDateTime     ldt = LocalDateTime.parse(dt, fmt);
		return ldt.toInstant(ZoneOffset.UTC);
	}

	public Instant getFrom() {
		return from;
	}

	public Instant getTo() {
		return to;
	}

	public long getHead() {
		return head;
	}

	public String getFile() {
		return file;
	}

	public List<Integer> getPos() {
		return pos;
	}

	public boolean isCount() {
		return count;
	}

	public String getGrep() {
		return grep;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	@Override
	public String toString() {
		return "Parameters{" +
				"from=" + from +
				", to=" + to +
				", head=" + head +
				", file='" + file + '\'' +
				", pos=" + pos +
				", count=" + count +
				", grep='" + grep + '\'' +
				", dateFmt='" + dateFmt + '\'' +
				'}';
	}
}
