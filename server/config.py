ACTION_MSG_DELIMITER = "$"
#SERIAL_PORT = "/dev/ttyAMA0"
SERIAL_PORT = "\\.\\COM4"
SERIAL_BAUD_RATE = 9600

# DEFAULT_SETTINGS = {
#     "poll_interval_minutes": 1,
#     "check_interval_minutes": 60,
#     "value_ranges": [
#         {
#             "device_id": 2,
#             "min_value": 50,
#             "max_value": 60
#         }
#     ],
#     "device_schedule": [
#         {
#             "id": 2,
#             "schedule": [
#                 {
#                     "on": {
#                         "hour": 18,
#                         "minute": 2,
#                         "second": 30
#                     },
#                     "off": {
#                         "hour": 18,
#                         "minute": 2,
#                         "second": 35
#                     }
#                 }
#             ]
#         }    
#     ]
# }

DEFAULT_SETTINGS = {
    "poll_interval_minutes": 10,
    "check_interval_minutes": 60,
    "value_ranges": [
    ],
    "device_schedule": [    
    ]
}


# DEFAULT_SETTINGS = {}
