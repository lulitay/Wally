package com.example.pam_app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.sqrt;

public class Bucket {
    public final String title;
    public final Date dueDate;
    public final BucketType bucketType;
    public final double target;
    public final List<BucketEntry> entries;
    public final Integer id;
    public final String imagePath;
    public final boolean isRecurrent;

    public Bucket(String title, Date dueDate, BucketType bucketType, double target,
                  List<BucketEntry> entries, boolean isRecurrent) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = entries;
        this.id = null;
        this.imagePath = null;
        this.isRecurrent = isRecurrent;
    }

    public Bucket(String title, Date dueDate, BucketType bucketType, double target,
                  List<BucketEntry> entries, Integer id, String imagePath, boolean isRecurrent) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = entries;
        this.id = id;
        this.imagePath = imagePath;
        this.isRecurrent = isRecurrent;
    }

    public Bucket(String title, Date dueDate, BucketType bucketType, double target, boolean isRecurrent) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = new LinkedList<>();
        this.id = null;
        this.imagePath = null;
        this.isRecurrent = isRecurrent;
    }

    public Bucket(String title, Date dueDate, BucketType bucketType, double target, String imagePath, boolean isRecurrent) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = new LinkedList<>();
        this.id = null;
        this.imagePath = imagePath;
        this.isRecurrent = isRecurrent;
    }

    public int getProgress() {
        double total = this.getTotal();

        if (total == 0) {
            return 0;
        }
        return (int) ((total / target) * 100);
    }

    public double getTotal() {
        double total = 0;
        for (BucketEntry entry: this.entries) {
            total += entry.amount;
        }
        return total;
    }

    public String getTotalString() {
        return "" + this.getTotal() + " / " + this.target;
    }

    public String getRemainingTime() {
        Date timeNow = new Date();
        if (dueDate.getTime() > timeNow.getTime()) {
            Map<TimeUnit,Long> diff = this.computeDiff(new Date(), dueDate);
            return "" + diff.get(TimeUnit.DAYS) + "D " + diff.get(TimeUnit.HOURS) + "Hs";
        }
        return "0D 0Hs";
    }

    public Bucket setDueDate(final Date newDueDate) {
        return new Bucket(title, newDueDate, bucketType, target,
                entries, id, imagePath, isRecurrent);
    }

    private Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillis = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<>();
        long milliesRest = diffInMillis;

        for (TimeUnit unit : units) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }

        return result;
    }
}
