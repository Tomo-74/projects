#ifndef CHRCAT_H
#define CHRCAT_H

#include <stdio.h>
#include <string.h>	// strcmp(), strlen(), strdup()
#include "error.h"
#include "list.h"

typedef struct {
	char* name;
	char* targetChars;
	int count;
} *ChrCat;


/**
 * Calculates and returns the length of a char array.
 *
 * @param array a char array to find the length of
 * @return the length of the provided array
 */
extern int getArrayLength(char* array);


/**
 * Takes in a struct whose members contain the information of a user-defined
 * character category. Adds this user-defined category to a categories container,
 * such as a list or an array.
 *
 * @param name an array of characters representing the name of a category
 * @param targetChars an array of characters representing the characters that will be searched for in the user's input
 * @return a pointer to the location of the newly constructed ChrCat
 */
extern ChrCat newCat(char* name, char* targetChars);


/**
 * Calculates and returns the number of occurrences of target characters in a char array 
 *
 * @param input the string to search through
 * @param inputLen the length of the string
 * @param targetChars an array of characters to search the input string for
 * @param numTargetChars the number of elements in the targetChars array
 * @return count the number of occurences
 */
extern int countOccurences(char* input, ssize_t inputLen, char* targetChars, int numTargetChars);

#endif
