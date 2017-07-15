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
import comm_implementation as comm

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

active_threads = {}
def app_start(argv):
    db_initialized = False
    gh.load_settings()
    
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
            active_threads["server"] = Thread(target = server.run, daemon = True)
            active_threads["server"].start()
        
        elif(arg == "run_console_app"):
            from simple_interface import SimpleInterface
            active_threads["console_app"] = Thread(target = SimpleInterface().cmdloop, daemon = True)
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

    #gh.apply_settings()
    
def cleanup():
    logger.debug("Exit by user request, performing cleanup...")
    
    gh.stop_running_actions()
    
    '''for t_name in active_threads:
        print(active_threads[t_name].isDaemon())
    '''

    logger.debug("Done")

app_start(sys.argv[1:])

# COMM TEST
'''b = Board.get(id = 1)
b.sync()
b.read_all()

d = Device.get_by_id(1)
d.write(1)

d.read()
'''

comm.handle_msg("reading$2_283")

#comm.handle_msg("board_init$STM32:dht11.1-humidity,temperature|ds18b20.0")

#comm.handle_msg("reading$2_2850")

atexit.register(cleanup)

while True:
    pass

