#ifndef __DHT11_H
#define __DHT11_H

#include "port.h"

void dht11_init(Port_t* port);
void dht11_update(Port_t* port);
Value_t* dht11_get_values(void);

#endif /* __DHT11_H */
