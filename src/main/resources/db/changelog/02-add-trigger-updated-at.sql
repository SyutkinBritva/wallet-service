CREATE OR REPLACE FUNCTION update_wallet_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language plpgsql;

-- Создание триггера, вызывающего функцию перед обновлением записи
CREATE TRIGGER set_wallet_updated_at
    BEFORE UPDATE
    ON wallet
    FOR EACH ROW
EXECUTE FUNCTION update_wallet_updated_at();