import logging
import json
from peewee import *
from playhouse.shortcuts import model_to_dict, dict_to_model

import configi
from datetime import datetime
from .base import *
from .device import *
from .device_reading import *

import comm_implementation as comm

logger = logging.getLogger(__name__)

class Board(BaseModel):
    name = CharField()

    class Meta:
        database = db_proxy

    def to_dict(self):
        return self.__dict__['_data']

    def send_data(self, data):
        comm.send_msg(json.dumps(data))

    def read_all(self):
        msg = {
            "action": "read",
            "device_id": -1
        }
        self.send_data(msg)

    def sync(self):
        msg = {
            "action": "sync",
            "board_id": self.name,
            "devices": []
        }

        for d in self.devices:
           msg["devices"].append(d.to_dict())
        
        self.send_data(msg) 

    def get_all():
        res = Board.select()
        return res

    def add_test_values():
        i = 0
        while i < 100
            b = Board.create(name = "STM32")
            b.save()

            humidity = Device.create(
                    name = "dht11",
                    type = 1,
                    board_id = b.id
                    )
            humidity.save()

            for v in randomize_values(20, 70):
                Device_reading.create(
                        device_id = humidity.id,
                        value = v,
                        timestamp = Datetime.now()
                        )
            

            temp = Device.create(
                    name = "ds18b20",
                    type = 2,
                    board_id = b.id
                    )
            temp.save()

            for v in randomize_values(20, 40):
                Device_reading.create(
                        device_id = temp.id,
                        value = v,
                        timestamp = Datetime.now()
                        )












