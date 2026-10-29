CREATE TABLE IF NOT EXISTS plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    data_allowance_gb BIGINT NOT NULL,
    validity_duration_days INTEGER NOT NULL,
    price_toman DECIMAL(15, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    telegram_user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    plan_snapshot_name VARCHAR(255) NOT NULL,
    plan_snapshot_data_gb BIGINT NOT NULL,
    plan_snapshot_duration_days INTEGER NOT NULL,
    plan_snapshot_price_toman DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
