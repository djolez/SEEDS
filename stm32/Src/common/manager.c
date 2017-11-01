
#include "manager.h"
#include <stdlib.h>
#include <string.h>
#include "config.h"
#include "communication.h"
#include "delay.h"

//Port_t* tmp;

//Port_t* entities[] = {
//	{ .Name = "ds18b20", .GPIOx = GPIOC, .GPIO_Pin = GPIO_PIN_7, .Type = DS18B20 },
//	{ .Name = "dht11", .GPIOx = GPIOA, .GPIO_Pin = GPIO_PIN_9, .Type = DHT11 },
//	{ .Name = "relay.light", .GPIOx = GPIOB, .GPIO_Pin = GPIO_PIN_6, .Type = RELAY }
//};

/*
 This should be located in config.h
 */

//Port_t* entities[3];
//uint8_t entities_length;
Port_t* devices[NUMBER_OF_ENTITIES];

void manager_init_all() {

	devices[0] = malloc(sizeof(Port_t));
	devices[0]->Name = "dht11-PA9";
	devices[0]->GPIOx = GPIOA;
	devices[0]->GPIO_Pin = GPIO_PIN_9;
	devices[0]->Type = DHT11;
//	devices[0]->db_id = 1;

	//Stopped working for no reason
	devices[1] = malloc(sizeof(Port_t));
	devices[1]->Name = "ds18b20-PC7";
	devices[1]->GPIOx = GPIOC;
	devices[1]->GPIO_Pin = GPIO_PIN_7;
	devices[1]->Type = DS18B20;
//	devices[1]->db_id = 2;

	devices[2] = malloc(sizeof(Port_t));
	devices[2]->Name = "float-switch-PA7";
	devices[2]->GPIOx = GPIOA;
	devices[2]->GPIO_Pin = GPIO_PIN_7;
	devices[2]->Type = SWITCH;
	devices[2]->Num_Sub_devices = 0;
//	devices[2]->db_id = 3;

	devices[3] = malloc(sizeof(Port_t));
	devices[3]->Name = "relay-light-PB6";
	devices[3]->GPIOx = GPIOB;
	devices[3]->GPIO_Pin = GPIO_PIN_6;
	devices[3]->Type = RELAY;
	devices[3]->Num_Sub_devices = 0;
//	devices[3]->db_id = 4;

//	devices[4] = malloc(sizeof(Port_t));
//	devices[4]->Name = "analog-light-PA6";
//	devices[4]->GPIOx = GPIOC;
//	devices[4]->GPIO_Pin = GPIO_PIN_0;
//	devices[4]->Type = ANALOG;
//	devices[4]->Num_Sub_devices = 0;
//	devices[4]->db_id = 5;

	uint8_t i;
	char msg[MAX_COMM_MSG_LENGTH];
	sprintf(msg, "board_init$STM32:");

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		manager_init_specific(devices[i]);

		int j = 0;
		sprintf(msg, "%s%s.%d", msg, devices[i]->Name, devices[i]->Type);
		// For the ds18b20, in the above line subdevice name is ok,
		// when it goes to the bottom line the name is changed to rubbish
		for (j = 0; j < devices[i]->Num_Sub_devices; j++) {
			if (j == 0)
				sprintf(msg, "%s_", msg);

			sprintf(msg, "%s%s", msg, devices[i]->Sub_devices[j].Name);

			if (j < devices[i]->Num_Sub_devices - 1)
				sprintf(msg, "%s,", msg);
		}

		if (i < NUMBER_OF_ENTITIES - 1)
			sprintf(msg, "%s|", msg);
	}

//	TODO: Check if this works

	comm_send_msg(msg);
//	comm_send_msg("\n");
}

void manager_init_specific(Port_t* device) {

	switch (device->Type) {
	case DS18B20:
		ds18b20_init_all(device);
		break;
	case DHT11:
		dht11_init(device);
		break;
	case RELAY:
		RELAY_INIT(device)
		;
		break;
	case SWITCH:

		break;
	case ANALOG:

		break;
	default:
		break;
	}
}

void manager_update_data_all() {
	uint8_t i;

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		manager_update_data_specific(devices[i]);
	}
}

