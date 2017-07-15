from peewee import *
import json

from .base import *
from .board import *
import comm_implementation as comm
import helper

class Device(BaseModel):
    board = ForeignKeyField(Board, related_name = "devices", null = False)
    # Used for subdevices (like temp and humidity in DHT11)
    parent_id = IntegerField(null = True)
    name = CharField()
    type = IntegerField(null = True)
    display_name = CharField(null = True)

    class Meta:
        database = db_proxy

    def to_dict(self):
        res = self.__dict__['_data']
        if(hasattr(self, "values")):
            res["values"] = helper.list_to_dict(self.values)
        if(hasattr(self, "sub_devices")):
            res["sub_devices"] = helper.list_to_dict(self.sub_devices)
        if(hasattr(self, "board_full")):
            res["board_full"] = self.board_full.to_dict()
        if(hasattr(self, "last_value")):
            res["last_value"] = self.last_value
        if(hasattr(self, "avg_value")):
            res["avg_value"] = self.avg_value
        
        return res
        #return self.__dict__['_data']

    def to_string(self):
        res = "ID: {}, Name: {}, Type: {}".format(self.id, self.name, self.type)

        if(hasattr(self, "values")):
            res += ", Num_Values: {}".format(len(self.values))
        if(hasattr(self, "sub_devices")):
            res += ", Num_Sub_devices: {}".format(len(self.sub_devices))

        return res

    def get_by_id(id):
        try:
            return Device.get(id = id)
        except Device.DoesNotExist:
            logger.error("Device with id {} not found".format(id))
    
    def get_parent_board(self):
        return Board.get(id = self.board_id)
    
    def get_sub_devices(self):
        res = Device.select().where(Device.parent_id == self.id)
        return res 
    
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

    def on(self):
        self.write(1)

    def off(self):
        self.write(0)
    
    def get_with_readings(id, start, end):
        try:
            # This is to solve the circular dependency problem
            from. device_reading import Device_reading
            
            device = Device.get(id = id)
            board = Board.get(id = device.board_id)
            
            device.board_full = board
            device.values = []
            device.sub_devices = []

            if(device.is_complex()):
                sub_devices = device.get_sub_devices()

                for s_dev in sub_devices:
                    s_dev.last_value = s_dev.readings.order_by(Device_reading.timestamp.desc()).get().value
                    tmp, s_dev.avg_value = Device.get_avg_for_subdevice(s_dev.id, start, end)

                    s_dev.values = []
                    readings = s_dev.readings.select().where(
                            Device_reading.timestamp.between(
                                start, end)) 
                    
                    for r in readings:
                        s_dev.values.append(r)
                        #s_dev.values.append(r.to_dict())

                    device.sub_devices.append(s_dev)
            else:
                device.last_value = device.readings.order_by(Device_reading.timestamp.desc()).get().value
                tmp, device.avg_value = Device.get_avg_for_subdevice(device.id, start, end)
                
                readings = device.readings.select().where(
                        Device_reading.timestamp.between(
                            start, end))

                for r in readings:
                    device.values.append(r)
                    #device.values.append(r.to_dict())
                
            return device
        except Device.DoesNotExist:
            logger.error("Device with id {} not found".format(id))

    def get_avg_for_subdevice(device_id, start, end):
        from .device_reading import Device_reading

        device = Device.get(id = device_id)
        readings = device.readings.select().where(
                Device_reading.timestamp.between(start, end))

        try:
            sum_all = 0
            for r in readings:
                sum_all += r.value

            return device, round(sum_all / len(readings), 2)
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

    def get_sub_devices(self):
        return Device.select().where(Device.parent_id == self.id)

    def get_last_reading(id):
        from .device_reading import Device_reading
        
        device = Device.get_by_id(id = id)
        device.values = []
        device.sub_devices = []

        try:
            # Get values for all subdevices
            if(device.is_complex()):
                sub_devices = device.get_sub_devices()
                
                for s_dev in sub_devices:
                    s_dev.values = [s_dev.readings.order_by(Device_reading.timestamp.desc()).get()]
                    device.sub_devices.append(s_dev)
                    '''dr = Device_reading\
                            .select()\
                            .where(
                                Device_reading.device_id == s_dev.id)\
                            .order_by(
                                Device_reading.timestamp.desc())\
                            .get()

                    res.append({"device": s_dev, "reading": dr})
                    '''
            else:
                dr = device.readings\
                    .order_by(
                        Device_reading.timestamp.desc())\
                    .get()
                
                device.values.append(dr)
                #res.append({"device": device, "reading": dr})
        finally:
            return device




