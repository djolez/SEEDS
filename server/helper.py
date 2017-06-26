from datetime import datetime

def string_to_datetime(date_str, format="%d-%m-%Y %X"):
    return datetime.strptime(date_str, format)
