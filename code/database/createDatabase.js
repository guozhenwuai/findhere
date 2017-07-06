db.users.insertOne({
	"_id":"yangruiheng1@126.com", 
	"name":"yang222", 
	"password":"1111", 
	"headPortraitID":"1",
	"qq":"2837240931", 
	"weixin":"yangruiheng"});

db.targets.insertOne({
	"_id":"1", 
	"creatorID":"1", 
	"targetImage":"532q3532432rer325", 
	"location":21234});

db.contents.insertOne({
	"_id":"1",
	"targetID":"1",
	"modelID":"1",
	"textID":"1",
	"musicID":"1",
	"videoID":"1"});

db.comments.insertOne({
	"_id":"1",
	"userID":"1",
	"type":"text",
	"targetID":"1",
	"contentID":"1",
	"text":"Hey!",
	"soundID":"1",
	"pictureID":"1"});
	
db.comments.insertOne({
	"_id":"2",
	"userID":"1",
	"type":"picture",
	"targetID":"1",
	"contentID":"1",
	"text":"Hey!",
	"soundID":"1",
	"pictureID":"595b61032f5443085caabe0a"});