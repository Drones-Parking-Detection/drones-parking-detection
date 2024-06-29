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

