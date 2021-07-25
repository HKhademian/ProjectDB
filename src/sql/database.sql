
-- Base Tables --

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
    level INTEGER NOT NULL DEFAULT 0,
    time INTEGER NOT NULL DEFAULT -1,
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
   time INTEGER NOT NULL DEFAULT -1,
   notified integer default 0 not null,
   primary key (by_userId, userId, skillId)
);

create table User_Background (
    bgId INTEGER primary key,
    userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    type INTEGER NOT NULL DEFAULT 0,
    title text not null,
    fromTime INTEGER NOT NULL DEFAULT -1,
    toTime INTEGER NOT NULL DEFAULT -1
);

create table Article (
    articleId INTEGER primary key,
    writer_userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    title text not null,
    content text,
    time INTEGER NOT NULL DEFAULT 0
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
    time INTEGER NOT NULL DEFAULT -1,
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
    time INTEGER NOT NULL DEFAULT -1,
    notified INTEGER NOT NULL DEFAULT 0,
    primary key (userId, articleId, commentId)
);

create table User_Accomplishment
(
    accId   INTEGER PRIMARY KEY,
    userId  INTEGER NOT NULL
        references User(userId)
        on update cascade on delete cascade,
    title   TEXT    NOT NULL,
    content TEXT    NOT NULL,
    time    INTEGER NOT NULL DEFAULT -1
);

create table User_Feature
(
    userId    INTEGER NOT NULL
        references User (userId)
        on update cascade on delete cascade,
    articleId INTEGER NOT NULL
        references Article (articleId)
        on update cascade on delete cascade,
    time      INTEGER NOT NULL DEFAULT -1,
    primary key (userId, articleId)
);

-- Network Tables --

create table Invitation (
    sender_userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    receiver_userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    message text,
    time INTEGER NOT NULL DEFAULT -1,
    status INTEGER NOT NULL DEFAULT 0,
    primary key (sender_userId, receiver_userId)
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
    time INTEGER NOT NULL DEFAULT -1,
    notified INTEGER NOT NULL DEFAULT 0,
    primary key (by_userId, profile_userId, time)
);


--- Messaging Tables ---

create table Chat (
    chatId INTEGER primary key,
    time INTEGER not null,
    title TEXT
);

create table Chat_User (
    chatId INTEGER not null
        references Chat(chatId)
        on update cascade on delete cascade,
    userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    joinTime INTEGER NOT NULL DEFAULT 0,
    isAdmin INTEGER NOT NULL DEFAULT 0,
    isArchived INTEGER NOT NULL DEFAULT 0,
    isMuted INTEGER NOT NULL DEFAULT 0,
    primary key (chatId, userId)
);

create table Message (
    messageId INTEGER primary key,
    chatId INTEGER not null
        references Chat(chatId)
        on update cascade on delete cascade,
    sender_userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    reply_messageId INTEGER
        references Message(messageId)
        on update cascade on delete cascade,
    content TEXT not null,
    time INTEGER NOT NULL DEFAULT 0
);

create table Message_State (
    messageId INTEGER not null
        references Message(messageId)
        on update cascade on delete cascade,
    userId INTEGER not null
        references User(userId)
        on update cascade on delete cascade,
    receiveTime INTEGER NOT NULL DEFAULT -1,
    seenTime INTEGER NOT NULL DEFAULT -1,
    primary key (messageId, userId)
);


-- Base Views --

CREATE VIEW ArticleStat as
    select A.*, ACL.like_count, ACC.comment_count
    from Article A
    left join (
        select A.articleId as articleId , count(C.commentId) as comment_count
        from Article A
        left join Comment C on A.articleId=C.articleId and C.reply_commentId is null
        group by A.articleId
    ) ACC on A.articleId = ACC.articleId
    left join (
        select A.articleId as articleId , count(UL.userId) as like_count
        from Article A
        left join User_Like UL on A.articleId=UL.articleId and UL.commentId is null
        group by A.articleId
    ) ACL on A.articleId = ACL.articleId
    order by A.time, ACL.like_count+ACC.comment_count desc
;

