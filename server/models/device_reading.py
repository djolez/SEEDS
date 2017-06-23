from peewee import *
from models.base import *
from models.device import *

class Device_reading(BaseModel):
    device = ForeignKeyField(Device, related_name = "readings")
    name = CharField()
    value = IntegerField()
    datetime = DateTimeField()

    class Meta:
        database = db_proxy

