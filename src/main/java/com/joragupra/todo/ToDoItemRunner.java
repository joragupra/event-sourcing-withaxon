package com.joragupra.todo;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;

import java.io.File;
import java.util.UUID;

public class ToDoItemRunner {
	
	public static void main(String[] args) {
		CommandBus commandBus = new SimpleCommandBus();
		CommandGateway commandGateway = new DefaultCommandGateway(commandBus);

		EventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
		EventBus eventBus = new SimpleEventBus();

		EventSourcingRepository repository = new EventSourcingRepository(ToDoItem.class);
		repository.setEventStore(eventStore);
		repository.setEventBus(eventBus);

		AggregateAnnotationCommandHandler.subscribe(ToDoItem.class, repository, commandBus);

		AnnotationEventListenerAdapter.subscribe(new ToDoEventHandler(), eventBus);

		final String itemId = UUID.randomUUID().toString();
		commandGateway.send(new CreateToDoItemCommand(itemId, "Need to do this"));
		commandGateway.send(new MarkCompletedCommand(itemId));
	}

}