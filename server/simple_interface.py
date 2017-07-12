import cmd
import logging
from datetime import datetime, timedelta

import server
from models.board import *
from models.device import *
import global_vars
import global_handler as gh

logger = logging.getLogger(__name__)

class SimpleInterface(cmd.Cmd):

    def get_int_input(text, min_val = None, max_val = None, default_val = 0):
        input_valid = False

        while not input_valid:
            display_text = ""
            if(min_val is not None and max_val is not None):
                display_text = "{}\t({}-{})\tdefault = {}\t: ".format(text, min_val, max_val, default_val)
            elif(default_val is not None):
                display_text = "{}\t\tdefault = {}\t: ".format(text, default_val)
            else:
                display_text = "{}: ".format(text)

            raw = input(display_text)
            if(raw == ""):
                return default_val

            int_val = int(raw)
            
            if(min_val is None or max_val is None):
                return int_val

            try:
                if(int_val < min_val or int_val > max_val):
                    raise ValueError("Value out of range")
                input_valid = True
                return int_val
            except ValueError as e:
                print(e)

    # SETTINGS

    def do_change_update_interval(self, arg):
        hour = SimpleInterface.get_int_input("hour")
        minute = SimpleInterface.get_int_input("minute")
    
        global_vars.SETTINGS["check_interval_minutes"] = hour * 60 + minute
        gh.save_settings_to_file()

    # BOARD

    def do_board_get_all(self, arg):
        boards = Board.get_all()

        print("ID\tNAME")
        print("-" * 20)

        for b in boards:
            print("{}\t{}".format(b.id, b.name))

    # DEVICE

    def do_device_get_by_board_id(self, board_id):
        board = Board.get_by_id(board_id)
        
        print("ID\tNAME\t\tTYPE")
        print("-" * 30)

        for d in board.devices:
            print("{}\t{}\t\t{}".format(d.id, d.name, global_vars.Device_type[d.type]))

    def do_device_get_last_reading(self, device_id):
        try:
            result = Device.get_last_reading(device_id)
            
            print(result.to_dict())
            return

            if(len(result) == 0):
                print("No entries found")

            for r in result:
                print()
                #print("{}\t\t{}\t{}".format(r["device"].name, r["reading"].value, r["reading"].timestamp))
        except Exception:
            pass

    def do_device_get_avg(self, id):
        now = datetime.now()
        start = now - timedelta(hours = 2)
        end = now
        
        print()
        print("Enter start datetime:")
        
        day = SimpleInterface.get_int_input("day", 1, 31, start.day)
        month = SimpleInterface.get_int_input("month", 1, 12, start.month)
        year = SimpleInterface.get_int_input("year", default_val = start.year)
        hour = SimpleInterface.get_int_input("hour", 0, 23, start.hour)
        minute = SimpleInterface.get_int_input("minute", 0, 59, start.minute)
        
        start.replace(day = day, month = month, year = year, hour = hour, minute= minute)
 
        print()
        print("Enter end datetime:")
        day = SimpleInterface.get_int_input("day", 1, 31, end.day)
        month = SimpleInterface.get_int_input("month", 1, 12, end.month)
        year = SimpleInterface.get_int_input("year", default_val = end.year)
        hour = SimpleInterface.get_int_input("hour", 0, 23, end.hour)
        minute = SimpleInterface.get_int_input("minute", 0, 59, end.minute)

        end.replace(day = day, month = month, year = year, hour = hour, minute= minute)

        device = Device.get_by_id(id)
        d, avg = device.get_avg_for_subdevice(start, end)
        print()
        print("AVG for selected period is {}".format(avg))


