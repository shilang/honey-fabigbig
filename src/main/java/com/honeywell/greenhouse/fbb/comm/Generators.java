package com.honeywell.greenhouse.fbb.comm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * This utility class is to authenticate if the HTTP request is invalid.
 *
 * @author H239522
 */
@Slf4j
@UtilityClass
public class Generators {

    /** Format 'yyyyMMddHHmmss' */
    private static final FastDateFormat sdfDateTime = FastDateFormat.getInstance("yyyyMMddHHmmss");

    /** Format 'yyyyMMdd' */
    private static final FastDateFormat sdfDate = FastDateFormat.getInstance("yyyyMMdd");

    /**
     * @param orderId
     * @return 年4位 + 月2位 + 日2位 + 时2位 + 分2位 + 秒2位 + userId6位(HexCode) = 20位
     */
    public static String genOrderNo(int orderId, Timestamp ts) {
        String idHex = "000000" + Integer.toString(orderId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDateTime.format(ts);
        return date + idHex.substring(idHex.length() - 6);
    }

    /**
     * @param orderInsuranceId
     * @return 年4位 + 月2位 + 日2位 + 时2位 + 分2位 + 秒2位 + userId6位(HexCode) = 20位
     */
    public static String genOrderInsuranceNo(int orderInsuranceId, Timestamp ts) {
        String idHex = "000000" + Integer.toString(orderInsuranceId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDateTime.format(ts);
        return date + idHex.substring(idHex.length() - 6);
    }

    /**
     * @param userInsuranceId
     * @return 年4位 + 月2位 + 日2位 + 时2位 + 分2位 + 秒2位 + userId6位(HexCode) = 20位
     */
    public static String genUserInsuranceNo(int userInsuranceId, Timestamp ts) {
        String idHex = "000000" + Integer.toString(userInsuranceId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDateTime.format(ts);
        return date + idHex.substring(idHex.length() - 6);
    }

    /**
     * @param commodityId
     * @return 年4位 + 月2位 + 日2位 + 时2位 + 分2位 + 秒2位 + commodityId6位(HexCode) = 20位
     */
    public static String genCommodityNo(int commodityId, Timestamp ts) {
        String idHex = "000000" + Integer.toString(commodityId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDateTime.format(ts);
        return date + idHex.substring(idHex.length() - 6);
    }

    /**
     * @param commodityOrderId
     * @return 年4位 + 月2位 + 日2位 + 时2位 + 分2位 + 秒2位 + commodityId6位(HexCode) = 20位
     */
    public static String genCommodityOrderNo(int commodityOrderId, Timestamp ts) {
        String idHex = "000000" + Integer.toString(commodityOrderId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDateTime.format(ts);
        return date + idHex.substring(idHex.length() - 6);
    }



    /**
     * @param prizeId
     * @return 年4位 + 月2位 + 日2位 + 时2位 + 分2位 + 秒2位 + prizeId位(HexCode) = 20位
     */
    public static String genPrizeNo(int prizeId, Timestamp ts) {
        String idHex = "000000" + Integer.toString(prizeId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDateTime.format(ts);
        return date + idHex.substring(idHex.length() - 6);
    }

    /**
     * @param lotteryActivityId
     * @return 前缀3位+年4位 + 月2位 + 日2位  + prizeId位(HexCode) = 15位
     */
    public static String genLotteryActivityNo(int lotteryActivityId, Timestamp ts) {
        String idHex = "0000" + Integer.toString(lotteryActivityId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDate.format(ts);
        return "CJD" + date + idHex.substring(idHex.length() - 4);
    }

    /**
     * @param roleId
     * @return 前缀6位+年4位 + 月2位 + 日2位  + roleId位(HexCode) = 18位
     */
    public static String genRoleCode(int roleId, Timestamp ts) {
        String idHex = "0000" + Integer.toString(roleId, Character.MAX_RADIX).toUpperCase();
        String date = sdfDate.format(ts);
        return "SSROLE" + date + idHex.substring(idHex.length() - 4);
    }




    /**
     * 生成车险单编号
     *
     * @param orderInsuranceId
     * @param ts
     * @return
     */
    public static String genInsuranceApplyNo(int orderInsuranceId, Timestamp ts) {
        String no = String.format("%04d", orderInsuranceId);
        String date = sdfDate.format(ts);
        return "TBD-" + date + "-" + no;
    }

    /**
     * @param nationCode
     * @param mobNo
     * @param
     * @return
     */
    public static final String genRedisSubKey(String nationCode, String mobNo) {
        return nationCode + ":" + mobNo;
    }

    public static final String genAppUsername(String nationCode, String mobNo) {
        return nationCode + ":" + mobNo;
    }



    /**
     * @param appType
     * @param deviceType
     * @return e.g. 1:2
     */
    public static final String genRedisVerSubKey(Integer appType, Integer deviceType, String domain) {
        return appType + ":" + deviceType + ":" + domain;
    }

    public static final String genHash(Object... objects) {
        return Integer.toHexString(StringUtils.join(objects, ":").hashCode());
    }

    public static final boolean checkHash(String hash, Object... objects) {
        Object[] checkObjects = Arrays.asList(objects).stream().filter(Objects::nonNull).toArray();
        return genHash(checkObjects).equals(hash);
    }

    /**
     * @param strictRandom if true, generates a random 6 digits; otherwise 6 digits of 'MMddHH' date format of current time for simulation.
     * @return random SMS code.
     */
    public static final String genRandomVerifyCode(boolean strictRandom) {
        if (strictRandom) {
            return Integer.toString(100000 + ThreadLocalRandom.current().nextInt(900000));
        } else {
            // easy for testing now, this implementation is unsecure.
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHH"));
        }
    }

    public static final String getExtentionWithDot(String fileName) {
        return getExtentionWithDot(fileName, null);
    }

    //Get extension from fileName (e.g. file.jpg). If it is empty, then contentType (image/jpeg) will be used.
    public static final String getExtentionWithDot(String fileName, String contentType) {
        String ext = FilenameUtils.getExtension(fileName);
        if (StringUtils.isEmpty(ext)) {
            if (!StringUtils.isEmpty(contentType)) {
                MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
                MimeType mimeType;
                try {
                    mimeType = allTypes.forName(contentType);
                    ext = mimeType.getExtension();
                } catch (MimeTypeException e) {
                    log.warn("Get mine type error from {}", contentType);
                }
            }
            if (ext == null) {
                ext = "";
            }
            return ext;
        } else {
            return "." + ext;
        }
    }

    public static BigDecimal calcAvgRating(Integer[] rt) {
        int totalScore = IntStream.range(0, rt.length).map(i -> rt[i] * (i + 1)).sum();
        int totalEvals = Stream.of(rt).mapToInt(i -> i.intValue()).sum();
        return BigDecimal.valueOf(totalEvals == 0 ? 0 : ((double) totalScore) / totalEvals).setScale(2, RoundingMode.CEILING);
    }

    /**
     * @param appType
     * @param userId  [0, 2^27)
     * @return
     */
    public static String encodeReferralCode(int appType, int userId) {
        if (appType < 1 || appType > 2 || userId < 0 || userId > 0x7FFFFFF) {
            throw new IllegalArgumentException(String.format("appType:%d, userId:%d", appType, userId));
        }
        int u = 0;
        for (int i = 0; i < 27; ++i) {
            int bitval = ((userId >>> i) & 1);
            int offset = i / 2;
            if (i % 2 == 0) {
                u |= (bitval << (30 - offset));
            } else {
                u |= (bitval << (4 + offset));
            }
        }
        int encoded = ~(u | ((appType - 1) << 3) | ((userId % 8) & 0x07));
        String valuePart = Integer.toString(encoded & 0x7FFFFFFF, Character.MAX_RADIX).toUpperCase();
        if (valuePart.length() < 6) {
            char[] zeros = new char[6 - valuePart.length()];
            Arrays.fill(zeros, '0');
            valuePart = new String(zeros) + valuePart;
        }
        return valuePart;
    }

    /**
     * @param referralCode
     * @param appTypeUserId output parameter, appTypeUserId[0]:appType, appTypeUserId[1]:userId
     * @return true if succeeded decoding given referral code.
     */
    public static boolean decodeReferralCode(String referralCode, int[] appTypeUserId) {
        if (referralCode == null || appTypeUserId == null || appTypeUserId.length != 2) {
            throw new IllegalArgumentException(String.format("referralCode:%s, appTypeUserId:%s", referralCode,
                    appTypeUserId == null ? null : Arrays.toString(appTypeUserId)));
        }
        int encoded = Integer.valueOf(referralCode.toLowerCase(), Character.MAX_RADIX);
        encoded = (~encoded) & 0x7FFFFFFF;
        int checksum = encoded & 0x07;
        int u = 0;
        for (int i = 0; i < 27; ++i) {
            int offset = i / 2;
            if (i % 2 == 0) {
                u |= (((encoded >> (30 - offset)) & 1) << i);
            } else {
                u |= (((encoded >> (4 + offset)) & 1) << i);
            }
        }
        appTypeUserId[0] = ((encoded >>> 3) & 1) + 1;
        appTypeUserId[1] = u;
        return ((u % 8) & 0x07) == checksum;
    }

    /**
     * @param datetime in "yyyy-MM-dd hh:mm:ss" format
     * @return
     * @throws ParseException
     */
    public static Timestamp parseDatetime(String datetime) throws ParseException {
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(datetime);
        return new Timestamp(parsedDate.getTime());
    }


    
    /**
     * @param locale string
     * @return Parsed locale
     */
    public static final Locale getLocaleFromStr(String locale){
        Locale rt = null;
        try {
            rt = org.springframework.util.StringUtils.parseLocaleString(locale);
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return rt;
    }

}
