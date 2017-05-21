package io.qameta.allure.bamboo;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

import static io.qameta.allure.bamboo.AllureConstants.ALLURE_CONFIG_DOWNLOAD_BASE_URL;
import static io.qameta.allure.bamboo.AllureConstants.ALLURE_CONFIG_DOWNLOAD_ENABLED;
import static io.qameta.allure.bamboo.AllureConstants.ALLURE_CONFIG_ENABLED_BY_DEFAULT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.parseBoolean;
import static java.util.Optional.ofNullable;
import static org.sonatype.aether.util.StringUtils.isEmpty;

class AllureGlobalConfig implements Serializable {
    private static final String DEFAULT_DOWNLOAD_BASE_URL = "https://dl.bintray.com/qameta/generic/io/qameta/allure/allure/";
    private final boolean downloadEnabled;
    private final boolean enabledByDefault;
    private final String downloadBaseUrl;

    AllureGlobalConfig() {
        this(TRUE.toString(), FALSE.toString(), DEFAULT_DOWNLOAD_BASE_URL);
    }

    AllureGlobalConfig(String downloadEnabled, String enabledByDefault, String downloadBaseUrl) {
        this.downloadEnabled = isEmpty(downloadEnabled) || parseBoolean(downloadEnabled);
        this.enabledByDefault = isEmpty(enabledByDefault) || parseBoolean(enabledByDefault);
        this.downloadBaseUrl = isEmpty(downloadBaseUrl) ? DEFAULT_DOWNLOAD_BASE_URL : downloadBaseUrl;
    }


    static AllureGlobalConfig fromContext(Map context) {
        return new AllureGlobalConfig(
                getSingleValue(context, ALLURE_CONFIG_DOWNLOAD_ENABLED, FALSE.toString()),
                getSingleValue(context, ALLURE_CONFIG_ENABLED_BY_DEFAULT, FALSE.toString()),
                getSingleValue(context, ALLURE_CONFIG_DOWNLOAD_BASE_URL, null)
        );
    }

    @Nullable
    private static String getSingleValue(Map context, String key, String defaultVal) {
        return ofNullable(context.get(key))
                .map(value -> value instanceof String[] ? ((String[]) value)[0] : (String) value)
                .orElse(defaultVal);
    }

    boolean isDownloadEnabled() {
        return downloadEnabled;
    }

    boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    void toContext(Map<String, Object> context) {
        context.put(ALLURE_CONFIG_DOWNLOAD_ENABLED, isDownloadEnabled());
        context.put(ALLURE_CONFIG_ENABLED_BY_DEFAULT, isEnabledByDefault());
        context.put(ALLURE_CONFIG_DOWNLOAD_BASE_URL, getDownloadBaseUrl());
    }

    String getDownloadBaseUrl() {
        return downloadBaseUrl;
    }

}
