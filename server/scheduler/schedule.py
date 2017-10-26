import logging
import threading
import pprint
from datetime import datetime

logger = logging.getLogger(__name__)

class Schedule:

    def __init__(self, name, date_time, callback, args=[], manual_start=False):
        self.name = name
        self.date_time = date_time
        self.callback = callback
        self.args = args
        self.timer_obj = None
        
        #logger.debug('Initialized Schedule {}'.format(self)) 
    
        if not manual_start:
            self.start()

    def __del__(self):
        if(self.timer_obj):
            self.timer_obj.cancel()

    def __str__(self):
        return '[name: {}, date_time: {}, args: {}]'.format(self.name, self.date_time, self.args)

        #return '[name: {}, date_time: {}, callback: {}, args: {}, timer_obj: {}]'.format(self.name, self.date_time, self.callback.__name__, self.args, self.timer_obj)

    def start(self):
        logger.debug('Setting timer for {}'.format(self))
        now = datetime.now()
        
        #Accept only time that is ahead of now
        if(self.date_time < now):
            logger.error('Cannot schedule call, specified date_time "{}" is before current time'.format(self.date_time))
            return

        time_in_sec = (self.date_time - now).total_seconds()

        t = threading.Timer(time_in_sec, self.callback, self.args)
        t.name = '{}-{}'.format(self.name, self.date_time.strftime('%H:%M:%S:%f'))
        t.start()
        self.timer_obj = t

    def stop(self):
        logger.debug('Stopping {}'.format(self))

        if(self.timer_obj):
            self.timer_obj.cancel()
            self.timer_obj = None


