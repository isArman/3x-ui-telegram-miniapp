CREATE TABLE IF NOT EXISTS receipts (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    submitted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    replaced_by_receipt_id BIGINT
);

CREATE INDEX IF NOT EXISTS idx_receipts_order_id ON receipts(order_id);

CREATE TABLE IF NOT EXISTS payment_audits (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    admin_telegram_user_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    notes VARCHAR(1000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_payment_audits_order_id ON payment_audits(order_id);
