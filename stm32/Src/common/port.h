#ifndef __PORT_H
#define __PORT_H

#include "gpio.h"
#include "tim.h"
#include "global.h"
#include "config.h"

/* All values are kept as an integer and will be divided by 100 on
 * backend to get the decimal value */
typedef struct{
	char* Name;
	uint16_t Value;
} Value_t ;

//typedef union sensor_val{
//	Value_t pippo;
//	Sensor_t pluto;
//};

typedef struct {
	uint16_t db_id;
	char* Name;
	uint16_t Last_Value;
} Sub_device_t;

typedef struct {
	GPIO_TypeDef* GPIOx;
	uint16_t GPIO_Pin;
	uint32_t Mode;
	uint16_t GPIO_Pin_Pos;
	char* Name;
	uint8_t Type;
	uint16_t db_id;
//	sensor_val lv;
	Value_t Last_Value;
	uint8_t Num_Sub_devices;
	Sub_device_t Sub_devices[10];
} Port_t;

//typedef struct {
//	GPIO_TypeDef* GPIOx;
//	uint16_t GPIO_Pin;
//	uint32_t Mode;
//	uint16_t GPIO_Pin_Pos;
//	char* Name;
//	uint8_t Type;
//	uint16_t db_id;
////	sensor_val lv;
//	Value_t Last_Values[10];
//	uint8_t Num_Sub_devices;
//} Port_t;

//Port*->lv.pluto;

extern TIM_HandleTypeDef htim2, htim3;

#define DELAY(x) do{\
	uint32_t uTime = SystemCoreClock/1000000UL * x;\
	HAL_TIM_Base_Init(&htim3);\
	HAL_TIM_Base_Start(&htim3);\
while(__HAL_TIM_GET_COUNTER(&htim3) < uTime ){}\
}while(0);

#define DELAY_us(x) do{\
	HAL_TIM_Base_Init(&htim2);\
	HAL_TIM_Base_Start(&htim2);\
while(__HAL_TIM_GET_COUNTER(&htim2) < x ){}\
}while(0);

#define PORT_LOW(structure) HAL_GPIO_WritePin((structure)->GPIOx,(structure)->GPIO_Pin,GPIO_PIN_RESET)
#define PORT_HIGH(structure) HAL_GPIO_WritePin((structure)->GPIOx,(structure)->GPIO_Pin,GPIO_PIN_SET)

//TODO Make the code work with commented input and output functions

#define PORT_INPUT(structure) do{\
	uint32_t tmp = 0;\
	tmp = (structure)->GPIOx->MODER;\
	tmp &= ~(GPIO_MODER_MODE0 << ((structure)->GPIO_Pin_Pos * 2));\
	tmp |= ((GPIO_MODE_INPUT & ((uint32_t)0x00000003)) << ((structure)->GPIO_Pin_Pos * 2));\
	(structure)->GPIOx->MODER = tmp;\
}while(0);

#define PORT_OUTPUT(structure) do{\
	uint32_t tmp = 0;\
	tmp = (structure)->GPIOx->MODER;\
	tmp &= ~(GPIO_MODER_MODE0 << ((structure)->GPIO_Pin_Pos * 2));\
	tmp |= ((GPIO_MODE_OUTPUT_PP & ((uint32_t)0x00000003)) << ((structure)->GPIO_Pin_Pos * 2));\
	(structure)->GPIOx->MODER = tmp;\
}while(0);


//#define PORT_INPUT(structure) do{\
//GPIO_InitTypeDef GPIO_InitStruct;\
//GPIO_InitStruct.Pin = (structure)->GPIO_Pin;\
//GPIO_InitStruct.Mode = GPIO_MODE_INPUT;\
//GPIO_InitStruct.Pull = GPIO_PULLUP;\
//GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_MEDIUM;\
//HAL_GPIO_Init(structure->GPIOx, &GPIO_InitStruct);\
//}while(0);
//
//#define PORT_OUTPUT(structure) do{\
//GPIO_InitTypeDef GPIO_InitStruct;\
//GPIO_InitStruct.Pin = (structure)->GPIO_Pin;\
//GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;\
//GPIO_InitStruct.Pull = GPIO_PULLUP;\
//GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_MEDIUM;\
//HAL_GPIO_Init(structure->GPIOx, &GPIO_InitStruct);\
//}while(0);


#endif /* __PORT_H */
