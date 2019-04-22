# Network analyser

Looking for some suspicious activity in authorization log files. Suspicious activity is 
multiple attempt to login from the same IP address for given period of time.

## Requirements 

If you want to build this project you will install sbt > 1.0 and JDK 8. 
For start you need only JRE 8.

## Build

`sbt assembly`

## Usage

`java -jar network-analyser-0.0-SNAPSHOT.jar <path to input file> -i <interval in seconds> -o <path to result file>`

Options:
* `-o, --output <value>` is path to result file, it's optional
* `-i, --interval <value>` is period of time in seconds, default value is 1 hour

## Input File

```csv
"TheRealJJ","77.92.76.250","2015-11-30 23:11:40"
"loginAuthTest","37.48.80.201","2015-11-30 23:11:51"
"ksiriusr","123.108.246.205","2015-11-30 23:11:55"
```

## Output File

```csv
116.104.116.42,2015-11-30 23:19:09,2015-11-30 23:20:54,"khuongz2:2015-11-30 23:19:09,khuongz3:2015-11-30 23:19:44,khuongz4:2015-11-30 23:20:13,khuongzz:2015-11-30 23:20:54"
87.54.190.189,2015-11-30 23:21:23,2015-11-30 23:21:50,"MattyTheGamerDK:2015-11-30 23:21:23,MattyTheGamerDK:2015-11-30 23:21:50"
```


