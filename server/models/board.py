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
            if(d.parent_id is not None):
                continue

            res = {}
            res["device"] = d.to_dict()
            res["sub_devices"] = []

            s_devices = d.get_sub_devices()
            for s_dev in s_devices:
                res["sub_devices"].append(s_dev.to_dict())

            msg["devices"].append(res)
        
        self.send_data(msg) 

    def send_data(self, data):
        msg = None

        if("action" in data):
            if(data["action"] == "sync"):
                msg = "sync" + config.ACTION_MSG_DELIMITER
                
                i = 0
                for i, d in enumerate(data["devices"]):
                    msg += "{}_{}".format(d["device"]["name"], d["device"]["id"])

                    if(len(d["sub_devices"]) > 0):
                        msg += ":"

                    for j, s_dev in enumerate(d["sub_devices"]):
                        msg += "{}_{}".format(s_dev["name"], s_dev["id"])

                        if(j < len(d["sub_devices"]) - 1):
                            msg += ","
                    
                    if(i < len(data["devices"]) - 1):
                        msg += "|"
                    
            elif(data["action"] == "read"):
                msg = "read{}{}".format(config.ACTION_MSG_DELIMITER, data["device_id"])

            elif(data["action"] == "write"):
                msg = "write{}{}_{}".format(config.ACTION_MSG_DELIMITER, data["device_id"], data["value"])
            
            else:
                logger.error("Unknown action '{}'".format(data["action"]))

            if(msg is not None):
                comm.send_msg(msg)









