from flask import Flask, request, jsonify, abort
import logging

from models.board import Board
from models.device import Device
from models.device_reading import Device_reading
import helper

logger = logging.getLogger(__name__)

app = Flask(__name__)
app.config.update({"DEBUG": True})

def list_to_dict(data):
    res = []
    for d in data:
        res.append(d.to_dict())
    return res

def bad_request(code = 400, msg = "An error occured"):
    response = jsonify({"message": msg})
    response.status_code = code
    return response

#BOARD
@app.route('/board')
def get_board_all():
    boards = Board.get_all()
    return jsonify(list_to_dict(boards))

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
        return jsonify(list_to_dict(board.devices))
    except Board.DoesNotExist:
        return bad_request(404, "Board not found")

#DEVICE
@app.route('/device/<int:id>')
def get_device(id):
    try:
        device = Device.get_by_id(id)
        return jsonify(device.to_dict())
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")

#DEVICE_READING
@app.route('/device/<int:id>/reading')
def get_all_device_readings(id):
    try:
        device = Device.get_by_id(id)
        return jsonify(list_to_dict(device.readings))
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")

def get_full_device_with_readings(id, start, end):
    device = Device.get_with_readings(id, start, end)
    board = Board.get_by_id(device.board_id)

    res = {
        "board": board.to_dict(),
        "device": device.to_dict(),
        }
    res["device"]["values"] = device.values


@app.route('/device/<int:id>/from/<start_datetime>/to/<end_datetime>')
def get_device_with_readings(id, start_datetime, end_datetime):
    try:
        logger.debug("Fetching device readings id: {}, start: {}, end: {}".format(id, start_datetime, end_datetime))
        start = helper.string_to_datetime(start_datetime)
        end = helper.string_to_datetime(end_datetime)
        res = get_full_device_with_readings(id, start, end)        

        return jsonify(res)
    except Device.DoesNotExist:
        return bad_request(404, "Device not found")
    except Exception as e:
        logger.exception(e)
        return bad_request()

@app.route('/device/from/<start_datetime>/to/<end_datetime>', methods = ["POST"])
def get_readings_from_devices_list(start_datetime, end_datetime):
    ids = request.form.getlist('ids')
    start = helper.string_to_datetime(start_datetime)
    end = helper.string_to_datetime(end_datetime)

    res = []
    for id in ids:
        dr = get_full_device_with_readings(int(id), start_datetime, end_datetime)
        res.append(dr)
    return jsonify(res)

def run():
    app.run(host='0.0.0.0', use_reloader=False)

if __name__ == '__main__':
    run()













