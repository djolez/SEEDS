import serial
import json
import threading
import logging

import config
import global_handler as gh

serial_conn = serial.Serial(config.SERIAL_PORT, config.SERIAL_BAUD_RATE, write_timeout = 1)
logger = logging.getLogger(__name__)

def send_msg(msg):
    try:
        logger.debug("Sending message ({})".format(msg))
        serial_conn.write(bytearray(msg + '\n', encoding='utf-8'))
    except serial.SerialTimeoutException as e:
        pass
        #logger.exception("Serial timeout expired")

def handle_msg(data):
    logger.debug("Trying to parse JSON string ({})".format(data))
    try:
        dict_obj = json.loads(data)
        gh.process_json_data(dict_obj)
    except ValueError:
        logger.exception('Failed to parse JSON: {}'.format(data))

def main():

    while True:
        try:
            raw = serial_conn.readline()
            data = str(raw, 'utf-8')
            handle_msg(data)
        
        except TypeError as e:
            logging.exception(e)
        except OSError as e:
            logging.exception(e)

#threading.Thread(target=main).start()
