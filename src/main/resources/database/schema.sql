CREATE TABLE invoices(
    id SERIAL PRIMARY KEY,
    sequential_number INT UNIQUE NOT NULL,
    status VARCHAR(255) NOT NULL,
    product_quantity INT NOT NULL
);

CREATE TABLE invoice_items(
    product_id INTEGER NOT NULL,
    product_quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE products(
     id SERIAL PRIMARY KEY,
     code VARCHAR(255) UNIQUE NOT NULL,
     name VARCHAR(50) NOT NULL,
     description TEXT NOT NULL,
     price NUMERIC(38, 2) NOT NULL,
     stock INT NOT NULL,
     category VARCHAR(50) NOT NULL,
);
