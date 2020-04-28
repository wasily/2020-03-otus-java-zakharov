package ru.otus.hw04;

import ru.otus.hw04.gcdemo.Demo;


/*
G1:
-Xmx250m -Xms250m -Xlog:gc -XX:+UseG1GC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+PrintCommandLineFlags
-Xmx7500m -Xms7500m -Xlog:gc -XX:+UseG1GC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+PrintCommandLineFlags

Shenandoah:
-Xmx250m -Xms250m -Xlog:gc -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+PrintCommandLineFlags
-Xmx7500m -Xms7500m -Xlog:gc -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+PrintCommandLineFlags

ZGC:
-Xmx250m -Xms250m -Xlog:gc -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+PrintCommandLineFlags
-Xmx7500m -Xms7500m -Xlog:gc -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+PrintCommandLineFlags
*/

public class Starter {
    public static void main(String[] args) {
        //small heap
        new Demo(1_000_000, 1_500_000).start();
        new Demo(1_300_000, 2_000_000).start();

        //large heap
        new Demo(30_000_000, 45_000_000).start();
        new Demo(40_000_000, 60_000_000).start();
    }
}
