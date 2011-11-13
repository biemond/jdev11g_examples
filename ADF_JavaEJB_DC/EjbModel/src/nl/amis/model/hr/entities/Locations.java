package nl.amis.model.hr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
  @NamedQuery(name = "Locations.findAll", query = "select o from Locations o")
})
public class Locations implements Serializable {
    @Column(nullable = false, length = 30)
    private String city;
    @Column(name="COUNTRY_ID")
    private String countryId;
    @Id
    @Column(name="LOCATION_ID", nullable = false)
    private Long locationId;
    @Column(name="POSTAL_CODE", length = 12)
    private String postalCode;
    @Column(name="STATE_PROVINCE", length = 25)
    private String stateProvince;
    @Column(name="STREET_ADDRESS", length = 40)
    private String streetAddress;

    public Locations() {
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}
