package com.payu.pos.server.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.payu.pos.server.model.Pos;
import com.payu.training.database.GenericDatabase;

@Component
public class PosDatabase extends GenericDatabase<Pos> {

	
	@PostConstruct
	private void fillWithInitData() {
		this.create(new Pos("Pos1", "visa", "pex", "mastercard"));
		this.create(new Pos("Pos2", "visa",  "mastercard"));
		this.create(new Pos("Pos2", "visa", "pbl",  "mastercard"));
	}

	@Override
	protected void setId(Pos object, long id) {
		object.setId(id);
	}

	@Override
	protected long getId(Pos object) {
		return object.getId();
	}

}
