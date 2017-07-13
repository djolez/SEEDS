#include "ds18b20.h"
#include "config.h"
#include <string.h>

char read_buffer[40];
uint8_t devices, i, j, count, alarm_count;
float tmp_value;
uint8_t device[EXPECTING_SENSORS][8];
uint8_t alarm_device[EXPECTING_SENSORS][8];
TM_OneWire_t OneWire1;

void ds18b20_init_all(Port_t* port)
{
	OneWire1.GPIOx = port->GPIOx;
	OneWire1.GPIO_Pin = port->GPIO_Pin;
	uint16_t position = 0;
	while ((port->GPIO_Pin >> position) != RESET){
	  position++;
	}
	OneWire1.GPIO_Pin_Pos = position-1;
	port->GPIO_Pin_Pos = position-1;

	/* Checks for any device on 1-wire */
	count = 0;
	devices = TM_OneWire_First(&OneWire1);

	while (devices) {
		char tmp_name[50];
		sprintf(tmp_name, "temp-%d", count);
		port->Sub_devices[count].Name = tmp_name;
//		port->Sub_devices[count].Name = "Water";
		/* Increase counter */
		count++;

		/* Get full ROM value, 8 bytes, give location of first byte where to save */
		TM_OneWire_GetFullROM(&OneWire1, device[count - 1]);

		/* Get next device */
		devices = TM_OneWire_Next(&OneWire1);
	}

	port->Num_Sub_devices = count;

	/* Go through all connected devices and set resolution to 12bits */
	for (i = 0; i < count; i++) {
		TM_DS18B20_SetResolution(&OneWire1, device[i], TM_DS18B20_Resolution_12bits);
	}
}

void ds18b20_update_all(Port_t* port)
{
	/* Start temperature conversion on all devices on one bus */
	TM_DS18B20_StartAll(&OneWire1);

	/* Wait until all are done on one onewire port */
	while (!TM_DS18B20_AllDone(&OneWire1));

	/* Read temperature from each device separatelly */
	for (i = 0; i < count; i++) {
		/* Read temperature from ROM address and store it to temps variable */
		if (TM_DS18B20_Read(&OneWire1, device[i], &tmp_value)) {
			port->Sub_devices[i].Last_Value = tmp_value * 100;
		} else {
		  /* Reading error */
		}
	}
}





