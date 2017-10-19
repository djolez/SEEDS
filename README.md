# SEEDS 
<img align="right" src="https://github.com/djolez/SEEDS/blob/master/seeds_logo.png"></img>
**SE**nsor**E**d hy**D**roponic **S**ystem is an application developed for health, money saving and ecology purposes: thanks to it you can make your own farm! By using your prefered kind of a microcontroller and sensors you will be able to manage your indoor growing hydroponic system.

## Functionalities
The app is based on a _client-server_ approach. First of all, the server interfaces directly to a microcontroller through serial connection, and it represents the entry point to all the devices that can measure the data about the hydroponic system. The board can communicate with connected devices thanks to embedded C scripts that poll the data according to the settings provided by the server. Then, the server runs a web server exposing _REST APIs_ which can be consumed by the client in order to retrieve data. Of course, users can also send data to the server and/or devices through those _APIs_. In fact, a user can set, for the devices that allow it, a range of acceptable values, a time schedule to work in, a polling interval and a check interval and finally which devices to show data about. When setting up the system, the user is able to specify as many boards he preferes, along with the devices attached to it. 

In particular, the data is shown in the client by plotting it in a graph related to a specific device (i.e: data belonging to a specific device, considering all the sub-devices connected to it, are shown in the same graph) with the exception of some kind of devices like relays which can be only toggled.
<br>
In this way, the plant growing can be completely automated in the sense that the only action that the user needs to perform is to change the nutrient solution at the right time.
<br><br><br><br><br>
![](https://github.com/djolez/SEEDS/blob/master/SEEDS_scheme.png#center)
<br>

## Technologies used
<img align="right" src="https://github.com/djolez/SEEDS/blob/master/SEEDS_server_techs.png"></img>

The whole system exploits a set of libraries and technologies in order to provide the functionalities mentioned above.
### Server
The server is completely written in python, so it can run even on small embedded systems like RaspberryPi. In particular, it makes use of:
* _Flask_ a micro web framework written in Python used for RESTful request dispatching
* _SQLite_ a lightweight database
* _Peewee_ a simple and small ORM used to easily store data on the underlying SQL database by means of _models_

### Client
<img align="right" src="https://github.com/djolez/SEEDS/blob/master/SEEDS_android_techs.png"></img>

The client is represented by an Android application, which uses some opensource libraries to let the user control the server:
* _Spring for Android_ a framework used to provide components of the Spring family like a Rest Client and auth support for accessing secure APIs. It can be found at https://github.com/spring-projects/spring-android
* _Jackson_ a library to serialize a Java Entity to a JSON String and control the mapping process. It can be found at https://github.com/FasterXML/jackson-core
* _PhilJay / MPAndroidChart_ a very easy to use library that cares of taking data and display them in a graph. It supports multiple kind of graphs (in this case, a lineChart), providing high flexibility in terms of retrieving data, operating with them and showing them in a customizable pretty way. It can be found at https://github.com/PhilJay/MPAndroidChart
* _Leavjenn / SmoothDateRangePicker_ an android widget for selecting date range quickly and easily, following Material Design principle.https://github.com/leavjenn/SmoothDateRangePicker






