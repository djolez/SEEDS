#include "communication.h"
#include <string.h>
#include "usart.h"

#include "global.h"

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