CREATE VIEW CommentStat as
    select C.*, CCL.like_count as like_count, CCR.reply_count as reply_count
    from Comment C
    left join (
        select C.commentId as commentId , count(UL.userId) as like_count
        from Comment C
        left join User_Like UL on C.articleId=UL.articleId and C.commentId=UL.commentId
        group by C.commentId
    ) CCL on C.commentId = CCL.commentId
    left join (
        select C.commentId as commentId , count(CR.commentId) as reply_count
        from Comment C
        left join Comment CR on C.articleId=CR.articleId and C.commentId=CR.reply_commentId
        group by C.commentId
    ) CCR on C.commentId = CCR.commentId
    order by C.time, CCL.like_count + CCR.reply_count desc
;

CREATE VIEW SuggestBackground as
    select distinct trim(UB.title) as suggestion
    from User_Background UB
    where UB.title is not null AND length(trim(UB.title))>0
    group by UB.title
    order by count(UB.userId) desc
;

CREATE VIEW SuggestLocation as
    select distinct trim(U.location) as suggestion
    from User U
    where U.location is not null AND length(trim(U.location))>0
    group by U.location
    order by count(U.userId) desc;
;


-- Network Views --

CREATE VIEW MutualConnection as
    select U1.userId as from_userId, U2.userId as to_userId, U3.userId as mutual_userId
    from User U1, User U2, User U3
    where 1
      and U1.userId != U2.userId
      and U1.userId != U3.userId
      and U2.userId != U3.userId
      and EXISTS( select * from Connection C where C.from_userId=U1.userId and C.to_userId=U3.userId  )
      and EXISTS( select * from Connection C where C.from_userId=U2.userId and C.to_userId=U3.userId  )
    order by U1.userId, U2.userId, U3.userId
;


CREATE VIEW MayKnow as
    select MC.from_userId as from_userId, MC.to_userId as to_userId, 'mutual' as type, MC.mutual_userId as target
    from MutualConnection MC
    UNION
    select U1.userId as from_userId, U2.userId as to_userId, 'skill' as type, S.skillId as target
    from User U1, User U2, Skill S
    where U1.userId != U2.userId
      and exists(select * from User_Skill US where US.userId=U1.userId and US.skillId=S.skillId)
      and exists(select * from User_Skill US where US.userId=U2.userId and US.skillId=S.skillId)
    UNION
    select U1.userId as from_userId, U2.userId as to_userId, 'location' as type, U1.location as target
    from User U1, User U2, Skill S
    where U1.userId != U2.userId AND U1.location=U2.location
;

CREATE VIEW MyNetwork as
select MN.from_userId as from_userId, MN.to_userId as to_userId, MN.type as type, count(distinct MC.mutual_userId) as mutual_count
from (
    select MK.from_userId as from_userId, MK.to_userId as to_userId, 'may_know' as type
    from MayKnow MK
    union
    select I.sender_userId as from_userId, I.receive_userId as to_userId, 'invited' as type
    from Invitation I where I.status>=0
    union
    select I.receive_userId as from_userId, I.sender_userId as to_userId, 'requested' as type
    from Invitation I where I.status>=0
) MN
left join MutualConnection MC on MC.from_userId=MN.from_userId and MC.to_userId=MN.to_userId
--where not exists(select * from Connection C where C.from_userId=MN.from_userId and C.to_userId=MN.to_userId)
group by MN.from_userId, MN.to_userId, MN.type
order by MN.from_userId, count(distinct MC.mutual_userId) desc
;

