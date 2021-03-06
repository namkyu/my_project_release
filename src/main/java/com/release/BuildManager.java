package com.release;

import com.release.common.ReleaseType;
import com.release.core.BuildHandler;
import com.release.util.Conf;
import org.apache.commons.lang.StringUtils;


/**
 * @FileName : BuildManager.java
 * @Project : my_project_release
 * @Date : 2012. 1. 20.
 * @작성자 : 이남규
 * @프로그램설명 :
 */
public class BuildManager {

    public static final int NEED_ARGS = 2;

    /**
     * <pre>
     * main
     *
     * <pre>
     * @param args
     */
    public static void main(String[] args) {
        try {
            if (args.length < NEED_ARGS) {
                help();
                return;
            }

            // 실행 옵션
            String command = args[0].toUpperCase();
            if ("HELP".equals(command)) {
                help();
                return;
            }

            // package number
            String packageNum = args[1];

            // upload file name
            String tarFileName = null;
            if (ReleaseType.PUTALL.name().equals(command)) {
                Conf.init();
                tarFileName = args[2];
            }

            BuildManager buildManager = new BuildManager();
            buildManager.valid(command, packageNum, tarFileName);
            buildManager.startProcess(command, packageNum, tarFileName);

        } catch (Exception e) {
            help();
            println("######################################");
            println("## error");
            println("######################################");
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * help
     *
     * <pre>
     */
    private static void help() {
        println("######################################");
        println("## 사용법");
        println("######################################");
        println("PACKAGE  : 적용할 소스 패키징");
        println("INSTALL  : 소스 적용");
        println("ROLLBACK : 소스 원복");
        println("PUTALL   : FTP file upload");
        println(StringUtils.EMPTY);
        println("java -jar release_file.jar 명령옵션 패키지번호");
        println(" - PACKAGE example : java -jar release_file.jar PACKAGE R001");
        println(" - INSTALL example : java -jar release_file.jar INSTALL R001");
        println(" - ROLLBACK example : java -jar release_file.jar ROLLBACK R001");
        println("java -jar release_file.jar 명령옵션 패키지번호 업로드파일명");
        println(" - PUTALL example : java -jar release_file.jar PUTALL R001 R001.tar.gz");
        println(StringUtils.EMPTY);
        println("release_file.jar 파일이 있는 경로에 패키지번호 디렉토리가 존재해야 함");
        println("패키지번호 디렉토리 안에는 적용할 소스 리스트가 기록되어 있는 package.txt 파일이 존재해야 함");
        println(StringUtils.EMPTY);
    }

    /**
     * <pre>
     * startProcess
     *
     * <pre>
     * @param command
     * @param releaseNum
     * @param tarFileName
     */
    public void startProcess(String command, String releaseNum, String tarFileName) {
        new BuildHandler(command, releaseNum, tarFileName).execute();
    }

    /**
     * @param command
     * @param releaseNum
     */
    public void startProcess(String command, String releaseNum) {
        new BuildHandler(command, releaseNum, null).execute();
    }

    /**
     * <pre>
     * valid
     *
     * <pre>
     * @param command
     * @param releaseNum
     * @param tarFileName
     */
    private void valid(String command, String releaseNum, String tarFileName) {
        // command 체크
        if (checkType(command) == false) {
            throw new RuntimeException("##check## (invalid command) command=" + command + ", releaseNum=" + releaseNum);
        }
        // 패키징 작업 시 package number 값 유무 확인
        else if (StringUtils.isEmpty(releaseNum)) {
            throw new RuntimeException("##check## (not be empty releaseNum) releaseNum=" + releaseNum + ", command=" + command);
        }
        // FTP 업로드인 경우 tarFileName 값 유무 확인
        else if (ReleaseType.PUTALL.name().equals(command)) {
            if (StringUtils.isEmpty(tarFileName)) {
                throw new RuntimeException("##check## (is not empty uploadFileName) command=" + command + ", uploadFileName=" + tarFileName);
            }
        }
    }

    /**
     * <pre>
     * checkType
     *
     * <pre>
     * @param command
     * @return
     */
    public boolean checkType(String command) {
        boolean check = false;
        ReleaseType[] types = ReleaseType.values();
        for (ReleaseType releaseType : types) {
            check = releaseType.name().equals(command);
            if (check) {
                break;
            }
        }
        return check;
    }

    /**
     * <pre>
     * println
     *
     * <pre>
     * @param str
     */
    private static void println(String str) {
        System.out.println(str);
    }
}
