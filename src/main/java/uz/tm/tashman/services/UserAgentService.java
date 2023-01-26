package uz.tm.tashman.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;
import uz.tm.tashman.models.IpInfoModel;
import uz.tm.tashman.models.UserAgentModel;
import uz.tm.tashman.repository.UserAgentRepository;
import uz.tm.tashman.util.HTTPUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

import static uz.tm.tashman.util.Util.isBlank;

@Service
public class UserAgentService extends HTTPUtil {
    private final UserAgentRepository userAgentRepository;
    private final LogService logService;

    public UserAgentService(UserAgentRepository userAgentRepository, LogService logService) {
        this.userAgentRepository = userAgentRepository;
        this.logService = logService;
    }

    public UserAgent save(UserAgent userAgent) {
        userAgentRepository.save(userAgent);
        return userAgent;
    }

    public UserAgent getUserAgentByEncodedId(String encodedId) {
        if (isBlank(encodedId)) {
            return null;
        }

        Optional<UserAgent> optUserAgent = userAgentRepository.findByEncodedId(encodedId);

        return optUserAgent.orElse(null);
    }

    public UserAgent getUserAgentByEncodedIdAndUserAndDeletedFalse(String encodedId, User user) {
        if (isBlank(encodedId)) {
            return null;
        }

        Optional<UserAgent> optUserAgent = userAgentRepository.findByEncodedIdAndUserAndIsDeletedFalse(encodedId, user);

        return optUserAgent.orElse(null);
    }

    public Boolean checkIfUserAgentExists(String deviceId, User user) {
        boolean userAgentExists = false;

        if (deviceId == null) {
            return userAgentExists;
        }

        UserAgent userAgent = getUserAgentByEncodedIdAndUserAndDeletedFalse(deviceId, user);

        if (userAgent != null) {
            userAgentExists = true;
        }

        return userAgentExists;
    }

    public Boolean checkIfUserAgentVerified(String encodedId, User user) {
        boolean isOtpVerified = false;

        UserAgent userAgent = getUserAgentByEncodedIdAndUserAndDeletedFalse(encodedId, user);

        if (userAgent != null) {
            if (userAgent.isVerified()) {
                isOtpVerified = true;
            }
        }

        return isOtpVerified;
    }

    public Boolean verifyUserAgent(UserAgent userAgent) {
        boolean userAgentVerified = false;

        if (userAgent != null) {
            userAgent.setVerified(true);
            userAgentRepository.save(userAgent);

            userAgentVerified = true;
        }

        return userAgentVerified;
    }

    public UserAgent saveUserAgentInfo(User user, UserAgentModel userAgentModel, HttpServletRequest header) {
        try {
            final String referer = getReferer(header);
            final String fullURL = getFullURL(header);
            final String clientIpAddr = getClientIpAddr(header);
            final String clientBrowser = getClientBrowser(header);
            final String userAgent = getUserAgent(header);
            final String clientOS = getClientOS(header);
            final String appVersion = getAppVersion(header);
            final String hardwareInfo = getHardwareInfo(header);

            IpInfoModel ipInfo = getInfoFromIPInfo(clientIpAddr);
            if (!ipInfo.getStatus()) {
                logService.saveToLog("Error in saving client info \n message: " + ipInfo.getMessage() + " \n data: " + ipInfo.getData());
                return null;
            }

            UserAgent agent = new UserAgent();
            agent.setUserAgent(userAgent);
            agent.setBrowser(clientBrowser);
            agent.setIp(clientIpAddr);
            agent.setFullURL(fullURL);
            agent.setReferer(referer);

            if (userAgentModel != null) {
                agent.setClientOS(userAgentModel.getClientOS());
                agent.setAppVersion(userAgentModel.getAppVersion());
                agent.setHardwareInfo(userAgentModel.getHardwareInfo());
                agent.setPlatform(userAgentModel.getPlatform());
                agent.setFcmToken(userAgentModel.getFcmToken());
                agent.setVoipToken(userAgentModel.getVoipToken());
            } else {
                agent.setClientOS(clientOS);
                agent.setAppVersion(appVersion);
                agent.setHardwareInfo(hardwareInfo);
            }

            agent.setCity(ipInfo.getCity());
            agent.setCountry(ipInfo.getCountry());
            agent.setRegion(ipInfo.getRegion());
            agent.setLocation(ipInfo.getLocation());
            agent.setOrg(ipInfo.getOrg());
            agent.setTimezone(ipInfo.getTimezone());
            agent.setReadme(ipInfo.getReadme());
            agent.setHostname(ipInfo.getHostname());

            agent.setOtp(Util.generateOtp());
            agent.setTokenDate(LocalDateTime.now());
            agent.setVerified(false);
            agent.setIsDeleted(false);
            agent.setUser(user);

            agent = userAgentRepository.save(agent);

            agent.setEncodedId(Util.encodeId(agent.getId()));
            userAgentRepository.save(agent);

            return agent;
        } catch (Exception e) {
            System.out.println(e);
            logService.saveToLog("Error in saving user agent: \n" + exceptionAsString(e));
            return null;
        }
    }

