create table Language (
    langCode text primary key,
    title text not null
);

create table Skill (
    skillId integer primary key,
    title text not null unique
);

create table User (
    userId INTEGER primary key,
    username TEXT not null unique,
    password TEXT not null,
    firstname TEXT not null,
    lastname TEXT,
    intro TEXT,
    about TEXT,
    avatar BLOB,
    accomp text,
    birthday integer,
    location text
);

create table User_Lang (
    userId INTEGER not null
       references User(userId)
           on update cascade on delete cascade,
    langCode TEXT not null
       references Language(langCode)
           on update cascade on delete cascade,
    primary key (userId, langCode)
);

create table User_Skill (
    userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    skillId INTEGER not null
        references Skill(skillId)
            on update cascade on delete cascade,
    level INTEGER default 0 not null,
    time INTEGER not null,
    primary key (userId, skillId)
);

create table Skill_Endorse (
   userId integer not null
       references User(userId)
           on update cascade on delete cascade,
   skillId integer not null
       references Skill(skillId)
           on update cascade on delete cascade,
   by_userId integer not null
       references User(userId)
           on update cascade on delete cascade,
   time integer not null,
   notified integer default 0 not null,
   primary key (by_userId, userId, skillId)
);

create table User_Background (
    bgId INTEGER primary key,
    userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    type INTEGER default 0 not null,
    title text not null,
    fromTime integer not null,
    toTime integer default -1 not null
);

create table Article (
    articleId INTEGER primary key,
    writer_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    title text not null,
    content text,
    time INTEGER not null,
    featured INTEGER default 0 not null
);

create table Comment (
    commentId INTEGER primary key,
    articleId INTEGER not null
        references Article(articleId)
            on update cascade on delete cascade,
    reply_commentId INTEGER
        references Comment(commentId)
            on update cascade on delete cascade,
    userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    content TEXT not null,
    time INTEGER default -1 not null,
    notified INTEGER default 0 not null
);

create table User_Like (
    userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    articleId INTEGER not null
        references Article(articleId)
            on update cascade on delete cascade,
    commentId INTEGER
        references Comment(commentId)
            on update cascade on delete cascade,
    time INTEGER not null,
    notified INTEGER default 0 not null,
    primary key (userId, articleId, commentId)
);

create table Invitation (
    to_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    from_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    time INTEGER not null,
    message text,
    status int default 0 not null,
    primary key (to_userId, from_userId)
);

create table Connection (
    from_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    to_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    time integer not null,
    primary key (from_userId, to_userId)
);

create table Event_ProfileVisit (
    by_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    profile_userId INTEGER not null
        references User(userId)
            on update cascade on delete cascade,
    time integer not null,
    notified INTEGER default 0 not null,
    primary key (by_userId, profile_userId, time)
);

