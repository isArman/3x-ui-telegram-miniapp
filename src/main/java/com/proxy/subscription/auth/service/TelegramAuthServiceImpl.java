package com.proxy.subscription.auth.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class TelegramAuthServiceImpl implements TelegramAuthService {

    @Override
    public boolean validateInitData(String initData, String botToken) {
        if (initData == null || initData.isEmpty() || botToken == null || botToken.isEmpty()) {
            return false;
        }

        try {
            Map<String, String> params = parseInitData(initData);
            String hash = params.remove("hash");
            if (hash == null || hash.isEmpty()) {
                return false;
            }

            String dataCheckString = buildDataCheckString(params);
            String computedHash = computeHmacSha256(dataCheckString, botToken);

            return MessageDigest.isEqual(
                computedHash.getBytes(StandardCharsets.UTF_8),
                hash.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long extractUserId(String initData) {
        if (initData == null || initData.isEmpty()) {
            return null;
        }
        try {
            Map<String, String> params = parseInitData(initData);
            String userParam = params.get("user");
            if (userParam == null || userParam.isEmpty()) {
                return null;
            }
            // The user parameter is a JSON object; we need to parse it to extract id
            // For simplicity, we extract the id from the JSON string
            String idStr = extractJsonValue(userParam, "id");
            if (idStr == null || idStr.isEmpty()) {
                return null;
            }
            return Long.parseLong(idStr);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> parseInitData(String initData) throws Exception {
        Map<String, String> params = new HashMap<>();
        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            int eqIndex = pair.indexOf('=');
            if (eqIndex > 0) {
                String key = URLDecoder.decode(pair.substring(0, eqIndex), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(eqIndex + 1), StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }

    private String buildDataCheckString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(key).append("=").append(params.get(key));
        }
        return sb.toString();
    }

    private String computeHmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String extractJsonValue(String json, String key) {
        // Simple JSON value extraction for the user.id field
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) {
            return null;
        }
        int colonIndex = json.indexOf(':', keyIndex);
        if (colonIndex == -1) {
            return null;
        }
        int valueStart = colonIndex + 1;
        // Skip whitespace
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        if (valueStart >= json.length()) {
            return null;
        }
        // Check if value is quoted
        if (json.charAt(valueStart) == '"') {
            valueStart++;
            int valueEnd = json.indexOf('"', valueStart);
            if (valueEnd == -1) {
                return null;
            }
            return json.substring(valueStart, valueEnd);
        } else {
            int valueEnd = valueStart;
            while (valueEnd < json.length() && json.charAt(valueEnd) != ',' && json.charAt(valueEnd) != '}') {
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd).trim();
        }
    }
}
