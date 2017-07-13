#include "dht11.h"
#include <string.h>
#include "task.h"

//Value_t values[] = {
//		{.Name = "Temperature"},
//		{.Name = "Humidity"}
//};

void dht11_init(Port_t* port)
{
	uint16_t position = 0;
	while ((port->GPIO_Pin >> position) != RESET){
	  position++;
	}
	port->GPIO_Pin_Pos = position-1;

	port->Sub_devices[0].Name = "Temperature";
	port->Sub_devices[1].Name = "Humidity";
	port->Num_Sub_devices = 2;
}

void dht11_update(Port_t* port) {

	PORT_OUTPUT(port);
	PORT_LOW(port);
	DELAY_us(18000);
	HAL_GPIO_TogglePin(GPIOA, GPIO_PIN_5);
	PORT_HIGH(port);
	DELAY_us(40);
	PORT_INPUT(port);

	// 80us LOW acknowledgment from the sensor
	while(!HAL_GPIO_ReadPin(port->GPIOx, port->GPIO_Pin)) {}
	HAL_GPIO_TogglePin(GPIOA, GPIO_PIN_5);
	// 80us HIGH preparing of the data
	while(HAL_GPIO_ReadPin(port->GPIOx, port->GPIO_Pin)) {}
	HAL_GPIO_TogglePin(GPIOA, GPIO_PIN_5);

	uint8_t i, j, time_elapsed_us;
	uint8_t sensor_values[5] = {0x00, 0x00, 0x00, 0x00, 0x00};

	HAL_TIM_Base_Init(&htim2);
	HAL_TIM_Base_Start(&htim2);

	for (i = 0; i < 5; i++) {
		for (j = 0; j < 8; j++) {
			// 50us LOW begin transmitting a bit of data

			while(!HAL_GPIO_ReadPin(port->GPIOx, port->GPIO_Pin)) {}
			time_elapsed_us = __HAL_TIM_GET_COUNTER(&htim2);
			HAL_GPIO_TogglePin(GPIOA, GPIO_PIN_5);
			while(HAL_GPIO_ReadPin(port->GPIOx, port->GPIO_Pin)) {}
			HAL_GPIO_TogglePin(GPIOA, GPIO_PIN_5);
			time_elapsed_us = __HAL_TIM_GET_COUNTER(&htim2)-time_elapsed_us;
			sensor_values[i] = sensor_values[i]<<1;

			if(time_elapsed_us > 30) {
				//values[i] = values[i] << 0;
				sensor_values[i] = sensor_values[i] + 1;
			}
		}
	}

//	CRC
	if(sensor_values[4] != (sensor_values[0] + sensor_values[2])) {
		return;
	}

	//Temp
//	port->Last_Values[0].Value = sensor_values[2] * 100 + sensor_values[3];
	port->Sub_devices[0].Last_Value = sensor_values[2] * 100 + sensor_values[3];
	//Humidity
//	port->Last_Values[1].Value = sensor_values[0] * 100 + sensor_values[1];
	port->Sub_devices[1].Last_Value = sensor_values[0] * 100 + sensor_values[1];

}

//Value_t* dht11_get_values()
//{
//	return values;
//}
//
//char* dht11_write_values_to_string(Port_t* port)
//{
//	char* str = to_string(port, &values, sizeof(values) / sizeof(values[0]));
//	return str;
////	char* str = to_string(port, &values, sizeof(values) / sizeof(values[0]));
////	return str;
//}






