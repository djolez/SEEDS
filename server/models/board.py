import logging
import json
from peewee import *
from playhouse.shortcuts import model_to_dict, dict_to_model

import config
from datetime import datetime
from .base import *

import comm_implementation as comm

logger = logging.getLogger(__name__)

class Board(BaseModel):
    name = CharField()

    class Meta:
        database = db_proxy

    def to_dict(self):
        return self.__dict__['_data']

    def get_all():
        res = Board.select()
        return res

    def get_by_id(id):
        try:
            board = Board.get(Board.id == id)
            return board 
        except Board.DoesNotExist:
            logger.error("Board with id {} not found".format(id))

    def read_all(self):
        msg = {
            "action": "read",
            "device_id": -1
        }
        self.send_data(msg)

    def sync(self):
        msg = {
            "action": "sync",
            "board_id": self.id,
            "devices": []
        }

        for d in self.devices:
           msg["devices"].append(d.to_dict())
        
        self.send_data(msg) 

    def send_data(self, data):
        msg = ""

        if("action" in data):
            if(data["action"] == "sync"):
                msg = "sync" + config.ACTION_MSG_DELIMITER
                
                i = 0
                for d in data["devices"]:
                    msg += "{}_{}".format(d["name"], d["id"])
                    if(i < len(data["devices"]) - 1):
                        msg += ","
                    i += 1
                    
            if(data["action"] == "read"):
                msg = "read{}{}".format(config.ACTION_MSG_DELIMITER, data["device_id"])

            if(data["action"] == "write"):
                msg = "write{}{}_{}".format(config.ACTION_MSG_DELIMITER, data["device_id"], data["value"])
            

        comm.send_msg(msg)









