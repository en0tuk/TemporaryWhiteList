package ru.reosfire.temporarywhitelist.Configuration.Localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.*;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class CommandResultsConfig extends YamlConfig
{
    public final AddCommandResultsConfig Add;
    public final SetCommandResultsConfig Set;
    public final RemoveCommandResultsConfig Remove;
    public final CheckCommandResultsConfig Check;
    public final ListCommandResultsConfig List;

    public CommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Add = new AddCommandResultsConfig(getSection("Add"));
        Set = new SetCommandResultsConfig(getSection("Set"));
        Remove = new RemoveCommandResultsConfig(getSection("Remove"));
        Check = new CheckCommandResultsConfig(getSection("Check"));
        List = new ListCommandResultsConfig(getSection("List"));
    }
}