package no.nav.pto_unleash;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import no.finn.unleash.UnleashContext;
import no.nav.common.featuretoggle.UnleashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static no.nav.common.auth.Constants.*;
import static no.nav.common.auth.utils.CookieUtils.getCookie;

@Slf4j
@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    private static final String UNLEASH_SESSION_ID_COOKIE_NAME = "UNLEASH_SESSION_ID";

    private static final List<String> TOKEN_COOKIE_NAMES = List.of(
            OPEN_AM_ID_TOKEN_COOKIE_NAME, AZURE_AD_ID_TOKEN_COOKIE_NAME, AZURE_AD_B2C_ID_TOKEN_COOKIE_NAME
    );

    private final UnleashService unleashService;

    @Autowired
    public FeatureController(UnleashService unleashService) {
        this.unleashService = unleashService;
    }

    @GetMapping
    public Map<String, Boolean> getFeatures(
            @RequestParam("feature") List<String> features,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String userSubject = findFirstSubject(TOKEN_COOKIE_NAMES, request).orElse(null);

        String sessionId = getCookie(UNLEASH_SESSION_ID_COOKIE_NAME, request)
                .map(Cookie::getValue)
                .orElseGet(() -> generateSessionId(response));

        UnleashContext unleashContext = UnleashContext.builder()
                .userId(userSubject)
                .sessionId(sessionId)
                .remoteAddress(request.getRemoteAddr())
                .build();

        return features.stream().collect(Collectors.toMap(e -> e, e -> unleashService.isEnabled(e, unleashContext)));
    }

    private static Optional<String> findFirstSubject(List<String> tokenCookieNames, HttpServletRequest request) {
        return tokenCookieNames.stream()
                .filter(cookieName -> getCookie(cookieName, request).isPresent())
                .findFirst()
                .flatMap(cookieNameWithToken -> {
                    String jwt = getCookie(cookieNameWithToken, request).orElseThrow().getValue();

                    try {
                        JWTClaimsSet claimsSet = JWTParser.parse(jwt).getJWTClaimsSet();
                        return Optional.ofNullable(claimsSet.getStringClaim(AAD_NAV_IDENT_CLAIM))
                                .or(() -> of(claimsSet.getSubject()));
                    } catch (ParseException e) {
                        log.warn(e.getMessage(), e);
                        return Optional.empty();
                    }
                });
    }

    private String generateSessionId(HttpServletResponse httpServletRequest) {
        UUID uuid = UUID.randomUUID();
        String sessionId = Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
        Cookie cookie = new Cookie(UNLEASH_SESSION_ID_COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        httpServletRequest.addCookie(cookie);
        return sessionId;
    }

}
