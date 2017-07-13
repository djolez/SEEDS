#ifndef __GLOBAL_H
#define __GLOBAL_H

#include "FreeRTOS.h"
#include "queue.h"
#include "config.h"

enum EntityType {
	DS18B20 = 0,
	DHT11 = 1,
	RELAY = 2,
	SWITCH = 3
};

xQueueHandle comm_handle;
xQueueHandle comm_handle_tx;

#endif /* __GLOBAL_H */