    private IpInfoModel getInfoFromIPInfo(String ip) {
        IpInfoModel ipInfoModel = new IpInfoModel();

        if (ip.equals("127.0.0.1")) {
            ipInfoModel.setStatus(true);
            ipInfoModel.setCountry("localhost");
            ipInfoModel.setCity("localhost");
            ipInfoModel.setLocation("localhost");
            ipInfoModel.setOrg("localhost");
            ipInfoModel.setTimezone("localhost");

            return ipInfoModel;
        } else {
            String url = "http://ipinfo.io/" + ip + "/json";

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<IpInfoModel> responseFromIpInfo = restTemplate.getForEntity(url, IpInfoModel.class);

            if (responseFromIpInfo.getStatusCode().is2xxSuccessful() && responseFromIpInfo.getBody() != null) {
                ipInfoModel.setStatus(true);
                ipInfoModel.setIp(responseFromIpInfo.getBody().getIp());
                ipInfoModel.setCity(responseFromIpInfo.getBody().getCity());
                ipInfoModel.setRegion(responseFromIpInfo.getBody().getRegion());
                ipInfoModel.setCountry(responseFromIpInfo.getBody().getCountry());
                ipInfoModel.setLocation(responseFromIpInfo.getBody().getLoc());
                ipInfoModel.setOrg(responseFromIpInfo.getBody().getOrg());
                ipInfoModel.setTimezone(responseFromIpInfo.getBody().getTimezone());
                ipInfoModel.setReadme(responseFromIpInfo.getBody().getReadme());

                return ipInfoModel;
            } else {
                ipInfoModel.setStatus(false);
                ipInfoModel.setData(responseFromIpInfo.getBody() == null ? null : responseFromIpInfo.getBody().toString());
                ipInfoModel.setMessage("unknown ip");

                return ipInfoModel;
            }
        }
    }

    private String getHardwareInfo(HttpServletRequest request) {
        return request.getHeader("hardware_info");
    }

    private String getAppVersion(HttpServletRequest request) {
        return request.getHeader("appVersion");
    }

    private String getReferer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    private String getFullURL(HttpServletRequest request) {
        final StringBuffer requestURL = request.getRequestURL();
        final String queryString = request.getQueryString();

        return queryString == null ? requestURL.toString() : requestURL.append('?')
                .append(queryString)
                .toString();
    }

    //http://stackoverflow.com/a/18030465/1845894
    private String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";

        return ip;
    }

    //http://stackoverflow.com/a/18030465/1845894
    private String getClientOS(HttpServletRequest request) {
        final String browserDetails = request.getHeader("User-Agent");

        //=================OS=======================
        final String lowerCaseBrowser = browserDetails.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            return "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            return "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            return "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            return "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            return "IPhone";
        } else {
            return "UnKnown, More-Info: " + browserDetails;
        }
    }

    //http://stackoverflow.com/a/18030465/1845894
    private String getClientBrowser(HttpServletRequest request) {
        final String browserDetails = request.getHeader("User-Agent");
        final String user = browserDetails.toLowerCase();

        String browser = "";

        //===============Browser===========================
        if (user.contains("msie")) {
            String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else {
            String[] split = browserDetails.substring(browserDetails.indexOf("Version")).split(" ");
            if (user.contains("safari") && user.contains("version")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-" + (split[0]).split("/")[1];
            } else if (user.contains("opr") || user.contains("opera")) {
                if (user.contains("opera")) browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-" + (split[0]).split("/")[1];
                else if (user.contains("opr"))
                    browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            } else if (user.contains("chrome")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf(
                    "mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf(
                    "mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
                browser = "Netscape-?";
            } else if (user.contains("firefox")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if (user.contains("rv")) {
                browser = "IE";
            } else {
                browser = "UnKnown, More-Info: " + browserDetails;
            }
        }

        return browser;
    }

    private String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}