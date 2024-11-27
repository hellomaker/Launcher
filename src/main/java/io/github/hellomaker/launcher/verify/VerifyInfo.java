package io.github.hellomaker.launcher.verify;


import lombok.Data;

import java.util.List;

/**
 * @author hellomaker
 */
@Data
public class VerifyInfo {

    private String serialNumber;

    private String validDate;

    private Long validTimes;

    private List<Long> validMenuIdList;

    private List<String> validMenuNameList;

}
