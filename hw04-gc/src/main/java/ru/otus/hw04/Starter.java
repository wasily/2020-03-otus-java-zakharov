package ru.otus.hw04;

import ru.otus.hw04.gcdemo.Demo;


/*
G1:
-Xmx7500m -Xms7500m -Xlog:gc -XX:+UseG1GC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level

Shenandoah:
-Xmx7500m -Xms7500m -Xlog:gc -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level

ZGC:
-Xmx7500m -Xms7500m -Xlog:gc -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level
*/

public class Starter {
    public static void main(String[] args) {
        new Demo().start();
    }
}
