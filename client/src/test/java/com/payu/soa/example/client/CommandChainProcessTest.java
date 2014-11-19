package com.payu.soa.example.client;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class CommandChainProcessTest {
	
	private StringBuilder log;
	private CommandChainProcess process;

	@Before
	public void setup() {
		this.log = new StringBuilder();
		this.process = new CommandChainProcess();
	}
	
	@Test
	public void shouldCompleteSuccessfully() throws Exception {
		addCommand("C1", true, true);
		addCommand("C2", true, true);
		addCommand("C3", true, true);
		assertProcessLog("executeC1 executeC2 executeC3");
	}
	
	@Test
	public void shouldUndoAfterExecuteFailure() throws Exception {
		addCommand("C1", true, true);
		addCommand("C2", false, true);
		addCommand("C3", true, true);
		assertProcessLog("executeC1 undoC2 undoC1");
	}
	
	@Test
	public void shouldContinueUndoAfterUndoFailure() throws Exception {
		addCommand("C1", true, true);
		addCommand("C2", true, false);
		addCommand("C3", false, true);
		assertProcessLog("executeC1 executeC2 undoC3 undoC1");
	}

	private void assertProcessLog(String expected) {
		process.runProcess();
		Assertions.assertThat(log.toString().trim()).isEqualTo(expected);
	}

	private void addCommand(String name, boolean successfulExecute,
			boolean successfulUndo) {
		process.add(new CommandStub(name, successfulExecute, successfulUndo, log));
	}
}

class CommandStub implements UndoableCommand{
	
	private String name;
	
	private boolean successfulExecute;
	private boolean successfulUndo;

	private Appendable log;
	
	

	public CommandStub(String name, boolean successfulExecute,
			boolean successfulUndo, Appendable log) {
		super();
		this.name = name;
		this.successfulExecute = successfulExecute;
		this.successfulUndo = successfulUndo;
		this.log = log;
	}

	@Override
	public void execute() throws Exception {
		logIfSuccessful(" execute", successfulExecute);
	}

	private void logIfSuccessful(String labelPrevix, boolean succeed)
			throws IOException {
		if (succeed) {
			log.append(labelPrevix).append(name);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void undo() throws Exception {
		logIfSuccessful(" undo", successfulUndo);
	}
	
}
