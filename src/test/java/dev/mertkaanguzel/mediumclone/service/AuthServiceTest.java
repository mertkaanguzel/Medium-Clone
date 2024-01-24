package dev.mertkaanguzel.mediumclone.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import dev.mertkaanguzel.mediumclone.exception.UserNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static dev.mertkaanguzel.mediumclone.TestSupport.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AuthServiceTest {
    private AuthService authService;
    private JwtEncoder jwtEncoder;
    private Authentication authentication;
    private SecurityContext securityContext;
    private final String jwtKey = "9faa372517ac1d389758d3750fc07acf00f542277f26fec1ce4593e93f64e338";
    private final String expectedTokenString = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiSmFjb2IiLCJleHAiOjE2OTgwODkyODAsImlhdCI6MTY5ODA4ODk4MCwic2NvcGUiOiJST0xFX1VTRVIifQ.SLBRqvbmMgKoW52DmWVRuZc6W3RgNNSq0dh4Oy73BjR62Bs41S-8b1GRHsYQq5p0pHVvpDYmGlsCMiGXN_ys27O-HXDmVuyr0IrcD2CZcpX22Gw0smtMuORQVYw6_Fv-4ck8zHmdMPrjUF9FJP7OwdIUXGm-QnuAcrT2kjX-M2YaFd2MfEcqn_nJ9W24eKFu7-lshUURZlGJD60V2Ea0b5lwXe2_xcQLG7IyK-LhnijWWEhYz4E7c77Aa_6JbRqYX4nHmoF17Iu7t_74wqS7lmCFpi5aLA6J0UCANRyxcGaoX0Gy81TcGvY3PDkPnPhepqbT55ATWrV0o5nFSDpijQ";
    @BeforeEach
    void setUp() {
        jwtEncoder = mock(JwtEncoder.class);
        NimbusJwtEncoder nimbusJwtEncoder = encoder();
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenAnswer(mock -> nimbusJwtEncoder.encode(mock.getArgument(0)));


        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);


        Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(getInstant());
        when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());

        authService = new AuthService(jwtEncoder, clock);
    }

    @Test
    void testGenerateToken_shouldReturnTokenString() {
        GrantedAuthority grantedAuthority = () -> "ROLE_USER";
        when(authentication.getAuthorities()).then(mock -> List.of(grantedAuthority));
        when(authentication.getName()).thenReturn("Jacob");

        String result = authService.generateToken(authentication);
        assertEquals(expectedTokenString, result);

    }

    @Test
    void testGetToken_shouldReturnTokenString() {
        Authentication authentication = new JwtAuthenticationToken(getJwt(encoder()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String result = authService.getToken();
        assertEquals(expectedTokenString, result);
    }
}
