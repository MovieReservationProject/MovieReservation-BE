//package com.github.moviereservationbe.service.service;
//@Service
//@RequiredArgsConstructor
//public class KakaoTestService {
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//
//    public SocialUserInfoDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
//        // 1. "인가 코드"로 "액세스 토큰" 요청
//        String accessToken = getAccessToken(code);
//
//        // 2. 토큰으로 카카오 API 호출
//        SocialUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
//
//        // 3. 카카오ID로 회원가입 처리
//        User kakaoUser = registerKakaoUserIfNeed(kakaoUserInfo);
//
//        // 4. 강제 로그인 처리
//        Authentication authentication = forceLogin(kakaoUser);
//
//        // 5. response Header에 JWT 토큰 추가
//        kakaoUsersAuthorizationInput(authentication, response);
//        return kakaoUserInfo;
//    }
//
//    private String getAccessToken(String code) throws JsonProcessingException {
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", CLIENT_ID);
//        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
//        body.add("code", code);
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }
//
//    // 2. 토큰으로 카카오 API 호출
//    private SocialUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        // responseBody에 있는 정보를 꺼냄
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        Long id = jsonNode.get("id").asLong();
//        String email = jsonNode.get("kakao_account").get("email").asText();
//        String nickname = jsonNode.get("properties")
//                .get("nickname").asText();
//
//        return new SocialUserInfoDto(id, nickname, email);
//    }
//
//    // 3. 카카오ID로 회원가입 처리
//    private User registerKakaoUserIfNeed (SocialUserInfoDto kakaoUserInfo) {
//        // DB 에 중복된 email이 있는지 확인
//        String kakaoEmail = kakaoUserInfo.getEmail();
//        String nickname = kakaoUserInfo.getNickname();
//        User kakaoUser = userRepository.findByUserEmail(kakaoEmail)
//                .orElse(null);
//
//        if (kakaoUser == null) {
//            // 회원가입
//            // password: random UUID
//            String password = UUID.randomUUID().toString();
//            String encodedPassword = passwordEncoder.encode(password);
//
//            String profile = "https://ossack.s3.ap-northeast-2.amazonaws.com/basicprofile.png";
//
//            kakaoUser = new User(kakaoEmail, nickname, profile, encodedPassword);
//            userRepository.save(kakaoUser);
//
//        }
//        return kakaoUser;
//    }
//
//    // 4. 강제 로그인 처리
//    private Authentication forceLogin(User kakaoUser) {
//        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return authentication;
//    }
//
//    // 5. response Header에 JWT 토큰 추가
//    private void kakaoUsersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
//        // response header에 token 추가
//        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
//        String token = JwtTokenUtils.generateJwtToken(userDetailsImpl);
//        response.addHeader("Authorization", "BEARER" + " " + token);
//    }
//
//
//}
