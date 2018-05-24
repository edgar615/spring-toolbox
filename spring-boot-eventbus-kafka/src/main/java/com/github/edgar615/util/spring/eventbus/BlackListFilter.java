package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;

import java.util.function.Function;

public interface BlackListFilter extends Function<Event, Boolean> {
}
