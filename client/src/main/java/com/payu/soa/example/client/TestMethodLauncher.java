package com.payu.soa.example.client;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.payu.discovery.Discover;

@Service
public class TestMethodLauncher {
	

	public static final int PARALELL_RUNS = 5;

	private final TestBean testBean;

	private ExcerciseStartupBean excerciseStartupBean;

	@Autowired
	public TestMethodLauncher(TestBean testBean, ExcerciseStartupBean excerciseStartupBean) {
		this.testBean = testBean;
		this.excerciseStartupBean = excerciseStartupBean;
	}

	private List<Integer> parallelRunArgs = IntStream
			.rangeClosed(0, PARALELL_RUNS).boxed()
			.collect(Collectors.toList());


	@Scheduled(initialDelay = 1000, fixedRate = 5000)
	public void processCron() {
		parallelRunArgs.parallelStream().forEach(testBean::runProcess);
	}
	
	@Scheduled(initialDelay = 10000, fixedRate = 60000)
	public void startupCron() throws InterruptedException {
		excerciseStartupBean.ensureServicesDiscovered();
	}

}
