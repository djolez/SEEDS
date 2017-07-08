from peewee import *
import json

from .base import *
from .board import *
import comm_implementation as comm

class Device(BaseModel):
    board = ForeignKeyField(Board, related_name = "devices", null = True)
    # Used for subdevices (like temp and humidity in DHT11)
    parent_id = IntegerField(null = True)
    name = CharField()
    type = IntegerField(null = True)
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

    def get_avg_for_subdevice(device_id, start, end):
        from .device_reading import Device_reading

        device = Device.get(id = device_id)
        readings = device.readings.select().where(Device_reading.timestamp.between(start, end))

        try:
            sum_all = 0
            for r in readings:
                sum_all += r.value

            return device, (sum_all / len(readings))
        except ZeroDivisionError:
            logger.warning("No records found for device {}".format(device_id))
            return device, None

    def value_out_of_range(self, direction):
        if(direction < 0):
            logger.warning("Device '{}' - value LOW".format(self.name))
        else:
            logger.warning("Device '{}' - value HIGH".format(self.name))

    def is_complex(self):
        return (self.parent_id is None) and (self.type is not None)

    def get_last_reading(id):
        from .device_reading import Device_reading
        
        device = Device.get_by_id(id = id)
        res = []

        try:
            # Get values for all subdevices
            if(device.is_complex()):
                sub_devices = Device.select().where(Device.parent_id == device.id)
                
                for s_dev in sub_devices:
                    dr = Device_reading.select().where(Device_reading.device_id == s_dev.id).order_by(Device_reading.timestamp.desc()).get()
                    res.append({"device": s_dev, "reading": dr})
            else:
                dr = device.readings.select().order_by(Device_reading.timestamp.desc()).get()
                res.append({"device": device, "reading": dr})
        finally:
            return res




