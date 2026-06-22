package com.example.assignment.model;

public class RoomFilterOptions {
    private String roomType;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private java.util.List<String> selectedFeatures;

    public RoomFilterOptions() {}

    public RoomFilterOptions(String roomType, Integer numberOfBedrooms,
                             Integer numberOfBathrooms, java.util.List<String> selectedFeatures) {
        this.roomType = roomType;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.selectedFeatures = selectedFeatures;
    }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public Integer getNumberOfBedrooms() { return numberOfBedrooms; }
    public void setNumberOfBedrooms(Integer numberOfBedrooms) { this.numberOfBedrooms = numberOfBedrooms; }

    public Integer getNumberOfBathrooms() { return numberOfBathrooms; }
    public void setNumberOfBathrooms(Integer numberOfBathrooms) { this.numberOfBathrooms = numberOfBathrooms; }

    public java.util.List<String> getSelectedFeatures() { return selectedFeatures; }
    public void setSelectedFeatures(java.util.List<String> selectedFeatures) { this.selectedFeatures = selectedFeatures; }
}
