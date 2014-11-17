package com.payu.soa.example.client;

public interface UndoableCommand {
	
	void execute() throws Exception;
	void undo() throws Exception;

}
