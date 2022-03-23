package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.EnableCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("enable")
@CommandPermission("TemporaryWhiteList.Administrate.Enable")
public class EnableCommand extends CommandNode
{
    private final TemporaryWhiteList _pluginInstance;
    private final EnableCommandResultsConfig _commandResults;

    public EnableCommand(MessagesConfig messagesConfig, TemporaryWhiteList pluginInstance)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Enable;
        _pluginInstance = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        try
        {
            if (_pluginInstance.Enable()) _commandResults.Success.Send(sender);
            else _commandResults.NothingChanged.Send(sender);
        }
        catch (Exception e)
        {
            _commandResults.Error.Send(sender);
            e.printStackTrace();
        }
        return true;
    }
}