/*
 * Created by Anubhav Nanda on 2021.9.27
 * Copyright © 2021 Anubhav Nanda. All rights reserved.
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "Car")
@Entity

public class Car implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the Car table in the CarsDB database.

    CREATE TABLE car
    (
        id            INT AUTO_INCREMENT NOT NULL,
        make          VARCHAR(64)        NOT NULL,
        logo_filename VARCHAR(64)        NOT NULL,
        make_url      VARCHAR(128)       NOT NULL,
        model         VARCHAR(64)        NOT NULL,
        year          INT                NOT NULL,
        price         INT                NOT NULL,
        mileage       INT                NOT NULL,
        city_mpg      INT                NOT NULL,
        highway_mpg   INT                NOT NULL,
        engine_type   VARCHAR(64)        NOT NULL,
        drive_type    VARCHAR(64)        NOT NULL,
        CONSTRAINT pk_car PRIMARY KEY (id)
    );
    ========================================================
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    public Car() {

    }

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /*
     Checks if the Car object identified by 'object' is the same as the Car object identified by 'id'
     Parameter object = Car object identified by 'object'
     Returns True if the Car 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

    public Car(Integer id, String make, String logoFilename, String makeUrl, String model, Integer year, Integer price, Integer mileage, Integer cityMpg, Integer highwayMpg, String engineType, String driveType) {
        this.id = id;
        this.make = make;
        this.logoFilename = logoFilename;
        this.makeUrl = makeUrl;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.cityMpg = cityMpg;
        this.highwayMpg = highwayMpg;
        this.engineType = engineType;
        this.driveType = driveType;
    }

    @Column(name = "make", nullable = false, length = 64)
    private String make;

    @Column(name = "logo_filename", nullable = false, length = 64)
    private String logoFilename;

    @Column(name = "make_url", nullable = false, length = 128)
    private String makeUrl;

    @Column(name = "model", nullable = false, length = 64)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "mileage", nullable = false)
    private Integer mileage;

    @Column(name = "city_mpg", nullable = false)
    private Integer cityMpg;

    @Column(name = "highway_mpg", nullable = false)
    private Integer highwayMpg;

    @Column(name = "engine_type", nullable = false, length = 64)
    private String engineType;

    @Column(name = "drive_type", nullable = false, length = 64)
    private String driveType;

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Integer getHighwayMpg() {
        return highwayMpg;
    }

    public void setHighwayMpg(Integer highwayMpg) {
        this.highwayMpg = highwayMpg;
    }

    public Integer getCityMpg() {
        return cityMpg;
    }

    public void setCityMpg(Integer cityMpg) {
        this.cityMpg = cityMpg;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMakeUrl() {
        return makeUrl;
    }

    public void setMakeUrl(String makeUrl) {
        this.makeUrl = makeUrl;
    }

    public String getLogoFilename() {
        return logoFilename;
    }

    public void setLogoFilename(String logoFilename) {
        this.logoFilename = logoFilename;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}