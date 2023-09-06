Thomas Lonowski
CS 253
HW6

*****************************
 Compile & run instructions
*****************************
	1. Navigate to the directory where the program is stored ("~/.../hw6/").
	2. Compile the program by running the "make" command. This creates an object file named "hw6" by default. 
	3. Run the object file by typing its name ("hw6") followed by an input file, text file, and an even number of "name/target characters" pairs (see example below).
	   To use stdin or stdout instead of files, type a - instead of a file name.
	
	***
	Compilation and run example (stdin/stdout):
	***
		[thomaslonowski@onyxnode56 hw6]$ make
		gcc -o chrcats.o -c chrcats.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o chrcat.o -c chrcat.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o list.o -c list.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o main.o -c main.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o hw6 chrcats.o chrcat.o list.o main.o -g -Wl,-Map=hw6.map
		[thomaslonowski@onyxnode56 hw6]$ hw6 - - digits 0-9 upperVowels AEIOU
		HellO World 123        
		<letters 10> <lower consonants 5> <lower vowels 2> <upperVowels 1> <digits 3> 

	***
	Compilation and run example (files):
	***
		[thomaslonowski@onyxnode56 hw6]$ make
		gcc -o chrcats.o -c chrcats.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o chrcat.o -c chrcat.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o list.o -c list.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o main.o -c main.c -g -Wall -MMD -D_GNU_SOURCE
		gcc -o hw6 chrcats.o chrcat.o list.o main.o -g -Wl,-Map=hw6.map
		[thomaslonowski@onyxnode56 hw6]$ hw6 i.txt o.txt digits 0-9 upperVowels AEIOU
		[thomaslonowski@onyxnode56 hw6]$ cat o.txt 
		<letters 10> <lower consonants 7> <lower vowels 3> <upperVowels 0> <digits 0> [thomaslonowski@onyxnode56 hw6]$ 

	***
	Special characters:
	***
		^	a carrot designates capitilization folding (only when a carrot is the first character). Characters following a carrot are case-insensitive (both upper and lower case are counted).
		-	a hyphen designated a character range. "0-9" designates all digits between 0 and 9. "A-Z" designates all capital letters between A and Z.


**************************
 Debugging documentation
**************************
	Got a segmentation fault when running the program. Used gdb with the -tui flag, setting
	breakpoints at main.c:31, 37, and 51 (at the time). The seg fault came at line 52, and I
	discovered it was caused by the strcmp method. I tried a few different things to get its
	arguments to both be pointers, and eventually I got it to work. Previously, I had been
	comparing the name of the file (char*) to the string literal "-", which evaluated to an int.
	

*************************
 Valgrind documentation
*************************
	[thomaslonowski@onyxnode56 hw6]$ make valgrind
	echo -e "Hello\nworld!" | valgrind --leak-check=full ./hw6 
	==413252== Memcheck, a memory error detector
	==413252== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
	==413252== Using Valgrind-3.18.1 and LibVEX; rerun with -h for copyright info
	==413252== Command: ./hw6
	==413252== 
	Usage: ./hw6 <INPUT_FILE> <OUTPUT_FILE> {<CATEGORY_NAME> <CHARACTERS>...}
	==413252== 
	==413252== HEAP SUMMARY:
	==413252==     in use at exit: 0 bytes in 0 blocks
	==413252==   total heap usage: 0 allocs, 0 frees, 0 bytes allocated
	==413252== 
	==413252== All heap blocks were freed -- no leaks are possible
	==413252== 
	==413252== For lists of detected and suppressed errors, rerun with: -s
	==413252== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 0 from 0)
	make: *** [GNUmakefile:24: valgrind] Error 1
