
from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO, emit
import json

app = Flask(__name__)
socketio = SocketIO(app)

alerts = []

@app.route('/')
def index():
    return render_template('index.html')


@app.route('/add_alert', methods=['POST'])
def receive_alert():
    try:
        alert_data = request.json
        socketio.emit('new_alert', alert_data)
        return jsonify({"status": "success"}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400


if __name__ == '__main__':
    socketio.run(app, debug=True, port=5000)