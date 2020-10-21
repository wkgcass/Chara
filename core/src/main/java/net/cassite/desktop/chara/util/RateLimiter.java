// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility: RateLimiter
 */
public class RateLimiter {
    private final List<Rule> rules;
    private final LinkedList<Event> events = new LinkedList<>();

    /**
     * Construct the rate limiter with a series of configurations.<br>
     * Each two integers form a pair:<br>
     * <ul>
     *     <li>The first is <code>samplingDuration</code></li>
     *     <li>The second is <code>maxCount</code></li>
     * </ul><br>
     * Each successful {@link #request()} will be recorded, and during the <code>samplingDuration</code>,
     * the successful count cannot exceed the <code>maxCount</code> value, otherwise the {@link #request()}
     * will return false, indicating that the <code>request</code> fails.<br>
     * You may set multiple <code>samplingDuration, maxCount</code> pairs. The {@link #request()} can only
     * succeed when all rules are satisfied.
     *
     * @param n configuration: <code>samplingDuration, maxCount, [samplingDuration, maxCount, [...]]</code>
     */
    public RateLimiter(int... n) { // samplingDuration, maxCount
        if (n.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Rule[] rules = new Rule[n.length / 2];
        for (int i = 0; i < n.length; i += 2) {
            Rule rule = new Rule(n[i], n[i + 1]);
            rules[i / 2] = rule;
        }
        this.rules = Arrays.asList(rules);
    }

    /**
     * Request
     *
     * @return true if all rules satisfied, false otherwise
     */
    public boolean request() {
        long current = System.currentTimeMillis();
        removeOutdatedEvents(current);

        assert Logger.debug("rateLimiter events count " + events.size());

        for (Rule r : rules) {
            if (r.fails(current)) {
                return false;
            }
        }
        events.add(new Event(current));
        return true;
    }

    private void removeOutdatedEvents(long current) {
        int minDuration = Integer.MAX_VALUE;
        for (Rule r : rules) {
            if (r.samplingDuration < minDuration) {
                minDuration = r.samplingDuration;
            }
        }
        long notBefore = current - minDuration;
        Iterator<Event> ite = events.iterator();
        while (ite.hasNext()) {
            var e = ite.next();
            if (e.time < notBefore) {
                ite.remove();
            } else {
                break;
            }
        }
    }

    private class Rule {
        public final int samplingDuration;
        public final int maxCount;

        public Rule(int samplingDuration, int maxCount) {
            this.samplingDuration = samplingDuration;
            this.maxCount = maxCount;
        }

        public boolean fails(long current) {
            long notBefore = current - samplingDuration;
            int count = 0;
            for (Event e : events) {
                if (e.time < notBefore) {
                    continue;
                }
                ++count;
                if (count >= maxCount) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class Event {
        public final long time;

        public Event(long time) {
            this.time = time;
        }
    }
}
