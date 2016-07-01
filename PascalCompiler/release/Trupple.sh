if test -z "$2"; then
	if test "$1" = "-h"; then
		echo "Please specifi parameters first. The source file location has to be the last argument of the command (for example: ./Trupple.sh -v test.pas, where test.pas is a source file)."
		echo ""
		echo "PARAMETERS:"
		echo "-v (verbose): print additional information from the compiler"
		exit;
	fi
fi

echo "NOT USING GRAAL JIT COMPILER!!!!!!!!!!!!"
java -jar Trupple.jar "$@"
