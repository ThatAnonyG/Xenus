package xen.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import xen.lib.Utils;
import xen.lib.command.Command;
import xen.lib.command.CommandContext;

public class Info extends Command {
  public Info() {
    super("info");
    setCd(5000);
    setDescription("Info about the bot and important links.");
  }

  @Override
  public void run(@NotNull CommandContext ctx) {
    long msUptime = System.currentTimeMillis() - ctx.getClient().getStartTime();
    byte seconds = (byte) Math.ceil((float) (msUptime / 1000) % 60);
    byte minutes = (byte) Math.ceil((float) (msUptime / (1000 * 60)) % 60);
    byte hours = (byte) Math.ceil((float) (msUptime / (1000 * 60 * 60)) % 24);

    EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(
                    ctx.getEvent().getJDA().getSelfUser().getName(),
                    "https://xenus.xyz",
                    ctx.getEvent().getJDA().getSelfUser().getAvatarUrl()
            )
            .setColor(Utils.getHex())
            .setImage("https://imgur.com/Vpi7ReD.png")
            .addField("Version", "1.0.0", true)
            .addField(
                    "Founder",
                    "[ThatAnonymousG](https://github.com/ThatAnonymousG)",
                    true
            )
            .addField(
                    "Library",
                    "[JDA](https://ci.dv8tion.net/job/JDA/javadoc/index.html)",
                    true
            )
            .addField(
                    "Current Users",
                    String.valueOf(ctx.getEvent().getJDA().getUsers().size()),
                    true
            )
            .addField(
                    "Current Guilds",
                    String.valueOf(ctx.getEvent().getJDA().getGuilds().size()),
                    true
            )
            .addField("Website", "[xenus.xyz](https://xenus.xyz)", true)
            .addField("Support Server", "[Join Now](https://xenus.xyz/discord)", true)
            .addField("Invite Bot", "[Click Here](https://xenus.xyz/invite)", true)
            .addField("Donate", "[PayPal](https://xenus.xyz/donate)", true)
            .setFooter(
                    "Uptime: " +
                            (hours < 10 ? "0" + String.valueOf(hours) : String.valueOf(hours)) +
                            "h " +
                            (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes)) +
                            "m " +
                            (seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds)) +
                            "s"
            );

    ctx.getEvent().getChannel().sendMessage(embed.build()).queue();
  }
}
