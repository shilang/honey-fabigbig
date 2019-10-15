package com.honeywell.greenhouse.fbb.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter.Value;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MiscUtils {

    private static final ThreadLocal<Tika> TIKA = ThreadLocal.withInitial(() -> new Tika());

    public static final Pattern NON_CHINESE_CHARACTERS_REGEX = Pattern.compile("[^\u4e00-\u9fa5]+");

    private static final List<String> IP_HEADER_CANDIDATES = Collections.unmodifiableList(Arrays.asList( //
            "X-Forwarded-For", //
            "Proxy-Client-IP", //
            "WL-Proxy-Client-IP", //
            "HTTP_X_FORWARDED_FOR", //
            "HTTP_X_FORWARDED", //
            "HTTP_X_CLUSTER_CLIENT_IP", //
            "HTTP_CLIENT_IP", //
            "HTTP_FORWARDED_FOR", //
            "HTTP_FORWARDED", //
            "HTTP_VIA", //
            "REMOTE_ADDR" //
    ));


    /**
     * @param url           the url to ping
     * @param timeoutMillis used for connect timeout and read timeout
     * @return true if ping successfully, i.e, response code is between [200, 400); otherwise false
     * @throws MalformedURLException if no protocol is specified, or an unknown
     *                               protocol is found or url is null
     */
    public static boolean isReachable(@Nonnull String url, int timeoutMillis) throws MalformedURLException {
        URL u = new URL(url);
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) u.openConnection();
            httpUrlConnection.setConnectTimeout(timeoutMillis);
            httpUrlConnection.setReadTimeout(timeoutMillis);
            httpUrlConnection.setRequestMethod("HEAD");
            int responseCode = httpUrlConnection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException e) {
            return false;
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
    }

    /**
     * @param personId
     * @return masked person id to hide some sensitive data.
     */
    public static String maskPersonId(@Nullable String personId) {
        if (StringUtils.isEmpty(personId)) {
            return personId;
        }
        return personId.replaceAll("^(\\w{3}).*(\\w{4})$", "$1***********$2");
    }

    /**
     * @param text
     * @return purged text which contains only Chinese characters, or null if parameter is null.
     */
    public static String purgeChinese(@Nullable String text) {
        return text == null ? null : NON_CHINESE_CHARACTERS_REGEX.matcher(text).replaceAll("");
    }


    /**
     * @param birthDate
     * @param currentDate
     * @return year differences if possible, otherwise 0.
     */
    public static int computeAge(@Nullable LocalDate birthDate, @Nullable LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return currentDate.getYear() - birthDate.getYear();
        } else {
            return 0;
        }
    }

    /**
     * @param url
     * @return forwarded url, i.e, "forward:" + url;
     */
    public static final String forward(@Nonnull String url) {
        return "forward:" + url;
    }

    /**
     * @param first
     * @param others
     * @return first non-null element if possible, otherwise return null.
     */
    @SafeVarargs
    public static <T> T coalesce(@Nullable T first, @Nullable T... others) {
        if (first != null) {
            return first;
        }
        if (others != null) {
            for (T o : others) {
                if (o != null) {
                    return o;
                }
            }
        }
        return null;
    }

    /**
     * @param t
     * @return root cause (never null) by recursively call {@link Throwable#getCause()}
     * @throws NullPointerException if given parameter is null
     */// a simple way to get root cause, sometimes root cause is nearly impossible (Yuebing Cao)
    public static Throwable getRootCause(@Nonnull Throwable t) {
        int depth = 0;
        Throwable root;
        do {
            root = t;
            t = t.getCause();
        } while (t != null && root != t && ++depth < 1024); // 1024 is a safe guard just in case infinite loop
        return root;
    }

    public static boolean isDueToTimeout(@Nullable Throwable t) {
        if (t != null) {
            int depth = 0; // 64 is a safe guard just in case infinite loop
            for (Throwable e = t; e != null && (depth++ < 64); e = e.getCause()) {
                if (e.getClass().getSimpleName().contains("TimeoutException")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return a default customized ObjectMapper according to properties
     */
    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Default configuration
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // important
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Yikai
        objectMapper.setDefaultPropertyInclusion(Include.NON_NULL);
        objectMapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_CREATORS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // Extra customization
        objectMapper.setDefaultSetterInfo(Value.construct(Nulls.SKIP, Nulls.DEFAULT));
        return objectMapper;
    }

    /**
     * @param localeStr     the locale string to be parsed
     * @param defaultLocale default locale if localeStr is null/empty or can't be parsed
     * @return successfully parsed {@link Locale} if localeStr is not empty, otherwise
     * default locale. No exception would be thrown.
     */
    public static Locale parseLocaleQuietly(@Nullable String localeStr, @Nullable Locale defaultLocale) {
        Locale result = null;
        if (StringUtils.isNotEmpty(localeStr)) {
            try {
                result = LocaleUtils.toLocale(localeStr);
            } catch (Exception e) {
                // dummy
            }
        }
        return result != null ? result : defaultLocale;
    }


    /**
     * @param classpathResource
     * @param
     * @return
     * @throws IOException
     */
    public static List<String> readAllLines(String classpathResource, boolean skipEmptyLines) throws IOException {
        String line;
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(classpathResource).getInputStream(), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                if (!skipEmptyLines || !StringUtils.isEmpty(line)) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    /**
     * Detects the media type of the given document. The type detection is based on
     * the first few bytes of a document.
     * <p>
     * For best results at least a few kilobytes of the document data are needed.
     * See also the other detect() methods for better alternatives when you have
     * more than just the document prefix available for type detection.
     *
     * @param prefix first few bytes of the document
     * @return detected media type, such as "image/png", "image/gif", "image/jpeg",
     * "application/pdf", "application/xml", "application/zip" etc, for more
     * details, please see {@link MediaType}
     * @since Apache Tika 0.9
     */
    public static String detectMediaType(byte[] prefix) {
        return TIKA.get().detect(prefix);
    }

    /**
     * @param province
     * @param city
     * @param district
     * @param address
     * @return
     */
    public static String makeAddress(String province, String city, String district, String address) {
        String cleanProvince = StringUtils.trimToEmpty(province);
        String cleanCity = StringUtils.trimToEmpty(city);
        String cleanDistrict = StringUtils.trimToEmpty(district);
        String cleanAddress = StringUtils.trimToEmpty(address);
        StringBuilder sb = new StringBuilder();
        if (!cleanProvince.isEmpty()) {
            sb.append(cleanProvince);
        }
        if (!cleanCity.isEmpty() && !cleanProvince.equals(cleanCity)) {
            sb.append(cleanCity);
        }
        if (!cleanDistrict.isEmpty()) {
            sb.append(cleanDistrict);
        }
        if (cleanAddress.startsWith(sb.toString())) {
            return cleanAddress; // detailed address, include province/city/district already
        }
        if (!cleanAddress.isEmpty()) {
            sb.append(cleanAddress);
        }
        return sb.toString();
    }
}
