#include "chrcats.h"
#include <fcntl.h>	// open()
#include <unistd.h>	// read()

char* prog;

/**
 * Prints the usage message for the character category count program and exits with an error signal. * 
 */
static void usage() {
  fprintf(stderr, "Usage: %s <INPUT_FILE> <OUTPUT_FILE> {<CATEGORY_NAME> <CHARACTERS>...}\n", prog);
}


/**
 * Takes user input and passes it to ccc(), which calculates and prints the number of
 * occurrences of character category elements in the input
 */
int main(int argc, char* argv[]) {
	prog = argv[0];

	/* Usage message and error handling */
	if(argc < 3) {
		usage();
		exit(1);
	} else if((argc - 3) % 2 != 0) {	// Odd number of category/characters pairs
		ERROR("User provided program with an odd number of category/characters pairs");
	}
	
	char* inFile = argv[1];	// File names
	char* outFile = argv[2];
	int ifd;	// File descriptors
	int ofd;

	/* Set file descriptors */
	if(!strcmp(inFile, "-")) ifd = fileno(stdin);	// Case: user wants to read from stdin
	else {						// Case: user wants to read from provided input file
		ifd = open(argv[1], O_RDONLY);
		if(ifd == -1) { usage(); ERROR("Failed to open input file: %s", inFile); }
	}
	
	if(!strcmp(outFile, "-")) ofd = fileno(stdout); // Case: user wants to write to stdout
	else {						// Case: user wants to write to provided output file
		ofd = open(argv[2], O_WRONLY | O_CREAT, 00700);	// If no file exists, create one with all permissions
		if(ofd == -1) { usage(); ERROR("Failed to open output file: %s", outFile); }
	}

	/* Add default categories */
	ChrCats ccs1=0;
	ChrCats ccs2=0;
	ccs1=addCat(ccs1, "lower vowels", "aeiou");
	ccs1=addCat(ccs1, "lower consonants", "bcdfghjklmnpqrstvwxyz");
	ccs1=addCat(ccs1, "letters", "^abcdefghijklmnopqrstuvwxyz");

	// Add user-defined categories
	for(int i = 3; i <= argc-1; i+=2) ccs2=addCat(ccs2, argv[i], argv[i+1]);


	//////////////////////////////////////////////
	// Read input, perform counting, and output //
	//////////////////////////////////////////////
	int inputLen=0;

	if(ifd==fileno(stdin)) {	/* Case: reading from stdin */	
		char* input=0;
		size_t inputSize=0;
		ssize_t inputLen=getline(&input, &inputSize, stdin);	// getline() adds \0 to the end of the input, so inputLen is one more than the number of chars
		ccc(ccs1, input, inputLen);
		ccc(ccs2, input, inputLen);		
	} else { 					/* Case: reading from file */
		enum { BUF_SIZE=100 };
		char buf[BUF_SIZE+1];
		ssize_t bytes;
		while(1) {
			bytes=read(ifd, buf, BUF_SIZE);	// Read BUF_SIZE bytes into a buffer from the input file
			inputLen+=bytes;	// Since a char is 1 byte, the number of bytes read equals the number of chars read
			if(bytes<BUF_SIZE) break;	// Break at EOF
		}
		buf[BUF_SIZE+1]='\0';
		ccc(ccs1, buf, inputLen+1);	// Perform counting
		ccc(ccs2, buf, inputLen+1); 
	}
	
	if(ofd==fileno(stdout)) {	/* Case: writing to stdout */	
		printf("%s", catsToString(ccs1));
		printf("%s\n", catsToString(ccs2));
	} else {	 				/* Case: writing to file */
		write(ofd, catsToString(ccs1), getArrayLength(catsToString(ccs1)));	// Write the toString return value into the output file
		write(ofd, catsToString(ccs2), getArrayLength(catsToString(ccs2)));
	}

	freeCats(ccs1);
	freeCats(ccs2);
	return 0;
}
