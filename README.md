# Scripts module of CraftLogic minecraft mod
LICENSE: CC BY-NC-SA https://creativecommons.org/licenses/by-nc-sa/4.0/

You dont't have to restart the server every time you add or delete a command or if you want to load a new script.
Just type: /script load script_name

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
rtp.gs:
```groovy
import net.minecraft.util.math.BlockPos

command('rtp') {
	rtp(senderAsPlayer().getEntity())
}

boolean rtp(def player) {
	BlockPos pos = findPos(player, 20)
	if (pos != null) {
		def pl = $server.playerManager.getOnline(player.getGameProfile().getId())
		return pl.teleport(pos.getX(), pos.getY(), pos.getZ(), 0f, 0f)
	}
	return false
}

BlockPos findPos(def player, int tries) {
	def world = player.world
	def rand = world.rand
	def wb = world.worldBorder
	int w2 = ((int)wb.maxX() - (int)wb.minX()) / 2;
	int h2 = ((int)wb.maxZ() - (int)wb.minZ()) / 2;

	int centerX = (int)wb.getCenterX();
	int centerZ = (int)wb.getCenterZ();

	for (int i = 0; i < tries; i++) {
		int x = centerX + rand.nextInt(w2) * (rand.nextBoolean() ? 1 : -1) 
		int z = centerZ + rand.nextInt(h2) * (rand.nextBoolean() ? 1 : -1)
		def chunk = world.getChunk(x >> 4, z >> 4) //FORCE CHUNK GEN
		int y = chunk.getHeightValue(x & 15, z & 15)
		def pos = new BlockPos(x, y, z);
		while (!world.isAirBlock(pos) && y < world.getHeight()) {
			pos = new BlockPos(x, y, z);
			y++
		} 
		if (y < world.getHeight()) {
			return pos
		}
	} 

	return null
}

when('player:respawn') {
	if (player.spawnPos == null) {
		if (rtp(player)) {
			player.sendMessage('You woke up in an unknown place'.green())
		} else {
			player.sendMessage('Unable to find suitable location in world'.red());
		}
	}
}

def kitStart(def player) {
	//TODO
}

when('player:login') {
	long playTime = System.currentTimeMillis() - player.firstPlayed
	if (playTime < 100 && player.world.provider.getDimension() == 0) {
		rtp(player)
		player.sendMessage('Welcome to '.yellow() + 'Our Server'.gold() + '!'.yellow())
		kitStart(player)
	}
}
```
