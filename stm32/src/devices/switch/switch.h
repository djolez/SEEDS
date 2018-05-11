#ifndef __SWITCH_H
#define __SWITCH_H

#import "manager.h";

#define SWITCH_INIT(structure) do{\
	HW_GPIO_SetIrq((device)->GPIOx, (device)->GPIO_Pin, 5, &switch_interrupt_handler);\
}while(0);

#define RELAY_INIT(structure) do{\
	GPIO_InitTypeDef tmp;\
	tmp.Pin = (structure)->GPIO_Pin;\
	tmp.Mode = GPIO_MODE_OUTPUT_OD;\
	tmp.Pull = GPIO_NOPULL;\
	tmp.Speed = GPIO_SPEED_FREQ_LOW;\
	HAL_GPIO_Init((structure)->GPIOx, &tmp);\
	PORT_HIGH(structure);\
}while(0);

#define RELAY_ON(structure) PORT_LOW(structure)
#define RELAY_OFF(structure) PORT_HIGH(structure)

#define SWITCH_READ_VALUE(structure) do{\
	(structure)->Last_Value = HAL_GPIO_ReadPin((structure)->GPIOx, (structure)->GPIO_Pin);\
}while(0);

#define RELAY_READ_VALUE(structure) do{\
	(structure)->Last_Value = (uint16_t)!HAL_GPIO_ReadPin((structure)->GPIOx, (structure)->GPIO_Pin);\
}while(0);

//typedef struct {
//	GPIO_TypeDef* GPIOx;
//	uint16_t GPIO_Pin;
//	uint32_t Mode;
//} Switch;

//#define SWITCH_INIT(structure) do{\
//	GPIO_InitTypeDef tmp;\
//	tmp.Pin = (structure)->GPIO_Pin;\
//	tmp.Mode = (structure)->Mode;\
//	tmp.Pull = GPIO_NOPULL;\
//	HAL_GPIO_Init((structure)->GPIOx, &tmp);\
//	if((structure)->GPIO_Pin == GPIO_PIN_0){\
//		HAL_NVIC_SetPriority(EXTI0_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI0_IRQn);\
//	}\
//	else if((structure)->GPIO_Pin == GPIO_PIN_1){\
//		HAL_NVIC_SetPriority(EXTI1_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI1_IRQn);\
//	}\
//	else if((structure)->GPIO_Pin == GPIO_PIN_2){\
//		HAL_NVIC_SetPriority(EXTI2_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI2_IRQn);\
//	}\
//	else if((structure)->GPIO_Pin == GPIO_PIN_3){\
//		HAL_NVIC_SetPriority(EXTI3_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI3_IRQn);\
//	}\
//	else if((structure)->GPIO_Pin == GPIO_PIN_4){\
//		HAL_NVIC_SetPriority(EXTI4_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI4_IRQn);\
//	}\
//	else if((structure)->GPIO_Pin < GPIO_PIN_10){\
//		HAL_NVIC_SetPriority(EXTI9_5_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI9_5_IRQn);\
//	}\
//	else{\
//		HAL_NVIC_SetPriority(EXTI15_10_IRQn, 0, 0);\
//		HAL_NVIC_EnableIRQ(EXTI15_10_IRQn);\
//	}\
//}while(0);

#endif /* __SWITCH_H */
