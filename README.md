
# JMeter Network Emulator
Control network parameters like delay, jitter corruption or reordering directly in JMeter environment. This plugin is basicly GUI wrapper for `netem` tool.

# Screenshots
![Network Emulator](screen_1.png)

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
