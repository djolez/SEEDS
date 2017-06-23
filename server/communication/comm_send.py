import logging

import config
import comm_implementation as comm

def send_cmd(name, params):
    comm.send(name + config.ACTION_MSG_DELIMITER + params)
