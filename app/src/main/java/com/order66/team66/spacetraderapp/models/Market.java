package com.order66.team66.spacetraderapp.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

/**
 * Represents the market on a given planet
 */
public class Market {

    private ResourceModifier worldModifier;
    private ResourceModifier eventModifier = ResourceModifier.NULL;
    private int techLevel;

    private final HashMap<String, Integer> resourceStock = new HashMap<>();
    private final HashMap<String, Integer> resourcePrice = new HashMap<>();
    private final HashMap<String, Boolean> canUseResource = new HashMap<>();
    private final HashMap<String, Boolean> canMakeResource = new HashMap<>();
    /**
     * Creates a new Market object
     *
     * @param worldModifier the world resource modifier of the associated planet
     * @param techLevel the tech level of the associated planet
     */
    public Market(ResourceModifier worldModifier, TechLevel techLevel) {
        this.worldModifier = worldModifier;
        this.techLevel = techLevel.getLevelNumber();

        generateMarket();
    }

    /**
     * Firebase constructor
     */
    public Market(){

    }

    /**
     * Using the tech level and worldModifier, populates the list of trade goods
     * in this market.
     */
    private void generateMarket(){
        int minTechToUse;
        int minTechToMake;

        for (Resource resource : Resource.values()) {
            minTechToUse = resource.getMinTechUse();
            minTechToMake = resource.getMinTechMake();

            if(techLevel >= minTechToUse) {
                canUseResource.put(resource.getName(), true);
                //resourcePrice.put(resource.getName(), resource.getPrice(techLevel, worldModifier, eventModifier));

                if(techLevel >= minTechToMake) {
                    canMakeResource.put(resource.getName(),true);
                    resourceStock.put(resource.getName(), resource.getStock(techLevel, worldModifier, eventModifier));
                }
                else {
                    canMakeResource.put(resource.getName(), false);
                    resourceStock.put(resource.getName(), 0);
                }
            }
            else {
                canUseResource.put(resource.getName(), false);
                resourcePrice.put(resource.getName(), 0);
                canMakeResource.put(resource.getName(), false);
                resourceStock.put(resource.getName(), 0);
            }
        }
    }

    /**
     * Returns if this market can buy the given resource from the player
     *
     * @param resource the resource to check
     * @return true if it can be bought from the player, otherwise false
     */
    public boolean canUse(Resource resource)
    {
        if ((resource != null) && (canUseResource.containsKey(resource.getName()))) {
            Boolean use = canUseResource.get(resource.getName());
            return (use != null) ? use : false;
        }
        return false;
    }

    /**
     * Returns if this market can sell the given resource to the player
     *
     * @param resource the resource to check
     * @return true if it can be sold to the player, otherwise false
     */
    private boolean canMake(Resource resource) {
        if ((resource != null) && (canMakeResource.containsKey(resource.getName()))) {
            Boolean  make = canMakeResource.get(resource.getName());
            return (make != null) ? make : false;
        }
        return false;
    }

    /**
     * Returns the price of the given resource
     *
     * @param resource the resource to check
     * @return the price if it has one, else 0
     */
    @Exclude
    public int getPrice(Resource resource) {
        if ((resource != null) && resourcePrice.containsKey(resource.getName())) {
            Integer price = resourcePrice.get(resource.getName());
            return (price != null) ? price : 0;
        }
        return 0;
    }

    /**
     * Returns the stock of the given resource
     *
     * @param resource the resource to check
     * @return the stock if any is present, else 0
     */
    @Exclude
    public int getStock(Resource resource) {
        if ((resource != null) && resourceStock.containsKey(resource.getName())) {
            Integer stock = resourceStock.get(resource.getName());
            return (stock != null) ? stock : 0;
        }
        return 0;
    }

    /**
     * Returns the current ResourceModifier event for this market
     *
     * @return the current ResourceModifier event, or null if there is none
     */
    public ResourceModifier getEventModifier() {
        return eventModifier;
    }

    /**
     * Gets world modifier
     *
     * @return world modifier
     */
    public ResourceModifier getWorldModifier() {
        return worldModifier;
    }

    /**
     * Updates market prices and stock for a new event resource modifier
     *
     * @param eventMod the event resource modifier to change the market
     */
    public void updateMarket(ResourceModifier eventMod) {
        this.eventModifier = eventMod;

        for (Resource resource : Resource.values()) {
            if(canUse(resource)) {
                //int newPrice = resource.getPrice(techLevel, worldModifier, eventModifier);
                //resourcePrice.put(resource.getName(), newPrice);

                if(canMake(resource)) {
                    int newStock = resource.getStock(techLevel, worldModifier, eventModifier);
                    resourceStock.put(resource.getName(), newStock);
                }
            }
        }
    }

    /**
     * Increases the stock of the given resource in this market
     *
     * @param resource the resource to modify
     * @param amount the amount to increase stock by
     */
    public void increaseStock(Resource resource, int amount) {
        String resName = resource.getName();
        if (resourceStock.containsKey(resName)) {
            Integer stock = resourceStock.get(resName);
            int newStock = ((stock != null) ? stock : 0) + amount;
            resourceStock.put(resName, newStock);
        }
    }

    /**
     *  Decreases the stock of the given resource in this market
     *
     * @param resource the resource to modify
     * @param amount the amount to decrease stock by
     */
    public void decreaseStock(Resource resource, int amount) {
        String resName = resource.getName();
        if (resourceStock.containsKey(resName)) {
            Integer stock = resourceStock.get(resName);
            int newStock = Math.max(((stock != null) ? stock : 0) - amount, 0);
            resourceStock.put(resName, newStock);
        }
    }

    //@Exclude
    //public Set<Resource> getAvailableResources() {
    //    return canUseResource.keySet();
    //}

    /**
     * Returns resource stock
     *
     * @return resource stock
     */
    public HashMap<String, Integer> getResourceStock(){
        return resourceStock;
    }

    /**
     * Returns resource price
     *
     * @return resource price
     */
    public HashMap<String, Integer> getResourcePrice() {
        return resourcePrice;
    }

    /**
     * Returns if you can use resource
     *
     * @return resource usability
     */
    public HashMap<String, Boolean> getCanUseResource() {
        return canUseResource;
    }

    /**
     * Returns if you can make resource
     *
     * @return ability to make resource
     */
    public HashMap<String, Boolean> getCanMakeResource() {
        return canMakeResource;
    }
}
