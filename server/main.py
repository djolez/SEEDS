from peewee import *
import logging
from logging.handlers import TimedRotatingFileHandler
import time
import json
import atexit
import sys, getopt
from threading import Thread

from models.base import *
from models.board import *
from models.device import *
from models.device_reading import *
from scheduler.action import * 
from scheduler.time_module import *
import server
import global_handler as gh

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


''' Log INFO and higher to console  '''
#console = logging.StreamHandler()
#console.setLevel(logging.DEBUG)
#logging.getLogger('').addHandler(console)

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

def init_db(file_path = "db/db.sqlite"):
    db_proxy.initialize(SqliteDatabase(file_path))
    db_proxy.create_tables([Board, Device, Device_reading], safe=True)

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

def apply_settings():
    try:
        settings = load_settings()

        if(settings["poll_interval"]):
            pass
            '''
            actions["data_polling"] = Action(
                "data_poll_all",
                repeat=Time(second=settings["poll_interval"]),
                callbacks=[gh.retrieve_data_all_boards]
                )
            actions["data_polling"].schedule()
            '''
    except KeyError:
        pass

actions = {}
def data_polling_thread(time):
    gh.retrieve_data_all_boards()
    active_threads["data_polling"] = threading.Timer(time, data_polling_thread, [time])
    active_threads["data_polling"].start()

server_thread = None
def app_start(argv):
    db_initialized = False
    apply_settings()
   
    #Parse arguments
    opts, args = getopt.getopt(argv, "i:")
    print(opts)
    print(args)

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
            server_thread = Thread(target = server.run)
            server_thread.start()
        elif(arg == "run_console_app"):
            pass
        else:
            logger.error("Unknown argument '{}' passed, skipping".format(arg))
    
    if(db_initialized is False):
        init_db()

def cleanup():
    logger.debug("Exit by user request, performing cleanup...")
    for name in actions:
        actions[name].deschedule()

    if(server_thread is not None):
        server_thread.stop()

    logger.debug("Done")

app_start(sys.argv[1:])
atexit.register(cleanup)


