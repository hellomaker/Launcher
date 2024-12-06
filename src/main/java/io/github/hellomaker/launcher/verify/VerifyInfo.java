package io.github.hellomaker.launcher.verify;



import java.util.List;

/**
 * @author hellomaker
 */
public class VerifyInfo {

    private String serialNumber;

    private String validDate;

    private Long validTimes;

    private List<String> validSubSystemNameList;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public Long getValidTimes() {
        return validTimes;
    }

    public void setValidTimes(Long validTimes) {
        this.validTimes = validTimes;
    }

    public List<String> getValidSubSystemNameList() {
        return validSubSystemNameList;
    }

    public void setValidSubSystemNameList(List<String> validSubSystemNameList) {
        this.validSubSystemNameList = validSubSystemNameList;
    }
}
