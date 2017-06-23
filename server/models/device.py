from peewee import *
from models.base import *
from models.board import *

class Device(BaseModel):
    board = ForeignKeyField(Board, related_name = "devices")
    name = CharField()
    type = IntegerField()
    display_name = CharField(null = True)

    class Meta:
        database = db_proxy

