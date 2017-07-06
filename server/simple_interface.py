import cmd
import logging

import server
from models.board import *
import global_vars

logger = logging.getLogger(__name__)

class SimpleInterface(cmd.Cmd):

    def get_int_input(text, min_val = None, max_val = None, default_val = 0):
        input_valid = False

        while not input_valid:
            display_text = ""
            if(min_val is not None and max_val is not None):
                display_text = "{} ({}-{}), default = {} : ".format(text, min_val, max_val, default_val)
            else:
                display_text = "{}: ".format(text)

            raw = input(display_text)
            if(raw == ""):
                return default_val

            try:
                int_val = int(raw)
                if(int_val < min_val or int_val > max_val):
                    raise ValueError("Value out of range")
                input_valid = True
                return int_val
            except ValueError as e:
                print(e)

    def do_change_update_interval(self, arg):
        hour = SimpleInterface.get_int_input("Hour", 0, 23)
        minute = SimpleInterface.get_int_input("Minute", 0, 59)
        second = SimpleInterface.get_int_input("Second", 0, 59)
    
        #TODO: save to the config file

    def do_get_all_boards(self, arg):
        boards = Board.get_all()

        print("ID\tNAME")
        print("-" * 20)

        for b in boards:
            print("{}\t{}".format(b.id, b.name))

    def do_get_devices_by_board_id(self, board_id):
        board = Board.get_by_id(board_id)
        
        print("ID\tNAME\t\tTYPE")
        print("-" * 30)

        for d in board.devices:
            print("{}\t{}\t\t{}".format(d.id, d.name, global_vars.Device_type[d.type]))







