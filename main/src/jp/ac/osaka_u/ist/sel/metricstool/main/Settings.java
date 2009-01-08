package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnavailableLanguageException;


/**
 * 
 * @author higo
 * 
 * 実行時の引数情報を格納するためのクラス
 * 
 */
public class Settings {

    private static Settings INSTANCE = null;

    public static Settings getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    private Settings() {
        this.verbose = false;
        this.targetDirectory = null;
        this.listFile = null;
        this.language = null;
        this.metrics = null;
        this.fileMetricsFile = null;
        this.classMetricsFile = null;
        this.methodMetricsFile = null;
        this.fieldMetricsFile = null;
    }

    /**
     * 冗長出力を行うかどうかを返す
     * 
     * @return 行う場合は true, 行わない場合は false
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(final boolean verbose) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.verbose = verbose;
    }

    /**
     * 
     * @return 解析対象ディレクトリ
     * 
     * 解析対象ディレクトリを返す．
     * 
     */
    public String getTargetDirectory() {
        return this.targetDirectory;
    }

    public void setTargetDirectory(final String targetDirectory) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetDirectory) {
            throw new IllegalArgumentException();
        }
        this.targetDirectory = targetDirectory;
    }

    /**
     * 解析対象ファイルの記述言語を返す
     * 
     * @return 解析対象ファイルの記述言語
     * @throws UnavailableLanguageException 利用不可能な言語が指定されている場合にスローされる
     */
    public LANGUAGE getLanguage() throws UnavailableLanguageException {
        assert null != this.language : "\"language\" is not set";
        return this.language;
    }

    public void setLanguage(final String language) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == language) {
            throw new IllegalArgumentException();
        }

        if (language.equalsIgnoreCase("java") || language.equalsIgnoreCase("java15")) {
            this.language = LANGUAGE.JAVA15;
        } else if (language.equalsIgnoreCase("java14")) {
            this.language = LANGUAGE.JAVA14;
        } else if (language.equalsIgnoreCase("java13")) {
            this.language = LANGUAGE.JAVA13;
            // }else if (language.equalsIgnoreCase("cpp")) {
            // return LANGUAGE.C_PLUS_PLUS;
            // }else if (language.equalsIgnoreCase("csharp")) {
            // return LANGUAGE.C_SHARP
        } else if (language.equalsIgnoreCase("csharp")) {
            this.language = LANGUAGE.CSHARP;
        } else {
            throw new UnavailableLanguageException("\"" + language
                    + "\" is not an available programming language!");
        }
    }

    /**
     * 
     * @return 解析対象ファイルのパスを記述しているファイル
     * 
     * 解析対象ファイルのパスを記述しているファイルのパスを返す
     * 
     */
    public String getListFile() {
        return listFile;
    }

    public void setListFile(final String listFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == listFile) {
            throw new IllegalArgumentException();
        }
        this.listFile = listFile;
    }

    /**
     * 
     * @return 計測するメトリクス
     * 
     * 計測するメトリクス一覧を返す
     * 
     */
    public String[] getMetrics() {
        return this.metrics;
    }

    public void setMetrics(final String metrics) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == metrics) {
            throw new IllegalArgumentException();
        }

        final StringTokenizer tokenizer = new StringTokenizer(metrics, ",", false);
        this.metrics = new String[tokenizer.countTokens()];
        for (int i = 0; i < this.metrics.length; i++) {
            this.metrics[i] = tokenizer.nextToken();
        }
    }

    /**
     * 
     * @return ファイルタイプのメトリクスを出力するファイル
     * 
     * ファイルタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public String getFileMetricsFile() {
        return fileMetricsFile;
    }

    public void setFileMetricsFile(final String fileMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.fieldMetricsFile = fileMetricsFile;
    }

    /**
     * 
     * @return クラスタイプのメトリクスを出力するファイル
     * 
     * クラスタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public String getClassMetricsFile() {
        return classMetricsFile;
    }

    public void setClassMetricsFile(final String classMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.classMetricsFile = classMetricsFile;
    }

    /**
     * 
     * @return メソッドタイプのメトリクスを出力するファイル
     * 
     * メソッドタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public String getMethodMetricsFile() {
        return methodMetricsFile;
    }

    public void setMethodMetricsFile(final String methodMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.methodMetricsFile = methodMetricsFile;
    }

    /**
     * 
     * @return フィールドタイプのメトリクスを出力するファイル
     */
    public String getFieldMetricsFile() {
        return fieldMetricsFile;
    }

    public void setFieldMetricsFile(final String fieldMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.fieldMetricsFile = fileMetricsFile;
    }

    /**
     * 冗長出力モードかどうかを記録するための変数
     */
    private boolean verbose;

    /**
     * 解析対象ディレクトリを記録するための変数
     */
    private String targetDirectory;

    /**
     * 解析対象ファイルのパスを記述したファイルのパスを記録するための変数
     */
    private String listFile;

    /**
     * 解析対象ファイルの記述言語を記録するための変数
     */
    private LANGUAGE language;

    /**
     * 計測するメトリクスを記録するための変数
     */
    private String[] metrics;

    /**
     * ファイルタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String fileMetricsFile;

    /**
     * クラスタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String classMetricsFile;

    /**
     * メソッドタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String methodMetricsFile;

    /**
     * フィールドタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String fieldMetricsFile;
}
