CREATE INDEX idx_payments_status ON payments (status);
CREATE INDEX idx_payments_btc_address ON payments (btc_address);
CREATE INDEX idx_payments_created_at ON payments (created_at);
