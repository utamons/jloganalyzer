# jloganalyzer

My own log analyzer.

I needed some tool for filtering logs. I found that Perl is ugly, and awk requires a lot of time to learn, 
before I would be able to achieve my goals with it.

So I stopped with Java. It takes less time to me to implement all I need comparing to use Perl/awk.
And that's fun after all..

```
 usage: jloganalyzer [options] file1 file2 ... fileN
 
 -cl,--count-lines             Counts printing lines
 -f,--from <date_from>        The date/time to read log fro
 -fmt,--time-format <format>   Date/time format (using JavaDateTimeFormatter). By default - yyyy-MM-dd HH:mm:ss.SSS
 -g,--grep <string>            Prints only lines containing <string>
 -h,--head <lines>             Only prints the first head <lines>
 -help,--help                  Help
 -p,--pos <segments>           Prints only given <segments> of line in %1%2.. format
 -s,--silent                   Silent mode. Use with -cl
 -t,--to <date_to>             The date/time to read log to
 ```
 
 Use **mvn package** to create jar, and run it with java.
