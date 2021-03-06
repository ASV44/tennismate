package com.example.hackintosh.tennismate.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by nicu on 11/11/17.
 */

public class Match {
    private String ownerId;
    private int joinLimit;
    private List<String> joins;
    private int locationId;
    private int terrainNumber;
    private Date plannedTime;

    public Match() {
    }

    public Match(String ownerId, int joinLimit, List<String> joins, int locationId, int terrainNumber, Date plannedTime) {
        this.ownerId = ownerId;
        this.joinLimit = joinLimit;
        this.joins = joins;
        this.locationId = locationId;
        this.terrainNumber = terrainNumber;
        this.plannedTime = plannedTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getJoinLimit() {
        return joinLimit;
    }

    public List<String> getJoins() {
        return joins;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getTerrainNumber() {
        return terrainNumber;
    }

    public boolean hasOpenPlaces(){
        return joinLimit > joins.size();
    }

    public Date getPlannedTime() {
        return plannedTime;
    }
}
