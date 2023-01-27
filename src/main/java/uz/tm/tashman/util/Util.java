package uz.tm.tashman.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.enums.CardType;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.models.ResponseModel;
import uz.tm.tashman.models.UserModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static uz.tm.tashman.util.CONSTANT.*;

@SuppressWarnings(value = "unused")
public class Util {

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
        StringBuilder stringBuilder = new StringBuilder();
        for (byte anEncodedPassword : encodedPassword) {
            if ((anEncodedPassword & 0xff) < 0x10) {
                stringBuilder.append("0");
            }

            stringBuilder.append(Long.toString(anEncodedPassword & 0xff, 16));
        }
        return stringBuilder.toString();
    }

    public static String makeZeroLead(long number, int len) {
        StringBuilder n = new StringBuilder("" + number);
        while (n.length() < len) {
            n.insert(0, "0");
        }
        return n.toString();
    }

    public static boolean isBlank(Object object) {
        return (object == null);
    }

    public static boolean isBlankString(String string) {
        return (string == null || string.trim().equals(""));
    }

    public static Double parseDouble(String str) {
        String strNum = str.replaceAll(",", ".");
        return Double.parseDouble(strNum);
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

    public static String encodeId(Long id) {
        if (id != null) {
            return passwordEncoder.encode(id.toString());
        }
        return "";
    }

    public static String getCardExpireDate(String expireDate) {
        String[] split = expireDate.length() != 5 ? Objects.requireNonNull(AES.decrypt(expireDate)).split("/")
                : expireDate.split("/");
        return split[1] + split[0];
    }

    public static boolean isDev() {
        String environment = System.getProperty("mode");
        return "development".equals(environment);
    }

    public static CardType checkCardType(String cardNumber) {
        if (cardNumber.startsWith("8600")) {
            return CardType.UZCARD;
        } else if (cardNumber.startsWith("9860")) {
            return CardType.HUMO;
        } else if (cardNumber.startsWith("1111")) {
            return CardType.BONUS;
        } else {
            return null;
        }
    }

    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Sahifa soni no'ldan kam bo'lishi mumkin emas.");
        }

        if (size > CONSTANT.MAX_PAGE_SIZE * 10) {
            throw new BadRequestException("Sahifa soni " + CONSTANT.MAX_PAGE_SIZE + " dan ko'p bo'lishi mumkin emas.");
        }
    }

    public static Pageable getPageable(Integer page, Integer size) {
        if (page == null) {
            page = CONSTANT.DEFAULT_PAGE_NUMBER;
        } else if (page == 0) {
            page = 0;
        } else {
            page--;
        }
        if (size == null) {
            size = CONSTANT.DEFAULT_PAGE_SIZE;
        }
        validatePageNumberAndSize(page, size);
        return PageRequest.of(page, size);
    }

    public static String generateOtp() {
        return IS_PRODUCTION ? Util.otpGeneration() : DEFAULT_OTP;
    }

    private static String otpGeneration() {
        int randomPin = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomPin);
    }

    public static Language getLanguageFromAuthentication(UserModel userModel) {
        return userModel.getLanguage() == null ? Language.RU : userModel.getLanguage();
    }

    public static Language getLanguageFromAuthentication() {
        Language language = Language.RU;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            language = user.getLanguage();
        }

        return language;
    }

    public static ResponseModel<String> saveImage(String uploadDirectory, String fileName, String fileExtension, MultipartFile image) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        try {
            if (image == null) {
                responseModel.setSuccess(false);
                responseModel.setMessage("Image is null");
                return responseModel;
            }

//            String separator = System.getProperty("file.separator");
            String separator = "/";

            String filePath = uploadDirectory + separator + fileName + fileExtension;

            File newFile = new File(filePath);
            InputStream inputStream = image.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, newFile);
            inputStream.close();

            responseModel.setSuccess(true);
            responseModel.setData(CONTENT + filePath);
        } catch (Exception e) {
            responseModel.setSuccess(true);
            responseModel.setException(e);
        }
        return responseModel;
    }

    public static ResponseModel<String> createThumbnail(String uploadDirectory, String fileName, String fileExtension, MultipartFile image, Integer size) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        try {
            if (image == null) {
                responseModel.setSuccess(false);
                responseModel.setMessage("Image is null");
                return responseModel;
            }

//            String separator = System.getProperty("file.separator");
            String separator = "/";

            String filePath = uploadDirectory + separator + fileName + fileExtension;

            File newFile = new File(filePath);

            Files.createDirectories(Paths.get(newFile.getAbsolutePath()));

            InputStream inputStream = image.getInputStream();

            BufferedImage img = ImageIO.read(inputStream);
            BufferedImage thumb = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) thumb.getGraphics();
            g2d.drawImage(img, 0, 0, thumb.getWidth() - 1, thumb.getHeight() - 1, 0, 0, img.getWidth() - 1, img.getHeight() - 1, null);
            g2d.dispose();
            ImageIO.write(thumb, "PNG", newFile);

            responseModel.setSuccess(true);
            responseModel.setData(CONTENT + filePath);
        } catch (Exception e) {
            responseModel.setSuccess(true);
            responseModel.setException(e);
        }
        return responseModel;
    }

    public static boolean saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
    }

    public static Language checkLanguage(Language language) {
        if (language == null) {
            language = DEFAULT_SYSTEM_lANGUAGE;
        }

        return language;
    }
}