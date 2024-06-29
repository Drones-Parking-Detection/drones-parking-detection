from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

alerts = []

@app.route('/')
def index():
    return render_template('./index.html')

@app.route('/alerts', methods=['GET'])
def get_alerts():
    return jsonify(alerts)

@app.route('/add_alert', methods=['POST'])
def add_alert():
    global alerts
    data = request.get_json()
    alerts.append(data)
    return jsonify({'status': 'success'}), 200

if __name__ == '__main__':
    app.run(debug=True)




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


