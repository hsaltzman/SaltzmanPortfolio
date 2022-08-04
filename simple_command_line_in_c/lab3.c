#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include "macros.h"

/* struct for nodes in the "linked list" of files and directories */
struct Node
{
	char name[MAXSIZE];
	char contents[FILESIZE];
	int count;
	struct Node *arr [MAXSIZE];
	struct Node *parent;
};

/* function to initialize the home root node */
struct Node* makeHome() {
	int i;
	struct Node *root;
	root = malloc(sizeof(struct Node));
	strcpy(root->name, "Home");
	strcpy(root->contents, "\0");
	root->count = 0;
	for (i = 0; i < MAXSIZE; i++){
		root->arr[i] = NULL;
	}
	return(root);
}	

/* function which returns the location in the array of any child */
int getNameLocation(struct Node * currDir, char nameDestination[MAXSIZE]) {
        int returnVal;
        int i;

        returnVal = -1;
        for (i = 0; i < MAXSIZE; i++) {
                if (currDir->arr[i] != NULL && !strcmp(nameDestination, currDir->arr[i]->name)) {
                        returnVal = i;
                        break;
                }
        }
        return(returnVal);

}

/* function supporting the mkdir command, initializes a new directory child in the current directory */
struct Node* mkdir(char nameDir[MAXSIZE]) {
	struct Node *dir;
	int i;
	dir = malloc(sizeof(struct Node));
	strcpy(dir->name, nameDir);
	strcpy(dir->contents, "\0");
	dir->count = 0;
	for (i = 0; i < MAXSIZE; i++){
                dir->arr[i] = NULL;
        }
	
	return(dir);
	
}

/* function supporting the file command, initializes a new file child in the current directory */
struct Node* file(char nameFile[MAXSIZE]) {
	struct Node *file;
	file = malloc(sizeof(struct Node));
	strcpy(file->name, nameFile);
	printf("Enter the file contents\n");
	scanf("%[^\n]%*c", file->contents);	
	file->count = -1;
	
	return(file);

}
/* function which makes a file with arbitrary contents */
struct Node * fileNoContent(char nameFile[MAXSIZE]) {

	struct Node *file;
        file = malloc(sizeof(struct Node));
        strcpy(file->name, nameFile);
	file->count = -1;
	strcpy(file->contents, "Arbitrary Contents");

	return(file);

}

/* function enables the ls command */	
void ls(struct Node *currDir) {
	
	int i;
	for (i = 0; i < MAXSIZE; i++){
                if (currDir->arr[i] != NULL) {
			printf("%s    ", currDir->arr[i]->name);
		}
        }
	printf("\n");			

}

/* function enables the cat command */
void cat(struct Node *currDir, char nameFile[MAXSIZE]) {
	
	int i;
	for (i = 0; i < MAXSIZE; i++){
		if (currDir->arr[i] != NULL && !strcmp(nameFile, currDir->arr[i]->name)) {
			printf("\n%s\n", currDir->arr[i]->contents);
		}
	}
			
}

/* function enables the rm command */
void rm(struct Node *currDir, char nameFile[MAXSIZE]) {

	int i;
	for (i = 0; i < MAXSIZE; i++){
		if (currDir->arr[i] != NULL && !strcmp(nameFile, currDir->arr[i]->name)) {
			
			if (!strcmp(currDir->arr[i]->contents, "\0")){
				printf("\nError: %s is a directory\n", currDir->arr[i]->name);	
			}
			else {
	
				currDir->arr[i] = NULL;
				currDir->count--;
			}
		}
	}	
}

/* function enables the rmdir command */
void rmdir(struct Node *currDir, char nameDir[MAXSIZE]) {

	int i;
        for (i = 0; i < MAXSIZE; i++){
                if (currDir->arr[i] != NULL && !strcmp(nameDir, currDir->arr[i]->name)) {

                        if (!strcmp(currDir->arr[i]->contents, "\0")){
                                int j;
				int emptyDirectory = 1;
				for (j=0; j<MAXSIZE; j++) {
					if (currDir->arr[i]->arr[j] != NULL) {
						printf("\nDirectory %s is nonempty\n", currDir->arr[i]->name);
						emptyDirectory = 0;
						break;
					}
				}
				if (emptyDirectory) {
					currDir->arr[i] = NULL;
					currDir->count--;
				}
			}
			else {
				printf("\nError: %s is a file, not a directory\n", currDir->arr[i]->name);	
						
                        }
                        
               			 }
	}        
}

