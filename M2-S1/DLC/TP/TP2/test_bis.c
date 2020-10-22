#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include"gmp.h"

// void main(int argc, char * argv[])
// {
// 	char * M=argv[1];
// 	char * m="";
// 	unsigned int i=0;
// 	mpz_t m_mpz,pow;
// 	mpz_inits(m_mpz,pow,NULL);
// 	mpz_set_ui(pow,1);
// 	do 
// 	{
// 	if (i>0) mpz_mul_ui(m_mpz,m_mpz,256);
// 	mpz_add_ui(m_mpz,m_mpz,M[strlen(M)-i]);
// 	i++;
// 	} while (i<=strlen(M)) ;


// 		gmp_printf("conversion : %Zu\n",m_mpz);

// }


void main(int argc, char * argv[])
{
	mpz_t C,m_t, pow;
	int i=0;
	unsigned long int m_c;
	char  m[1000],ch;
	mpz_inits(C,m_t,pow,NULL);
	mpz_set_ui(pow,1);
	mpz_set_str(C,argv[1],10);


	while (mpz_cmp_ui(C,0)!=0)
	{ 
		mpz_mod_ui(m_t,C,256);
		m[i]=(char)mpz_get_ui(m_t);
		mpz_sub(C,C,m_t);
		mpz_cdiv_q_ui(C,C,256);
		i++;
	}
 printf("%s\n",m);
}