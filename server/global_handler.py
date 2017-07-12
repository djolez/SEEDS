import json
import logging
from random import randint
from datetime import datetime, timedelta

import config
import models
import helper
import global_vars
import global_handler as gh
from scheduler.action import *
from scheduler.time_module import *

logger = logging.getLogger(__name__)

def retrieve_data_all_boards():
    boards = models.board.Board.get_all()

    for b in boards:
        b.read_all()

def process_comm_data(data):
    tmp = data.split(config.ACTION_MSG_DELIMITER)
    action_name = tmp[0]

    if(action_name == "board_init"):
        try:
            payload = tmp[1]
            
            tmp = payload.split(":")
            board_name = tmp[0]
            devices = tmp[1].split("|")

            board, created = models.board.Board.get_or_create(name = board_name)
            
            if(created):
                board.save()
            
            for d in devices:
                tmp = d.split("-")
                
                has_sub_devices = False
                if(len(tmp) > 1):
                    has_sub_devices = True

                device_name = tmp[0].split(".")[0]
                device_type = (int)(tmp[0].split(".")[1])

                device, created = models.device.Device.get_or_create(
                        board = board.id,
                        name = device_name,
                        type = device_type
                        )
                if(created):
                    device.save()
                
                if(has_sub_devices is False):
                    continue

                sub_devices = tmp[1].split(",")
                
                for s_d in sub_devices:
                    sub_device, created = models.device.Device.get_or_create(
                            board_id = board.id,
                            name = s_d,
                            parent_id = device.id
                            )
                    if(created):
                        sub_device.save()

            board.sync()
        except Exception:
            logger.exception("An error occured while initializing board DB")
    elif(action_name == "reading"):
        payload = tmp[1]
        tmp = payload.split("_")

        device_id = tmp[0]
        value = tmp[1]

        models.device_reading.Device_reading.add(device_id, value)
        
        '''for d in data["data"]:
            try:
                #loop through all the values for a device
                for r in d["values"]:
                    models.device_reading.Device_reading.add_from_json(r, d)
            except Exception:
                logger.exception("An error occured while entering device data to DB")
        '''

# SETTINGS

def load_settings(path = "settings.json"):
    res = config.DEFAULT_SETTINGS
    try:
        logger.debug("Trying to open '{}'".format(path))

        with open(path, "r") as file:
            res = json.load(file)
    except IOError:
        logger.warning("Settings file not found in path, using default settings".format(path))
    except ValueError:
        logger.error("Failed to parse as JSON, using default settings".format(file))
    finally:
        global_vars.SETTINGS = res

def stop_running_actions():
    global actions
    for name in actions:
        actions[name].stop()

    actions = {}

actions = {}
settings = {}
def apply_settings():
    stop_running_actions()
    
    try:
        if(global_vars.SETTINGS["poll_interval_minutes"]):
            
            actions["data_polling"] = Action(
                "data_poll_all",
                repeat=Time(second=global_vars.SETTINGS["poll_interval_minutes"]),
                callbacks=[gh.retrieve_data_all_boards]
                )
            actions["data_polling"].schedule()
                    
        if(global_vars.SETTINGS["check_interval_minutes"]):

            actions["analyze_values"] = Action(
                "analyze_values",
                repeat=Time(second=global_vars.SETTINGS["check_interval_minutes"]),
                callbacks=[analyze_db_values],
                )
            actions["analyze_values"].schedule()
    except KeyError:
        pass

def save_settings_to_file(reload_actions = True):
    logger.debug("Writing settings to a file")

    try:
        with open("settings.json", "w") as file:
            file.write(json.dumps(global_vars.SETTINGS))

        if(reload_actions):
            apply_settings()
    except Exception as e:
        logger.exception(e)

# DB

def analyze_db_values():
    now = datetime.now()
    # TODO: Change hours to minutes
    start = now - timedelta(hours = global_vars.SETTINGS["check_interval_minutes"])
    end = now

    try:
        for device in global_vars.SETTINGS["value_ranges"]:
            db_device, avg = models.device.Device.get_avg_for_subdevice(
                    device["device_id"],
                    start,
                    end
                    )
            # No records found
            if(avg is None):
                continue

            if(avg < device["min_value"]):
                db_device.value_out_of_range(-1)            
            elif(avg > device["max_value"]):
                db_device.value_out_of_range(1)
    except KeyError as e:
        logger.exception("value_ranges not found or badly formatted")
        '''actions["analyze_values"].stop()
        del actions["analyze_values"]
        '''

# MOCK DATA

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
            board_id = b.id,
            name = "temperature",
            parent_id = dht.id
            )
    if c:
        dht_temp.save()

    dht_hum, c = models.device.Device.get_or_create(
            board_id = b.id,
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









