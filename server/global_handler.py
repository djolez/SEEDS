import json
import logging
from random import randint
from datetime import datetime

import config
import models
import helper
import global_vars

logger = logging.getLogger(__name__)

def retrieve_data_all_boards():
    boards = models.board.Board.get_all()

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
    elif(action_name == "reading"):
        for d in data["data"]:
            try:
                #loop through all the values for a device
                for r in d["values"]:
                    models.device_reading.Device_reading.add_from_json(r, d)
            except Exception:
                logger.exception("An error occured while entering device data to DB")

def save_settings_to_file():
    logger.debug("Writing settings to a file")

    try:
        with open("settings.json", "w") as file:
            file.write(json.dumps(global_vars.SETTINGS))
    except Exception:
        logger.error("Something bad happened")

def randomize_values(count, min_val, max_val):
    res = []
    
    for i in range(0, count):
        res.append(randint(min_val, max_val))
    return res

def add_test_values():
    i = 0
    b, c = models.board.Board.get_or_create(name = "STM32")
    if c:
        b.save()

    dht, c = models.device.Device.get_or_create(
            name = "dht11",
            type = 1,
            board_id = b.id
            )
    if c:
        dht.save()

    dht_temp, c = models.device.Device.get_or_create(
            name = "temperature",
            parent_id = dht.id
            )
    if c:
        dht_temp.save()

    dht_hum, c = models.device.Device.get_or_create(
            name = "humidity",
            parent_id = dht.id
            )
    if c:
        dht_hum.save()
 

    temp, c = models.device.Device.get_or_create(
            name = "ds18b20",
            type = 2,
            board_id = b.id
            )
    if c:
        temp.save()

    for v in randomize_values(50, 20, 70):
        models.device_reading.Device_reading.create(
                device_id = dht_hum.id,
                value = v,
                timestamp = datetime.now()
                )
    
    for v in randomize_values(50, 20, 40):
        models.device_reading.Device_reading.create(
                device_id = dht_temp.id,
                value = v,
                timestamp = datetime.now()
                )

    '''for v in randomize_values(50, 20, 40):
        models.device_reading.Device_reading.create(
                device_id = temp.id,
                name = "water_temp",
                value = v,
                timestamp = datetime.now()
             )
    '''