void manager_update_data_specific(Port_t* device) {
	switch (device->Type) {
	case DS18B20:
		ds18b20_update_all(device);
		break;
	case DHT11:
		dht11_update(device);
		break;
	case RELAY:
		RELAY_READ_VALUE(device)
		;
		break;
	case SWITCH:
		SWITCH_READ_VALUE(device)
		;
		break;
	case ANALOG:
		// TODO: implement
		SWITCH_READ_VALUE(device)
		;
		break;
	default:
		break;
	}
}

void manager_write_data(uint16_t id, uint8_t value) {
	Port_t* device = find_device_by_id(id);

	//Workaround to check if returned device is NULL
	if (device->db_id != id) {
		comm_send_error_msg("Device not found");
	}

	switch (device->Type) {
	case DS18B20:
	case DHT11:
	case SWITCH:
		comm_send_error_msg("Device does not support writing");
		break;
	case RELAY:
		if (value <= 0)
			RELAY_OFF(device);
		else if (value >= 1)
			RELAY_ON(device);
		break;
	default:
		break;
	}
}

void manager_send_data_all() {
	uint8_t i;

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		manager_send_data_specific(devices[i]);
	}
}

void manager_send_data_specific(Port_t* port) {
	char msg[100];
	int i = 0;

	sprintf(msg, "device_reading$%d", port->db_id);

	if (port->Num_Sub_devices > 0) {
		sprintf(msg, "%s|", msg);

		for (i = 0; i < port->Num_Sub_devices; i++) {
			sprintf(msg, "%s%d_%d", msg, port->Sub_devices[i].db_id,
					port->Sub_devices[i].Last_Value);
			if (i < port->Num_Sub_devices - 1)
				sprintf(msg, "%s,", msg);

//			sprintf(msg, "reading$%d_%d", port->Sub_devices[i].db_id, port->Sub_devices[i].Last_Value);
			//xQueueSend(comm_handle_tx, msg, 100);
		}
	} else {
		sprintf(msg, "%s_%d", msg, port->Last_Value);
//		sprintf(msg, "reading$%d_%d", port->db_id, port->Last_Value);
	}
	comm_send_msg(msg);
}

void manager_update_device_id(char* name, char* parent_name, int id) {
	for (int i = 0; i < NUMBER_OF_ENTITIES; i++) {
		if (parent_name && strcmp(devices[i]->Name, parent_name) == 0) {
			for (int j = 0; j < devices[i]->Num_Sub_devices; j++) {
				if (strcmp(devices[i]->Sub_devices[j].Name, name) == 0)
					devices[i]->Sub_devices[j].db_id = id;
			}
		} else if (strcmp(devices[i]->Name, name) == 0) {
			devices[i]->db_id = id;
		}
	}
}

Port_t* find_device_by_id(uint16_t id) {
	uint8_t i;

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		if (devices[i]->db_id == id)
			return devices[i];
	}
	return NULL;
}

void manager_print_all_devices() {
	char buff[50];

	for (int i = 0; i < NUMBER_OF_ENTITIES; i++) {
		sprintf(&buff, "%s, id: %d", devices[i]->Name, devices[i]->db_id);
		comm_send_msg(buff);

		for (int j = 0; j < devices[i]->Num_Sub_devices; j++) {
			sprintf(&buff, "\t%s, id: %d", devices[i]->Sub_devices[j].Name,
					devices[i]->Sub_devices[j].db_id);
			comm_send_msg(buff);
		}
		comm_send_msg("\n");
	}
}

void HAL_GPIO_EXTI_Callback(uint16_t GPIO_Pin) {

	NVIC_DisableIRQ(EXTI9_5_IRQn);

	DELAY(20);
	__HAL_GPIO_EXTI_CLEAR_IT(GPIO_Pin);
	NVIC_EnableIRQ(EXTI9_5_IRQn);

	for (int i = 0; i < NUMBER_OF_ENTITIES; i++) {
//		Change this to somehow check GPIO port, TYPE is just a temp solution
		if (devices[i]->GPIO_Pin == GPIO_Pin && devices[i]->Type == SWITCH) {
			//Send a notification to the server
			GPIO_PinState current_value = HAL_GPIO_ReadPin(devices[i]->GPIOx,
					devices[i]->GPIO_Pin);
			char* msg[MAX_COMM_MSG_LENGTH];
			sprintf(msg, "interrupt$%d_%d\r", devices[i]->db_id, current_value);
//			xQueueSendFromISR(comm_handle_tx, msg, NULL);
			comm_send_msg(msg);

			break;
		}
	}

}
