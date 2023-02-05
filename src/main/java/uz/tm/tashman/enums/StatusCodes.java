package uz.tm.tashman.enums;

import lombok.Getter;
import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

import static uz.tm.tashman.util.Util.checkLanguage;

@Getter
public enum StatusCodes {
    SUCCESSFULLY_FOUND(2000, "successfully_found", "Successfully found", "Найдено", "Topildi"),
    SUCCESSFULLY_ADDED(2001, "successfully_added", "Successfully added", "Добавлено", "Qo'shildi "),
    SUCCESSFULLY_EDITED(2002, "successfully_edited", "Successfully edited", "Изменено", "To'g’irlandi"),
    SUCCESSFULLY_DELETED(2003, "successfully_deleted", "Successfully deleted", "Удалено", "O'chirib tashlandi"),
    SUCCESSFULLY_PAYED(2004, "successfully_payed", "Successfully payed", "Оплачено", "To’landi"),
    SUCCESSFULLY_CANCELLED(2005, "successfully_cancelled", "Successfully cancelled", "Отменено", "Bekor qilindi"),
    USER_LOGGED_IN(2006, "user_logged_in", "User logged in", "Пользователь вошел в систему", "Foydalanuvchi tizimga kirdi"),
    USER_LOGGED_OUT(2007, "user_logged_out", "User logged out", "Пользователь вышел из системы", " Foydalanuvchi tizimdan chiqdi"),
    VERIFICATION_SENT(2008, "verification_sent", "Verification sent", "Подтверждение отправлено", "Tasdiqlash yuborildi"),
    SUCCESS(2009, "success", "Success", "Успешно", "Muvaffaqiyatli"),
    OTP_VERIFIED(2010, "otp_verified", "Verification code verified successfully", "Код верификации подтвержден", "Tasdiqlash ko'di muvaffaqiyatli"),
    RECEIVER_NOTIFIED(2011, "receiver_notified", "Receiver notified successfully", "Получатель уведемлён успешно", "Qabul qiluvchi ogohlantirildi"),
    USER_PHONE_NOT_ENTERED(3000, "user_phone_not_entered", "User phone not entered", "Не введен номер телефона пользователя", "Foydalanuvchining telefoni kiritilmadi"),
    USER_NAME_NOT_ENTERED(3001, "user_name_not_entered", "User name not entered", "Не введено имя пользователя", "Foydalanuvchi ismi kiritilmadi"),
    USER_DOB_NOT_ENTERED(3002, "user_dob_not_entered", "User dob not entered", "Не введено дата рождения пользователя", "Foydalanuvchi tug'ilgan sanasi kiritilmadi"),
    USER_GENDER_NOT_ENTERED(3003, "user_gender_not_entered", "User gender not entered", "Не введен пол пользователя", "Foydalanuvchi jinsi kiritilmadi"),
    USER_PASSWORD_NOT_ENTERED(3004, "user_password_not_entered", "User password not entered", "Не введен пароль пользователя", "Foydalanuvchi paroli kiritilmadi"),
    USER_FILE_NOT_SELECTED(3005, "user_file_not_selected", "User file not selected", "Файл не выбран", "Foydalanuvchi fayli tanlanmagan"),
    USER_OTP_NOT_ENTERED(3007, "user_otp_not_entered", "User otp not entered", "Не введен верефикационный код пользователя", "Tasdiqlash kodi kiritilmadi"),
    USER_NOT_REGISTERED(3008, "user_not_registered", "User not registered", "Пользователь не зарегестрирован", "Foydalanuvchi ro'yxatdan o'tmagan"),
    USER_ALREADY_REGISTERED(3009, "user_already_registered", "User already registered", "Пользователь уже зарегестрирован", "Foydalanuvchi ro'yxatdan o'tgan"),
    USER_WRONG_OTP(3010, "user_wrong_otp", "User wrong otp", "Неправильно введен верификационный код", "Tasdiqlash kodi notog’ri kiritildi"),
    USER_WRONG_PASSWORD(3011, "user_incorrect_password", "User incorrect password", "Неправильный пароль", "Notog’ri parol"),
    USER_ACCOUNT_EXPIRED(3012, "user_account_expired", "User account expired", "Срок действия учетной записи истек", "Hisob muddati tugadi"),
    USER_PHONE_NUMBER_OR_PASSWORD_ERROR(3013, "user_phone_number_or_password_error", "User phone number or password error", "Ошибка номера или пароля пользователя", "Foydalanuvchi telefon raqami yoki parol xatosi"),
    USER_BLOCKED_BY_ADMIN(3014, "blocked_by_admin", "Blocked by admin", "Заблокирован админом", "Admin orqali blocklangan"),
    NO_SUCH_DEVICE(3015, "no_such_device", "No such device", "Нет такого устройства", "Bunday qurilma yo'q"),
    DEVICE_ALREADY_VERIFIED(3016, "device_already_verified", "Device already verified", "Устройство уже подтверждено", "Qurilma allaqachon tasdiqdan o'tgan"),
    UNAUTHORIZED_CONTENT(3019, "unauthorized_content", "Unauthorized content", "Несанкционированный контент", "Ruxsatsiz kontent"),
    USER_PIN_CODE_NOT_ENTERED(3025, "user_pin_code_not_entered", "User pin code not entered", "Не введен пин код пользователя", "Foydalanuvchi PIN-kodi kiritilmagan"),
    USER_WRONG_PIN_CODE(3026, "user_wrong_pin_code", "User wrong pin code", "Неправильно введен пин-код", "PIN kod noto‘g‘ri kiritildi"),
    USER_AGENT_DELETED(3027, "user_agent_deleted", "User agent deleted", "Аккаунт пользователя удален", "Foydalanuvchi akkaunti o'chirilgan"),
    USER_NOT_VERIFIED(3028, "user_not_verified", "User not verified", "Пользователь не подтвержден", "Foydalanuvchi tasdiqdan o'tmagan"),
    DOCUMENT_NOT_FOUND(3030, "document_not_found", "Document not found", "Документ не найден", "Hujjat topilmadi"),
    OTP_NOT_SENT(3031, "otp_not_sent", "Verification code not sent", "Не отправлен код верификации", "Tasdiqlash kodi yuborilmadi"),
    USER_DEVICE_NOT_VERIFIED(3032, "user_device_not_verified", "User device not verified", "Устройство пользователя не подтверждено", "Foydalanuvchi qurilmasi tasdiqlanmagan"),
    USER_ROLE_NOT_FOUND(3033, "user_role_not_found", "User role not found", "Роль пользователя не найдена", "Foydalanuvchi ro'li topilmadi"),
    USER_NOT_VERIFIED_OR_DELETED(3034, "user_not_verified_or_deleted", "User not verified or deleted", "Пользователя не одобрен или удален", "Foydalanuvchi tasdiqlanmagan, yoki o'chirilgan"),
    PAYMENT_ERROR_IN_PROVIDER(4000, "payment_error_in_provider", "Error in getting data from Payment provider (Payme)", " Ошибка при получении данных от платежной системы (Payme)", " To'lov tizimidan maʼlumotlarni olishda xatolik (Payme)"),
    PAYMENT_NOT_ENOUGH_FUNDS(4001, "payment_not_enough_funds", "Payment not enough funds", "Недостаточно средств для оплаты", "To'lov uchun mablag' yetarli emas"),
    PAYMENT_RECEIPT_ID_NOT_ENTERED(4002, "payment_receipt_id_not_entered", "Payment receipt id not entered", "Идентификатор платежной квитанции не введен", "To'lov varaqasi id raqami kiritilmagan"),
    PAYMENT_MONEY_AMOUNT_NOT_ENTERED(4003, "payment_money_amount_not_entered", "Payment money amount not entered", "Сумма платежа не указана", "To'lov miqdori ko'rsatilmagan"),
    PAYMENT_DESCRIPTION_NOT_ENTERED(4004, "payment_description_not_entered", "Payment description not entered", "Не введено описание платежа", "To'lov tavsifi kiritilmagan"),
    PAYMENT_DETAILS_NOT_ENTERED(4005, "payment_details_not_entered", "Payment details not entered", "Не введены платежные реквизиты", "To'lov ma'lumotlari kiritilmagan"),
    PAYMENT_CARD_NOT_SELECTED(4006, "payment_card_not_selected", "Payment card not selected", "Не выбрана платежная карта", " To'lov kartasi tanlanmagan"),
    PAYMENT_PHONE_NUMBER_NOT_ENTERED(4007, "payment_phone_number_not_entered", "Payment phone number not entered", "Не введен номер телефона для оплаты", "To'lov uchun telefon raqami kiritilmadi"),
    PAYMENT_DEFAULT_CARD_ZERO_BALANCE(4009, "payment_default_card_zero_balance", "Default card has 0 balance", "Баланс на карте 0", "Karta balansi 0"),
    PAYMENT_DISCOUNT_CARD_ZERO_BALANCE(4010, "payment_discount_card_zero_balance", "Discount card zero balance", "Дисконт карта с нулевым балансом", "Diskont kartada balans 0"),
    PAYMENT_CARD_NOT_VERIFIED(4011, "payment_card_not_verified", "Card is not verified", "Карта не верифицирована", "Karta tasdiqlanmagan"),
    PAYMENT_CARD_DOESNT_EXIST(4012, "payment_card_doesnt_exist", "Card doesn't exist", "Карта не существует", "Karta mavjud emas"),
    PAYMENT_CARD_NOT_FOUND(4013, "payment_card_not_found", "Card not found", "Карта не найдена", "Karta topilmadi"),
    RECEIPT_CARD_DOESNT_EXIST(4014, "receipt_card_doesnt_exist", "Receipt doesn't exist", "Квитанции не существует", "Kvitansiya mavjud emas"),
    RECEIPT_ERROR_IN_CANCELLING(4015, "receipt_error_in_cancelling", "Error in cancelling receipt", "Ошибка при отмене чека", "Chek bekor qilishda xatolik"),
    RECEIPT_ERROR_IN_GETTING(4016, "receipt_error_in_getting", "Error in getting receipt", "Ошибка при получении чека", "Chekni olishda xatolik"),
    RECEIPT_DOESNT_EXIST(4017, "receipt_doesnt_exist", "Receipt doesn't exist", "Чека не существует", "Chek topilmadi"),
    INTERNAL_ERROR(5000, "internal_error", "Internal error", "Внутренняя ошибка", "Sistemada xatolik"),
    EXTERNAL_ERROR(5000, "external_error", "External error", "Внешняя ошибка", "Tashqi sistema xatoligi"),
    INCORRECT_LANGUAGE_CODE(7003, "incorrect_language_code", "Incorrect language code", "Неправильный код языка", "Til ko'di notog'ri"),
    NOT_FOUND(7004, "not_found", "Not found", "Не найден", "Topilmadi"),
    ALREADY_APPROVED(7005, "already_approved", "Already approved", "Уже одобрен", "Allaqachon tasdiqlangan"),
    ID_NOT_ENTERED(7006, "id_not_entered", "Id not entered", "Id не ввели", "Id kiritilmadi"),
    NEWS_TITLE_NOT_ENTERED(7007, "news_title_not_entered", "News title not entered", "Не введен заголовок новости", "Yangilik sarlavhasi kiritilmagan"),
    NEWS_BODY_NOT_ENTERED(7008, "news_body_not_entered", "News body not entered", "Не введен текст новости", "Yangilik matni kiritilmadi"),
    NEWS_CATEGORY_NOT_ENTERED(7009, "news_category_not_entered", "News category not entered", "Категория новостей не указана", "Yangiliklar toifasi belgilanmagan"),
    ROLE_NOT_CHOSEN(7010, "role_not_chosen", "Role not chosen", "Роль не выбрана", "Ro'l belgilanmagan"),
    ALREADY_COMPLETED(7011, "already_completed", "Already completed", "Уже закончен", "Allaqachon tugatilgan"),
    ALREADY_REJECTED(7012, "already_rejected", "Already rejected", "Уже отказан", "Allaqachon rad etilgan"),
    USER_IS_NOT_ADMIN(7013, "user_is_not_admin", "User is not admin", "Пользователь не является админом", "Foydalanuvchi admin emas"),
    USER_NOT_FOUND(7014, "user_not_found", "User not found", "Пользователь не найден", "Foydalanuvchi topilmadi"),
    USER_OTP_NOT_VERIFIED(7015, "user_otp_not_verified", "User otp not verified", "Верефикационный код пользователя не одобрено", "Tasdiqlash kodi tasdiqlanmagan"),
    DEVICE_INFO_MISSING(7016, "device_info_missing", "Device info is missing", "Идентификатор устройства отсутствует", "Qurilma malumoti yo'q"),
    OTP_SENT(7017, "otp_sent", "Otp sent", "Верефикационный код отправлен", "Tasdiqlash kodi yuborildi"),
    PASSWORD_CHANGED_SUCCESSFULLY(7018, "password_changed_successfully", "Password changed successfully", "Пароль успешно изменен", "Parol muvaffaqiyatli o'zgartirildi"),
    UNABLE_TO_ADD_PRODUCT(7020, "unable_to_add_product", "Unable to add product", "Не удалось добавить товар", "Mahsulotni qo'shib bo'lmadi"),
    PRODUCT_ALREADY_REGISTERED(7021, "product_already_registered", "Product already registered", "Продукт уже зарегестрирован", "Mahsulot ro'yxatdan o'tgan"),
    USER_DETAILS_ARE_MISSING(7022, "user_details_are_missing", "User details are missing", "User details are missing", "User details are missing"),
    USER_AGENT_DETAILS_ARE_MISSING(7022, "user_agent_details_are_missing", "User agent details are missing", "User agent details are missing", "User agent details are missing"),
    PRODUCT_INFO_MISSING(7023, "product_info_missing", "Product info missing", "Информация о продукте не полная", "Mahsulot ma'lumotlari to'liq emas"),
    USER_IS_NOT_CLIENT(7024, "user_is_not_client", "User is not client", "Пользователь не является клиентом", "Foydalanuvchi mijoz emas")
    ;

    private final int id;
    private final String code;
    private final String englishName;
    private final String russianName;
    private final String uzbekName;

    StatusCodes(int id, String code, String englishName, String russianName, String uzbekName) {
        this.id = id;
        this.code = code;
        this.englishName = englishName;
        this.russianName = russianName;
        this.uzbekName = uzbekName;
    }

    public static StatusCodes get(String value) {
        for (StatusCodes target : values()) {
            if (target.name().equals(value)) {
                return target;
            }
        }
        return null;
    }

    public static StatusCodes getById(long id) {
        for (StatusCodes target : values()) {
            if (target.id == id) {
                return target;
            }
        }
        return null;
    }

    public static String getNameByLanguage(StatusCodes target, Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return target.englishName;
            case UZ:
                return target.uzbekName;
            case RU:
            default:
                return target.russianName;
        }
    }

    public static ArrayList<HashMapModel> getAllByLanguage(Language language) {
        language = checkLanguage(language);

        ArrayList<HashMapModel> result = new ArrayList<>();
        switch (language) {
            case EN:
                for (StatusCodes target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.englishName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case UZ:
                for (StatusCodes target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.uzbekName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case RU:
            default:
                for (StatusCodes target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.russianName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
        }
    }
}