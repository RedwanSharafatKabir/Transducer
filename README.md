### Transducer
Sensor app featuring Accelerometer, Gyroscope, Light Sensor and Proximity Sensor. Record sensor changing and movement data in SQLite database. Refresh and store data for every 5 minutes.
- - - -
#### Features -
* Implementation of light sensor, proximity sensor, gyroscope and accelerometer
* Proximity gives the reading when it detects any object within 1 centimeter
* Accelerometer and gyroscope gives the readings of 3 axis' X, Y and Z movement values
* When the home page is open the app records values of above 4 sensors for 5 minutes
* It runs even when the app is paused or minimized<br><br>
#### Challenges -
* To learn SQLite database Insert, Retrieve, Update and Delete operation
* To learn sensor implementation in android application
* For insufficient time I could not implement the chart to show the sensors value, I have shown the values of 4 sensors in a serialize textview from SQLite<br><br>
#### uses-feature in AndroidManifest.xml -
* uses-feature android:name="android.hardware.Sensor"<br>android:required="true"
* uses-feature android:name="android.hardware.sensor.accelerometer"<br>android:required="true"<br><br>
#### BroadCastReceiver service -
* service android:name=".BroadCastReceiverServices.BroadCastService"<br><br>
#### Outputs:

* Home page -
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![home](https://user-images.githubusercontent.com/37416018/112770196-5b134780-9047-11eb-880c-691884993f49.jpg)
- - - -
<br>

* Light sensor -
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![light](https://user-images.githubusercontent.com/37416018/112770199-5c447480-9047-11eb-9c19-563620762a56.jpg)
- - - -
<br>

* Proximity - 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![proximity](https://user-images.githubusercontent.com/37416018/112770197-5babde00-9047-11eb-877a-ff93852e19d5.jpg)
- - - -
<br>

* Gyroscope -
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![gyroscope](https://user-images.githubusercontent.com/37416018/112770192-59498400-9047-11eb-9830-6f9375c3c843.jpg)
- - - -
<br>

* About app -
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![about](https://user-images.githubusercontent.com/37416018/112770195-5b134780-9047-11eb-9a8c-9c4ceec198c7.jpg)
- - - -
<br>

* Built With - 
    * Android Studio
    * Java
    * XML
    * BroadCastReceiver
    * Sensors
- - - -
