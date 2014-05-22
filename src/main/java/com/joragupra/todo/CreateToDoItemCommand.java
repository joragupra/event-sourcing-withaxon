package com.joragupra.todo;

public class CreateToDoItemCommand {
	
	private final String todoId;
	private final String description;

	public CreateToDoItemCommand(String todoId, String description) {
		this.todoId = todoId;
		this.description = description;
	}

	public String getTodoId() {
		return todoId;
	}

	public String getDescription() {
		return description;
	}

}