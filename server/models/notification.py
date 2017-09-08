from peewee import *
import json
import logging

from .base import *
from .device import *

logger = logging.getLogger(__name__)

class Notification(BaseModel):
    device = ForeignKeyField(Device, related_name = "notifications", null = True)
    # Can be between 0 and 3
    importance = IntegerField()
    text = CharField()
    active = BooleanField(default = True)

    class Meta:
        database = db_proxy

    def to_dict(self):
        return self.__dict__["_data"]

    def to_string(self):
        res = "device: {}, importance: {}, text: {}, active: {}".format(self.device, self.importance, self.text, self.active)

        return res

    def add(text, device_id = None, importance = 0):
        logger.debug("Adding notification '{}' for device {}".format(text, device_id))
        
        n = Notification.insert(
            device = device_id,
            importance = importance,
            text = text)
        n.execute()

