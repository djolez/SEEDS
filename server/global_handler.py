import json
import logging

import config
import models
import helper
'''from models.base import *
from models.board import *
from models.device import *
from models.device_reading import *
'''
logger = logging.getLogger(__name__)

def retrieve_data_all_boards():
    boards = models.board.Board.select()

    for b in boards:
        b.read_all()

def process_json_data(data):
    action_name = data["action"].split(config.ACTION_MSG_DELIMITER)[0]

    if(action_name == "board_init"):
        try:
            board, created = models.board.Board.get_or_create(name = data["name"])
            if(created):
                board.save()

            for d in data["devices"]:
                device, created = models.device.Device.get_or_create(board = board.id, name = d["name"], type = d["type"])
                if(created):
                    device.save()
            
                #d["id"] = device.id

            #send back the data to the board in order to save ID of the device
            board.sync()
        except Exception:
            logger.exception("An error occured while initializing board DB")
    elif(action_name == "read"):
        for d in data["data"]:
            try:
                #loop through all the values for a device
                for r in d["values"]:
                    dr = models.device_reading.Device_reading.create(device = d["id"], name = r["name"], value = r["value"], timestamp = helper.string_to_datetime(r["timestamp"]))
                    dr.save()
            except Exception:
                logger.exception("An error occured while entering device data to DB")
            
        
