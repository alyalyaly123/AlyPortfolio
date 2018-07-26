var Discord = require('discord.io');
var logger = require('winston');
var auth = require('./auth.json');
// Configure logger settings
logger.remove(logger.transports.Console);
logger.add(new logger.transports.Console, {
colorize: true});
logger.level = 'debug';
// Initialize Discord Bot
var bot = new Discord.Client({
token: auth.token,
autorun: true
});
bot.on('ready', function (evt) {
logger.info('Connected');
logger.info('Logged in as: ');
logger.info(bot.username + ' – (' + bot.id + ')');
});


bot.on('message', function (user, userID, channelID, message, evt) {
if (message.substring(0, 1) == '!') {
var args = message.substring(1).split(' ');
var cmd = args[0];

args = args.splice(1);
switch(cmd) {
case 'intro':
bot.sendMessage({
to: channelID,
message: "Greetings! Welcome to the server!"

});
break;
case 'hi' : 
bot.sendMessage({
	to: channelID,
	message: "Hi "+ user +" " + userID + "!"
	
});
break;
case 'name' : 
bot.sendMessage({
	to: channelID,
	message: "Your name is "+ user +""
	
});
break;
}
}



//age
if (message.substring(0,4)== 'age.') {
	var args = message.substring(4).split(' ');
	var cmd=args[0];
	args=args.splice(4);
	
	if(isNaN(parseInt(cmd))){
	bot.sendMessage({
		
	to: channelID,
	message:"Please input a number!" + cmd +"is not a number"
	});
	}
	
	//if not a number
	else{
	
	var numArgs= parseInt(cmd);
	
	var dateThing= new Date();
	var year= dateThing.getFullYear();
	var birthYear= year-numArgs;
	bot.sendMessage({
		
	to: channelID,
	message:"birthyear is "+ birthYear +"."
	});
		logger.info("you did it i guess");
	}

}


})