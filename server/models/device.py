from peewee import *
import json

from .base import *
from .board import Board
import comm_implementation as comm

class Device(BaseModel):
    board = ForeignKeyField(Board, related_name = "devices")
    name = CharField()
    type = IntegerField()
    display_name = CharField(null = True)

    class Meta:
        database = db_proxy

    def to_dict(self):
        return self.__dict__['_data']
    
    def get_parent_board(self):
        return Board.get(id = self.board_id)
            
    def read(self):
        msg = {
            "action": "read",
            "device_id": self.id
        }
        self.get_parent_board().send_data(msg)

    def write(self, value):
        msg = {
            "action": "write",
            "device_id": self.id,
            "value": value
        }
        self.get_parent_board().send_data(msg)
         
