package ashiqur.androidsensors;

class SensorData {
    String light, accelerometer, gyro, magneto, temperature, pressure, humidity, proximity, orientation, pedometer;
    public SensorData() {

    }
    public SensorData(String light, String accelerometer, String gyro, String magneto, String temperature, String pressure, String humidity, String proximity, String orientation, String pedometer) {
        this.light = light;
        this.accelerometer = accelerometer;
        this.gyro = gyro;
        this.magneto = magneto;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.proximity = proximity;
        this.orientation = orientation;
        this.pedometer = pedometer;
    }


    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(String accelerometer) {
        this.accelerometer = accelerometer;
    }

    public String getGyro() {
        return gyro;
    }

    public void setGyro(String gyro) {
        this.gyro = gyro;
    }

    public String getMagneto() {
        return magneto;
    }

    public void setMagneto(String magneto) {
        this.magneto = magneto;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getProximity() {
        return proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getPedometer() {
        return pedometer;
    }

    public void setPedometer(String pedometer) {
        this.pedometer = pedometer;
    }
}
