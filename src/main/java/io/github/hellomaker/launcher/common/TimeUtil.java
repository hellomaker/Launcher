package io.github.hellomaker.launcher.common;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TimeUtil {

    static class TimePoint {
        long startPoint;
        long lastPoint;

        public TimePoint(long startPoint, long lastPoint) {
            this.startPoint = startPoint;
            this.lastPoint = lastPoint;
        }

        public long getLastPoint() {
            return lastPoint;
        }

        public void setLastPoint(long lastPoint) {
            this.lastPoint = lastPoint;
        }

        public long getStartPoint() {
            return startPoint;
        }

        public void setStartPoint(long startPoint) {
            this.startPoint = startPoint;
        }
    }

    static ThreadLocal<ConcurrentLinkedDeque<TimePoint>> timePointThreadLocal = new ThreadLocal<>();


    public static void startPoint() {
        long l = System.currentTimeMillis();
        TimePoint timePoint = new TimePoint(l, l);
        ConcurrentLinkedDeque<TimePoint> timePoints = timePointThreadLocal.get();
        if (timePoints == null) {
            timePoints = new ConcurrentLinkedDeque<>();
            timePointThreadLocal.set(timePoints);
        }
        timePoints.addFirst(timePoint);
//        timePointThreadLocal.set(timePoint);
    }

    public static long timeToLastPoint() {
        ConcurrentLinkedDeque<TimePoint> timePoints = timePointThreadLocal.get();
//        TimePoint timePoint = (TimePoint) timePoints;
        if (timePoints == null || timePoints.isEmpty()) {
            throw new IllegalStateException("TimePoint has not been started");
        }
        TimePoint timePoint = timePoints.getFirst();
        long thisPoint = System.currentTimeMillis();
        long timeToLastPoint = thisPoint - timePoint.getLastPoint();
        timePoint.setLastPoint(thisPoint);
        return timeToLastPoint;
    }

    public static long timeToStartPoint() {
        ConcurrentLinkedDeque<TimePoint> timePoints = timePointThreadLocal.get();
        if (timePoints == null || timePoints.isEmpty()) {
            throw new IllegalStateException("TimePoint has not been started");
        }
        TimePoint timePoint = timePoints.getFirst();
        long thisPoint = System.currentTimeMillis();
        long timeToStartPoint = thisPoint - timePoint.getStartPoint();
        closePoint();
        return timeToStartPoint;
    }

    public static void printMillsToLastPoint(String prefix) {
        System.out.println(">>>> TIME UTIL >> ->" + prefix + "<- > : " + timeToLastPoint() + "ms");
    }

    public static void printMillsToStartPoint(String prefix) {
        System.out.println(">>>> TIME UTIL >> ->" + prefix + "<- >>> : " + timeToStartPoint() + "ms");
    }

    public static void closePoint() {
        ConcurrentLinkedDeque<TimePoint> timePoints = timePointThreadLocal.get();
        if (timePoints != null) {
            if (timePoints.isEmpty() || timePoints.size() == 1) {
                timePointThreadLocal.remove();
            } else {
                timePoints.pollFirst();
            }
        }
    }

}
