#include "chrcat.h"


/////////////////
/// Functions ///
/////////////////
extern int getArrayLength(char* array) {
	int length = 0;
	for(char* i = array; *i != '\0'; i++) {	// Loop until the end of the array is reached
		length++;
	}
	return length;
}


/**
 * Determines whether a given character is alphabetic or not.
 *
 * @param c a character to check
 * @ return isAlpha whether or not c is alphabetic
*/
static int isAlpha(char c) {
	int isAlpha = 0;
	if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) isAlpha = 1;
	return isAlpha;
}


extern ChrCat newCat(char* name, char* targetChars) {
	ChrCat newCat = (ChrCat)malloc(sizeof(*newCat));
	if(!newCat) ERROR("malloc() failed");

	newCat->name = name;
	newCat->targetChars = targetChars;
	newCat->count = 0;
	
	return newCat;
}


extern int countOccurences(char* input, ssize_t inputLen, char* targetChars, int numTargetChars) {
	int count = 0;
	int foundMatch = 0;

	for(int i = 0; i < inputLen-1; i++) {	// For each char in the user's input line (-1 to account for \0 added by getline)
		for(int j = 0; j < numTargetChars; j++) {	// For each targetChar
			/* Capitalization folding ^ */
			if(targetChars[0] == '^' && isAlpha(input[i])) {	// If the current targetChar is a carrot and the current input char is alphabetic
				for(int k = 1; k < numTargetChars; k++) {
					if(input[i] == targetChars[k] || input[i] == targetChars[k]-32 || input[i] == targetChars[k]+32) {	// If the input char and the target char are equal regardless of case
						count++;
						foundMatch = 1;
						break;
					}
				}
			}
			
			/* Char range - */
			else if(targetChars[j] == '-' && j-1 >= 0 && j+1 <= numTargetChars) {	// Else if the current targetChar is a hyphen that is not located at the front or end of targetChars
				char startChar = targetChars[j-1];
				char endChar = targetChars[j+1];
	
				for(int k = startChar; k < endChar; k++) {
					if(input[i] == k) {
						count++;
						foundMatch = 1;
						break;
					}
				}
			}
			
			/* No special characters */
			else if(input[i] == targetChars[j]) {
				count++;
				break;
			}
			
			if(foundMatch) { foundMatch = 0; break; }	// Boolean flag used to prevent the j for-loop from continuing to loop after a character match has already been found
		}
	}
	return count;
}
