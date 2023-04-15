
create TABLE authors (
    id bigint auto_increment primary key,
    firstname varchar(100) not null,
    lastname varchar(100) not null,
    email varchar(100) not null
);

create TABLE posts (
    id bigint auto_increment primary key,
    title varchar(200) not null,
    body varchar(1000) not null,
    publish_date char(8),
    author_id bigint references authors(id)
);




create TABLE comments (
    id bigint auto_increment primary key,
    email varchar(100) not null,
    body varchar(200) not null,
    date char(8),
    post_id bigint references posts(id)
);
