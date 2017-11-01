import json
import logging
from random import randint
from datetime import datetime, timedelta
import os, sys

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
                tmp = d.split("_")
                
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
    elif(action_name == "device_reading"):
        # Values are passed without decimal point because of
        # problems with floating point arithmetics on the board
        # so they need to be cast back into float
        payload = tmp[1]
        tmp = payload.split("|")

        try:
            # Has subdevices
            if(len(tmp) > 1):
                device_id = int(tmp[0])

                tmp = tmp[1].split(",")
                for s_d in tmp:
                    id = int(s_d.split("_")[0])
                    value = int(s_d.split("_")[1])
                    float_val = (value / 100) if value > 1000 else ((value / 10) if value > 10 else value)
                    # logger.debug("#### ID: {}, INT_VALUE: {}, FLOAT_VALUE: {}".format(id, value, float_val))

                    models.device_reading.Device_reading.add(id, float_val)
            else:
                tmp = tmp[0].split("_")
                id = int(tmp[0])
                value = int(tmp[1])
                # float_val = (value / 100) if value > 1000 else value / 10
                float_val = (value / 100) if value > 1000 else ((value / 10) if value > 10 else value)
                    
                models.device_reading.Device_reading.add(id, float_val)
        
        except ValueError:
            logger.error("Failed to parse as int")
    elif(action_name == "interrupt"):
        tmp = tmp[1].split("_")
        id = int(tmp[0])
        value = int(tmp[1])
        
        device = models.device.Device.get_by_id(id)
        device.interrupt(value)
    elif(action_name == "error"):
        logger.warning("Board error: '{}'".format(tmp[1]))
        #TODO: Send notification to the user?
    else:
        logger.warning("Unknown serial command '{}'".format(data))

# SETTINGS

def load_settings(path = "settings.json"):
    res = config.DEFAULT_SETTINGS
    try:
        logger.debug("Trying to open '{}'".format(path))

        # Needed to write like this because the commented line doesn't work on windows
        with open(os.path.join(sys.path[0], path), "r") as file:
        # with open(path, "r") as file:
            res = json.load(file)
            print(res)
    except IOError:
        logger.warning("Settings file not found in path, using default settings".format(path))
    except ValueError:
        logger.error("Failed to parse as JSON, using default settings".format(file))
    finally:
        global_vars.SETTINGS = res
        logger.debug("Settings loaded")

def stop_running_actions():
    global actions
    for name in actions:
        actions[name].stop()

    actions = {}

actions = {}
settings = {}
def apply_settings():
    #TODO: Remove this and check if time is set to minute instead of second
    # return
    
    stop_running_actions()
    # print(global_vars.SETTINGS) 
    
    if("poll_interval_minutes" in global_vars.SETTINGS):
        actions["data_polling"] = Action(
            "data_poll_all",
            repeat=Time(minute=global_vars.SETTINGS["poll_interval_minutes"]),
            callbacks=[gh.retrieve_data_all_boards]
            )
        actions["data_polling"].schedule()
                
    # if("check_interval_minutes" in global_vars.SETTINGS):
        
    #     actions["analyze_values"] = Action(
    #         "analyze_values",
    #         repeat=Time(minute=global_vars.SETTINGS["check_interval_minutes"]),
    #         callbacks=[analyze_db_values],
    #         )
    #     actions["analyze_values"].schedule()
    
    if("device_schedule" in global_vars.SETTINGS):
        for d in global_vars.SETTINGS["device_schedule"]:
            device = models.device.Device.get_by_id(d["id"])

            for time in d["schedule"]:
                on_time = Time(time["on"]["hour"], time["on"]["minute"], time["on"]["second"])
                off_time = Time(time["off"]["hour"], time["off"]["minute"], time["off"]["second"])

                # action_on = "device-{}-on".format(d["id"])
                # action_off = "device-{}-off".format(d["id"])
                # logger.debug(global_vars.SETTINGS)

                action_on = "device-{}-on-{}".format(d["id"], on_time)
                action_off = "device-{}-off-{}".format(d["id"], off_time)

                actions[action_on] = Action(
                        action_on,
                        time = on_time,
                        callbacks = [device.on])
                actions[action_on].schedule()

                actions[action_off] = Action(
                        action_off,
                        time = off_time,
                        callbacks = [device.off])
                actions[action_off].schedule()


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
            parent_id = dht.id,
            #type = -1
            )
    if c:
        dht_temp.save()

    dht_hum, c = models.device.Device.get_or_create(
            board_id = b.id,
            name = "humidity",
            parent_id = dht.id,
            #type = -1
            )
    if c:
        dht_hum.save()
 

    temp, c = models.device.Device.get_or_create(
            name = "ds18b20",
            type = 0,
            board_id = b.id
            )
    if c:
        temp.save()
    
    water_temp, c = models.device.Device.get_or_create(
            board_id = b.id,
            name = "water_temp",
            parent_id = temp.id,
            #type = -1
            )
    if c:
        water_temp.save()
    
    water_level, c = models.device.Device.get_or_create(
            name = "water_level",
            type = 3,
            board_id = b.id
            )
    if c:
        water_level.save()

    relay, c = models.device.Device.get_or_create(
            name = "relay",
            type = 2,
            board_id = b.id
            )
    if c:
        relay.save()
    
    now = datetime.now()

    for v in randomize_values(60, 60, 70):
        models.device_reading.Device_reading.create(
                device_id = dht_hum.id,
                value = v,
                timestamp = now
                )
        now = now + timedelta(minutes = 1)
    
    now = datetime.now()

    for v in randomize_values(60, 20, 25):
        models.device_reading.Device_reading.create(
                device_id = dht_temp.id,
                value = v,
                timestamp = now
                )
        now = now + timedelta(minutes = 1)

    now = datetime.now()

    for v in randomize_values(60, 18, 19):
        models.device_reading.Device_reading.create(
                device_id = water_temp.id,
                value = v,
                timestamp = now
             )
        now = now + timedelta(minutes = 1)
    
    now = datetime.now()

    for i in range(60):
        models.device_reading.Device_reading.create(
                device_id = water_level.id,
                value = 1 if (i < 20 or i > 40) else 0 ,
                timestamp = now
             )
        now = now + timedelta(minutes = 1)









