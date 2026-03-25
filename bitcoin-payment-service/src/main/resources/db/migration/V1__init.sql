CREATE TABLE payments (
    id UUID PRIMARY KEY,
    merchant_reference VARCHAR(255),
    fiat_currency VARCHAR(3) NOT NULL,
    fiat_amount_minor BIGINT NOT NULL,
    amount_satoshi BIGINT NOT NULL,
    btc_address VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    tx_id VARCHAR(128),
    confirmations INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
