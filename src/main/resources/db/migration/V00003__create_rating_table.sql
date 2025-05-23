CREATE TABLE rating (
    id VARCHAR(36) PRIMARY KEY,
    menu_id VARCHAR(36) NOT NULL,
    session_id VARCHAR(36) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rating_menu FOREIGN KEY (menu_id) REFERENCES menu(id)
);