from flask import Flask, request, jsonify, abort
import logging
import json

from models.board import Board
from models.device import Device
from models.device_reading import Device_reading
import helper
import global_vars
import global_handler as gh

logger = logging.getLogger(__name__)

app = Flask(__name__)
app.config.update({"DEBUG": True})

def bad_request(code = 400, msg = "An error occured"):
    response = jsonify({"message": msg})
    response.status_code = code
    return response

# BOARD
@app.route('/board')
def get_board_all():
    boards = Board.get_all()
    return jsonify(helper.list_to_dict(boards))

@app.route('/board/<int:id>')
def get_board(id):
    try:
        board = Board.get_by_id(id)
        return jsonify(board.to_dict())
    except Board.DoesNotExist:
        return bad_request(404, "Board not found")

@app.route('/board/<int:id>/device')
def get_devices_by_board_id(id):
    try:
        board = Board.get_by_id(id)
        res = []
        
        if(board):
            for d in board.devices:
                if(d.parent_id is None):
                    d.sub_devices = d.get_sub_devices()
                    res.append(d)

        return jsonify(helper.list_to_dict(res))
    except Board.DoesNotExist:
        return bad_request(404, "Board not found")

# DEVICE
@app.route('/device/<int:id>')
def get_device(id):
    try:
        device = Device.get_by_id(id)
        return jsonify(device.to_dict())
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")

# DEVICE_READING
@app.route('/device/<int:id>/reading')
def get_all_device_readings(id):
    try:
        device = Device.get_by_id(id)
        return jsonify(helper.list_to_dict(device.readings))
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")

def get_full_device_with_readings(id, start, end):
    try:
        device = Device.get_with_readings(id, start, end)

        return device
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")

@app.route('/device/<int:id>/from/<start_datetime>/to/<end_datetime>')
def get_device_with_readings(id, start_datetime, end_datetime):
    try:
        logger.debug("Fetching device readings id: {}, start: {}, end: {}".format(id, start_datetime, end_datetime))
        start = helper.string_to_datetime(start_datetime)
        end = helper.string_to_datetime(end_datetime)
        res = get_full_device_with_readings(id, start, end)        

        return jsonify(res.to_dict())
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")
    except Exception as e:
        logger.exception(e)
        return bad_request()

@app.route('/device/from/<start_datetime>/to/<end_datetime>', methods = ["POST"])
def get_readings_from_devices_list(start_datetime, end_datetime):
    ids = request.form.getlist('ids')
    print(ids)
    start = helper.string_to_datetime(start_datetime)
    end = helper.string_to_datetime(end_datetime)

    res = []
    for id in ids:
        dr = get_full_device_with_readings(int(id), start, end)
        if(dr is not None):
            res.append(dr.to_dict())
    return jsonify(res)

@app.route('/device/<int:id>/last-reading')
def get_device_last_reading(id):
    try:
        logger.debug("Fetching device last reading, id: {}".format(id))
        device = Device.get_last_reading(id)        
        return jsonify(device.to_dict())

    except Device.DoesNotExist:
        return bad_request(404, "Device not found")
    except Exception as e:
        logger.exception(e)
        return bad_request()

@app.route('/device/<int:id>/write/<int:value>')
def device_write_value(id, value):
    try:
        logger.debug("Writing to a device, id: {}, value: {}".format(id, value))
        device = Device.get_by_id(id)
        device.write(value)

        return ("OK")

    except Device.DoesNotExist:
        return bad_request(404, "Device not found")
    except Exception as e:
        logger.exception(e)
        return bad_request()


# SETTINGS
@app.route('/settings')
def get_settings():
    return jsonify(global_vars.SETTINGS)

@app.route('/settings', methods = ["POST"])
def save_settings():
    try:
        data = request.get_json(force = True)
        print(data)
        
        global_vars.SETTINGS = data

        gh.save_settings_to_file()
        
        return("OK")
    except Exception:
        logger.exception("Failed to write settings to a file")
        return bad_request(400, "An error occured while trying to write settings to a file")


def run():
    app.run(host='0.0.0.0', use_reloader=False)

if __name__ == '__main__':
    run()











