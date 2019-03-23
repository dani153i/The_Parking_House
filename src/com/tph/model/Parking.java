package com.tph.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parking {

	private int id;
	private int lotCount;

	// cars currently in the parking house
	private Set<Integer> carIds;
	public int getLotCount() {
		return this.lotCount;
	}

	public Set<Integer> getCarIds() { return carIds; }
	public void setCarIds(Set<Integer> carIds) { this.carIds = carIds; }


	public Parking(int id, int lotCount, Set<Integer> carIds) {
		this.id = id;
		this.lotCount = lotCount;
		this.carIds = carIds;
	}

	public int getLotsUsed() {
		return carIds.size();
	}

	public boolean addCar(int carId) {
		if(carIds.contains(carId))
			return false;
		carIds.add(carId);
		return true;
	}

	public boolean removeCar(int carId) {
		if(!carIds.contains(carId))
			return false;
		carIds.remove(carId);
		return true;
	}
}