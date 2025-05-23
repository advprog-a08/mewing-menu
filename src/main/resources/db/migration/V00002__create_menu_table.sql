CREATE TABLE menu (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE CHECK (char_length(name) >= 5),
    description VARCHAR(255) NOT NULL CHECK (char_length(description) <= 255),
    image_url TEXT NOT NULL,
    quantity NUMERIC CHECK (quantity >= 0 AND quantity <= 1000),
    price NUMERIC NOT NULL CHECK (price >= 0.0 AND price <= 1000000.0),
    category_id VARCHAR(36),
    CONSTRAINT fk_menu_category FOREIGN KEY (category_id) REFERENCES menu_category(id)
);

