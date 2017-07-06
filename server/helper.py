from datetime import datetime

from global import *

def string_to_datetime(date_str, format="%d-%m-%Y %X"):
    return datetime.strptime(date_str, format)
