package com.payu.pos.server.service;

import com.payu.pos.server.model.Pos;



/** 
 * Service for manipulateion of Poses. 
 *
 */
public interface PosService {

	/**
	 * Creates a single pos
	 * @param pos the pos to create
	 */
    void createPos(Pos pos);

    /**
     * Finds pos for a given id.
     * @param id the id of a pos to find.
     * @return Pos with laizily loaded data (properties), or <code>null</code> if such a pos does not exist
     * @throws unexpected exceptions. 
     */
    public Pos getPosById(Long id);

    /**
     * Believe it or not, it deletes all posses in a single data shard.
     * @return numbe of deleted poses.
     */
	public abstract int deletePoses();

	/**
	 * Activates pos 
	 * @param posId the id of a pos to activates.
	 * @throws NoSuchPosException if the pos could not be found
	 */
	public void activatePos(long posId);

}
