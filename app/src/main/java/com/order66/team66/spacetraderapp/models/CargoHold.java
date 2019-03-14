package com.order66.team66.spacetraderapp.models;

import java.util.EnumMap;

public class CargoHold {

    private EnumMap<Resource, Integer> cargoStock = new EnumMap<>(Resource.class);
    private int maxCapacity;
    private int currentCapactity;

    /**
     * Creates a cargohold object and populates the cargoStock with resources
     *
     * @param capacity the max storage capacity of the CargoHold
     */
    public CargoHold(int capacity) {
        maxCapacity = capacity;

        for (Resource resource : Resource.values()) {
            cargoStock.put(resource, 0);
        }
    }

    /**
     * Gets the current amount of the given resource in the CargoHold
     *
     * @param resource the resource to check the stock of
     * @return the amount of the given resource in the CargoHold
     */
    public int getStock(Resource resource) {
        return cargoStock.get(resource);
    }

    /**
     * Adds the given amount to the stock of the given resource
     *
     * @param resource the resource to increase the stock of
     * @param amount the amount to increase the stock by
     */
    public void increaseStock(Resource resource, int amount) {
        int newStock = cargoStock.get(resource) + amount;
        cargoStock.put(resource, newStock);
        currentCapactity += amount;
    }

    /**
     * Removes the given amount from the stock of the given resource
     *
     * @param resource the resource to decrease the stock of
     * @param amount the amount to decrease the stock by
     */
    public void decreaseStock(Resource resource, int amount) {
        int newStock = Math.max(cargoStock.get(resource) - amount, 0);
        cargoStock.put(resource, newStock);
        currentCapactity -= amount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentCapactity() {
        return currentCapactity;
    }

}
