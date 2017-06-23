import json
import logging

import config
from models.base import *
from models.board import *
from models.device import *
from models.device_reading import *


logger = logging.getLogger(__name__)

def process_json_data(data):
    action_name = data["action"].split(config.ACTION_MSG_DELIMITER)[0]

    if(action_name == "board_init"):
        board = Board.create(name = data["name"])
        board.save()

        for d in data["devices"]:
            device = Device.create(board = board.id, name = d["name"], type = d["type"])
            device.save()
            d["id"] = device.id

        #send back the data to the board in order to save ID of the device

    elif(action_name == "read"):
        for d in data["data"]:
            #loop through all the values for a device
            for r in d["values"]:
                dr = Device_reading.create(device = d["id"], name = r["name"], value = r["value"], datetime = r["datetime"])
                dr.save()
            
        
