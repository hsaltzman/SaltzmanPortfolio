/* Henry Saltzman */

#include <stdio.h>
#include "macros.h"

int main(){

	char array [7];
	int numChars, j, chInt;
	char ch;
	unsigned char character;
	numChars = 0;
	ch = getchar();
	/* read chars until end of file reached */
	while (ch != EOF) {

		array[numChars] = ch;
		ch = getchar();
		
		numChars = numChars + 1;
		
		/* if array has been filled */
		if (numChars == 7) {
			int i;
			char retChar;
			numChars = 0;
			retChar = 0;
			
			for (i = 0; i < 7; i++) {
				chInt = array[i];
				character = chInt;
				/* retchar created by right operating 1 bit and then masking with 64 if the most significant bit in character is a 1*/
				retChar = retChar >> 1;
				if (((int) character) > 127) {
					retChar = retChar | 64;
					character = character & 127;
				}
				
				putchar(character);
			}
			/* output 8th character */
			
			putchar(retChar);
		}
		
	}
	/* output extra elements */
	for (j = 0; j < numChars; j++) {
		chInt = array[j];
		character = chInt;
		putchar(character);
	}

	return 0;

}



	
						
