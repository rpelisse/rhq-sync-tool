ReadMe
======

Here is some sample usage of rhq-sync-tool:

$ ./bin/jon-mig.sh -d exportDir  --url http://localhost:7080/  -u rhqadmin -p 12V9VyCiyKaKSSbzV3dosP export
$ ./bin/jon-mig.sh -d samples/testcase/  --url http://localhost:7080/  -u rhqadmin -p 12V9VyCiyKaKSSbzV3dosP import -f path/to/file
$ ./bin/jon-mig.sh -d exportDir  --url https://localhost:7443/  -u rhqadmin -p rhqadmin export -e

Note: If you want to change the logging settings of the tool, simply add log4j.properties file,
based on the one embedded in the jar, in the classpath. By the default, the run script provided
includes the current directory in the classpath.
