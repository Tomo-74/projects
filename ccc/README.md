# Character Chategory Counter  
- Author: Thomas Lonowski
- Date: Fall 2022

## Description
**Character Category Counter** (ccc): counts the occurrences of specified characters in a given input.
- Usage: `ccc <inputSource> <outputSource> <categoryName targetCharacters>`  

Takes input from a text file or stdin. Allows the user to designate "character categories." These categories contain characters which the user would like to count in the specified input. The program counts the number of hits of each "character category" in the input, and prints the results to a designated output file or stdout.  

Special characters:  
- `^` when specifying character categories on the command line, a carrot designates capitilization folding (only when a carrot is the first character). Characters following a carrot are case-insensitive (both upper and lower case are counted).  
- `-` when specifying character categories on the command line, a hyphen designates a character range. E.g. `0-9` designates all digits between 0 and 9, inclusive. `A-Z` designates all capital letters between A and Z, inclusive.

## Compile & run instructions (Linux)
### Installing dependencies
1. Check if `make` is installed on your system:
	- `sudo apt update`
	- `make -version`
3. If `make` is not installed, install it:
	- `sudo apt install make`
4. Check the `make` installation:
	- `ls /usr/bin/make`

### Compilation and run
1. Navigate to the directory where the `ccc` program is stored.
2. Compile the program:
   	- `make`
4. Run the object file, including as command line arguments input and output sources and an even number of character categories:
	- `ccc <inputSource> <outputSource> <categoryName targetCharacters>`  
<br>

- To use text files as I/O sources, type the file names. To use stdin/stdout, type a hyphen (-) for both input and output. These methods can be mixed:
	- `i.txt o.txt	// Using input and output text files`
   	- `i.txt -	// Using input text file and stdout`
	- `- o.txt	// Using stdin and output text file`
  	- `- -		// Using stdin and stdout`
	
### Compilation and run example (using stdin/stdout):  
	[thomaslonowski@onyxnode56 ccc]$ make  
	gcc -o chrcats.o -c chrcats.c -g -Wall -MMD -D_GNU_SOURCE   
	gcc -o chrcat.o -c chrcat.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o list.o -c list.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o main.o -c main.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o ccc chrcats.o chrcat.o list.o main.o -g -Wl,-Map=ccc.map  
	[thomaslonowski@onyxnode56 ccc]$ ccc - - digits 0-9 upperVowels AEIOU  
	HellO World 123  
	<letters 10> <lower consonants 5> <lower vowels 2> <upperVowels 1> <digits 3>
 ### Compilation and run example (using files):  
	[thomaslonowski@onyxnode56 ccc]$ make  
	gcc -o chrcats.o -c chrcats.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o chrcat.o -c chrcat.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o list.o -c list.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o main.o -c main.c -g -Wall -MMD -D_GNU_SOURCE  
	gcc -o ccc chrcats.o chrcat.o list.o main.o -g -Wl,-Map=ccc.map  
	 [thomaslonowski@onyxnode56 ccc]$ ccc i.txt o.txt digits 0-9 upperVowels AEIOU  
	 [thomaslonowski@onyxnode56 ccc]$ cat o.txt  
	 <letters 10> <lower consonants 7> <lower vowels 3> <upperVowels 0> <digits 0>
     
- After compiling and running, project directory can be cleaned up with the following command: `make clean`
- Analysis of the program's memory usage can be seen with the following command: `make valgrind`  
  
<br>  

## Valgrind documentation
### Program halts with no memory leaks
	[thomaslonowski@onyxnode56 ccc]$ make valgrind  
	echo -e "Hello\nworld!" | valgrind --leak-check=full ./ccc  
	==413252== Memcheck, a memory error detector  
	==413252== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.  
	==413252== Using Valgrind-3.18.1 and LibVEX; rerun with -h for copyright info  
	==413252== Command: ./ccc  
	==413252==  
	Usage: ./ccc <INPUT_FILE> <OUTPUT_FILE> {<CATEGORY_NAME> <CHARACTERS>...}  
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
