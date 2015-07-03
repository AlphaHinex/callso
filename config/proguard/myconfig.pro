-injars ../../build/libs/callso.jar
-outjars ../../build/libs/callso-min.jar
-libraryjars /Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home/jre/lib/rt.jar:android-4.1.1.4.jar
-keep public class org.hinex.alpha.callso.Detector {
    public protected *;
}
-keep public class com.lpr.LPR {
    public protected *;
}
