package io.smartcat.ranger.data.generator.rules;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import io.smartcat.ranger.data.generator.distribution.Distribution;

/**
 * Rule for creating random range values.
 */
public class RangeRuleDate extends RangeRule<Date> {

    /**
     * For documentation, look at {@link RangeRule#RangeRule(List)}.
     *
     * @param rangeMarkers List of dates that denotes the ranges.
     */
    public RangeRuleDate(List<Date> rangeMarkers) {
        super(rangeMarkers);
    }

    /**
     * For documentation, look at {@link RangeRule#RangeRule(List, Distribution)}.
     *
     * @param rangeMarkers List of dates that denotes the ranges.
     * @param distribution Distribution to be used when generating values.
     */
    public RangeRuleDate(List<Date> rangeMarkers, Distribution distribution) {
        super(rangeMarkers, distribution);
    }

    @Override
    protected Date nextValue(Date lower, Date upper) {
        Long value = distribution.nextLong(lower.getTime(), upper.getTime());
        Instant instant = Instant.ofEpochMilli(value);
        return Date.from(instant);
    }

    @Override
    protected Date nextEdgeCase() {
        // Since the definition of the range is inclusive at the beginning and end of the range is exclusive,
        // one millisecond is subtracted from the end of the range.
        // Ranges are defined with a list and there can be several ranges defined in one list, e.g. [a,b),[c,d),[e,f),
        // therefore ends of the ranges are on odd positions.
        if (rangeEdges.size() % 2 == 0) {
            return rangeEdges.remove(0);
        } else {
            long edge = rangeEdges.remove(0).getTime() - 1;
            Instant largestInstant = Instant.ofEpochMilli(edge);
            return Date.from(largestInstant);
        }
    }
}
