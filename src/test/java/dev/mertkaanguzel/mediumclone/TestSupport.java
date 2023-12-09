package dev.mertkaanguzel.mediumclone;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class TestSupport {
    public static Instant getInstant() {
        Instant expectedInstant = Instant.parse("2023-10-23T19:23:00Z");
        Clock clock = Clock.fixed(expectedInstant, Clock.systemDefaultZone().getZone());

        return Instant.now(clock);
    }

    public static LocalDateTime getLocalDateTime() {
        return  LocalDateTime.ofInstant(getInstant(), Clock.systemDefaultZone().getZone());
    }

    public static Jwt getJwt() {
        String jwtKey = "9faa372517ac1d389758d3750fc07acf00f542277f26fec1ce4593e93f64e338";
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
        Instant now = getInstant();//Instant.now();

        String scope = "ROLE_USER";

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject("Jacob")
                .claim("scope", scope)
                .build();

        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        return encoder.encode(encoderParameters);
    }
}
