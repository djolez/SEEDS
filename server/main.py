from peewee import *
import logging

from models.base import *
from models.board import *
from models.device import *
from models.device_reading import *
#import communication.comm_implementation as comm
from scheduler.action import * 
from scheduler.time_module import *

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

logger = logging.getLogger(__name__)
pw = logging.getLogger("peewee")
pw.disabled = True

def init_db(file_path = 'db/db.sqlite'):
    db_proxy.initialize(SqliteDatabase(file_path))
    db_proxy.create_tables([Board, Device, Device_reading], safe=True)

init_db()

comm.handle_msg("""
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

b, c = Board.get_or_create(name = "STM32")
#b.read_all()
a = Action("test", time = Time(hour=18, minute=33, second=5), callbacks=[b.devices[0].read])
a.schedule()

#b.devices[1].write(123)


'''comm.handle_msg("""
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
                        "datetime": "15-11-1990"
                    },
                    {
                        "name": "Humidity",
                        "value": 64,
                        "datetime": "15-11-1990"
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