CREATE VIEW Notification as
    select UN.from_userId as userId, 'birth' as event, UN.to_userId as by_userId, null as targetId
    from Connection UN
    join User U on UN.to_userId = U.userId
    where strftime('%d-%m', date(U.birthday, 'unixepoch')) = strftime('%d-%m', 'now')
    UNION
    select PV.profile_userId as userId, 'visit' as event, PV.by_userId as by_userId, null as targetId
    from Event_ProfileVisit PV
    where PV.notified = 0
    UNION
    select A.writer_userId as userId, 'like_article' as event, UL.userId as by_userId, null as targetId
    from User_Like UL
    join Article A on UL.articleId = A.articleId and UL.commentId is null
    where UL.notified = 0
    UNION
    select C.userId as userId, 'like_comment' as event, UL.userId as by_userId, null as targetId
    from User_Like UL
    join Comment C on UL.commentId = C.commentId
    where UL.notified = 0
    UNION
    select A.writer_userId as userId, 'comment' as event, C.userId as by_userId, C.articleId as targetId
    from Comment C
    join Article A on C.articleId = A.articleId and C.reply_commentId is null
    where C.notified=0
    UNION
    select C2.userId as userId, 'reply' as event, C1.userId as by_userId,  C1.commentId as targetId
    from Comment C1
    join Comment C2 on C1.reply_commentId = C2.commentId
    where C1.notified=0
    UNION
    select SE.userId as userId, 'endorse' as event, SE.by_userId as by_userId,  SE.skillId as targetId
    from Skill_Endorse SE
    where SE.notified=0
;

CREATE VIEW Home as
    select A.writer_userId as userId, A.articleId as articleId, A.time as time, 'owner' as type, A.writer_userId as by_useId
    from Article A
    UNION
    select CON.from_userId as userId, A.articleId as articleId, A.time as time, 'posted' as type, CON.to_userId as by_useId
    from Connection CON
    join Article A on CON.to_userId = A.writer_userId
    UNION
    select CON.from_userId as userId, UL.articleId as articleId, UL.time as time, 'liked' as type, CON.to_userId as by_useId
    from Connection CON
    join User_Like UL on CON.to_userId = UL.userId and UL.commentId is null
    UNION
    select CON.from_userId as userId, C.articleId as articleId, C.time as time, 'commented' as type, CON.to_userId as by_useId
    from Connection CON
    join Comment C on CON.to_userId = C.userId and C.reply_commentId is null
;

CREATE VIEW HomeArticle as
    select A.*,
        U.userId                 as home_userId,
        MAX(H.time)              as home_time,
        count(ALL H.userId)      as home_count,
        (U_L.userId IS NOT NULL) as home_isLiked,
        (U_F.userId IS NOT NULL) as home_isFeatured,
        (H.userId IS NOT NULL)   as home_isInHome
    from User U, ArticleStat A
    LEFT JOIN Home H ON H.userId = U.userId AND H.articleId = A.articleId
    LEFT JOIN User_Like U_L ON U_L.userId = U.userId AND U_L.articleId = A.articleId AND U_L.commentId IS NULL
    LEFT JOIN User_Feature U_F ON U_F.userId = U.userId AND U_F.articleId = A.articleId
    group by U.userId, A.articleId
    order by U.userId, MAX(H.time) DESC
;

CREATE VIEW HomeComment AS
    SELECT CS.*, U.userId as home_userId, count(UL.time) as home_isLiked
    from User U, CommentStat CS
    LEFT JOIN User_Like UL ON UL.userId = U.userId AND UL.articleId = CS.articleId AND UL.commentId = CS.commentId
    group by U.userId, CS.commentId
;


-- Messaging Views --

CREATE VIEW UserMessage as
    select U.userId, MS.receiveTime, MS.seenTime, M.*
    from User U, Message M
    JOIN Chat C ON M.chatId = C.chatId
    JOIN Chat_User CU ON U.userId = CU.userId
    LEFT JOIN Message_State MS ON MS.messageId = M.messageId AND MS.userId = U.userId
;

CREATE VIEW UserChat as
    SELECT C.title, C.time, CU.*, MAX(UM.seenTime) AS lastSeen_time
    FROM Chat C
    JOIN (
        select CU.*, count(UM.messageId) as unread_count
        from Chat_User CU
        LEFT JOIN UserMessage UM ON CU.chatId = UM.chatId AND CU.userId = UM.userId AND (UM.seenTime is NULL OR UM.seenTime <= 0)
        group by CU.userId, CU.chatId
    ) CU ON CU.chatId = C.chatId
    LEFT JOIN UserMessage UM ON CU.chatId = UM.chatId AND CU.userId = UM.userId
    GROUP BY CU.userId, CU.chatId
;



