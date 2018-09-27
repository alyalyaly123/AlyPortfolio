var Discord = require('discord.io');
var logger = require('winston');
var auth = require('./auth.json');
// Configure logger settings
logger.remove(logger.transports.Console);
logger.add(new logger.transports.Console, {
    colorize: true
});
logger.level = 'debug';
// Initialize Discord Bot
var bot = new Discord.Client({
    token: auth.token,
    autorun: true
});
bot.on('ready', function(evt) {
    logger.info('Connected');
    logger.info('Logged in as: ');
    logger.info(bot.username + ' – (' + bot.id + ')');
});


bot.on('message', function(user, userID, channelID, message, evt) {
    if (message.substring(0, 1) == '!') {
        var args = message.substring(1).split(' ');
        var cmd = args[0];

        args = args.splice(1);
        switch (cmd) {
            case 'intro':
                bot.sendMessage({
                    to: channelID,
                    message: "Greetings! Welcome to the server!"

                });
                break;
            case 'hi':
                bot.sendMessage({
                    to: channelID,
                    message: "Hi " + user + " " + userID + "!"

                });
                break;
            case 'name':
                bot.sendMessage({
                    to: channelID,
                    message: "Your name is " + user + ""

                });
                break;
        }
    }



    //age
    if (message.substring(0, 4) == 'age.') {
        var args = message.substring(4).split(' ');
        var cmd = args[0];
        args = args.splice(4);

        if (isNaN(parseInt(cmd))) {
            bot.sendMessage({

                to: channelID,
                message: "Please input a number!" + cmd + "is not a number"
            });
        }

        //if not a number
        else {

            var numArgs = parseInt(cmd);

            var dateThing = new Date();
            var year = dateThing.getFullYear();
            var birthYear = year - numArgs;
            bot.sendMessage({

                to: channelID,
                message: "birthyear is " + birthYear + "."
            });
            logger.info("you did it i guess");
        }

    }


    //returns the exact response of the user: General message testing
    if (message.substring(0, 7) == 'parrot.') {
        logger.info("Parroted!");

        var total = 0;
        var args = message.substring(7).split(' ');
        var cmd = args[0]
        messageTest(user, userID, channelID, message, evt, cmd);
        logger.info("you did it i guess");
    }
    //ROCK PAPER SCISSORS
    if (message.substring(0, 4) == 'rps.') {
        logger.info("Rock paper scissors!");
        var total = 0;
        var args = message.substring(4).split(' ');
        var cmd = args[0]
        rockPaper(user, userID, channelID, message, evt, cmd);

    }




    if (message.substring(0, 5) == 'dice.') {
        logger.info("Dice roll!");

        var args = message.substring(5).split(' ');
        var cmd = args[0]
        dice(user, userID, channelID, message, evt, cmd);

    }

    //find mean of a string of numbers-- move average to a function
    if (message.substring(0, 4) == 'avg.') {
        var total = 0;
        var sum = Number(0);
        sum += 1;
        logger.info("FIRST SUM" + sum);
        var args = message.substring(4).split(' ');
        var cmd = args[0].split(',');
        bot.sendMessage({
            to: channelID,
            message: "Message is " + cmd + "."
        });

        if (!isNaN(cmd[0])) {
            sum = sum + cmd[0];
            logger.info("sum is" + sum);
            for (var x = 0; x < cmd.length; x++) {
                sum = +sum + 1;
                total = +total + +cmd[x];
                logger.info(total);
                logger.info("sums is " + sum);
            }
            var average = Number(total) / Number(cmd.length);
            bot.sendMessage({
                to: channelID,
                message: "Mean is: " + average + "."
            });
        } else {
            bot.sendMessage({
                to: channelID,
                message: "Not a number!"
            });
        }

        bot.sendMessage({
            to: channelID,
            message: "Total is " + total + "."
        });

    }
})

function messageTest(user, userID, channelID, message, evt, cmd) {
    bot.sendMessage({
        to: channelID,
        message: "Message is " + cmd + "."
    });

}

var winCount = 0;
var loseCount = 0;
var drawCount = 0;
//Rock paper scissors! Basic atm, gotta make more clear
function rockPaper(user, userID, channelID, message, evt, cmd) {
    //Validation check

    if (cmd == "rock" || cmd == "paper" || cmd == "scissors") {
        var compResponse = "item";
        logger.info("Correctly input!");

        var numb = Math.floor((Math.random() * 3) + 1);
        logger.info("number is " + numb + ".");

        if (numb == 1) {
            compResponse = "rock";
        } else if (numb == 2) {
            compResponse = "paper";
        } else {
            compResponse = "scissors";
        }

        if (cmd == compResponse) {
            logger.info("Draw!");
            bot.sendMessage({
                to: channelID,
                message: "Draw! Player" + cmd + " Computer" + compResponse
            });
        } else if ((cmd == "rock" && compResponse == "scissors") ||
            (cmd == "paper" && compResponse == "rock") ||
            (cmd == "scissors" && compResponse == "paper")) {
            logger.info("Player won! Player " + cmd + " computer" + compResponse);
            bot.sendMessage({
                to: channelID,
                message: "Player won! Player" + cmd + " Computer" + compResponse
            });
            winCount = winCount + 1;
        } else {
            logger.info("Computer Lost! Player " + cmd + "computer " + compResponse);
            bot.sendMessage({
                to: channelID,
                message: "Player Lost! Player " + cmd + " Computer" + compResponse
            });
            loseCount = loseCount + 1;
        }


    } else if (cmd == "score") {
        bot.sendMessage({
            to: channelID,
            message: "No of wins :" + winCount + ". No of loses: " + loseCount + "No of draws: " + drawCount
        });
        drawCount = drawCount + 1
    } else if (cmd == "reset") {
        bot.sendMessage({
            to: channelID,
            message: "Resetting scores!"
        });
        winCount = 0;
        loseCount = 0;
        drawCount = 0;
    } else {
        logger.info("Incorrect input!Try again!");
        bot.sendMessage({
            to: channelID,
            message: cmd + " is not a valid rock paper scissors input!"
        });
    }

}

//basic dice with chosen number of sides
function dice(user, userID, channelID, message, evt, cmd) {
    //cmd refers to sides of dice. Must be an integer
    if (!isNaN(cmd)) {

        var numb = Math.floor((Math.random() * cmd) + 1);

        bot.sendMessage({
            to: channelID,
            message: cmd + " is a number, random number generated is " + numb + ""
        });
        logger.info(cmd + "is a number, random number generated is " + numb + "");
    } else {
        bot.sendMessage({
            to: channelID,
            message: cmd + " is not a number."
        });

        logger.info("Not a number");
    }
}
