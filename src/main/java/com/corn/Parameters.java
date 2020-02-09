package com.corn;

import org.apache.commons.cli.*;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static java.lang.System.exit;

/**
 * @author Oleg Z. (cornknight@gmail.com)
 */
public class Parameters {

	public static final  String  SILENT           = "s";
	public static final  String  FROM             = "f";
	public static final  String  TO               = "t";
	public static final  String  HEAD             = "h";
	public static final  String  POS              = "p";
	public static final  String  GREP             = "g";
	public static final  String  REGEXP           = "r";
	public static final  String  HELP             = "help";
	public static final  String  COUNT_LINES      = "cl";
	public static final  String  FMT              = "fmt";
	public static final  String  DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final Pattern posPattern       = Pattern.compile("^(%\\d+)+");

	private final Pattern       regexp;
	private final String        grep;
	private final Instant       from;
	private final Instant       to;
	private final Long          head;
	private final List<File>    files;
	private final List<Integer> pos;
	private final boolean       count;
	private final String        dateFmt;
	private final boolean       silent;

	public Parameters(String[] args) throws ParseException {
		Options options = new Options()
				.addOption(Option.builder(FROM).longOpt("from").hasArg().desc("The date/time to read log from").argName("date_from").build())
				.addOption(Option.builder(TO).longOpt("to").hasArg().desc("The date/time to read log to").argName("date_to").build())
				.addOption(Option.builder(HEAD).longOpt("head").hasArg().desc("Only prints the first head <lines>").argName("lines").build())
				.addOption(Option.builder(POS).longOpt("pos").hasArg().desc("Prints only given <fields> of line in %1%2.. format").argName("fields").build())
				.addOption(Option.builder(GREP).longOpt("grep").hasArg().desc("Prints only lines containing <string>").argName("string").build())
				.addOption(Option.builder(REGEXP).longOpt("regexp").hasArg().desc("Prints only lines matching <regexp>").argName("regexp").build())
				.addOption(Option.builder(HELP).longOpt(HELP).hasArg(false).desc("Help").argName("string").build())
				.addOption(Option.builder(COUNT_LINES).longOpt("count-lines").hasArg(false).desc("Counts printing lines").build())
				.addOption(Option.builder(SILENT).longOpt("silent").hasArg(false).desc("Silent mode. Use with -" + COUNT_LINES).build())
				.addOption(Option.builder(FMT).longOpt("time-format").hasArg().desc("Date/time format (using Java DateTimeFormatter). By default - " + DEFAULT_DATE_FMT).argName("format").build());

		CommandLineParser parser = new DefaultParser();
		CommandLine       cmd    = parser.parse(options, args);

		if (cmd.hasOption(HELP) || args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("jloganalyzer [options] file1 file2 ... fileN", options);
			exit(0);
		}

		if (cmd.hasOption(HEAD))
			head = Long.parseLong(cmd.getOptionValue(HEAD));
		else
			head = null;

		silent = cmd.hasOption(SILENT);
		count = cmd.hasOption(COUNT_LINES);

		if (cmd.hasOption(POS))
			pos = parsePos(cmd.getOptionValue(POS));
		else
			pos = null;

		if (cmd.hasOption(GREP) && cmd.hasOption(REGEXP)) {
			throw new ParseException("Use either '"+GREP+"' or "+"'"+REGEXP+"' option.");
		}

		if (cmd.hasOption(GREP))
			grep = cmd.getOptionValue(GREP);
		else
			grep = null;

		if (cmd.hasOption(REGEXP)) {
			String regexpStr = cmd.getOptionValue(REGEXP);
			try {
				regexp = Pattern.compile(regexpStr);
			} catch (PatternSyntaxException e) {
				throw new ParseException("regexp error: "+e.getMessage());
			}
		} else
			regexp = null;

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

		List<String> fileNames = cmd.getArgList();
		if (fileNames.isEmpty())
			throw new ParseException("No files to process.");
		else {
			files = new ArrayList<>();
			for (String f : fileNames) {
				File file = new File(f);
				if (!file.exists())
					throw new ParseException("File " + f + " not found.");
				else
					files.add(file);
			}
		}
	}

	private List<Integer> parsePos(String posStr) throws ParseException {
		if (posPattern.matcher(posStr).matches()) {
			String[] pos = posStr.substring(1).split("%");
			return Arrays.stream(pos).map(Integer::parseInt).collect(Collectors.toList());
		} else {
			throw new ParseException("Pos should contain <segments> in format %num1%num2..%numN");
		}
	}

	public Instant parseDate(String dt) throws ParseException {
		try {
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFmt, Locale.US);
			LocalDateTime     ldt = LocalDateTime.parse(dt, fmt);
			return ldt.toInstant(ZoneOffset.UTC);
		} catch (Exception e) {
			throw new ParseException(e.getMessage());
		}
	}

	public String getGrep() {
		return grep;
	}

	public Instant getFrom() {
		return from;
	}

	public Instant getTo() {
		return to;
	}

	public Long getHead() {
		return head;
	}

	public List<File> getFiles() {
		return files;
	}

	public List<Integer> getPos() {
		return pos;
	}

	public boolean isCount() {
		return count;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	public boolean isSilent() {
		return silent;
	}

	public Pattern getRegexp() {
		return regexp;
	}

	@Override
	public String toString() {
		return "Parameters{" +
				"regexp=" + (regexp==null? null :regexp.pattern()) + '\'' +
				", grep='" + grep + '\'' +
				", from=" + from +
				", to=" + to +
				", head=" + head +
				", files=" + files +
				", pos=" + pos +
				", count=" + count +
				", dateFmt='" + dateFmt + '\'' +
				", silent=" + silent +
				'}';
	}
}
