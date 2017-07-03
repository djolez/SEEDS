from peewee import *
import logging
from logging.handlers import TimedRotatingFileHandler
import time
import json
import atexit

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

def init_db(file_path = "db/db.sqlite", init_values = False):
    db_proxy.initialize(SqliteDatabase(file_path))
    db_proxy.create_tables([Board, Device, Device_reading], safe=True)

    if(init_values):
        gh.add_test_values()

def load_settings(path = "settings.json"):
    res = config.DEFAULT_SETTINGS
    try:
        logger.debug("Trying to open '{}'".format(path))

        with open(path, "r") as file:
            res = json.load(file)
    except IOError:
        logger.exception("Settings file not found in path, using default settings".format(path))
    except ValueError:
        logger.exception("Failed to parse as JSON, using default settings".format(file))
    finally:
        return res

def apply_settings():
    try:
        settings = load_settings()

        if(settings["poll_interval"]):
            pass
            #actions["data_polling"] = Action("data_poll_all", repeat=Time(second=settings["poll_interval"]), callbacks=[gh.retrieve_data_all_boards])
            #actions["data_polling"].schedule()
    except KeyError:
        pass

actions = {}
def data_polling_thread(time):
    gh.retrieve_data_all_boards()
    active_threads["data_polling"] = threading.Timer(time, data_polling_thread, [time])
    active_threads["data_polling"].start()

def cleanup():
    logger.debug("Exit by user request, performing cleanup...")
    for name in actions:
        actions[name].deschedule()
    logger.debug("Done")

init_db()
#init_db(init_values = True)

apply_settings()

'''comm.handle_msg("""
    {
        "action": "board_init",
        "name": "STM32",
        "devices": [
            {
                "name": "dht11",
                "type": 1
            },
            {
                "name": "ds18b20",
                "type": 2
            }

        ]
    }
""")
'''

#b,c = Board.get_or_create(name = "STM32")
#b.read_all()
#a = Action("test", time = Time(hour=18, minute=33, second=5), callbacks=[b.devices[0].read])
#a.schedule()

'''
comm.handle_msg("""
    {
        "action": "read$all",
        "data": [
            {
                "id": 1,
                "name": "dht11",
                "values": [
                    {
                        "name": "Temperature",
                        "value": 28,
                        "timestamp": "15-11-1990 13:30:21"
                    },
                    {
                        "name": "Humidity",
                        "value": 64,
                        "timestamp": "15-11-1990 13:30:21"
                    }

                ]
            }
        ]
    }
""")
'''

'''b = Board.create(name = "Board_1")
b.save()
d = Device.create(name = "Temp_sensor", type = 1, board = b.id)
d.save()
dr = Device_reading.create(device = d.id, value = 28, datetime = "22-06-2017 16:37:00")
dr.save()
'''

server.run()
atexit.register(cleanup)