/* function finds a null element in the children array for inserting a new node */
int findInsertionPoint(struct Node *currDir) {

	int i;
        for (i = 0; i < MAXSIZE; i++){
		if (currDir->arr[i] == NULL) {
			break;
		}
	}

	return(i);
}

/* function returns a pointer to the node at nameDir */
struct Node * findDirNoPath(struct Node *pointToCurrentNode, char nameDir[MAXSIZE]) {
	
	int i;
	struct Node *returnVal;


	for (i = 0; i < MAXSIZE; i++) {
		if (pointToCurrentNode->arr[i] != NULL && !strcmp(nameDir, pointToCurrentNode->arr[i]->name)) {
			returnVal = pointToCurrentNode->arr[i];
			break;
		}	
	}

	
			
	return returnVal;
}

/* function returns a pointer to the home node */
struct Node * findDirPath(struct Node *pointToHomeNode, char namePath[MAXSIZE]) {

	return pointToHomeNode;
}

/* function returns a pointer to the parent node */
struct Node * findDirDoubleDot(struct Node *pointToCurrentNode) {
	
	struct Node *traversalPointer;
	
	traversalPointer = pointToCurrentNode->parent;   
		  
	return (traversalPointer);

}

/* function enables the cd command */
struct Node * cd(struct Node *currDir, char nameDir[MAXSIZE]) {

	struct Node *copyptr;
	
	if (!strcmp("..", nameDir)) { /*double dot*/

		copyptr = findDirDoubleDot(currDir);
	
	}
	else { /*nopath (regular cd)*/
		
		copyptr = findDirNoPath(currDir, nameDir);
	}		
		
	return copyptr;		
	
}

/* function enables the cp command */
void cp(struct Node *currDir, char nameSource[MAXSIZE], char nameDestination[MAXSIZE]) {
	struct Node * newFile;
	struct Node * pointToFile;
	int i, foundElement, repeatName;

	newFile = malloc(sizeof(struct Node));

	foundElement = getNameLocation(currDir, nameSource);
	repeatName = getNameLocation(currDir, nameDestination);
	
	if (foundElement == -1){
		printf("\n%s does not exist.\n", nameSource);
			
	}
	else if (repeatName != -1) {
		printf("\n%s is already a file in this directory\n", nameDestination);
	}
	else if (currDir->count <= 63){
		strcpy(newFile->name, nameDestination);
		strcpy(newFile->contents, currDir->arr[foundElement]->contents);	
		currDir->arr[findInsertionPoint(currDir)] = newFile;
		currDir->count++;		
	}
	else {
		printf("This directory has the max number of elements\n");

	}	
			
}

/* function enables the mv command */
void mv(struct Node * currDir, char nameMove[MAXSIZE], char nameDest[MAXSIZE]) {


	int locationMove, locationDest;
	struct Node * pointer;

	pointer = malloc(sizeof(struct Node));
	locationMove = getNameLocation(currDir, nameMove);
	locationDest = getNameLocation(currDir, nameDest);

	if (locationMove == -1) {

		printf("\nThis element does not exist in your directory\n");
		
	}
        else if (locationDest == -1) {

                strcpy(currDir->arr[locationMove]->name, nameDest);

        }
	/* if first argument is file */
	else if (currDir->arr[locationMove]->count == -1) {
		
		if (currDir->arr[locationDest]->count > 63){

			printf("This directory has the max number of elements\n");
		
		}
		else{
		
		currDir->arr[locationDest]->arr[findInsertionPoint(currDir->arr[locationDest])] = currDir->arr[locationMove];
		currDir->arr[locationMove] = NULL;
		currDir->count--;
		currDir->arr[locationDest]->count++;
		}
		
	}

	else {


				
		if (currDir->arr[locationDest]->count > -1) {
			
			if (currDir->arr[locationDest]->count > 63){

                        	printf("This directory has the max number of elements\n");
                
                	}
                	else{	
				currDir->arr[locationDest]->arr[findInsertionPoint(currDir->arr[locationDest])] = currDir->arr[locationMove];
				currDir->arr[locationMove] = NULL;
				currDir->count--;
				currDir->arr[locationDest]->count++;
			
			}
		}
		else {
			printf("\nCannot move a directory into a file\n");
		}

	}

	

}

