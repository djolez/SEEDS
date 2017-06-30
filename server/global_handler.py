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
    #TODO: Change to JSON 
    action_name = data["action"].split(config.ACTION_MSG_DELIMITER)[0]

    if(action_name == "board_init"):
        try:
            board, created = models.board.Board.get_or_create(name = data["name"])
            if(created):
                board.save()

            #Add mising devices to DB
            for d in data["devices"]:
                device, created = models.device.Device.get_or_create(
                        board = board.id,
                        name = d["name"],
                        type = d["type"]
                        )
                if(created):
                    device.save()
            
            #send back the data to the board in order to save IDs of the devices
            board.sync()
        except Exception:
            logger.exception("An error occured while initializing board DB")
    elif(action_name == "read"):
        for d in data["data"]:
            try:
                #loop through all the values for a device
                for r in d["values"]:
                    models.device_reading.Device_reading.add_from_json(r, d)
            except Exception:
                logger.exception("An error occured while entering device data to DB")
            
        
