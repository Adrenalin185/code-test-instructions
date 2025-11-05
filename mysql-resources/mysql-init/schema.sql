CREATE TABLE IF NOT EXISTS url (
    original_url varchar(255) not null,
    shortened_url varchar(255),
    alias varchar(255),
    unique (original_url),
    primary key (original_url)
);