/* function enables the dirprint command */
void dirprint(struct Node * currDir) {


	if (!strcmp(currDir->name, "Home")) {

		printf("Current Directory: Home\n");

	}
	else {
		int i;
		int sibsFound = 0;
		printf("Current Directory: %s\n", currDir->name);
		printf("Siblings:");
	
		for (i=0;i<MAXSIZE;i++) {
	
			if (currDir->parent->arr[i] != NULL && strcmp(currDir->parent->arr[i]->name, currDir->name)) {
			
				printf(" %s ", currDir->parent->arr[i]->name);
				sibsFound++;
			
			}
			

		}
		if (!sibsFound) {
                                
                        printf(" None\n");
                        
                        }
                else {  
                       printf("\n");
                        
                }
		currDir = currDir->parent;
		while (strcmp(currDir->name, "Home")) {

		int i;
		sibsFound = 0;
                printf("Parent Directory: %s\n", currDir->name);
                printf("Siblings:");
        
                for (i=0;i<MAXSIZE;i++) {
        
                        if (currDir->parent->arr[i] != NULL && strcmp(currDir->parent->arr[i]->name, currDir->name)) {

                                printf(" %s ", currDir->parent->arr[i]->name);
                      		sibsFound++;		  
                        }

                }
		if (!sibsFound) {

                 printf("None");

                }
		printf("\n");
		currDir = currDir->parent;

		}
		printf("Parent Directory: Home\n");
	}
}

/* function enables the clear command */
void clear (struct Node * currDir) {

	int i;
	for (i = 0; i < MAXSIZE; i++) {

		if (currDir->arr[i] != NULL) {
		
			currDir->arr[i] = NULL;
		}

	}
	currDir->count = 0;

}

/* function enables the edit command */
void edit (struct Node * currDir, char nameFile[MAXSIZE]) {

	int nameLoc = getNameLocation(currDir, nameFile);
	if (nameLoc > -1) {
		printf("Enter the new file contents\n");
        	scanf("%[^\n]%*c", currDir->arr[nameLoc]->contents);
	}
	else {
		printf("%s is not a file\n", nameFile);
	}

}

/* function enables the count command */
void count (struct Node * currDir) {

	int countFile, countDir, i;
	
	for (i = 0; i < MAXSIZE; i++) {
		
		if (currDir->arr[i] != NULL) {
			
			if (!strcmp(currDir->arr[i]->contents, "\0")) {
			
				countDir++;	
			}
			else {

				countFile++;
				
			}
		}
	}	
	printf("Number of files in %s: %d\n", currDir->name, countFile);
	printf("Number of directories in %s: %d\n", currDir->name, countDir);

}

/* function checks if the nameDestination node is in currDir */	
int checkName(struct Node * currDir, char nameDestination[MAXSIZE]) {
	int returnVal;
	int i;

	returnVal = 1;
	for (i = 0; i < MAXSIZE; i++) {
        	if (currDir->arr[i] != NULL && !strcmp(nameDestination, currDir->arr[i]->name)) {
                        returnVal = 0;
                        break;
                }
        }
	return(returnVal);

}

/* function enables the help command */
void help() {

	printf("This system begins in a Home Directory\n");
	printf("This system supports a maximum of 3 arguments: (arg0 (the command), arg1, arg2)\n");
	printf("mkdir: supports 1 or 2 arguments after command, and will create 1 or 2 directories named arg1 and/or arg2\n");
	printf("Directories can hold a maximum of 64 elements (files and directories)\n");
	printf("file: supports 1 or 2 arguments after command, and will create 1 or 2 files named arg1 and/or arg2.");
	printf(" System will prompt for contents of file(s) (Max 256 characters allowed)\n");
	printf("ls: 0 arguments after command, prints all files and directories in current directory.\n");
	printf("cat: supports a max of 1 argument after command, prints the contents of arg1 (which must be a file).\n");
	printf("rm: supports 1 or 2 arguments after command, and will remove arg1 and/or arg2 (files) from current directory.\n");
	printf("rmdir: supports 1 or 2 arguments after command, and will remove arg1 and/or arg2 (empty directories) from current directory.\n");
	printf("cp: supports 2 arguments after command, will copy the contents of arg1 (a file) into a file named arg2.\n");
	printf("mv: supports 2 arguments after command, args1 must be an element in the directory.\n");
	printf("if args2 does not exist in current directory, renames args1 to args2\n");	
	printf("if args2 is a directory, moves args1 into args2\n");
	printf("dirprint: 0 arguments after command, prints current node and siblings up to and including home node\n");
	printf("clear: 0 arguments after command, clears all elements in currend directory\n");
	printf("edit: 1 argument after command, allows user to edit contents of args1 ( a file )\n");
	printf("count: 0 arguments after command, outputs the number of files and directories in the current directory\n");
}

