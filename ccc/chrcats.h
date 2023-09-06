#ifndef CHRCATS_H
#define CHRCATS_H

#include "chrcat.h"

typedef void* ChrCats;


/**
 * Getter method for the length of the toString 
 * @return the length of the toString description
 */
extern int getToStringLength();

/**
 * Constructs a new node that contains a user-defined character category
 * and adds it to a singly-linked list of all the categories.
 *
 * @param name an array of characters representing the name of a category
 * @param targetChars an array of characters representing the characters that will be searched for in the user's input
 * @return the newly constructed node
 */
extern List addCat(ChrCats this, char* name, char* targetChars);


/**
 * Finds the number of times the character categories' target characters occur in a given string input.
 *
 * @param input the string to search through
 * @param inputLen the length of the string
 */
extern void ccc(ChrCats this, char* input, ssize_t len);


/**
 * Returns a string representation of the character category search results.
 *
 * @param this a list of character category structs
 * @param i an integer (must be 0) to start the recursion
 * @return a string representation of the character category count results
 */
extern char* catsToString(ChrCats this);


/**
 * Clears memory allocated during runtime.
 */
extern void freeCats(ChrCats this);

#endif
