# Scripts module of CraftLogic minecraft mod
LICENSE: CC BY-NC-SA https://creativecommons.org/licenses/by-nc-sa/4.0/

# Example scripts
automessage.gs:
```groovy
def counter = 0
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
}
```
godmode.gs:
```groovy
command('god', syntax: ['', '<target:Player>']) { ctx ->
	def self = !ctx.has('target')
	def target = self ? ctx.senderAsPlayer() : ctx.target.asPlayer()
	if (self || ctx.checkPermission(true, 'commands.god.others', 2)) {
		def cap = target.entity.capabilities
		def msg
		if (cap.disableDamage) {
			cap.disableDamage = false
			msg = 'You\'ve turned the god mode off'.gray()
		} else {
			cap.disableDamage = true
			msg = 'You\'ve turned the god mode on'.gray()
		}
		if (!self) {
			msg = msg.appendText(' for ')
			         .appendSibling(target.name.darkGray())
		}
		ctx.sendNotification(msg)
	}
}
```
