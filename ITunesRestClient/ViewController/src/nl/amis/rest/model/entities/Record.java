package nl.amis.rest.model.entities;

import java.io.Serializable;

public class Record implements Serializable {

	private static final long serialVersionUID = 1L;

	public Record() {
    }

    String wrapperType;
    String mediaType;
    String kind;
    String artistName;
    String itemParentName;
    String itemParentCensoredName;
    String itemCensoredName;
    String itemName;
    String artistLinkUrl;
    String artworkUrl60;
    String artworkUrl100;
    String country;
    String currency;
    String discCount;
    String discNumber;
    String itemExplicitness;
    String itemLinkUrl;
    Double itemPrice;
    String itemParentExplicitness;
    String itemParentLinkUrl;
    Double itemParentPrice;
    String previewUrl;
    String primaryGenreName;
    Integer trackCount;
    Integer trackNumber;
    Long trackTime;

    public void setWrapperType(String wrapperType) {
        this.wrapperType = wrapperType;
    }

    public String getWrapperType() {
        return wrapperType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setItemParentName(String itemParentName) {
        this.itemParentName = itemParentName;
    }

    public String getItemParentName() {
        return itemParentName;
    }

    public void setItemParentCensoredName(String itemParentCensoredName) {
        this.itemParentCensoredName = itemParentCensoredName;
    }

    public String getItemParentCensoredName() {
        return itemParentCensoredName;
    }

    public void setItemCensoredName(String itemCensoredName) {
        this.itemCensoredName = itemCensoredName;
    }

    public String getItemCensoredName() {
        return itemCensoredName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setArtistLinkUrl(String artistLinkUrl) {
        this.artistLinkUrl = artistLinkUrl;
    }

    public String getArtistLinkUrl() {
        return artistLinkUrl;
    }

    public void setArtworkUrl60(String artworkUrl60) {
        this.artworkUrl60 = artworkUrl60;
    }

    public String getArtworkUrl60() {
        return artworkUrl60;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setDiscCount(String discCount) {
        this.discCount = discCount;
    }

    public String getDiscCount() {
        return discCount;
    }

    public void setDiscNumber(String discNumber) {
        this.discNumber = discNumber;
    }

    public String getDiscNumber() {
        return discNumber;
    }

    public void setItemExplicitness(String itemExplicitness) {
        this.itemExplicitness = itemExplicitness;
    }

    public String getItemExplicitness() {
        return itemExplicitness;
    }

    public void setItemLinkUrl(String itemLinkUrl) {
        this.itemLinkUrl = itemLinkUrl;
    }

    public String getItemLinkUrl() {
        return itemLinkUrl;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemParentExplicitness(String itemParentExplicitness) {
        this.itemParentExplicitness = itemParentExplicitness;
    }

    public String getItemParentExplicitness() {
        return itemParentExplicitness;
    }

    public void setItemParentLinkUrl(String itemParentLinkUrl) {
        this.itemParentLinkUrl = itemParentLinkUrl;
    }

    public String getItemParentLinkUrl() {
        return itemParentLinkUrl;
    }

    public void setItemParentPrice(Double itemParentPrice) {
        this.itemParentPrice = itemParentPrice;
    }

    public Double getItemParentPrice() {
        return itemParentPrice;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPrimaryGenreName(String primaryGenreName) {
        this.primaryGenreName = primaryGenreName;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackTime(Long trackTime) {
        this.trackTime = trackTime;
    }

    public Long getTrackTime() {
        return trackTime;
    }
}
