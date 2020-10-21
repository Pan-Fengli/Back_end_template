//load("D:/Code/Java/Back_end_2/src/main/resources/taoxqInfo.js");

let url = "mongodb://localhost:27017/taoxq_info";
let db = connect(url);
db.user_info.remove({});
db.discuss_info.remove({});
db.comment_info.remove({});
db.reply_info.remove({});
db.user_info.insert([
    {
        "_id": 1,
        "icon": ""
    },
    {
        "_id": 2,
        "icon": ""
    },
    {
        "_id": 3,
        "icon": ""
    },
    {
        "_id": 4,
        "icon": ""
    },
    {
        "_id": 5,
        "icon": ""
    },
]);
db.discuss_info.insert([
    {
        "_id": 1,
        "content": "this is detail of discuss1"
    },
    {
        "_id": 2,
        "content": "this is detail of discuss2"
    },
    {
        "_id": 3,
        "content": "this is detail of discuss3"
    },
    {
        "_id": 4,
        "content": "this is detail of discuss4"
    },
    {
        "_id": 5,
        "content": "this is detail of discuss5"
    },
    {
        "_id": 6,
        "content": "this is detail of discuss6"
    }
]);
db.comment_info.insert([
    {
        "_id": 1,
        "content": "comment1: user2 comment in discuss1"
    },
    {
        "_id": 2,
        "content": "comment2: user3 comment in discuss1"
    },
    {
        "_id": 3,
        "content": "comment3: user3 comment in discuss2"
    },
    {
        "_id": 4,
        "content": "comment4: user4 comment in discuss2"
    },
    {
        "_id": 5,
        "content": "comment5: user4 comment in discuss3"
    },
    {
        "_id": 6,
        "content": "comment6: user5 comment in discuss3"
    },
    {
        "_id": 7,
        "content": "comment7: user5 comment in discuss4"
    },
    {
        "_id": 8,
        "content": "comment8: user1 comment in discuss4"
    },
    {
        "_id": 9,
        "content": "comment9: user1 comment in discuss5"
    },
    {
        "_id": 10,
        "content": "comment10: user2 comment in discuss5"
    },
]);
db.reply_info.insert([
    {
        "_id": 1,
        "content": "reply1: user3 reply user2 in comment1"
    },
    {
        "_id": 2,
        "content": "reply2: user4 reply user3 in comment1"
    },
    {
        "_id": 3,
        "content": "reply3: user4 reply user3 in comment2"
    },
    {
        "_id": 4,
        "content": "reply4: user5 reply user4 in comment2"
    },
    {
        "_id": 5,
        "content": "reply5: user4 reply user3 in comment3"
    },
    {
        "_id": 6,
        "content": "reply6: user5 reply user4 in comment3"
    },
    {
        "_id": 7,
        "content": "reply7: user5 reply user4 in comment4"
    },
    {
        "_id": 8,
        "content": "reply8: user1 reply user5 in comment4"
    },
    {
        "_id": 9,
        "content": "reply9: user5 reply user4 in comment5"
    },
    {
        "_id": 10,
        "content": "reply10: user1 reply user5 in comment5"
    },
    {
        "_id": 11,
        "content": "reply11: user1 reply user5 in comment6"
    },
    {
        "_id": 12,
        "content": "reply12: user2 reply user1 in comment6"
    },
    {
        "_id": 13,
        "content": "reply13: user1 reply user5 in comment7"
    },
    {
        "_id": 14,
        "content": "reply14: user2 reply user1 in comment7"
    },
    {
        "_id": 15,
        "content": "reply15: user2 reply user1 in comment8"
    },
    {
        "_id": 16,
        "content": "reply16: user3 reply user2 in comment8"
    },
    {
        "_id": 17,
        "content": "reply17: user2 reply user1 in comment9"
    },
    {
        "_id": 18,
        "content": "reply18: user3 reply user2 in comment9"
    },
    {
        "_id": 19,
        "content": "reply19: user3 reply user2 in comment10"
    },
    {
        "_id": 20,
        "content": "reply20: user4 reply user3 in comment10"
    },
]);

