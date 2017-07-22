#ifndef __COMM_H
#define __COMM_H

#include "port.h"
#include "config.h"
#include <stdint.h>


//Needed to put extern as explained here https://stackoverflow.com/a/7926300
extern uint8_t msg_pending;

void comm_start(void);
//void finished_reading(void);
//void to_string(Port_t* port, Value_t* values, uint8_t values_size, char* ret_val);
void to_string(Port_t* port, char* ret_val);
void comm_send_msg(char* msg);
void comm_send_error_msg(char* msg);
void comm_handle_msg(char* msg);
#endif /* __COMM_H */