/* function runs the "linux" program */
void runLinux(struct Node * currNode) {

        struct Node *pointToHomeNode;
	struct Node *path;
	struct Node *pathptr;
        char userInput[MAXSIZE];
        char *ptr;
        char *commands[3];
        char **array = commands;
        int numCalls;

        pointToHomeNode = currNode;
	path = malloc(sizeof(struct Node));
	strcpy(path->name, "Home");
	pathptr = path;
	printf("System begins in empty Home directory\n");

	while (1){
				
        	printf("$ ");
		
        	scanf("%[^\n]%*c", userInput);
		
       		ptr = strtok(userInput, " ");
        	numCalls = 0;
        	while (ptr != NULL) {
	
                	array [numCalls] = ptr;
                	ptr = strtok(NULL, " ");
                	numCalls++;
			if (numCalls == 4){
				numCalls = 3;
				
				break;
			}
        	}


		if (!strcmp(array[0], "cd")) {
			
			if (numCalls == 3) {

				printf("\ncd command supports only 1 additional argument\n");
			}
			else if (numCalls == 1) {
				
				if (strcmp(currNode->name, "Home")){
					currNode = pointToHomeNode;
					pathptr = path;
					pathptr->arr[0] = NULL;						
				}
			}

			else if (numCalls == 2) {

				if (!strcmp(array[1], "..")) {
				
					if (strcmp(currNode->name, "Home")){
						
						currNode = cd(currNode, array[1]);
						pathptr = pathptr->parent;
						pathptr->arr[0] = NULL;		
					}
				}
				else {
					currNode = cd(currNode, array[1]);
					pathptr->arr[0] = malloc(sizeof(struct Node));	
					pathptr->arr[0]->parent = pathptr;
					pathptr = pathptr->arr[0];
					strcpy(pathptr->name, currNode->name);
					
				}
			}	
		}

		else if (!strcmp(array[0], "mkdir")) {
			
			int point1 = findInsertionPoint(currNode);
			int point2 = findInsertionPoint(currNode);
			
			if (numCalls == 1) {

				printf("\nmkdir command must have 2 or 3 additional arguments\n");
			}
			else if (numCalls == 2) {
				
				if (checkName(currNode, array[1])) {
					
					if (currNode->count <= 63){
						currNode->arr[point1] = mkdir(array[1]);
						currNode->arr[point1]->parent = currNode;
						currNode->count++;
					}
					else {

						printf("This directory has the max number of elements\n");
					}
				}
				else {
                                        printf("\nCannot name directory a repeat name\n");
                                }
				
			}
			else {
				if (checkName(currNode, array[1])) {

                                        if (currNode->count <= 63){
                                                currNode->arr[point1] = mkdir(array[1]);
                                                currNode->arr[point1]->parent = currNode;
                                                currNode->count++;
                                        }
                                        else {

                                                printf("This directory has the max number of elements\n");
                                        }
                                }
                                else {
                                        printf("\nCannot name directory a repeat name\n");
                                }
				if (checkName(currNode, array[2])) {

                                        if (currNode->count <= 63){
                                                currNode->arr[point1] = mkdir(array[1]);
                                                currNode->arr[point1]->parent = currNode;
                                                currNode->count++;
                                        }
                                        else {

                                                printf("This directory has the max number of elements\n");
                                        }
                                }
                                else {
                                        printf("\nCannot name directory a repeat name\n");
                                }
				
			}
		}

		else if (!strcmp(array[0], "file")) {

			int point1 = findInsertionPoint(currNode);
                        int point2;

                        if (numCalls == 1) {

                                printf("\nFile must have a name\n");
                        }
                        else if (numCalls == 2) {
				
				if (checkName(currNode, array[1])) {

					if (currNode->count <= 63) {
						currNode->arr[point1] = file(array[1]);
						currNode->count++;
					}
					else {

						printf("This directory has the max number of elements\n");
					}
				}
                                else {
					printf("\nCannot name file a repeat name\n");
				}
                                

                        }
                        else {
                                if (checkName(currNode, array[1])) {

                                        if (currNode->count <= 63) {
                                                currNode->arr[point1] = file(array[1]);
                                                currNode->count++;
                                        }
                                        else {

                                                printf("This directory has the max number of elements\n");
                                        }
                                }
                                else {
                                        printf("\nCannot name file a repeat name\n");
                                }

				if (checkName(currNode, array[2])) {
					
					if (currNode->count <= 63) {
						point2 = findInsertionPoint(currNode);
                                        	currNode->arr[point2] = file(array[2]);
						currNode->count++;
					}
					else {

						printf("This directory has the max number of elements\n");
					}
                                }
                                else {
                                        printf("\nCannot name file a repeat name\n");
                                }
                               
                            }			
			
		}

		else if (!strcmp(array[0], "mv")) {
			
			if (numCalls != 3) {

				printf("\nMust have 2 arguments after mv\n");
			}
			else {
				mv(currNode, array[1], array[2]);
			}
		}

		else if (!strcmp(array[0], "ls")) {

			ls(currNode);
		}
		else if (!strcmp(array[0], "cp")) {

			
			if (numCalls != 3) {

                                printf("\nMust have 2 arguments after mv\n");
                        }

			else {

				cp(currNode, array[1], array[2]);
			}


		}
		else if (!strcmp(array[0], "cat")) {

			if (numCalls != 2) {

				 printf("\ncat command only supports 1 additional argument\n");	

			}
			else {

				cat(currNode, array[1]);
			}


		}

		else if (!strcmp(array[0], "rm")) {

			if (numCalls == 1) {

				printf("\nrm command needs 1 or 2 more arguments\n");

			}	
			else if (numCalls == 2) {

				rm(currNode, array[1]);
			}		
			else {
				rm(currNode, array[1]);
				rm(currNode, array[2]);
			}
			

		}
		else if (!strcmp(array[0], "rmdir")) {

			
			if (numCalls == 1) {

                                printf("\nrm command needs 1 or 2 more arguments\n");

                        }
                        else if (numCalls == 2) {

                                rmdir(currNode, array[1]);
                        }
                        else {
                                rmdir(currNode, array[1]);
                                rmdir(currNode, array[2]);
                        }			

		}
		else if (!strcmp(array[0], "pwd")) {

			struct Node *keepSpot;
			keepSpot = path;	
			printf("/%s", path->name);
			while (keepSpot->arr[0] != NULL) {
				
				printf("/%s", keepSpot->arr[0]->name);
				keepSpot = keepSpot->arr[0];
			}
			printf("\n"); 

		}
		else if (!strcmp(array[0], "dirprint")) {

			struct Node * copyCurr = currNode;
			dirprint(copyCurr);


		}
		else if (!strcmp(array[0], "clear")) {

			clear(currNode);

		}
		else if (!strcmp(array[0], "edit")) {

			if (numCalls != 2) {

				printf("\nedit command requires only 1 additional argument\n");			
				
			}
			else {
				
				edit(currNode, array[1]);
			}

		}
		else if (!strcmp(array[0], "count")) {

			count(currNode);
		}
		else if (!strcmp(array[0], "help")) {
			help();
		}
		else if (!strcmp(array[0], "quit")) {
			
			break;
		}
		else {
			printf("\nInvalid Command\n");			
		}
			
		
	}	
}

int main (int argc, char *argv[]) {


        struct dirent *file;
	int i;
	struct Node *currNode = makeHome();
	
        if (argc > 2) {

                printf("\nInvalid number of command line arguments, no files could be read.\n");
        }
        else if (argc == 2) {
                DIR *dir = opendir(argv[1]);

                if (dir == NULL) {

                        printf("\nDirectory could not be opened");

                }
		
		i = 0;
                while (( file = readdir(dir)) != NULL) {
			
			currNode->arr[i] = fileNoContent(file->d_name);
			i++;
			if (i >= MAXSIZE) {
			
				printf("\nDirectory exceeds max number of files. First 64 have been succesfully added.");
				break;
			}
                }
                closedir(dir);
        }

	

	runLinux(currNode);
		
	return 0;


}



