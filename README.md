# Scripts module of CraftLogic minecraft mod
LICENSE: CC BY-NC-SA https://creativecommons.org/licenses/by-nc-sa/4.0/

# Example scripts
automessage.gs:
`def counter = 0
def interval = 5 * 60 * 20
def prefix = '['.gray() + 'Hint'.yellow() + ']'.gray()
def messages = [
	'Region creation - '+'/rg create'.white().suggest('/rg create').tooltip('Click to copy'),
	'Invite a friend to a region - '+'/rg invite nickname'.white().suggest('/rg invite ').tooltip('Click to copy'),
	'Region deletion - '+'/rg delete'.white().suggest('/rg delete').tooltip('Click to copy'),
	'You can find more information about this server on our '+'forum'.white().openURL('https://your-project.com/forum/').tooltip('Click to open the URL')
]

when('server:tick') { event ->
	if (counter++ == interval * 2) {
		counter = 0
		def message = messages[new Random().nextInt(messages.size())]
		$server.broadcast(prefix + ' ' + message)
	}
}`
