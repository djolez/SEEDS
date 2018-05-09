#ifndef __MANAGER_H
#define __MANAGER_H

// The hw.h include is a quick fix to the strange compile errors (bool, int unknown types)
#include "hw.h"
#include "lora.h"

#include "ds18b20.h"
#include "dht11.h"
#include "switch.h"
#include "port.h"

void manager_init_all(void);
void manager_init_specific(Port_t* entity);
void manager_update_data_all();
void manager_update_data_specific(Port_t* entity);
void manager_send_data_all(void);
void manager_send_data_specific(Port_t* port);
Port_t* find_device_by_id(uint16_t id);
void switch_interrupt_handler();
void manager_write_to_buffer_all(lora_AppData_t *AppData);

#endif /* __MANAGER_H */
