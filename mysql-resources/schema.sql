CREATE TABLE IF NOT EXISTS users (
    id int auto_increment not null,
    username varchar(255) not null,
    unique (id),
    primary key (id)
    );

CREATE TABLE IF NOT EXISTS url (
    id int auto_increment not null,
    customer_id int not null,
    original_url varchar(255) not null,
    shortened_url varchar(255) not null,
    alias varchar(255),
    unique (id),
    primary key (id),
    foreign key (customer_id) references users(id)
);