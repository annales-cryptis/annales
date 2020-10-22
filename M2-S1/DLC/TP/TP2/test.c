#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include"gmp.h"
#define TAILLE_MAX 1000 // Tableau de taille 1000

void main()
{
	char sc[TAILLE_MAX]="";
	char sn[TAILLE_MAX]="";
	char sd[TAILLE_MAX]="";

	char sd[TAILLE_MAX]="";
	FILE* fcipher=NULL;
	FILE* fprivate_key=NULL;

	mpz_t c,n,d,m;
	mpz_inits(c,n,d,m,NULL);

	fcipher=fopen("ciphertext.txt","r");
	fprivate_key=fopen("private_key.txt","r");	

	gmp_fscanf(fcipher,"c =%Zx",c);
	fscanf(fprivate_key,"n =%Zx\nd =%Zx",n,d);

	fclose(fcipher);
	fclose(fprivate_key);

	mpz_powm(m,c,d,n);
	gmp_printf("m=%Zx\n",m);
}