# jloganalyzer

My own log analyzer.

I needed some tool for filtering logs. I found that Perl is ugly, and awk requires a lot of time to learn, 
before I would be able to achieve my goals with it.

So I stopped with Java. It takes less time to me to implement all I need comparing to use Perl/awk.
And that fun after all..

**usage: jloganalyzer [options] file1 file2 ... fileN**\
 _-cl,--count-lines_             Counts printing lines\
 _-f,--from <date_from>_         The date/time to read log from\
 _-fmt,--time-format <format>_   Date/time format (using Java\
                               DateTimeFormatter). By default - yyyy-MM-dd HH:mm:ss.SSS\
 _-g,--grep <string>_            Prints only lines containing <string>\
 _-h,--head <lines>_             Only prints the first head <lines>\
 _-help,--help_                  Help\
 _-p,--pos <segments>_           Prints only given <segments> of line in %1%2.. format\
 _-s,--silent_                   Silent mode. Use with -cl\
 _-t,--to <date_to>_             The date/time to read log to
