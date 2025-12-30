
# JMeter Network Emulator
Control network parameters like delay, jitter, corruption or reordering directly in Apache JMeter environment. This plugin is basically a GUI wrapper for [`tc`](https://man7.org/linux/man-pages/man8/tc.8.html)'s [`netem`](https://man7.org/linux/man-pages/man8/tc-netem.8.html) tool.

## Screenshots
![Network Emulator](docs/screen_1.png)
![Network Interface](docs/screen_2.png)
![Emulation Rule](docs/screen_3.png)

## Set up

Clone the project

~~~bash
git clone https://github.com/xbilek26/jmeter-network-emulator.git
~~~

Go to the project directory

~~~bash
cd jmeter-network-emulator
~~~

Build using gradle

~~~bash
./gradlew build
~~~

Copy JAR to JMeter

~~~bash
cp build/libs/jmeter-network-emulator.jar ~/jmeter/location/lib/ext
~~~
