package com.proxy.subscription.order.payment.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.auth.service.TelegramAuthService;
import com.proxy.subscription.order.payment.domain.Receipt;
import com.proxy.subscription.order.payment.service.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;
    private final TelegramAuthService telegramAuthService;
    private final AdminAuthorization adminAuthorization;

    public ReceiptController(ReceiptService receiptService,
                              TelegramAuthService telegramAuthService,
                              AdminAuthorization adminAuthorization) {
        this.receiptService = receiptService;
        this.telegramAuthService = telegramAuthService;
        this.adminAuthorization = adminAuthorization;
    }

    @PostMapping("/upload")
    public ResponseEntity<Receipt> uploadReceipt(
            @RequestHeader(value = "X-Telegram-Init-Data", required = false) String initData,
            @RequestHeader(value = "X-Telegram-Bot-Token", required = false) String botToken,
            @RequestParam("file") MultipartFile file,
            @RequestParam("orderId") Long orderId) throws Exception {
        if (initData == null || botToken == null || !telegramAuthService.validateInitData(initData, botToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Receipt receipt = receiptService.uploadReceipt(orderId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Receipt>> getReceiptsForOrder(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @PathVariable Long orderId) {
        adminAuthorization.enforceAdmin(userId);
        return ResponseEntity.ok(receiptService.getReceiptsForOrder(orderId));
    }

    @GetMapping("/download/{receiptId}")
    public ResponseEntity<byte[]> downloadReceipt(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @PathVariable Long receiptId) throws Exception {
        adminAuthorization.enforceAdmin(userId);
        Path filePath = receiptService.getReceiptFilePath(receiptId);
        byte[] data = Files.readAllBytes(filePath);
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(data);
    }
}
