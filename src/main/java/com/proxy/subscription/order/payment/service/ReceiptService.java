package com.proxy.subscription.order.payment.service;

import com.proxy.subscription.order.payment.domain.Receipt;
import com.proxy.subscription.order.payment.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private static final String RECEIPT_DIR = "receipts";

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
        try {
            Files.createDirectories(Paths.get(RECEIPT_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create receipt directory", e);
        }
    }

    public Receipt uploadReceipt(Long orderId, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + (originalFilename != null ? originalFilename : "receipt");
        Path targetPath = Paths.get(RECEIPT_DIR, fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Receipt receipt = new Receipt(orderId, targetPath.toString(), originalFilename);
        return receiptRepository.save(receipt);
    }

    public java.nio.file.Path getReceiptFilePath(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        return Paths.get(receipt.getFilePath());
    }

    public java.util.List<Receipt> getReceiptsForOrder(Long orderId) {
        return receiptRepository.findByOrderIdOrderBySubmittedAtDesc(orderId);
    }
}
