# SEEDS 
<img align="right" src="https://github.com/djolez/SEEDS/blob/master/seeds_logo.png"></img>
**SE**nsor**E**d hy**D**roponic **S**ystem is an application developed for healt, money saving and ecology purposes: thanks to it you can make your own place, a farm! Being a very _green_ app, it can improve your skills and provide you a very _green_ thumb! In fact, by using your preferite kind of board and sensors you will be able to manage your indoor growing hydroponic system, just by scrolling over a list of graphs displaying important data about it.

## Functionalities
The apps is based on a _client-server_ approach. First of all, the server interfaces directly to a board through serial connection, and it represents the entry point to all the devices that can measure data about the system. The board can communicate with connected devices thanks to embedded C scripts that poll data according to the settings provided by the server. Then, the server runs a web server exposing _REST APIs_ which can be consumed by the client in order to retrieve data. Of course, users can also send data to the server and/or devices through those _APIs_. In fact, a user can set, for the devices that allow it, a range of acceptable values, a time schedule to work in, a polling interval and a check interval and finally which devices to show data about.<br>
In this way, the plant growing can be completely automatic in the sense that the only action that the user needs to perform is to change the nutrient solution at the right time.
![](https://github.com/djolez/SEEDS/blob/master/SEEDS_scheme(1).png#center)

## Technologies used
The whole system exploits a set of libraries and technologies in order to provide the functionalities mentioned above.
### Server
<img align="right" src="https://github.com/djolez/SEEDS/blob/master/SEEDS_server_techs.png"></img>
* Peewee: a simple and small ORM used to easily store data on the underlying SQL database by means of _models_
* Flask: a micro web framework written in Python used for RESTful request dispatching





