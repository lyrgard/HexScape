package fr.lyrgard.hexScape.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class MessageBusExceptionHandler implements SubscriberExceptionHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger("Message Bus");
	
	@Override
	public void handleException(Throwable e, SubscriberExceptionContext context) {
		LOGGER.error("Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(), e);
	}

}
