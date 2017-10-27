#include "communication.h"
#include <string.h>
#include "usart.h"
#include "FreeRTOS.h"
#include "queue.h"
#include "manager.h"

#include "global.h"

void handle_msg(char* msg);

/* Possible message formats (parameters in brackets are optional):
 *  RX
 * 	read$-1 | read$dbID
 * 	write$dbID_value
 * 	sync$deviceName1_dbID1:subDeviceName1_dbID, subDeviceName2_dbID | deviceName2_dbID2...
 *
 *	TX
 *	board_init $ boardName: deviceName1.type_ subDeviceName1, subDeviceName2 | deviceName2.type- ..., ... | ...
 *	device_reading $ dbID(_value) (| sub-device1-id_value,  sub-device2-id_value...)
 *	interrupt $ dbID_value
 * */

char rx_data[2], rx_indx, rx_buffer[MAX_COMM_MSG_LENGTH];
uint8_t msg_pending = 0;

void comm_start() {
	HAL_UART_Receive_IT(&huart2, rx_data, 1);
}

void comm_send_msg(char* msg) {
	HAL_UART_Transmit(&huart2, (uint8_t*) msg, strlen(msg), HAL_MAX_DELAY);
	HAL_UART_Transmit(&huart2, (uint8_t*) "\n", strlen("\n"), HAL_MAX_DELAY);
}

char* error_msg = "error$";
void comm_send_error_msg(char* msg) {
//	char* buff[MAX_COMM_MSG_LENGTH];
//	sprintf(buff, "%s %s", error_msg, msg);
//	comm_send_msg(buff);
	char * s = malloc(snprintf(NULL, 0, "%s%s", error_msg, msg) + 1);
	sprintf(s, "%s %s", error_msg, msg);
	comm_send_msg(s);
}


void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart) {

	uint8_t i;
	if (huart->Instance == USART2)	//current UART
	{
		if (rx_indx == 0) {
			for (i = 0; i < MAX_COMM_MSG_LENGTH; i++)
				rx_buffer[i] = 0;
		}	//clear Rx_Buffer before receiving new data

		if(rx_indx < MAX_COMM_MSG_LENGTH)
		{
			if (rx_data[0] != 10)
			{
				rx_buffer[rx_indx++] = rx_data[0];	//add data to Rx_Buffer
//				HAL_UART_Receive_IT(&huart2, rx_data, 1);
			}
			else //if received data == "\n"
			{
//				comm_send_msg("-----------------------------------------");
//				xQueueSendFromISR(comm_handle, rx_buffer, NULL);
				handle_msg(rx_buffer);
				rx_indx = 0;
			}
		}
		else
		{
			char msg[70];
			sprintf(msg, "Error: Message size is greater than %d chars\n", MAX_COMM_MSG_LENGTH);
			comm_send_msg(msg);
//			comm_send_msg("Error: Message size is greater than %d chars");
//			HAL_UART_Transmit(&huart2, (uint8_t*) msg, strlen(msg), 1000);
			rx_indx = 0;
//			HAL_UART_Receive_IT(&huart2, rx_data, 1);
		}
		HAL_UART_Receive_IT(&huart2, rx_data, 1);
	}
}

void handle_msg(char* msg) {
	char* action;
	char* payload;

	action = strsep(&msg, MSG_DELIMITER);
	payload = strsep(&msg, MSG_DELIMITER);

	if (strcmp(action, "read") == 0) {

		int device_id = string_to_int(payload);

		if (device_id < 0) {
//			comm_send_msg("reading from all devices...");
			manager_update_data_all();
			manager_send_data_all();
//			comm_send_msg("\n");
		} else {
//			comm_send_msg("reading from a device...");
			Port_t* device = find_device_by_id(device_id);

			manager_update_data_specific(device);
			manager_send_data_specific(device);
//			comm_send_msg("\n");
		}

	} else if (strcmp(action, "write") == 0) {
//		comm_send_msg("writing to a device...");
		uint16_t id = string_to_int(strsep(&payload, "_"));
		uint8_t value = string_to_int(strsep(&payload, "_"));

		manager_write_data(id, value);
//		comm_send_msg("Done.\n");

	} else if (strcmp(action, "sync") == 0) {
//		comm_send_msg("syncing...");

//		manager_print_all_devices();

		char* full_device;
		char* device;
		char* device_name;
		uint16_t device_id;

		char* sub_devices;
		char* sub_device;
		char* sub_device_name;
		uint16_t sub_device_id;

		full_device = strsep(&payload, "|");

		while(full_device) {
			device = strsep(&full_device, ":");
			device_name = strsep(&device, "_");

//			char* tmp = strsep(&device, "_");
//			device_id = strtol(strsep(&device, "_"), NULL, 10);
			device_id = string_to_int(strsep(&device, "_"));
//			uint16_t device_id = strtol(tmp, NULL, 10);
			manager_update_device_id(device_name, NULL, device_id);

			sub_devices = strsep(&full_device, ":");
			sub_device = strsep(&sub_devices, ",");

			while(sub_device) {
				sub_device_name = strsep(&sub_device, "_");
				sub_device_id = string_to_int(strsep(&sub_device, "_"));
//				sub_device_id = strtol(strsep(&sub_device, "_"), NULL, 10);
				sub_device = strsep(&sub_devices, ",");

				manager_update_device_id(sub_device_name, device_name, sub_device_id);
			}
			full_device = strsep(&payload, "|");
		}
//		comm_send_msg("Done. Printing all devices...\n");
//		manager_print_all_devices();

	} else {
//		ERROR
		comm_send_error_msg("Unknown command");
	}
	//transmit_msg(payload);

//	while ((action = strsep(&msg, "_")) != NULL) {
//		HAL_UART_Transmit(&huart2, action, strlen(action), 100);
//		HAL_UART_Transmit(&huart2, "\r\n", strlen("\n"), 100);
//	}
}





