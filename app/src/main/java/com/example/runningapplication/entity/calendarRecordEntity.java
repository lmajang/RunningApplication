package com.example.runningapplication.entity;

public class calendarRecordEntity {
    String recordId;
    String recordYear;
    String recordMonth;
    String recordDay;
    String uploadId;

    calendarRecordEntity(){}
    calendarRecordEntity(String recordId,
                        String recordYear,
                        String recordMonth,
                        String recordDay,
                        String uploadId){
        this.recordId = recordId;
        this.recordYear = recordYear;
        this.recordMonth = recordMonth;
        this.recordDay = recordDay;
        this.uploadId = uploadId;
    }

    public String getRecordDay() {
        return recordDay;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getRecordMonth() {
        return recordMonth;
    }

    public String getRecordYear() {
        return recordYear;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setRecordDay(String recordDay) {
        this.recordDay = recordDay;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public void setRecordMonth(String recordMonth) {
        this.recordMonth = recordMonth;
    }

    public void setRecordYear(String recordYear) {
        this.recordYear = recordYear;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
}
