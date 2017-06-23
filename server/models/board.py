from peewee import *
from models.base import *

class Board(BaseModel):
    name = CharField()

    class Meta:
        database = db_proxy


