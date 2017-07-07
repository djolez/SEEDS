ACTION_MSG_DELIMITER = "$"
SERIAL_PORT = "/dev/ttyAMA0"
SERIAL_BAUD_RATE = 9600

DEFAULT_SETTINGS = {
    "poll_interval_minutes": 10,
    "check_interval_minutes": 5,
    "value_ranges": [
        {
            "device_id": 2,
            "min_value": 50,
            "max_value": 60
        }
    ]
}

