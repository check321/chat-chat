package net.check321.chat.core.handler.strategy;

public interface Strategy<P,S> {

    S getStrategy(P param);

}
