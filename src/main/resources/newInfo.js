//load("D:\\Code\\Java\\Back_end_2\\src\\main\\resources\\newInfo.js");
let image1 = "data:image/jpeg;base64,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"
let image2 = "data:image/jpeg;base64,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"
let base = 1;
let discussN = base * 1000, userN = base * 1000;
let commentN = discussN * 10;
let replyN = commentN * 10;

let userInfo = [], discussInfo = [], commentInfo = [], replyInfo = [];

for (let i = 0; i < userN + 11; i++) {
    userInfo.push({
        "_id": i + 1,
        "icon": i % 2 === 1 ? image1 : image2
    });
}

for (let i = 0; i < discussN; i++) {
    let tmp = Math.random().toString(36).slice(-8);
    discussInfo.push({
        "_id": i + 1,
        "content": "this is the detail of discuss " + tmp,
        "text": "this is the detail of discuss " + tmp
    });
}

for (let i = 0; i < commentN; i++) {
    commentInfo.push({
        "_id": i + 1,
        "content": "this is the detail of comment " + Math.random().toString(36).slice(-8)
    });
}

for (let i = 0; i < replyN; i++) {
    replyInfo.push({
        "_id": i + 1,
        "content": "this is the detail of reply " + Math.random().toString(36).slice(-8)
    });
}


let url = "mongodb://localhost:27017/taoxq_info";
let db = connect(url);
db.user_info.remove({});
db.discuss_info.remove({});
db.comment_info.remove({});
db.reply_info.remove({});

db.user_info.insert(userInfo);
db.discuss_info.insert(discussInfo);
db.comment_info.insert(commentInfo);
db.reply_info.insert(replyInfo);