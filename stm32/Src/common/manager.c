#include "cmsis_os.h"

#include "manager.h"
#include <stdlib.h>
#include <string.h>
#include "config.h"
#include "communication.h"

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

Port_t* entities[NUMBER_OF_ENTITIES];

void manager_init_all() {

	entities[0] = malloc(sizeof(Port_t));
	entities[0]->Name = "dht11-PA9";
	entities[0]->GPIOx = GPIOA;
	entities[0]->GPIO_Pin = GPIO_PIN_9;
	entities[0]->Type = DHT11;
	entities[0]->db_id = 1;

	//Stopped working for no reason
	entities[1] = malloc(sizeof(Port_t));
	entities[1]->Name = "ds18b20-PC7";
	entities[1]->GPIOx = GPIOC;
	entities[1]->GPIO_Pin = GPIO_PIN_7;
	entities[1]->Type = DS18B20;
	entities[1]->db_id = 2;
	/*
	 * This worked normally the first time it was tested,
	 * now the callback (located in main) is called twice and not always
	 *
	 * Solved with pull down??
	 */
	entities[2] = malloc(sizeof(Port_t));
	entities[2]->Name = "float-switch-PA7";
	entities[2]->GPIOx = GPIOA;
	entities[2]->GPIO_Pin = GPIO_PIN_7;
	entities[2]->Type = SWITCH;
	entities[2]->Num_Sub_devices = 0;
	entities[2]->db_id = 3;

	entities[3] = malloc(sizeof(Port_t));
	entities[3]->Name = "relay-light-PB6";
	entities[3]->GPIOx = GPIOB;
	entities[3]->GPIO_Pin = GPIO_PIN_6;
	entities[3]->Type = RELAY;
	entities[3]->Num_Sub_devices = 0;
	entities[3]->db_id = 4;

	uint8_t i;
//	entities_length = sizeof(entities) / sizeof(Port_t*);

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		manager_init_specific(entities[i]);
	}
}

void manager_init_specific(Port_t* entity) {

	switch (entity->Type) {
	case DS18B20:
		ds18b20_init_all(entity);
		break;
	case DHT11:
		dht11_init(entity);
		break;
	case RELAY:
		RELAY_INIT(entity);
		break;
	case SWITCH:

		break;
	default:
		break;
	}
}

void manager_update_data_all() {
	uint8_t i;

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		manager_update_data_specific(entities[i]);
	}

//	manager_update_data_specific(entities[0]);
}

void manager_update_data_specific(Port_t* entity) {
	switch (entity->Type) {
	case DS18B20:
		ds18b20_update_all(entity);
		break;
	case DHT11:
		dht11_update(entity);
		break;
	case RELAY:
		RELAY_READ_VALUE(entity);
		break;
	case SWITCH:
		SWITCH_READ_VALUE(entity);
		break;
	default:
		break;
	}
}

void manager_write_data(uint16_t id, uint8_t value) {
	Port_t* entity = find_device_by_id(id);

	//Workaround to check if entity is NULL
	if(entity->db_id != id) {
		comm_send_error_msg("Device not found");
	}

	switch (entity->Type) {
		case DS18B20:
		case DHT11:
		case SWITCH:
			comm_send_error_msg("Device does not support writing");
			break;
		case RELAY:
			if(value == 0)
				RELAY_OFF(entity);
			else if(value == 1)
				RELAY_ON(entity);
			break;
		default:
			break;
	}
}

void manager_send_data_all() {
	uint8_t i;

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		manager_send_data_specific(entities[i]);
	}
}

void manager_send_data_specific(Port_t* port) {
	char msg[MAX_COMM_MSG_LENGTH];
	int i = 0;

	sprintf(msg, "device_reading$%d", port->db_id);

	if(port->Num_Sub_devices > 0) {
		sprintf(msg, "%s|", msg);

		for (i = 0; i < port->Num_Sub_devices; i++) {
			sprintf(msg, "%s%d_%d", msg, port->Sub_devices[i].db_id, port->Sub_devices[i].Last_Value);
			if(i < port->Num_Sub_devices - 1)
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
		if(parent_name && strcmp(entities[i]->Name, parent_name) == 0) {
			for(int j = 0; j < entities[i]->Num_Sub_devices; j++) {
				if(strcmp(entities[i]->Sub_devices[j].Name, name) == 0)
					entities[i]->Sub_devices[j].db_id = id;
			}
		} else if(strcmp(entities[i]->Name, name) == 0) {
			entities[i]->db_id = id;
		}
	}
}

Port_t* find_device_by_id(uint16_t id) {
	uint8_t i;

	for (i = 0; i < NUMBER_OF_ENTITIES; i++) {
		if(entities[i]->db_id == id)
			return entities[i];
	}
	return NULL;
}

void manager_print_all_devices() {
	char buff[50];

	for (int i = 0; i < NUMBER_OF_ENTITIES; i++) {
		sprintf(&buff, "%s, id: %d", entities[i]->Name, entities[i]->db_id);
		comm_send_msg(buff);

		for (int j = 0; j < entities[i]->Num_Sub_devices; j++) {
			sprintf(&buff, "\t%s, id: %d", entities[i]->Sub_devices[j].Name, entities[i]->Sub_devices[j].db_id);
			comm_send_msg(buff);
		}
//		comm_send_msg("\n");
	}
}

extern osTimerId timer_handle;
extern uint16_t interrupt_pin;
void HAL_GPIO_EXTI_Callback(uint16_t GPIO_Pin) {

	NVIC_DisableIRQ(EXTI9_5_IRQn);
	interrupt_pin = GPIO_Pin;
	xTimerStartFromISR(timer_handle, pdFALSE);

	for (int i = 0; i < NUMBER_OF_ENTITIES; i++) {
//		Change this to somehow check GPIO port, TYPE is just a temp solution
		if (entities[i]->GPIO_Pin == GPIO_Pin && entities[i]->Type == SWITCH) {
			//Send a notification to the server
			GPIO_PinState current_value = HAL_GPIO_ReadPin(entities[i]->GPIOx, entities[i]->GPIO_Pin);
			char* msg[MAX_COMM_MSG_LENGTH];
			sprintf(msg, "interrupt$%d_%d\r", entities[i]->db_id, current_value);
//			xQueueSendFromISR(comm_handle_tx, msg, NULL);
			comm_send_msg(msg);

			break;
		}
	}

}
