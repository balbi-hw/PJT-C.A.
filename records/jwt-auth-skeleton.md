# JWT 기반 회원가입/로그인 스켈레톤

현재 프로젝트는 `Spring Boot + JPA + Thymeleaf` 구조이며, 이미 `Member` 엔티티가 존재한다.  
JWT 인증은 기존 `Member` 도메인을 확장하는 방식으로 작게 시작하는 것이 좋다.

이 문서는 Java/Spring 생태계에 익숙해지기 위해 직접 코드를 작성할 때 참고할 수 있는 단계별 스켈레톤이다.

## 1. 의존성 추가

위치: `app/build.gradle`

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'

    // JWT 라이브러리 예시
    implementation 'io.jsonwebtoken:jjwt-api:{version}'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:{version}'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:{version}'
}
```

처음 구현할 때는 `{version}` 자리에 실제 버전을 확인해서 넣는다.

핵심 의존성은 다음 두 가지다.

- `spring-boot-starter-security`
- JWT 생성/검증 라이브러리

## 2. Member 엔티티 확장

위치: `app/src/main/java/anki/hw/domain/Member.java`

기존 `Member`에 인증용 필드를 추가한다.

```java
@Entity
@Table(name = "members")
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(String name, String email, String encodedPassword) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = encodedPassword;
        member.role = Role.USER;
        return member;
    }
}
```

## 3. Role enum 추가

위치 예시: `app/src/main/java/anki/hw/domain/Role.java`

```java
package anki.hw.domain;

public enum Role {
    USER,
    ADMIN
}
```

## 4. 요청/응답 DTO 작성

위치 예시: `app/src/main/java/anki/hw/dto/auth`

### SignupRequest

```java
@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
```

### LoginRequest

```java
@Getter
@Setter
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
```

### TokenResponse

```java
@Getter
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String tokenType;
}
```

### MemberResponse

```java
@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String name;
    private String email;
    private String role;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole().name()
        );
    }
}
```

## 5. MemberRepository에 이메일 조회 추가

위치: `app/src/main/java/anki/hw/repository/MemberRepository.java`

```java
public Optional<Member> findByEmail(String email) {
    List<Member> result = em.createQuery(
            "select m from Member m where m.email = :email", Member.class)
            .setParameter("email", email)
            .getResultList();

    return result.stream().findFirst();
}

public boolean existsByEmail(String email) {
    Long count = em.createQuery(
            "select count(m) from Member m where m.email = :email", Long.class)
            .setParameter("email", email)
            .getSingleResult();

    return count > 0;
}
```

## 6. 비밀번호 인코더 설정

위치 예시: `app/src/main/java/anki/hw/config/PasswordConfig.java`

```java
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

회원가입 시에는 원문 비밀번호를 저장하지 않고 항상 `passwordEncoder.encode(password)` 결과만 저장한다.

## 7. JWT 설정값 추가

위치: `app/src/main/resources/application.yml`

```yaml
jwt:
  secret: "충분히-긴-랜덤-문자열-최소-32바이트-이상"
```

운영 환경에서는 코드나 설정 파일에 직접 고정하지 않고 환경변수로 분리하는 것이 좋다.

## 8. JwtTokenProvider 작성

위치 예시: `app/src/main/java/anki/hw/security/JwtTokenProvider.java`

```java
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenValidityMillis = 1000L * 60 * 60;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityMillis);

        return Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim("email", member.getEmail())
                .claim("role", member.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getMemberId(String token) {
        String subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Long.parseLong(subject);
    }
}
```

## 9. AuthService 작성

위치 예시: `app/src/main/java/anki/hw/service/AuthService.java`

```java
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Long signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.createMember(
                request.getName(),
                request.getEmail(),
                encodedPassword
        );

        memberRepository.save(member);
        return member.getId();
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member);

        return new TokenResponse(accessToken, "Bearer");
    }
}
```

## 10. AuthController 작성

위치 예시: `app/src/main/java/anki/hw/controller/AuthController.java`

기존 Thymeleaf 컨트롤러와 분리해서 REST API로 시작한다.

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@Valid @RequestBody SignupRequest request) {
        Long memberId = authService.signup(request);
        return ResponseEntity.ok(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
```

## 11. JWT 인증 필터 작성

위치 예시: `app/src/main/java/anki/hw/security/JwtAuthenticationFilter.java`

요청 헤더에서 토큰을 꺼내고, 유효한 토큰이면 Spring Security 인증 객체를 등록한다.

```java
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long memberId = jwtTokenProvider.getMemberId(token);
            Member member = memberRepository.findOne(memberId);

            Authentication authentication = createAuthentication(member);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private Authentication createAuthentication(Member member) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + member.getRole().name())
        );

        UserDetails principal = new User(
                member.getEmail(),
                member.getPassword(),
                authorities
        );

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities
        );
    }
}
```

## 12. SecurityConfig 작성

위치 예시: `app/src/main/java/anki/hw/config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtTokenProvider, memberRepository);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/members/**", "/api/auth/**").permitAll()
                        .requestMatchers("/api/members/me").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

처음에는 기존 Thymeleaf 화면이 깨지지 않도록 `/members/**`는 열어두고, JWT 테스트용 API만 보호하는 식으로 시작한다.

## 13. 내 정보 조회 API 작성

위치 예시: `app/src/main/java/anki/hw/controller/MemberApiController.java`

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberRepository memberRepository;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(Authentication authentication) {
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(MemberResponse.from(member));
    }
}
```

## 구현 순서 추천

1. `Member`에 `email`, `password`, `role` 추가
2. `Role` enum 추가
3. `SignupRequest`, `LoginRequest`, `TokenResponse` DTO 작성
4. `MemberRepository.findByEmail`, `existsByEmail` 작성
5. `PasswordEncoder` 설정
6. `AuthService.signup()` 작성
7. `AuthController.signup()` 테스트
8. `JwtTokenProvider` 작성
9. `AuthService.login()` 작성
10. `AuthController.login()` 테스트
11. `JwtAuthenticationFilter` 작성
12. `SecurityConfig`에서 `/api/members/me` 보호
13. `Authorization: Bearer <token>`으로 `/api/members/me` 호출 테스트

## 첫 목표

처음 목표는 Refresh Token 없이 다음 흐름을 완성하는 것이다.

1. 회원가입
2. 로그인
3. Access Token 발급
4. 보호 API 접근

이 흐름이 동작한 뒤에 로그아웃, Refresh Token, 예외 응답 포맷 정리를 추가하면 Spring Security 흐름을 더 안정적으로 익힐 수 있다.
