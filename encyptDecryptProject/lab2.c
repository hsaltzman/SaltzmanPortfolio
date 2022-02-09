/* Henry Saltzman */

#include <stdio.h>
#include "macros.h"

int main (){

	char array [NUM];
	int numChars, j;
	char ch;
	numChars = 0;
	ch = getchar();
	/* read chars until end of file */
	while (ch != EOF) {
	
		array[numChars] = ch;
		ch = getchar();
		numChars = numChars + 1;
		/* if array has been filled, encrypt all of the chars */
		if (numChars == NUM) {
			int i, pushLeft;
			char lastElement, currChar;
			pushLeft = 7;
			for (i = 0; i < NUM - 1; i++){
				lastElement = array[NUM - 1];
				currChar = array[i];
				lastElement = lastElement << pushLeft;
				lastElement = lastElement & 10000000;
				currChar = currChar | lastElement;
				putchar(currChar);
				pushLeft --;
			}
			numChars = 0;
		}
	}

	/* output all remaining chars that were not encrypted */
	for (j = 0; j < numChars; j++){
		putchar(array[j]);
	}

	return 0;
}

		
