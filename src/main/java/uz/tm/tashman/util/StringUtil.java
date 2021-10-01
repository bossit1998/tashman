package uz.tm.tashman.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class StringUtil {

    private static final String ABC = "abcdefghijklmnopqrstuvwxyz6352810974";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final NumberFormat qtyNumberFormat = NumberFormat.getInstance();
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static {
        qtyNumberFormat.setGroupingUsed(true);
        qtyNumberFormat.setMaximumFractionDigits(2);
        qtyNumberFormat.setMinimumFractionDigits(2);
    }

    public static String formatDate(Date date) {
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    public static String formatAmount(Double amount) {
        return qtyNumberFormat.format(amount);
    }

    public static String cleanNumber(String number) {
        if (number == null) return "";
        return number.replaceAll("\\+", "").replaceAll(" ", "").replaceAll(",", "")
                .replaceAll("-", "").replaceAll("\\(", "").replaceAll("\\)", "");
    }

    public static String encodePassword(String password) {
        byte[] unencodedPassword = password.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            return password;
        }
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (byte anEncodedPassword : encodedPassword) {
            if ((anEncodedPassword & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString(anEncodedPassword & 0xff, 16));
        }
        return buf.toString();
    }

    public static String makeZeroLead(long number, int len) {
        StringBuilder n = new StringBuilder("" + number);
        while (n.length() < len) {
            n.insert(0, "0");
        }
        return n.toString();
    }

//    public static ResponseEntity<?> check(Object object, String objectName) {
//        if (object == null) {
//            return BadRequestResponse(objectName + " not entered");
//        } else {
//            return null;
//        }
//    }

    public static boolean isBlank(Object object) {
        return (object == null);
    }

    public static boolean isBlankString(String string) {
        return (string == null || string.trim().equals(""));
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    public static Double parseDouble(String str) {
        String strNum = str.replaceAll(",", ".");
        return Double.parseDouble(strNum);
    }

    public static boolean isIntegerDouble(Double dec) {
        Double dec100 = dec * 100;
        return dec.intValue() * 100 == dec100.intValue();
    }

    public static String maskPhoneNumber(String phoneNumber) {
        phoneNumber = cleanNumber(phoneNumber);
        if (StringUtils.isNotEmpty(phoneNumber) && phoneNumber.length() == 12) {
            String part1 = phoneNumber.substring(0, 3);
            String part2 = phoneNumber.substring(3, 5);
            String part3 = phoneNumber.substring(10, 12);
            phoneNumber = "+" + part1 + " " + part2 + " *** ** " + part3;
        }
        return phoneNumber;
    }

    public static String getLanguageCode(Integer languageId) {
        if (languageId != null) {
            switch (languageId) {
                case 1:
                    return "uz";
                case 2:
                    return "ru";
                case 3:
                    return "en";
                default:
                    return "uz";
            }
        }
        return "uz";
    }

    public static String decrypt(String s) {
        StringBuffer buf = new StringBuffer();
        try {
            int pos;

            if ((s.length() & 1) != 0)
                throw new ParseException("Odd string length", s.length());

            for (int i = 0; i < s.length(); i += 2) {
                try {
                    pos = Integer.parseInt(s.substring(i, i + 2));
                } catch (NumberFormatException nfe) {
                    throw new ParseException("Invalid characters pair", i);
                }
                if (pos < 0 || pos >= ABC.length())
                    throw new ParseException("Invalid index", i);

                buf.append(ABC.charAt(pos));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }

    public static byte[] encrypt(byte[] b1) {
        try {
            BigInteger modulus = new BigInteger("BA9B1DE44B0B239DCA40B94832EA238F40AD81981B59AA687F7F1A75A319ABD9334D9FA2CE25736C3AB4272171404DB49BC6A8AC210B92449F1DD1628497DAE7", 16);
            BigInteger pubExp = new BigInteger("000011", 16);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, pubExp);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(b1);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    public static String encodeId(Long id) {
        if (id != null) {
            return passwordEncoder.encode(id.toString());
        }
        return "";
    }

    public static String getCardExpireDate(String expireDate) {
        String[] split = expireDate.length() != 5 ? AES.decrypt(expireDate).split("/")
                : expireDate.split("/");
        return split[1] + split[0];
    }

    public static boolean isDev() {
        String environment = System.getProperty("mode");
        return "development".equals(environment);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

//    public static CardType checkCardType(String cardNumber) {
//        if (cardNumber.startsWith("8600")) {
//            return CardType.UZCARD;
//        } else if (cardNumber.startsWith("9860")) {
//            return CardType.HUMOCARD;
//        } else if (cardNumber.startsWith("1111")) {
//            return CardType.BONUSCARD;
//        } else {
//            return null;
//        }
//    }

    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Sahifa soni noldan kam bo'lishi mumkin emas.");
        }

        if (size > CONSTANT.MAX_PAGE_SIZE * 10) {
            throw new BadRequestException("Sahifa soni " + CONSTANT.MAX_PAGE_SIZE + " dan ko'p bo'lishi mumkin emas.");
        }
    }

    public static Pageable getPageable(int page, int size) {
        validatePageNumberAndSize(page, size);
        return PageRequest.of(page, size);
    }
}
