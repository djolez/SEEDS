from peewee import *
import logging
from logging.handlers import TimedRotatingFileHandler
import time
import json
import atexit
import sys, getopt
from threading import Thread
from datetime import datetime, timedelta

from models.base import *
from models.board import *
from models.device import *
from models.device_reading import *
from scheduler.action import * 
from scheduler.time_module import *
import server
import global_handler as gh
import global_vars

logging.basicConfig(
    level = logging.DEBUG,
    format='%(asctime)s -- %(name)s -- %(levelname)s -- %(message)s',
    datefmt='%d-%m-%Y %H:%M:%S')

"""logging.basicConfig(
    level = logging.DEBUG,
    format='%(asctime)s -- %(name)s -- %(levelname)s -- %(message)s',
    datefmt='%d-%m-%Y %H:%M:%S',
    filename='log/all.log')
"""


logger = logging.getLogger('')
log_handler = TimedRotatingFileHandler(
        "logs/log",
        when="midnight",
        #interval=1,
        backupCount=5)
#log_handler.suffix = "any strftime permitted string"
logger.addHandler(log_handler)

pw = logging.getLogger("peewee")
pw.disabled = True

# DB

def init_db(file_path = "db/db.sqlite"):
    db_proxy.initialize(SqliteDatabase(file_path))
    db_proxy.create_tables([Board, Device, Device_reading], safe=True)

def analyze_db_values():
    now = datetime.now()
    # TODO: Change hours to minutes
    start = now - timedelta(hours = settings["check_interval_minutes"])
    end = now

    for device in settings["value_ranges"]:
        db_device, avg = Device.get_avg_for_subdevice(
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
        return res

actions = {}
settings = {}
def apply_settings():
    try:
        if(global_vars.SETTINGS["poll_interval_minutes"]):
            pass
            
            actions["data_polling"] = Action(
                "data_poll_all",
                repeat=Time(minute=global_vars.SETTINGS["poll_interval"]),
                callbacks=[gh.retrieve_data_all_boards]
                )
            actions["data_polling"].schedule()
                    
        if(global_vars.SETTINGS["check_interval_minutes"]):
            pass

            actions["analyze_values"] = Action(
                "analyze_values",
                repeat=Time(second=global_vars.SETTINGS["check_interval_minutes"]),
                callbacks=[analyze_db_values],
                force_execute=True
                )
            actions["analyze_values"].schedule()
    except KeyError:
        pass

active_threads = {}
def data_polling_thread(time):
    gh.retrieve_data_all_boards()
    active_threads["data_polling"] = threading.Timer(time, data_polling_thread, [time])
    active_threads["data_polling"].start()

server_thread = None
def app_start(argv):
    db_initialized = False
    global settings
    global_vars.SETTINGS = load_settings()
    
    #Parse arguments
    opts, args = getopt.getopt(argv, "i:")

    for arg in args:
        
        if(arg == "init_db"):
            logger.debug("DB init start")
            from subprocess import call
            
            call(["rm", "db/db.sqlite"])
            call(["touch", "db/db.sqlite"])
            init_db()
            gh.add_test_values()
            
            logger.debug("DB init finished")
        
        elif(arg == "run_server"):
            active_threads["server"] = Thread(target = server.run)
            active_threads["server"].start()
        
        elif(arg == "run_console_app"):
            from simple_interface import SimpleInterface
            active_threads["console_app"] = Thread(target = SimpleInterface().cmdloop)
            active_threads["console_app"].start()
            global_vars.CONSOLE_MODE = True

        else:
            logger.error("Unknown argument '{}' passed, skipping".format(arg))
   
    if(db_initialized is False):
        init_db()
   
    # TODO: Not working
    # Setup which logs are displayed in the console
    '''console = logging.StreamHandler()
    if(global_vars.CONSOLE_MODE):
        console.setLevel(logging.WARNING)
    else:
        console.setLevel(logging.DEBUG)
    logging.getLogger('').addHandler(console)
    '''
    apply_settings()
    
def cleanup():
    pass
    '''logger.debug("Exit by user request, performing cleanup...")
    for t_name in active_threads:
        active_threads[t_name].stop()

    logger.debug("Done")
    '''
app_start(sys.argv[1:])
#Fix cleaning up of scheduled Actions and Threads(if possible)
atexit.register(cleanup)

while True:
    pass

