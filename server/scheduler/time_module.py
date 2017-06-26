import datetime as dt

class Time:

    def __init__(self, hour=0, minute=0, second=0, day=0):
        self.day = day
        self.hour = hour
        self.minute = minute
        self.second = second

    def __str__(self):
        hour = ('0' + str(self.hour)) if self.hour < 10 else self.hour
        minute = ('0' + str(self.minute)) if self.minute < 10 else self.minute
        second = ('0' + str(self.second)) if self.second < 10 else self.second

        return '[{}:{}:{}]'.format(hour, minute, second)

    def to_seconds(self):
        result = 0
        
        if(self.day > 0):
            result += self.day * 24 * 60 * 60
        if(self.hour > 0):
            result += self.hour * 60 * 60
        if(self.minute > 0):
            result += self.minute * 60
        if(self.second > 0):
            result += self.second
        return result

    def to_date(self):
        now = dt.datetime.now()
            
        result = now.replace(hour=self.hour, minute=self.minute, second=self.second)

        return result
