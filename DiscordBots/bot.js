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
//find mean of a string of numbers
/*(if (message.substring(0,4)== 'avg.') {
	var total=0;
	var args = message.substring(4).split(' ');
	var cmd=args[0].split(",");
	args=args.splice(4);
	for(
	if(isNaN(parseInt(cmd))){
	bot.sendMessage({
		
	to: channelID,
	message:"Please input a number!" + cmd +"is not a number"
	});
	break;
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

}*/

//returns the exact response of the user: General message testing
if (message.substring(0,7)== 'parrot.') {
		logger.info("Parroted!");

	var total=0;
	var args = message.substring(7).split(' ');
	var cmd=args[0]
	messageTest(user, userID, channelID, message, evt,cmd);
		logger.info("you did it i guess");
		}
//ROCK PAPER SCISSORS
if (message.substring(0,4)== 'rps.') {
		logger.info("Rock paper scissors!");
	var total=0;
	var args = message.substring(4).split(' ');
	var cmd=args[0]
	rockPaper(user, userID, channelID, message, evt,cmd);
	
	}
})


/*
if (message.substring(0,5)== 'dice.') {
		logger.info("Dice roll!");

	var args = message.substring(6).split(' ');
	var cmd=args[0]
	dice(user, userID, channelID, message, evt,cmd);
	
	}
*/



function messageTest(user, userID, channelID, message, evt,cmd){
	bot.sendMessage({
	to: channelID,
	message:"Message is "+ cmd + "."
	});
	
}

//Rock paper scissors! Basic atm, gotta make more clear
function rockPaper(user, userID, channelID, message, evt,cmd){
	//Validation check
	if(cmd=="rock" || cmd=="paper"|| cmd=="scissors"){
		var compResponse="item";
		logger.info("Correctly input!");
		
		var numb = Math.floor((Math.random() * 3) + 1);
		logger.info("number is "+ numb +".");
		
		if(numb==1){
			compResponse="rock";
		}
		else if(numb==2){
			compResponse="paper";
		}
		else{
			compResponse="scissors";
		}
		
		if(cmd==compResponse){
			logger.info("Draw!");
		}
		else if((cmd=="rock" &&compResponse=="scissors") 
				|| 
				(cmd=="paper" && compResponse=="rock")
				||
				(cmd=="scissors"&&compResponse=="paper")){
			logger.info("Player won! Player " +cmd +" computer" +compResponse);
			}
		else{
			logger.info("Computer won! Player "+cmd +"computer " +compResponse);
			
		}
			
			
		

	}
	else{
		logger.info("Incorrect input!Try again!");
			bot.sendMessage({
	to: channelID,
	message:cmd +" is not a valid rock paper scissors input!"
	});
	}
	
}

/*
function dice (user, userID, channelID, message, evt,cmd){
	if(!isNaN(cmd)){
		
		var numb = Math.floor((Math.random() * cmd) + 1);
			

		logger.info("is a number, random number generated is "+ cmd+"");
	}
	else{
		
	
	logger.info("Not a number");
	}		
}
*/