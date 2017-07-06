from peewee import *
import json

from .base import *
from .board import *
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
    
    def get_by_id(id):
        try:
            return Device.get(id = id)
        except Device.DoesNotExist:
            logger.error("Device with id {} not found".format(id))
    
    def get_with_readings(id, start, end):
        try:
            # This is to solve the circular dependency problem
            from. device_reading import Device_reading
            
            device = Device.get(id = id)
            board = Board.get(id = device.board_id)
            readings = device.readings.select().where(Device_reading.timestamp.between(start, end))

            device.values = {}
            for r in readings:
                if(not r.name in device.values):
                    logger.debug("Found new sub-device: '{}'".format(r.name))
                    device.values[r.name] = []
                device.values[r.name].append(r.to_dict())
            
            return device
        except Device.DoesNotExist:
            logger.error("Device with id {} not found".format(id))

















