from peewee import *

db_proxy = Proxy()

class BaseModel(Model):
    class Meta:
        database = db_proxy

