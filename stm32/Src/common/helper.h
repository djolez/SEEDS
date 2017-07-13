/*
 * helper.h
 *
 *  Created on: Jul 4, 2017
 *      Author: Djordje
 */

#ifndef HELPER_H_
#define HELPER_H_

#include <stdio.h>
#include <errno.h>
#include <stdlib.h>

#include "port.h"

int string_to_int(char* s) {
	return strtol(s, NULL, 10);
}

#endif /* HELPER_H_ */
