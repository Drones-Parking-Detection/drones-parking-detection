
from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO, emit
import json

app = Flask(__name__)
socketio = SocketIO(app)

alerts = []

@app.route('/')
def index():
    return render_template('index.html')

# @app.route('/alerts')
# def get_alerts():
#     return jsonify(alerts)

@app.route('/add_alert', methods=['POST'])
def receive_alert():
    try:
        alert_data = request.json
        socketio.emit('new_alert', alert_data)
        return jsonify({"status": "success"}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400

# @socketio.on('connect')
# def handle_connect():
#     print('Client connected')

# @socketio.on('disconnect')
# def handle_disconnect():
#     print('Client disconnected')

if __name__ == '__main__':
    socketio.run(app, debug=True, port=5000)




# from flask import Flask, render_template, jsonify, request
# app = Flask(__name__)

# alerts = []

# @app.route('/')
# def index():
#     return render_template('./index.html')

# @app.route('/alerts', methods=['GET'])
# def get_alerts():
#     return jsonify(alerts)

# @app.route('/add_alert', methods=['POST'])
# def add_alert():
#     global alerts
#     data = request.get_json()
#     alerts.append(data)
#     return jsonify({'status': 'success'}), 200

# if __name__ == '__main__':
#     app.run(debug=True)




# from flask import Flask, render_template, jsonify, request, session

# app = Flask(__name__)

# app.secret_key = "admin"

# @app.route('/')
# def index():
#     session["alerts"] = []
#     return render_template('./index.html')

# @app.route('/alerts', methods=['GET'])
# def get_alerts():
#     res = session.get("alerts")
#     session.pop("alerts", default=[])
#     return jsonify(res)

# @app.route('/add_alert', methods=['POST'])
# def add_alert():
#     if session["alerts"]:
#         data = request.get_json()
#         session["alerts"].append(data)
#         return jsonify({'status': 'success'}), 200
#     return jsonify({'status': 'a'}), 200


# if __name__ == '__main__':
#     app.run(debug=True)


