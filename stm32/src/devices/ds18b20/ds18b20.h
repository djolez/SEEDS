#ifndef __DS18B20_H
#define __DS18B20_H

#include "tm_stm32f4_onewire.h"
#include "tm_stm32f4_ds18b20.h"

#include "port.h"

void ds18b20_init_all(Port_t* port);
void ds18b20_update_all(Port_t* port);
void ds18b20_write_values_to_string(Port_t* port, char* buffer);
Value_t* ds18b20_get_values(void);

#define EXPECTING_SENSORS    1

#endif /* __DS18B20_H */
