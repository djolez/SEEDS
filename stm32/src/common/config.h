#ifndef __CONFIG_H
#define __CONFIG_H

#include "port.h"
#include "global.h"

//#define RX_BUFFER_SIZE 150
//#define TX_BUFFER_SIZE 150
#define MSG_DELIMITER "$"
#define MAX_COMMS_DELAY 500
#define MAX_COMM_MSG_LENGTH 200
#define MAX_SUBDEVICES_NUMBER 10
#define NUMBER_OF_ENTITIES 4
//Port_t* entities[] = {
//	{ .Name = "ds18b20", .GPIOx = GPIOC, .GPIO_Pin = GPIO_PIN_7, .Type = DS18B20 },
//	{ .Name = "dht11", .GPIOx = GPIOA, .GPIO_Pin = GPIO_PIN_9, .Type = DHT11 },
//	{ .Name = "relay.light", .GPIOx = GPIOB, .GPIO_Pin = GPIO_PIN_6, .Type = RELAY }
//};



#endif /* __CONFIG_H */
