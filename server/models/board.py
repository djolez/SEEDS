import logging
import json
from peewee import *
from playhouse.shortcuts import model_to_dict, dict_to_model

import config
from .base import *
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


