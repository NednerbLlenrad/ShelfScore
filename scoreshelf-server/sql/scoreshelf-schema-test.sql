drop database if exists scoreshelf_test;
create database scoreshelf_test;
use scoreshelf_test;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(30) not null unique,
    email varchar(100) not null unique,
    password_hash varchar(2048) not null
);

create table game (
    game_id int primary key auto_increment,
    game_name varchar(100) not null,
    image_url varchar(250),
    category varchar(50) not null,
    min_players int not null,
    max_players int not null,
    is_private boolean not null default true,
    app_user_id int not null,
    constraint fk_game_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id)
);

create table player (
    player_id int primary key auto_increment,
    app_user_id int not null,
    player_name varchar(50) not null,
    linked_app_user_id int null,
    constraint fk_player_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_player_linked_app_user_id
        foreign key (linked_app_user_id)
        references app_user(app_user_id),
    constraint uq_player_app_user_name
        unique (app_user_id, player_name)
);

create table user_game_library (
    app_user_id int not null,
    game_id int not null,
    constraint pk_user_game_library
        primary key (app_user_id, game_id),
    constraint fk_user_game_library_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_user_game_library_game_id
        foreign key (game_id)
        references game(game_id)
);

create table score_sheet (
    score_sheet_id int primary key auto_increment,
    game_id int not null,
    score_sheet_name varchar(50) not null,
    constraint fk_score_sheet_game_id
        foreign key (game_id)
        references game(game_id)
);

create table score_sheet_row (
    score_sheet_row_id int primary key auto_increment,
    score_sheet_id int not null,
    row_name varchar(50) not null,
    display_order int not null,
    row_type varchar(50) not null,
    expression varchar(255),
    constraint fk_score_sheet_row_score_sheet_id
        foreign key (score_sheet_id)
        references score_sheet(score_sheet_id)
);

create table game_session (
    game_session_id int primary key auto_increment,
    game_id int not null,
    app_user_id int not null,
    played_at timestamp not null default current_timestamp,
    constraint fk_game_session_game_id
        foreign key (game_id)
        references game(game_id),
    constraint fk_game_session_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id)
);

create table game_session_player (
    game_session_player_id int primary key auto_increment,
    game_session_id int not null,
    player_id int null,
    player_name varchar(50) not null,
    total_score int not null default 0,
    is_winner boolean not null default false,
    constraint fk_game_session_player_game_session_id
        foreign key (game_session_id)
        references game_session(game_session_id),
    constraint fk_game_session_player_player_id
        foreign key (player_id)
        references player(player_id)
);

create table score_entry (
    score_entry_id int primary key auto_increment,
    game_session_player_id int not null,
    score_sheet_row_id int not null,
    value int not null,
    constraint fk_score_entry_game_session_player_id
        foreign key (game_session_player_id)
        references game_session_player(game_session_player_id),
    constraint fk_score_entry_score_sheet_row_id
        foreign key (score_sheet_row_id)
        references score_sheet_row(score_sheet_row_id)
);

delimiter //

create procedure set_known_good_state()
begin

    delete from score_entry;
    alter table score_entry auto_increment = 1;

    delete from game_session_player;
    alter table game_session_player auto_increment = 1;

    delete from game_session;
    alter table game_session auto_increment = 1;

    delete from score_sheet_row;
    alter table score_sheet_row auto_increment = 1;

    delete from score_sheet;
    alter table score_sheet auto_increment = 1;

    delete from user_game_library;

    delete from player;
    alter table player auto_increment = 1;

    delete from game;
    alter table game auto_increment = 1;

    delete from app_user;
    alter table app_user auto_increment = 1;

    insert into app_user (username, email, password_hash)
    values
        ('brenden', 'brenden@test.com', 'hash'),
        ('sally', 'sally@test.com', 'hash');

    insert into game (
        game_name,
        image_url,
        category,
        min_players,
        max_players,
        is_private,
        app_user_id
    )
    values
        (
            'Wingspan',
            null,
            'Strategy',
            1,
            5,
            false,
            1
        ),
        (
            'Catan',
            null,
            'Strategy',
            3,
            4,
            false,
            1
        ),
        (
            'Skull',
            null,
            'Party',
            3,
            6,
            false,
            2
        );
    insert into score_sheet (
        game_id,
        score_sheet_name
    )
    values
        (
            1,
            'Default Wingspan Score Sheet'
        ),
        (
            2,
            'Default Catan Score Sheet'
        ),
        (
            3,
            'Default Skull Score Sheet'
        );

    insert into score_sheet_row (
        score_sheet_id,
        row_name,
        display_order,
        row_type,
        expression
    )
    values
        (
            1,
            'Birds',
            1,
            'INPUT',
            null
        ),
        (
            1,
            'Bonus Cards',
            2,
            'INPUT',
            null
        ),
        (
            1,
            'Total',
            3,
            'TOTAL',
            null
        ),
        (
            2,
            'Victory Points',
            1,
            'INPUT',
            null
        ),
        (
            2,
            'Longest Road',
            2,
            'INPUT',
            null
        ),
        (
            2,
            'Total',
            3,
            'TOTAL',
            null
        );
end //

delimiter ;

call set_known_good_state();