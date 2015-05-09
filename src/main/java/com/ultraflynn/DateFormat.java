package com.ultraflynn;

import com.google.common.collect.Lists;
import com.ocpsoft.pretty.time.PrettyTime;
import com.ocpsoft.pretty.time.TimeUnit;
import com.ocpsoft.pretty.time.units.JustNow;
import org.joda.time.DateTime;

import java.util.List;

class DateFormat {
    private final PrettyTime dateFormatter;

    DateFormat() {
        dateFormatter = new PrettyTime();
        List<TimeUnit> newUnits = Lists.newArrayList();
        List<TimeUnit> units = dateFormatter.getUnits();
        for (TimeUnit unit : units) {
            if (!(unit instanceof JustNow)) {
                newUnits.add(unit);
            }
        }
        dateFormatter.setUnits(newUnits);
    }

    String format(DateTime start, DateTime end) {
        dateFormatter.setReference(end.toDate());
        return dateFormatter.format(start.toDate());
    }
}
