for i in {3..100}
do
 	vars=$i
   	tests=`expr $RANDOM % $vars`
	tests=`expr $tests + $RANDOM % $vars`
	tests=`expr $tests + $RANDOM % $vars`
	tests=`expr $tests + $RANDOM % $vars`
	tests=`expr $tests + $RANDOM % $vars`
	tests=`expr $tests + $RANDOM % $vars`
	
	#echo "Testing $vars $tests"
	java assig2.GenerateTests $vars 10 > a.txt
	


	
	a=`java assig2.Assig2 a.txt`
	START=$(date +%s)
	END=$(date +%s)
	DIFF=$(( $END - $START ))
	STARTS=$(date +%s)
	b=`java assig2.Assig2 -nobrute a.txt`
	ENDS=$(date +%s)
	DIFFS=$(( $END - $START ))
	
	echo "$DIFF $DIFF2"
	if [ $a != $b ]
	then
		echo "Error:"
		echo "Implication graph result: $a"
		echo "Brute force result: $b"
		echo "See failing test case in a.txt"
		exit 1
	fi
done