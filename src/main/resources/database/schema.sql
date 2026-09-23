CREATE TABLE invoices(
    id SERIAL PRIMARY KEY,
    sequential_number INT UNIQUE NOT NULL,
    invoice_status VARCHAR(255) NOT NULL,
    total_price NUMERIC(38, 2) NOT NULL,
    total_product_quantity INT NOT NULL,
    issued_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE invoice_items(
    product_id INTEGER NOT NULL,
    product_quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE invoices_invoice_items(
    invoice_id INTEGER NOT NULL,
    invoice_items_id INTEGER NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (invoice_items_id) REFERENCES invoice_items(id)
);

CREATE TABLE products(
    id SERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(38, 2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(50) NOT NULL
);

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    birth_date TIMESTAMP NOT NULL,
    roles VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE customers(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    address_cep VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE addresses(
    cep VARCHAR(255) NOT NULL,
    logradouro VARCHAR(255),
    complemento VARCHAR(255),
    unidade VARCHAR(255),
    bairro VARCHAR(255),
    localidade VARCHAR(255),
    uf VARCHAR(255),
    estado VARCHAR(255),
    regiao VARCHAR(255),
    ibge VARCHAR(255),
    gia VARCHAR(255),
    ddd VARCHAR(255),
    siafi VARCHAR(255)
);
