#include "tim.h"

TIM_HandleTypeDef htim2;
TIM_HandleTypeDef htim3;
LPTIM_HandleTypeDef hlptim1;

/* TIM2 init function */
void MX_TIM2_Init(void) {
	TIM_ClockConfigTypeDef sClockSourceConfig;
	TIM_MasterConfigTypeDef sMasterConfig;

	htim2.Instance = TIM2;
	htim2.Init.Prescaler = 80;
	htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
	htim2.Init.Period = 65535;
	htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
	if (HAL_TIM_Base_Init(&htim2) != HAL_OK) {
		Error_Handler();
	}

	sClockSourceConfig.ClockSource = TIM_CLOCKSOURCE_INTERNAL;
	if (HAL_TIM_ConfigClockSource(&htim2, &sClockSourceConfig) != HAL_OK) {
		Error_Handler();
	}

	sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
	sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
	if (HAL_TIMEx_MasterConfigSynchronization(&htim2, &sMasterConfig)
			!= HAL_OK) {
		Error_Handler();
	}

}
/* TIM3 init function */
void MX_TIM3_Init(void) {
	TIM_ClockConfigTypeDef sClockSourceConfig;
	TIM_MasterConfigTypeDef sMasterConfig;

	htim3.Instance = TIM3;
	htim3.Init.Prescaler = 0;
	htim3.Init.CounterMode = TIM_COUNTERMODE_UP;
	htim3.Init.Period = 65535;
	htim3.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
	if (HAL_TIM_Base_Init(&htim3) != HAL_OK) {
		Error_Handler();
	}

	sClockSourceConfig.ClockSource = TIM_CLOCKSOURCE_INTERNAL;
	if (HAL_TIM_ConfigClockSource(&htim3, &sClockSourceConfig) != HAL_OK) {
		Error_Handler();
	}

	sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
	sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
	if (HAL_TIMEx_MasterConfigSynchronization(&htim3, &sMasterConfig)
			!= HAL_OK) {
		Error_Handler();
	}

}

/* LPTIM1 init function
 * Instance = LPTIM1
 * Clock source = LSE
 * Trigger source = LPTIM_ETR
 * Trigger Active edge = Rising edge
 * Counter source = Internal*/
void MX_LPTIM1_Init(void) {

	hlptim1.Instance = LPTIM1;
	hlptim1.Init.Clock.Source = LPTIM_CLOCKSOURCE_APBCLOCK_LPOSC;
	hlptim1.Init.Clock.Prescaler = LPTIM_PRESCALER_DIV128;
	hlptim1.Init.Trigger.Source = LPTIM_TRIGSOURCE_0;
//	hlptim1.Init.Trigger.ActiveEdge = LPTIM_ACTIVEEDGE_RISING;
//	hlptim1.Init.Trigger.SampleTime = LPTIM_TRIGSAMPLETIME_DIRECTTRANSITION;
	hlptim1.Init.OutputPolarity = LPTIM_OUTPUTPOLARITY_HIGH;
	hlptim1.Init.UpdateMode = LPTIM_UPDATE_IMMEDIATE;
	hlptim1.Init.CounterSource = LPTIM_COUNTERSOURCE_INTERNAL;
	hlptim1.Init.Input1Source = LPTIM_INPUT1SOURCE_GPIO;
	hlptim1.Init.Input2Source = LPTIM_INPUT2SOURCE_GPIO;

//	hlptim1.Instance = LPTIM1;
//	hlptim1.Init.Clock.Source = LPTIM_CLOCKSOURCE_APBCLOCK_LPOSC;
//	hlptim1.Init.Clock.Prescaler = LPTIM_PRESCALER_DIV1;
//	hlptim1.Init.Trigger.Source = LPTIM_TRIGSOURCE_SOFTWARE;
//	hlptim1.Init.OutputPolarity = LPTIM_OUTPUTPOLARITY_HIGH;
//	hlptim1.Init.UpdateMode = LPTIM_UPDATE_IMMEDIATE;
//	hlptim1.Init.CounterSource = LPTIM_COUNTERSOURCE_INTERNAL;
//	hlptim1.Init.Input1Source = LPTIM_INPUT1SOURCE_GPIO;
//	hlptim1.Init.Input2Source = LPTIM_INPUT2SOURCE_GPIO;

	if (HAL_LPTIM_Init(&hlptim1) != HAL_OK) {
		Error_Handler();
	}

}
