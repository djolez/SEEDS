#include "switch.h"
#include "port.h"

//void RELAY_INIT(Port_t* structure)
//{
//	PORT_HIGH(structure);
//	GPIO_InitTypeDef tmp;
//	tmp.Pin = (structure)->GPIO_Pin;
//	tmp.Mode = GPIO_MODE_OUTPUT_OD;
//	tmp.Pull = GPIO_NOPULL;
//	tmp.Speed = GPIO_SPEED_FREQ_LOW;
//	HAL_GPIO_Init((structure)->GPIOx, &tmp);
//}
//
//void RELAY_ON(Port_t* structure)
//{
//	PORT_LOW(structure);
//}
//
//void RELAY_OFF(Port_t* structure)
//{
//	PORT_LOW(structure);
//}

//void SWITCH_EXTERNAL_INIT(Port_t* structure)
//{
//	GPIO_InitTypeDef tmp;
//	tmp.Pin = (structure)->GPIO_Pin;
//	tmp.Mode = (structure)->Mode;
//	tmp.Pull = GPIO_NOPULL;
//	HAL_GPIO_Init((structure)->GPIOx, &tmp);
//	if((structure)->GPIO_Pin == GPIO_PIN_0){
//		HAL_NVIC_SetPriority(EXTI0_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI0_IRQn);
//	}
//	else if((structure)->GPIO_Pin == GPIO_PIN_1){
//		HAL_NVIC_SetPriority(EXTI1_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI1_IRQn);
//	}
//	else if((structure)->GPIO_Pin == GPIO_PIN_2){
//		HAL_NVIC_SetPriority(EXTI2_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI2_IRQn);
//	}
//	else if((structure)->GPIO_Pin == GPIO_PIN_3){
//		HAL_NVIC_SetPriority(EXTI3_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI3_IRQn);
//	}
//	else if((structure)->GPIO_Pin == GPIO_PIN_4){
//		HAL_NVIC_SetPriority(EXTI4_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI4_IRQn);
//	}
//	else if((structure)->GPIO_Pin < GPIO_PIN_10){
//		HAL_NVIC_SetPriority(EXTI9_5_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI9_5_IRQn);
//	}
//	else{
//		HAL_NVIC_SetPriority(EXTI15_10_IRQn, 0, 0);
//		HAL_NVIC_EnableIRQ(EXTI15_10_IRQn);
//	}
//}
