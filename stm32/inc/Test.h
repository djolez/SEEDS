/*
 * Test.h
 *
 *  Created on: 26 apr 2018
 *      Author: Antonello
 */

#ifndef TEST_H_
#define TEST_H_

#include "hw.h"
#include "vcom.h"
#include "ds18b20.h"
#include "radio.h"
#include "sx1276.h"

uint8_t FunctionalTEST();

uint8_t TestSPI1();
uint8_t TestTemp1();
uint8_t TestTemp2();


#endif /* TEST_H_ */
