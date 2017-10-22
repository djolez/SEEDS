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
        logger.debug("Sending message '{}'".format(msg))
        # serial_conn.write(bytearray(msg + '\n', encoding='ISO-8859-1'))
    except serial.SerialTimeoutException as e:
        pass
        #logger.exception("Serial timeout expired")

def handle_msg(data):
    logger.debug("Received msg: '{}'".format(data))
    gh.process_comm_data(data)

def main():

    while True:
        try:
            #pass
            raw = serial_conn.readline()
            data = str(raw, 'ISO-8859-1')
            handle_msg(data)
        except TypeError as e:
            logging.exception(e)
        except OSError as e:
            logging.exception(e)

def start():
    threading.Thread(target=main).start()
