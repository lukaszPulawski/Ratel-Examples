package com.payu.soa.example.client;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandChainProcess {

	private static Logger LOGGER = LoggerFactory
			.getLogger(CommandChainProcess.class);

	private List<UndoableCommand> commands = new LinkedList<UndoableCommand>();

	private ExecutionStartegy executionStrategy = ExecutionStartegy.PERFORM;

	private ListIterator<UndoableCommand> currentStepIterator;

	public boolean add(UndoableCommand e) {
		return commands.add(e);
	}

	public void runProcess() {
		currentStepIterator = commands.listIterator();
		while (executionStrategy != null) {
			executionStrategy = executionStrategy.doStep(currentStepIterator);
		}
	}

	private static enum ExecutionStartegy {
		PERFORM {
			@Override
			public ExecutionStartegy doStep(
					ListIterator<UndoableCommand> currentStepIterator) {
				if (!currentStepIterator.hasNext()) {
					return null;
				}

				try {
					currentStepIterator.next().execute();
					return PERFORM;
				} catch (Exception e) {
					return COMPENSATE;
				}
			}
		},

		COMPENSATE {
			@Override
			public ExecutionStartegy doStep(
					ListIterator<UndoableCommand> currentStepIterator) {
				if (!currentStepIterator.hasPrevious()) {
					return null;
				}

				try {
					currentStepIterator.previous().undo();
				} catch (Exception e) {
					LOGGER.error("Compensation of step {0} unsuccessful");
				}
				return COMPENSATE;
			}
		};

		public abstract ExecutionStartegy doStep(
				ListIterator<UndoableCommand> currentStepIterator);
	}
}