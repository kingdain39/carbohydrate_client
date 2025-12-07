package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthService {

    private final ObjectMapper objectMapper;
    private final String serverUrl;

    public AuthService(String serverUrl) {
        this.serverUrl = serverUrl;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 로그인
     * @return userId
     * @throws AuthException 로그인 실패 시
     */
    public Long login(String username, String password) throws AuthException {
        try {
            URL url = new URL(serverUrl + "/api/v1/users/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            // 요청 JSON
            String body = String.format("{\"userName\":\"%s\", \"password\":\"%s\"}", username, password);
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            int code = conn.getResponseCode();

            if (code == 200) {
                // 응답 JSON 읽기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String response = sb.toString();

                // JSON 파싱 - userId 추출
                JsonNode jsonNode = objectMapper.readTree(response);

                // 서버 응답에 userId 필드가 있다고 가정 (없으면 팀원한테 확인)
                if (jsonNode.has("userId")) {
                    return jsonNode.get("userId").asLong();
                } else if (jsonNode.has("id")) {
                    return jsonNode.get("id").asLong();
                } else {
                    // userId 필드를 못 찾으면 임시로 랜덤 (나중에 수정)
                    System.err.println("Warning: userId not found in response, using random");
                    return (long) (Math.random() * 10000);
                }

            } else {
                throw new AuthException("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
            }

        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("서버 연결 오류: " + e.getMessage());
        }
    }

    /**
     * 회원가입
     * @throws AuthException 회원가입 실패 시
     */
    public void signup(String username, String password) throws AuthException {
        try {
            URL url = new URL(serverUrl + "/api/v1/users/signup");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            // 요청 JSON
            String body = String.format("{\"userName\":\"%s\", \"password\":\"%s\"}", username, password);
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            int code = conn.getResponseCode();

            if (code == 200) {
                // 성공
                return;
            } else {
                throw new AuthException("회원가입 실패: 아이디가 이미 존재할 수 있습니다.");
            }

        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("서버 연결 오류: " + e.getMessage());
        }
    }

    /**
     * 인증 예외
     */
    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}
