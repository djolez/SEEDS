/**
 ******************************************************************************
 * @file    main.c
 * @author  Ac6
 * @version V1.0
 * @date    01-December-2013
 * @brief   Default main function.
 ******************************************************************************
 */

/*
 / _____)             _              | |
 ( (____  _____ ____ _| |_ _____  ____| |__
 \____ \| ___ |    (_   _) ___ |/ ___)  _ \
 _____) ) ____| | | || |_| ____( (___| | | |
 (______/|_____)_|_|_| \__)_____)\____)_| |_|
 (C)2013 Semtech

 Description: Ping-Pong implementation

 License: Revised BSD License, see LICENSE.TXT file include in the project

 Maintainer: Miguel Luis and Gregory Cristian
 */
/******************************************************************************
 * @file    main.c
 * @author  MCD Application Team
 * @version V1.1.2
 * @date    08-September-2017
 * @brief   this is the main!
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; Copyright (c) 2017 STMicroelectronics International N.V.
 * All rights reserved.</center></h2>
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
#include <string.h>
#include "stm32l4xx_nucleo.h"
#include "stm32l4xx.h"
#include "hw.h"
#include "radio.h"
#include "timeServer.h"
#include "delay.h"
#include "low_power.h"
#include "vcom.h"
#include "lora.h"

#include "config.h"
//#include "communication.h"
#include "manager.h"

extern LPTIM_HandleTypeDef hlptim1;

/*	LORA_CONF	*/

typedef enum {
	LOWPOWER, RX, RX_TIMEOUT, RX_ERROR, TX, TX_TIMEOUT,
} States_t;

#define APP_TX_DUTYCYCLE                            10000
#define LORAWAN_ADR_ON                              0
#define LORAWAN_CONFIRMED_MSG                    DISABLE
#define LORAWAN_APP_PORT                            111
#define LORAWAN_DEFAULT_DATARATE                    DR_5
#define JOINREQ_NBTRIALS                            3

static LoRaParam_t LoRaParamInit = { TX_ON_EVENT,
APP_TX_DUTYCYCLE, CLASS_A,
LORAWAN_ADR_ON, LORAWAN_DEFAULT_DATARATE, LORAWAN_PUBLIC_NETWORK,
JOINREQ_NBTRIALS };

/* call back when LoRa will transmit a frame*/
static void LoraTxData(lora_AppData_t *AppData, FunctionalState* IsTxConfirmed);

/* call back when LoRa has received a frame*/
static void LoraRxData(lora_AppData_t *AppData);

static LoRaMainCallback_t LoRaMainCallbacks = { HW_GetBatteryLevel,
		HW_GetUniqueId, HW_GetRandomSeed, LoraTxData, LoraRxData };

/*	////LORA_CONF	*/

bool lptim1_expired = false;

typedef enum eManagerState
{
	IDLE,
    WRITE,
	UPDATE,
	UPDATE_ALL,
	WRITE_TO_BUFFER,
	WRITE_TO_BUFFER_ALL,
	UPDATE_SEND_ALL
} ManagerState_t;

ManagerState_t current_manager_state;


/**
 * Main application entry point.
 */
extern Port_t* devices;
int main(void) {

	HAL_Init();

	SystemClock_Config();

	DBG_Init();

	HW_Init();
	GPIO_Init();
	MX_TIM2_Init();
	MX_TIM3_Init();
	MX_LPTIM1_Init();

	while(1){
		FunctionalTEST();
//		HAL_GPIO_TogglePin(GPIOA, GPIO_PIN_2);
//		HAL_Delay(500);
	}

	lora_Init(&LoRaMainCallbacks, &LoRaParamInit);

//	manager_init_all();
//	manager_update_data_all();

//	seconds = period / (clock_speed / prescaler)
//	period = (seconds * clock_speed) / prescaler

	HAL_LPTIM_Counter_Start_IT(&hlptim1, 2560); //10 seconds
//	HAL_LPTIM_Counter_Start_IT(&hlptim1, 1280); //5 seconds
//	HAL_LPTIM_Counter_Start_IT(&hlptim1, 9000);

//	Cannot use this because there is a limit on minimum number of channels
//	LoRaMacChannelRemove(1);
//	LoRaMacChannelRemove(2);

	while (1) {
		/* run the LoRa class A state machine*/
		lora_fsm();
		DISABLE_IRQ( );

		/* if an interrupt has occurred after DISABLE_IRQ, it is kept pending
		 * and cortex will not enter low power anyway  */

		if (lora_getDeviceState() == DEVICE_STATE_SLEEP) {
			if(lptim1_expired) {
				current_manager_state = UPDATE_SEND_ALL;
				OnSendEvent();
				lptim1_expired = false;
			}

			LowPower_Handler();
		}
		ENABLE_IRQ();
	}
}

void HAL_LPTIM_AutoReloadMatchCallback(LPTIM_HandleTypeDef *hlptim) {
	if(hlptim->Instance==LPTIM1) {
		lptim1_expired = true;
	}
}

static uint8_t tmp[64];
static void LoraTxData(lora_AppData_t *AppData, FunctionalState* IsTxConfirmed) {

//	memcpy(&AppData->Buff[0], &tmp, 64);
//	AppData->BuffSize = 0;
//	AppData->Port = 0;

	memcpy(&AppData->Buff[0], "F", 1);
	AppData->BuffSize = 1;

	switch(current_manager_state) {
		case UPDATE_SEND_ALL: {
//			manager_update_data_all();
//			manager_write_to_buffer_all(AppData);
			current_manager_state = IDLE;
		}
	}
	AppData->Port = LORAWAN_APP_PORT;
//	*IsTxConfirmed =  LORAWAN_CONFIRMED_MSG;
}

static void LoraRxData(lora_AppData_t *AppData) {
	int i = 0;
}


