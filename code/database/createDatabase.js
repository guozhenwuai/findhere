db.users.insertOne({
	"_id":1, 
	"name":"yang222", 
	"password":"1111", 
	"qq":"2837240931", 
	"weixin":"yangruiheng", 
	"email":"yangruiheng1@126.com"});

db.targets.insertOne({
	"_id":1, 
	"creator_id":1, 
	"target_image":"532q3532432rer325", 
	"location":21234});

db.contents.insertOne({
	"_id":1,
	"target_id":1,
	"model_id":1,
	"text_id":1,
	"music_id":1,
	"video_id":1});

db.comments.insertOne({
	"_id":1,
	"user_id":1,
	"type":"text",
	"target_id":1,
	"content_id":1,
	"text":"Hey!",
	"sound_id":1});