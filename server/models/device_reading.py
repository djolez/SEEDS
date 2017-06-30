from peewee import *
from models.base import *
from models.device import *

class Device_reading(BaseModel):
    device = ForeignKeyField(Device, related_name = "readings")
    name = CharField()
    value = IntegerField()
    timestamp = DateTimeField()

    class Meta:
        database = db_proxy

    def add_from_json(value, device):
        dr = Device_reading.create(device_id = device["id"], name = value["name"], value = value["value"], timestamp = helper.string_to_datetime(value["timestamp"]))
        dr.save()

    def to_dict(self):
        return self.__dict__['_data']
