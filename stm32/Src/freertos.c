/**
  ******************************************************************************
  * File Name          : freertos.c
  * Description        : Code for freertos applications
  ******************************************************************************
  *
  * Copyright (c) 2017 STMicroelectronics International N.V. 
  * All rights reserved.
  *
  * Redistribution and use in source and binary forms, with or without 
  * modification, are permitted, provided that the following conditions are met:
  *
  * 1. Redistribution of source code must retain the above copyright notice, 
  *    this list of conditions and the following disclaimer.
  * 2. Redistributions in binary form must reproduce the above copyright notice,
  *    this list of conditions and the following disclaimer in the documentation
  *    and/or other materials provided with the distribution.
  * 3. Neither the name of STMicroelectronics nor the names of other 
  *    contributors to this software may be used to endorse or promote products 
  *    derived from this software without specific written permission.
  * 4. This software, including modifications and/or derivative works of this 
  *    software, must execute solely and exclusively on microcontroller or
  *    microprocessor devices manufactured by or for STMicroelectronics.
  * 5. Redistribution and use of this software other than as permitted under 
  *    this license is void and will automatically terminate your rights under 
  *    this license. 
  *
  * THIS SOFTWARE IS PROVIDED BY STMICROELECTRONICS AND CONTRIBUTORS "AS IS" 
  * AND ANY EXPRESS, IMPLIED OR STATUTORY WARRANTIES, INCLUDING, BUT NOT 
  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
  * PARTICULAR PURPOSE AND NON-INFRINGEMENT OF THIRD PARTY INTELLECTUAL PROPERTY
  * RIGHTS ARE DISCLAIMED TO THE FULLEST EXTENT PERMITTED BY LAW. IN NO EVENT 
  * SHALL STMICROELECTRONICS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
  * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
  * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
  * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
  * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  *
  ******************************************************************************
  */

/* Includes ------------------------------------------------------------------*/
#include "FreeRTOS.h"
#include "task.h"
#include "cmsis_os.h"

/* USER CODE BEGIN Includes */     
#include "global.h"
#include "config.h"
#include "helper.h"
#include "usart.h"
#include "gpio.h"
#include "jsmn.h"
#include "manager.h"
#include "communication.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>
/* USER CODE END Includes */

/* Variables -----------------------------------------------------------------*/
osThreadId commHandle;

/* USER CODE BEGIN Variables */
extern char rx_data[2];
extern Port_t* devices[NUMBER_OF_ENTITIES];
char rx_buffer[MAX_COMM_MSG_LENGTH];
char tx_buffer[MAX_COMM_MSG_LENGTH];
osTimerId timer_handle;

/* USER CODE END Variables */

/* Function prototypes -------------------------------------------------------*/
void StartCommTask(void const * argument);

void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */

/* USER CODE BEGIN FunctionPrototypes */
TimerCallbackFunction_t Callback01(TimerHandle_t xTimer);

void transmit_msg(char* msg);
void handle_msg(char* msg);
void manager_print_all_devices();
void manager_update_device_id(char*, char* parent_name, int );
/* USER CODE END FunctionPrototypes */

/* Hook prototypes */

/* Init FreeRTOS */

void MX_FREERTOS_Init(void) {
  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* USER CODE BEGIN RTOS_MUTEX */
	/* add mutexes, ... */
  /* USER CODE END RTOS_MUTEX */

  /* USER CODE BEGIN RTOS_SEMAPHORES */
	/* add semaphores, ... */
  /* USER CODE END RTOS_SEMAPHORES */

  /* USER CODE BEGIN RTOS_TIMERS */
	/* start timers, add new ones, ... */
  timer_handle = xTimerCreate("timer", 10, pdFALSE, ( void * ) 0, Callback01);
  /* USER CODE END RTOS_TIMERS */

  /* Create the thread(s) */
  /* definition and creation of comm */
  osThreadDef(comm, StartCommTask, osPriorityNormal, 0, 128);
  commHandle = osThreadCreate(osThread(comm), NULL);

  /* USER CODE BEGIN RTOS_THREADS */
	/* add threads, ... */
  /* USER CODE END RTOS_THREADS */

  /* USER CODE BEGIN RTOS_QUEUES */
	/* add queues, ... */
	comm_handle = xQueueCreate(3, MAX_COMM_MSG_LENGTH * sizeof(char));
	comm_handle_tx = xQueueCreate(10, MAX_COMM_MSG_LENGTH * sizeof(char));
  /* USER CODE END RTOS_QUEUES */
}

/* StartCommTask function */
void StartCommTask(void const * argument)
{

  /* USER CODE BEGIN StartCommTask */

//	xTimerStart(timer_handle, 200);
	/* Infinite loop */
	for (;;) {
//		comm_send_msg("HERE");
		if (xQueueReceive(comm_handle, &rx_buffer, portMAX_DELAY)) {
			handle_msg(rx_buffer);

			//link to the rx_data from communication
//			HAL_UART_Receive_IT(&huart2, rx_data, 1);//activate UART receive interrupt every time
		}
//		if (xQueueReceive(comm_handle_tx, &tx_buffer, MAX_COMMS_DELAY)) {
//			comm_send_msg(tx_buffer);
//		}
	}
  /* USER CODE END StartCommTask */
}

/* USER CODE BEGIN Application */

uint16_t interrupt_pin;
TimerCallbackFunction_t Callback01(TimerHandle_t xTimer)
{
  /* USER CODE BEGIN Callback01 */
    __HAL_GPIO_EXTI_CLEAR_IT(interrupt_pin);
	NVIC_EnableIRQ(EXTI9_5_IRQn);
	return 0;
  /* USER CODE END Callback01 */
//	return Callback01;
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
/* USER CODE END Application */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
