ALTER TABLE menu
ADD COLUMN average_rating NUMERIC(3,2) DEFAULT 0.0;

CREATE OR REPLACE FUNCTION update_average_rating()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE menu
    SET average_rating = (
        SELECT ROUND(AVG(rating)::NUMERIC, 2)
        FROM rating
        WHERE menu_id = NEW.menu_id
    )
    WHERE id = NEW.menu_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_average_rating_on_delete()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE menu
    SET average_rating = (
        SELECT COALESCE(ROUND(AVG(rating)::NUMERIC, 2), 0.0)
        FROM rating
        WHERE menu_id = OLD.menu_id
    )
    WHERE id = OLD.menu_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_avg_rating_after_insert_update
AFTER INSERT OR UPDATE ON rating
FOR EACH ROW
EXECUTE FUNCTION update_average_rating();

CREATE TRIGGER trg_update_avg_rating_after_delete
AFTER DELETE ON rating
FOR EACH ROW
EXECUTE FUNCTION update_average_rating_on_delete();
