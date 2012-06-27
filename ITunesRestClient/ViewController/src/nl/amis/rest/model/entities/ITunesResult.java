package nl.amis.rest.model.entities;

import java.io.Serializable;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ITunesResult implements Serializable {

    private static final long serialVersionUID = 1L;


    public ITunesResult() {
    }

    Integer resultCount;

    @SerializedName("results")
    List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public Integer getResultCount() {
        return resultCount;
    }


}
