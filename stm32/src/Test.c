/* Includes ------------------------------------------------------------------*/
#include "Test.h"

static uint8_t TEST1=0x00;
static uint8_t TEST2=0xFF;
static uint8_t TEST3=0xAA;
static uint8_t TEST4=0x55;


uint8_t FunctionalTEST(){
	TestSPI1();
	return 0;
}


/***
 * 	TEST SPI-RADIO
 * 	return 0: test ok 1: test error
 * ****/

uint8_t TestSPI1(){
	uint16_t add=0x06;  //RegSymbTimeoutL
	uint8_t oldValue;
	uint8_t ret = 111;
	uint8_t error=0;

	oldValue=SX1276Read( add );
	DelayMs(1);
	SX1276Write( add, TEST1 );
	DelayMs(1);
	ret=SX1276Read( add );
	if(ret==TEST1){
		PRINTF("SPI1 test1 ok\r\n");
	}
	else{
		PRINTF("SPI1 test1 error\r\n");
		error=1;
	}
	DelayMs(1);
	SX1276Write( add,TEST2 );
	DelayMs(1);
	ret=SX1276Read( add );
	if(ret==TEST2){
		PRINTF("SPI1 test2 ok\r\n");
	}
	else{
		PRINTF("SPI1 test2 error\r\n");
		error=1;
	}
	DelayMs(1);
	SX1276Write( add,TEST3 );
	DelayMs(1);
	ret=SX1276Read( add );
	if(ret==TEST3){
		PRINTF("SPI1 test3 ok\r\n");
	}
	else{
		PRINTF("SPI1 test3 error\r\n");
		error=1;
	}
	DelayMs(1);
	SX1276Write( add,TEST4 );
	DelayMs(1);
	ret=SX1276Read( add );
	if(ret==TEST4){
		PRINTF("SPI1 test4 ok\r\n");
	}
	else{
		PRINTF("SPI1 test4 error\r\n");
		error=1;
	}
	DelayMs(1);
	SX1276Write( add,oldValue );
	DelayMs(1);
	ret=SX1276Read( add );
	if(ret==oldValue){
		PRINTF("SPI1 oldVelue ok\r\n");
	}
	else{
		PRINTF("SPI1 OldValue error\r\n");
		error=1;
	}
	return error;
}
